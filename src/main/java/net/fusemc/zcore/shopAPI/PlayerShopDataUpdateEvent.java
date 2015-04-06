package net.fusemc.zcore.shopAPI;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Created by Niklas on 21.08.2014.
 */
public class PlayerShopDataUpdateEvent extends Event {
    private static final HandlerList handlers = new HandlerList();

    private final Player player;

    public PlayerShopDataUpdateEvent(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

    public HandlerList getHandlers() {
        return handlers;
    }
    public static HandlerList getHandlerList() {
        return handlers;
    }
}
