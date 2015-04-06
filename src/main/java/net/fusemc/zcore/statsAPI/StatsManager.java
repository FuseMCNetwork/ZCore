package net.fusemc.zcore.statsAPI;

import net.fusemc.zcore.mysql.MySQL;
import net.fusemc.zcore.mysql.MySQLDBType;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Niklas on 22.09.2014.
 */
public class StatsManager {

    private boolean initialized = false;
    private String prefix = null;
    private Map<String, Stat> stats;
    private MySQL mySQL;

    public StatsManager() {
        stats = new HashMap<>();
        mySQL = new MySQL(MySQLDBType.STATS);
    }

    public void init(String prefix, String[] databaseNames, String[] displayNames) {
        if (initialized) return;
        if (databaseNames == null || displayNames == null || databaseNames.length != displayNames.length || databaseNames.length == 0) return;
        this.prefix = prefix.toLowerCase();
        for (int i = 0; i < databaseNames.length; i++) {
            addStat(databaseNames[i], displayNames[i]);
        }
        this.initialized = true;
    }

    private void addStat(String databaseName, String displayName) {
        if (!initialized) return;
        Stat stat = new Stat(prefix, databaseName, displayName);
        stats.put(databaseName, stat);
    }

    public Stat getStat(String databaseName) {
        return stats.get(databaseName);
    }

    public Stat[] getStats() {
        return stats.values().toArray(new Stat[stats.values().size()]);
    }

    public void flushStats() {
        for (Stat stat : stats.values()) {
            stat.flush();
        }
    }

    public void loadFromDb(String[] uuids) {
        for (Stat stat : stats.values()) {
            for (String uuid : uuids) {
                stat.refreshFromDatabase(uuid);
            }
        }
    }

    public String getPrefix() {
        return prefix;
    }

    boolean isInitialized() {
        return initialized;
    }

    public Connection getStatConnection() throws SQLException{
        return mySQL.getConnection();
    }
}
