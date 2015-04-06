package net.fusemc.zcore.featureSystem.features.lobbyFeature;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class CountdownTickEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private int time;

    public CountdownTickEvent() {
    }

    public CountdownTickEvent(int time){
        this.time = time;
    }

	public HandlerList getHandlers() {
		return handlers;
	}
	public static HandlerList getHandlerList() {
        return handlers;
    }

    public int getTime()
    {
        return time;
    }
}


