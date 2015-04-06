package net.fusemc.zcore.util;

import me.michidk.DKLib.utils.RandomFirework;
import net.fusemc.zcore.ZCore;
import net.fusemc.zcore.featureSystem.features.disguiseFeature.DisguiseFeature;
import net.fusemc.zcore.featureSystem.features.spectatorFeature.SpectatorFeature;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * Created by ml on 13.08.14.
 */
public final class WinHelper {

    public static void win(Player p){
        win(p, true);
    }

    public static void win(Player p, boolean teleport) {
        Bukkit.broadcastMessage("\u00A78\u00A7l[\u00A7b\u2756\u00A78\u00A7l] \u00A73" + p.getName() + " \u00A7dhat die Runde gewonnen!");

        if(teleport) {
            for (Player target : Bukkit.getOnlinePlayers()) {
                target.teleport(p);
            }
        }

        if (ZCore.getFeatureManager().getFeature(SpectatorFeature.class).isEnabled()) {
            SpectatorFeature spec = ZCore.getFeatureManager().getFeature(SpectatorFeature.class);
            spec.controller.setVisible(true);
        }

        if (ZCore.getFeatureManager().getFeature(DisguiseFeature.class).isEnabled()) {
            ZCore.getFeatureManager().getFeature(DisguiseFeature.class).disable();  //disable that players are become visible
        }

        new FireworkTask(p);

        Bukkit.getScheduler().scheduleSyncDelayedTask(ZCore.getInstance(), new Runnable() {
            @Override
            public void run() {
                for (Player p:Bukkit.getOnlinePlayers()) {
                    p.kickPlayer("lobby");
                }
                Bukkit.shutdown();
            }
        }, 200);
    }

    private static final class FireworkTask implements Runnable {

        private Player player;
        private int schedulerId;
        private int count;

        public FireworkTask(Player player) {
            this.player = player;
            this.schedulerId = Bukkit.getScheduler().scheduleSyncRepeatingTask(ZCore.getInstance(), this, 0, 20);
        }

        @Override
        public void run() {
            if(!player.isOnline()){
                Bukkit.getScheduler().cancelTask(schedulerId);
                return;
            }
            RandomFirework.spawnRandomFirework(player.getLocation());
            count++;
            if(count > 5){
                Bukkit.getScheduler().cancelTask(schedulerId);
                count = 0;
            }
        }
    }
}
