package net.fusemc.zcore.holoAPI;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;

/**
 * @author michidk
 */

public class HoloManager implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        for (Holo holo:Holo.list) {
            holo.display(e.getPlayer());
        }
    }

    @EventHandler
    public void onWorldChange(PlayerChangedWorldEvent e) {
        for (Holo holo:Holo.list) {
            holo.display(e.getPlayer());
        }
    }
}
