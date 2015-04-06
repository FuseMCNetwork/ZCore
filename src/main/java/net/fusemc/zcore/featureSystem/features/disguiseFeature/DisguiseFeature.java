package net.fusemc.zcore.featureSystem.features.disguiseFeature;

import me.johnking.jlib.JLib;
import me.johnking.jlib.protocol.PacketType;
import net.fusemc.zcore.featureSystem.Feature;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class DisguiseFeature extends Feature {

    private DisguiseController controller;

    public DisguiseFeature() {
        this.controller = new DisguiseController();
    }

    @Override
    public boolean enable() {
        if(super.enable()) {
            JLib.getProtocolManager().registerListener(this.controller, PacketType.Server.NAMED_ENTITY_SPAWN);
            return true;
        }
        return false;
    }

    @Override
    public boolean disable() {
        if(super.disable()) {
            JLib.getProtocolManager().unregisterListener(this.controller);
            this.controller.unDisguiseAll();
            return true;
        }
        return false;
    }

    @EventHandler (priority = EventPriority.LOWEST)
    public void onLogin(PlayerLoginEvent event) {
        controller.handleLogin(event.getPlayer());
    }

    @EventHandler (priority = EventPriority.MONITOR)
    public void onQuit(PlayerQuitEvent event){
        controller.handleQuit(event.getPlayer());
    }

    @EventHandler (priority = EventPriority.MONITOR)
    public void onKick(PlayerKickEvent event){
        controller.handleQuit(event.getPlayer());
    }

    public boolean isDisguised(Player player){
        return controller.isDisguised(player);
    }
}
