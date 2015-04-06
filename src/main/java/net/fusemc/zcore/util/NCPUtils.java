package net.fusemc.zcore.util;

import fr.neatmonster.nocheatplus.checks.CheckType;
import fr.neatmonster.nocheatplus.hooks.NCPExemptionManager;
import net.fusemc.zcore.ZCore;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

/**
 * Copyright by michidk
 * Created: 22.09.2014.
 */
public class NCPUtils {

    //ORGINAL UTILS
    private static Map<String, Integer> origSchedulerID = new HashMap<>();
    public static void exemptPlayer(final Player player, float time, final CheckType type) {   //time in secs
        if (!ZCore.isUseNocheatplus()) return;

        NCPExemptionManager.exemptPermanently(player, type);

        if (origSchedulerID.get(player.getName()) != null && origSchedulerID.get(player.getName()) != 0) {
            Bukkit.getScheduler().cancelTask(origSchedulerID.get(player.getName()));
        }

        origSchedulerID.put(player.getName(),
                Bukkit.getScheduler().scheduleSyncDelayedTask(ZCore.getInstance(), new Runnable() {
                    @Override
                    public void run() {

                        NCPExemptionManager.unexempt(player, type);
                        origSchedulerID.put(player.getName(), 0);

                    }
                }, (long) time * 20L)
        );
    }

}
