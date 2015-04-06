package net.fusemc.zcore.statsAPI;

import net.fusemc.zcore.ZCore;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Niklas on 22.09.2014.
 */
public class Stat {

    private String prefix;
    private String databaseName;
    private String displayName;
    private Map<String, Integer> values;

    Stat(String prefix, String databaseName, String displayName) {
        this.prefix = prefix;
        this.databaseName = databaseName;
        this.displayName = displayName;
        this.values = new HashMap<>();
    }

    public void incrementStat(Player player, int amount) {
        if (!ZCore.getStatsManager().isInitialized()) return;
        int value = values.get(player);
        value += amount;
        values.put(player.getUniqueId().toString(), value);
    }

    public void decrementStat(Player player, int amount) {
        if (!ZCore.getStatsManager().isInitialized()) return;
        int value = values.get(player);
        value -= amount;
        values.put(player.getUniqueId().toString(), value);
    }

    public void flush() {
        try (Connection connection = ZCore.getPlayerConnection()) {
            connection.setAutoCommit(false);
            try(PreparedStatement ps = connection.prepareStatement("INSERT INTO " + databaseName + "(`uuid`,`?`) VALUES(?,?) ON DUPLICATE KEY UPDATE `?`=?")) {
                ps.setString(1, databaseName);
                ps.setString(4, databaseName);
                for (String uuid : values.keySet()) {
                    int value = values.get(uuid);
                    ps.setString(2, uuid);
                    ps.setInt(3, value);
                    ps.setInt(5, value);
                    ps.addBatch();
                }
                ps.executeBatch();
                connection.commit();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            connection.setAutoCommit(true);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public int getValue(Player player) {
        if (values.get(player.getUniqueId().toString()) == null) refreshFromDatabase(player.getUniqueId().toString());
        return values.get(player.getUniqueId().toString());
    }

    public void refreshFromDatabase(String uuid) {
        try(Connection connection = ZCore.getPlayerConnection()) {
            PreparedStatement ps = connection.prepareStatement("SELECT `?` FROM " + prefix + " WHERE `uuid`=?");
            ps.setString(1, databaseName);
            ps.setString(2, uuid);
            ResultSet res = ps.executeQuery();
            if (res.next()) {
                values.put(uuid, res.getInt(databaseName));
            }
            res.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
