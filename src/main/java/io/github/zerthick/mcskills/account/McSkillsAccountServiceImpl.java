package io.github.zerthick.mcskills.account;

import io.github.zerthick.mcskills.McSkills;
import io.github.zerthick.mcskills.api.account.McSkillsAccount;
import io.github.zerthick.mcskills.api.account.McSkillsAccountService;
import io.github.zerthick.mcskills.utils.database.Database;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.filter.Getter;
import org.spongepowered.api.event.network.ClientConnectionEvent;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class McSkillsAccountServiceImpl implements McSkillsAccountService {

    private McSkills plugin;
    private Database db;
    private Map<UUID, McSkillsAccountImpl> accountCache;

    public McSkillsAccountServiceImpl(McSkills plugin) throws SQLException {
        this.plugin = plugin;
        db = new Database(plugin);
        accountCache = new HashMap<>();
    }

    @Override
    public McSkillsAccount getOrCreateAccount(UUID playerUniqueIdentifier) {
        if (accountCache.containsKey(playerUniqueIdentifier)) {
            return accountCache.get(playerUniqueIdentifier);
        } else {
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
        UUID playerUniqueIdentifier = player.getUniqueId();
        db.getPlayerAccount(playerUniqueIdentifier).ifPresent(playerAccount -> accountCache.put(playerUniqueIdentifier, playerAccount));
    }

    @Listener
    public void onPlayerLeave(ClientConnectionEvent.Disconnect event, @Getter("getTargetEntity") Player player) {
        UUID playerUniqueIdentifier = player.getUniqueId();
        if (accountCache.containsKey(playerUniqueIdentifier)) {
            db.savePlayerAccount(accountCache.remove(playerUniqueIdentifier));
        }
    }
}
