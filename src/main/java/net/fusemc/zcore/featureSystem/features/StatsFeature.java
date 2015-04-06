package net.fusemc.zcore.featureSystem.features;

import net.fusemc.zcore.ZCore;
import net.fusemc.zcore.featureSystem.Feature;
import net.fusemc.zcore.statsAPI.Stat;
import net.fusemc.zcore.statsAPI.StatsManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Niklas on 22.09.2014.
 */
public class StatsFeature extends Feature {

    public boolean enable(String prefix, String[] databaseNames, String[] displayNames) {
        if (databaseNames == null || displayNames == null || databaseNames.length != displayNames.length || databaseNames.length == 0) return false;
        if (!super.enable()) return false;

        ZCore.getStatsManager().init(prefix, databaseNames, displayNames);
        return true;
    }

    @Override
    protected boolean disable() {
        if (!super.disable()) return false;
        ZCore.getStatsManager().flushStats();
        return true;
    }

    public void loadFromDb() {
        List<String> uuids = new ArrayList<>();
        for (Player player : Bukkit.getOnlinePlayers()) {
            uuids.add(player.getUniqueId().toString());
        }
        ZCore.getStatsManager().loadFromDb(uuids.toArray(new String[uuids.size()]));
    }

    public void printStats() {
        StatsManager sm = ZCore.getStatsManager();
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.sendMessage("\u00a77-------- \u00a73Deine Statistiken \u00a77--------");
            for (Stat stat : sm.getStats()) {
                player.sendMessage("\u00a73" + stat.getDisplayName() + "\u00a77: " + "\u00a76" + stat.getValue(player));
            }
            player.sendMessage("\u00a77-------------------------------");
        }
    }
}
