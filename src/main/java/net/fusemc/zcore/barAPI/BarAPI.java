package net.fusemc.zcore.barAPI;

import me.johnking.jlib.JLib;
import me.johnking.jlib.reflection.ReflectionUtil;
import net.fusemc.zcore.ZCore;
import net.fusemc.zcore.util.MC1_8Utils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.UUID;

/**
 * Created by Marco on 13.10.2014.
 */
public class BarAPI implements Listener, Runnable{

    private static final Class<?> classEntity = ReflectionUtil.getMinecraftClass("Entity");
    private static final Field entityCount = ReflectionUtil.getField(classEntity, "entityCount");

    private HashMap<UUID, EntityBar> playerCache = new HashMap<>();
    private int entityID;
    private String message;
    private float percent = 100.0F;

    public BarAPI() {
        this.entityID = getFreeEntityID();
        Bukkit.getPluginManager().registerEvents(this, ZCore.getInstance());
        Bukkit.getScheduler().scheduleSyncRepeatingTask(ZCore.getInstance(), this, 0, 10L); //update interval
    }

    public void removeBar() {
        this.message = null;
        this.percent = 100.0F;
        for(Player player : Bukkit.getOnlinePlayers()) {
            removeBar(player);
        }
    }

    public void setMessage(String message) {
        setMessage(message, 100.0F);
    }

    public void setMessage(String message, float percent) {
        this.message = message;
        this.percent = percent;
        for(Player player : Bukkit.getOnlinePlayers()) {
            setMessage(player, message, percent);
        }
    }

    public void setMessage(Player player, String message) {
        setMessage(player, message, 100.0F);
    }

    public void setMessage(Player player, String message, float percent) {
        EntityBar bar = getBar(player);
        bar.setMessage(message);
        bar.setPercent(percent);
    }

    public void removeBar(Player player) {
        EntityBar bar = getBar(player);
        bar.setPercent(100.0F);
        bar.die();
    }

    @Override
    public void run() {
        for(Player player : Bukkit.getOnlinePlayers()) {
            EntityBar bar = getBar(player);
            if(bar == null) {
                continue;
            }
            if(bar.isAction()) {
                if(bar.isDead()) {
                    JLib.getProtocolManager().sendPacket(bar.getDestroyPacket().getHandle(), player);
                } else {
                    bar.updateLocation(player);
                    JLib.getProtocolManager().sendPacket(bar.getSpawnPacket().getHandle(), player);
                    continue;
                }
            } else {
                if(bar.hasChanged()) {
                    JLib.getProtocolManager().sendPacket(bar.getMetaDataPacket(), player);
                }
            }
            bar.updateLocation(player);
            JLib.getProtocolManager().sendPacket(bar.getTeleportPacket(), player);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerJoin(PlayerJoinEvent event) {
        EntityBar entity = getBar(event.getPlayer());
        if(message != null) {
            entity.setMessage(message);
            entity.setPercent(percent);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerKick(PlayerKickEvent event) {
        playerCache.remove(event.getPlayer().getUniqueId());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerLeft(PlayerQuitEvent event) {
        playerCache.remove(event.getPlayer().getUniqueId());
    }

    private EntityBar getBar(Player player) {
        EntityBar bar = playerCache.get(player.getUniqueId());
        if(bar == null) {
            bar = MC1_8Utils.is1_8(player) ? new WitherBar(entityID) : new DragonBar(entityID);
            playerCache.put(player.getUniqueId(), bar);
        }
        return bar;
    }

    private int getFreeEntityID() {
        try {
            int entityId = entityCount.getInt(null);
            entityCount.setInt(null, entityId + 1);
            return entityId;
        } catch (IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return -1;
    }
}
