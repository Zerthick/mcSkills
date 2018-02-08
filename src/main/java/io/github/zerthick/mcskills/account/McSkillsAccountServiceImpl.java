package io.github.zerthick.mcskills.account;

import io.github.zerthick.mcskills.McSkills;
import io.github.zerthick.mcskills.api.account.McSkillsAccount;
import io.github.zerthick.mcskills.api.account.McSkillsAccountService;
import io.github.zerthick.mcskills.utils.database.Database;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.filter.Getter;
import org.spongepowered.api.event.network.ClientConnectionEvent;
import org.spongepowered.api.scheduler.Task;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class McSkillsAccountServiceImpl implements McSkillsAccountService {

    private McSkills plugin;
    private Database db;
    private Map<UUID, McSkillsAccountImpl> accountCache;

    public McSkillsAccountServiceImpl(McSkills plugin) throws SQLException {

        this.plugin = plugin;
        db = new Database(plugin);
        accountCache = new HashMap<>();

        // Save all accounts to the DB asynchronously every 5 mins
        Task.builder()
                .async()
                .interval(5, TimeUnit.MINUTES)
                .name("McSkills Account Save Task")
                .execute(() -> accountCache.values().forEach(a -> db.savePlayerAccount(a)))
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
}
