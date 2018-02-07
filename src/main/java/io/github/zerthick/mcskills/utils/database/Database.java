package io.github.zerthick.mcskills.utils.database;

import io.github.zerthick.mcskills.McSkills;
import io.github.zerthick.mcskills.account.McSkillsAccountEntry;
import io.github.zerthick.mcskills.account.McSkillsAccountImpl;
import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.service.sql.SqlService;

import javax.sql.DataSource;
import java.sql.*;
import java.util.*;

public class Database {

    private SqlService sql;
    private String databaseUrl;
    private Logger logger;
    private static Database instance;

    private Database(McSkills plugin) throws SQLException {
        logger = plugin.getLogger();
        String configDir = plugin.getDefaultConfigDir().toString();
        databaseUrl = "jdbc:h2:"+ configDir +"/data;mode=MySQL";
        createDatabaseTables();
    }

    public static Database getInstance(McSkills plugin) throws SQLException {
        if (instance == null) {
            instance = new Database(plugin);
        }
        return instance;
    }

    private DataSource getDataSource() throws SQLException {
        if (this.sql == null) {
            Optional<SqlService> sqlServiceOptional = Sponge.getServiceManager().provide(SqlService.class);
            this.sql = sqlServiceOptional.orElseThrow(SQLException::new);
        }
        return sql.getDataSource(databaseUrl);
    }

    private void createDatabaseTables() throws SQLException {
        String sqlCreateTable = "CREATE TABLE IF NOT EXISTS" +
                "  `playerData` (" +
                "  `playerUUID` VARCHAR(20) NOT NULL," +
                "  `skillID` VARCHAR(20) NOT NULL," +
                "  `skillExperience` BIGINT NULL," +
                "  `skillLevel` INT NULL," +
                "  PRIMARY KEY (`playerUUID`, `skillID`));";

        Connection conn = getDataSource().getConnection();
        Statement createStatement = conn.createStatement();
        createStatement.execute(sqlCreateTable);
        logger.info("Database Connection Established!");
    }

    /**
     * Returns a McSkills Player account from the database
     *
     * @param uuid uuid of the player you want to receive an account for
     */
    public Optional<McSkillsAccountImpl> getPlayerAccount(UUID uuid) {
        // set skillMap variable
        Map<String, McSkillsAccountEntry> skillMap = new HashMap<>();

        // Create connection and statement
        try(
        Connection conn = getDataSource().getConnection();
        PreparedStatement ps = conn.prepareStatement("SELECT skillID, skillExperience, skillLevel " +
                "FROM playerData " +
                "WHERE playerUUID = ?")
        ) {
            // set uuid to provided uuid
            ps.setString(1, String.valueOf(uuid));
            ResultSet rs = ps.executeQuery();

            // check if no data
            if (!rs.isBeforeFirst()){
                return Optional.empty();
            }

            // for each result
            while (rs.next()) {
                String skillID = rs.getString("skillID");
                Long skillExperience = rs.getLong("skillExperience");
                int skillLevel = rs.getInt("skillLevel");
                // map level and experience
                McSkillsAccountEntry skillMapEntry = new McSkillsAccountEntry(skillLevel, skillExperience);
                // insert skill data into map
                skillMap.put(skillID, skillMapEntry);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
        }
        // return skills map
        return Optional.of(new McSkillsAccountImpl(uuid, skillMap));
    }

    /**
     * Saves the McSkills account information to the database
     *
     * @param mcSkillsAccount account model for entering into the database
     */
    public void savePlayerAccount(McSkillsAccountImpl mcSkillsAccount) {
        Map<String, McSkillsAccountEntry> skillMap = mcSkillsAccount.getSkillMap();
        UUID uuid = mcSkillsAccount.getPlayerUUID();

        try(
                Connection conn = getDataSource().getConnection();
                PreparedStatement ps = conn.prepareStatement("INSERT INTO playerdata SET playerUUID = ?, skillID = ?, skillExperience = ?, skillLevel = ? " +
                        "ON DUPLICATE KEY UPDATE " +
                        "skillExperience = VALUES(skillExperience), skillLevel = VALUES(skillLevel)")
                ) {
            skillMap.forEach((k,v) -> {
                try {
                    ps.setLong(3, v.getExperience());
                    ps.setInt(4, v.getLevel());
                    ps.setString(1, uuid.toString());
                    ps.setString(2, k);
                    ps.addBatch();
                } catch (SQLException e) {
                    logger.error(e.getMessage(), e);
                }
        });
            ps.executeBatch();
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
        }
    }
}