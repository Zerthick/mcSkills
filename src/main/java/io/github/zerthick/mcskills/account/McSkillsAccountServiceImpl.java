/*
 * Copyright (C) 2018  Zerthick
 *
 * This file is part of mcSkills.
 *
 * mcSkills is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2 of the License, or
 * (at your option) any later version.
 *
 * mcSkills is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mcSkills.  If not, see <http://www.gnu.org/licenses/>.
 */

package io.github.zerthick.mcskills.account;

import io.github.zerthick.mcskills.McSkills;
import io.github.zerthick.mcskills.api.account.McSkillsAccount;
import io.github.zerthick.mcskills.api.account.McSkillsAccountService;
import io.github.zerthick.mcskills.utils.database.Database;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.filter.Getter;
import org.spongepowered.api.event.game.state.GameStoppedServerEvent;
import org.spongepowered.api.event.network.ClientConnectionEvent;
import org.spongepowered.api.scheduler.Task;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class McSkillsAccountServiceImpl implements McSkillsAccountService {

    private Database db;
    private Map<UUID, McSkillsAccountImpl> accountCache;

    public McSkillsAccountServiceImpl(McSkills plugin) {

        db = new Database(plugin);
        accountCache = new ConcurrentHashMap<>();

        // Save all accounts to the DB asynchronously every 5 mins
        Task.builder()
                .async()
                .interval(5, TimeUnit.MINUTES)
                .name("McSkills Account Save Task")
                .execute(() -> accountCache.values().forEach(db::savePlayerAccount))
                .submit(plugin);
    }

    @Override
    public McSkillsAccount getOrCreateAccount(UUID playerUniqueIdentifier) {

        if (accountCache.containsKey(playerUniqueIdentifier)) { // First check local player cache
            return accountCache.get(playerUniqueIdentifier);
        } else { // Fetch the account from the DB or create it if it doesn't exist
            McSkillsAccountImpl playerAccount = db.getPlayerAccount(playerUniqueIdentifier).orElse(new McSkillsAccountImpl(playerUniqueIdentifier, new HashMap<>()));
            accountCache.put(playerUniqueIdentifier, playerAccount);
            return playerAccount;
        }
    }

    @Override
    public boolean hasAccount(UUID playerUniqueIdentifier) {
        return accountCache.containsKey(playerUniqueIdentifier) || db.getPlayerAccount(playerUniqueIdentifier).isPresent();
    }

    @Listener
    public void onPlayerJoin(ClientConnectionEvent.Join event, @Getter("getTargetEntity") Player player) {

        // Load player account into the cache if it exists
        UUID playerUniqueIdentifier = player.getUniqueId();
        db.getPlayerAccount(playerUniqueIdentifier).ifPresent(playerAccount -> accountCache.put(playerUniqueIdentifier, playerAccount));
    }

    @Listener
    public void onPlayerLeave(ClientConnectionEvent.Disconnect event, @Getter("getTargetEntity") Player player) {

        // Remove player account from the cache and save it to the DB if it exists
        UUID playerUniqueIdentifier = player.getUniqueId();
        if (accountCache.containsKey(playerUniqueIdentifier)) {
            db.savePlayerAccount(accountCache.remove(playerUniqueIdentifier));
        }
    }

    @Listener
    public void onServerStop(GameStoppedServerEvent event) {

        // Save any remaining accounts
        accountCache.values().forEach(db::savePlayerAccount);
    }
}
