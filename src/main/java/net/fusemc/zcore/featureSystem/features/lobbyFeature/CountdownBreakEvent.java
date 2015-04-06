package net.fusemc.zcore.featureSystem.features.lobbyFeature;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class CountdownBreakEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    public CountdownBreakEvent() {}

	public HandlerList getHandlers() {
		return handlers;
	}
	public static HandlerList getHandlerList() {
        return handlers;
    }
}


