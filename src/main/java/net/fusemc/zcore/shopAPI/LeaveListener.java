package net.fusemc.zcore.shopAPI;

import net.fusemc.zcore.PlayerLeaveEvent;
import net.fusemc.zcore.ZCore;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

/**
 * Created by Niklas on 21.08.2014.
 */
public class LeaveListener implements Listener {
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onLeave(PlayerLeaveEvent event) {
        ZCore.getShopManager().removeFromCache(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onJoin(PlayerJoinEvent event) {
        ZCore.getShopManager().getPlayerData(event.getPlayer());
    }
}
