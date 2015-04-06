package net.fusemc.zcore.featureSystem.features.trailFeature;

import me.michidk.DKLib.event.PlayerBlockMoveEvent;
import net.fusemc.zcore.ZCore;
import net.fusemc.zcore.featureSystem.Feature;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;

public class TrailFeature extends Feature {

    private DespawnTimer timer;
    private EntityIDPool entityIDPool;
    private int schedulerId = -1;

    @Override
    public boolean enable(){
        if (!super.enable()) return false;
        entityIDPool = new EntityIDPool();
        timer = new DespawnTimer(20, entityIDPool);
        schedulerId = Bukkit.getScheduler().scheduleSyncRepeatingTask(ZCore.getInstance(), timer, 0, 1);
        return true;
    }

    @Override
    public boolean disable(){
        if (!super.disable()) return false;
        Bukkit.getScheduler().cancelTask(schedulerId);
        return true;
    }

    public DespawnTimer getTimer() {
        return timer;
    }

    public EntityIDPool getEntityIDPool() {
        return entityIDPool;
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
        if (e.getFrom().distanceSquared(e.getTo()) == 0) return;
        for(Trail trail : Trail.values()){
            if (ZCore.getShopManager().getPlayerData(e.getPlayer()).hasItemSelected("trail_" + trail.getName()))
                trail.playEffect(e.getPlayer(), e.getPlayer().getLocation().add(e.getPlayer().getLocation().getDirection().setY(0).multiply(-0.5)));
        }
    }

    @EventHandler
    public void onPlayerBlockMove(PlayerBlockMoveEvent e) {
        for(Trail trail : Trail.values()){
            if (ZCore.getShopManager().getPlayerData(e.getPlayer()).hasItemSelected("trail_" + trail.getName()))
                trail.playItem(e.getPlayer(), e.getPlayer().getLocation().add(e.getPlayer().getLocation().getDirection().multiply(-0.5).setY(0.5)));
        }
    }
}
