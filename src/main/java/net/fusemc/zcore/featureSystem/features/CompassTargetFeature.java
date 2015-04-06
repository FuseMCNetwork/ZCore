package net.fusemc.zcore.featureSystem.features;

import net.fusemc.zcore.ZCore;
import net.fusemc.zcore.featureSystem.Feature;
import net.fusemc.zcore.featureSystem.features.spectatorFeature.SpectatorFeature;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;

/**
 * Created by Marco on 03.06.2014.
 */
public class CompassTargetFeature extends Feature implements Runnable{

    private HashMap<String, Player> map;
    private Location defaultTarget;
    private int schedulerId;

    public boolean enable(Location defaultTarget){
        if(super.enable()){
            map = new HashMap<>();
            this.defaultTarget = defaultTarget;
            schedulerId = Bukkit.getScheduler().scheduleSyncRepeatingTask(ZCore.getInstance(), this, 0, 10L);
            return true;
        }
        return false;
    }

    @Override
    public boolean disable(){
        if(super.disable()){
            map = null;
            Bukkit.getScheduler().cancelTask(schedulerId);
            return true;
        }
        return false;
    }

    @Override
    public void run(){
        for(Player player : Bukkit.getOnlinePlayers()){
            Player target = map.get(player.getName());
            if(target == null){
                continue;
            }
            player.setCompassTarget(target.getLocation());
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e){
        Player p = e.getPlayer();

        if (!p.getItemInHand().getType().equals(Material.COMPASS)) return;
        if (!(e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK)) return;

        SpectatorFeature spec = ZCore.getFeatureManager().getFeature(SpectatorFeature.class);
        if (spec.isEnabled()) {
            if (spec.getSpectators().contains(p)){
                return;
            }
        }

        Player target = null;
        double distanceSquared = 1000000;
        for(Player onlinePlayer: Bukkit.getOnlinePlayers()) {
            if (onlinePlayer.equals(p)) {
                continue;
            }
            double distance = p.getLocation().distanceSquared(onlinePlayer.getLocation());
            if (distance < distanceSquared) {
                if (p.canSee(onlinePlayer)) {
                    target = onlinePlayer;
                    distanceSquared = distance;
                }
            }
        }
        if(target == null){
            p.sendMessage("\u00A78\u00A7l[\u00A79Kompass\u00A78\u00A7l] \u00A7aEs ist kein Spieler in der n\u00E4he.");
            map.remove(p.getName());
            p.setCompassTarget(defaultTarget);
            return;
        }
        map.put(p.getName(), target);
        p.sendMessage("\u00A78\u00A7l[\u00A79Compass\u00A78\u00A7l] \u00A7aDein Kompass zeigt auf \u00A73" + target.getDisplayName() + " \u00A7ader \u00A73" + ((int)Math.sqrt(distanceSquared)) + " \u00A7aBl\u00F6cke entfernt ist.");
        p.setCompassTarget(target.getLocation());
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e){
        if(!map.containsValue(e.getEntity())){
            return;
        }
        for(Player player : Bukkit.getOnlinePlayers()){
            Player target = map.get(player.getName());
            if(target == null){
                continue;
            }
            if(target.equals(e.getEntity())){
                map.put(player.getName(), null);
                player.setCompassTarget(defaultTarget);
                player.sendMessage("\u00A78\u00A7l[\u00A79Kompass\u00A78\u00A7l] \u00A7aDein Ziel ist gestorben.");
            }
        }
        map.remove(e.getEntity().getName());
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent e){
        if(!map.containsValue(e.getPlayer())){
            return;
        }
        for(Player player : Bukkit.getOnlinePlayers()){
            Player target = map.get(player.getName());
            if(target == null){
                continue;
            }
            if(target.equals(e.getPlayer())){
                map.put(player.getName(), null);
                player.setCompassTarget(defaultTarget);
            }
        }
        map.remove(e.getPlayer().getName());
    }

    @EventHandler
    public void onPlayerKick(PlayerKickEvent e){
        if(!map.containsValue(e.getPlayer())){
            return;
        }
        for(Player player : Bukkit.getOnlinePlayers()){
            Player target = map.get(player.getName());
            if(target == null){
                continue;
            }
            if(target.equals(e.getPlayer())){
                map.put(player.getName(), null);
                player.setCompassTarget(defaultTarget);
            }
        }
        map.remove(e.getPlayer().getName());
    }

    public void removePlayer(Player p) {
        if (map.containsKey(p.getName())) {
            map.remove(p.getName());
        }
    }

}
