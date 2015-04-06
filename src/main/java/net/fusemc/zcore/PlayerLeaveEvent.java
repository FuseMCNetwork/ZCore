package net.fusemc.zcore;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

/**
 * Copyright by michidk
 * Created: 14.08.2014.
 */
public class PlayerLeaveEvent extends PlayerEvent {

    private static final HandlerList handlers = new HandlerList();

    private String leaveMessage;
    private String reason;

    public PlayerLeaveEvent(Player p, String leaveMessage, String reason) {
        super(p);
        this.leaveMessage = leaveMessage;
        this.reason = reason;
    }

    public String getLeaveMessage() {
        return leaveMessage;
    }

    public String getReason() {
        return reason;
    }

    public HandlerList getHandlers() {
        return handlers;
    }
    public static HandlerList getHandlerList() {
        return handlers;
    }
}
