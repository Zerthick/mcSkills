package io.github.zerthick.mcskills.utils.database;

import io.github.zerthick.mcskills.McSkills;
import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.service.sql.SqlService;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

public class Database {

    private SqlService sql;
    private McSkills plugin;
    private String databaseUrl = "jdbc:h2:" + plugin.getDefaultConfigDir() + "/data";
    private Logger logger = plugin.getLogger();

    public Database(McSkills plugin) throws SQLException {
        this.plugin = plugin;
        createDatabaseTables();
    }

    private DataSource getDataSource() throws SQLException {
        if (this.sql == null) {
            Optional<SqlService> sqlServiceOptional = Sponge.getServiceManager().provide(SqlService.class);

            if (sqlServiceOptional.isPresent()) {
                this.sql = sqlServiceOptional.get();
            } else {
                throw new SQLException("Was not able to get database connection");
            }
        }
        return sql.getDataSource(databaseUrl);
    }

    private void createDatabaseTables() throws SQLException {
        String sqlCreateTable = "CREATE TABLE IF NOT EXISTS" +
                "  `playerData` (" +
                "  `playerUUID` VARCHAR() NOT NULL," +
                "  `skillID` INT NOT NULL," +
                "  `skillExperience` INT NULL," +
                "  `skillLevel` INT NULL," +
                "  PRIMARY KEY (`playerUUID`, `skillID`));";

        boolean createStatement = this.getDataSource().getConnection().createStatement().execute(sqlCreateTable);
        if (createStatement) {
            logger.info("Database Connection Established!");
        } else {
            logger.error("Failed to create database tables");
        }
    }
}