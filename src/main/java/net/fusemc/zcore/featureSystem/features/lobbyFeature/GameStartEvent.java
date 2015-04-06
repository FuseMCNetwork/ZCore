package net.fusemc.zcore.featureSystem.features.lobbyFeature;

import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class GameStartEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    
    private World world;
    private MapInfo mapInfo;
    private Player minigameWinner;

    public GameStartEvent()
    {

    }
    
    public GameStartEvent(World world, MapInfo mapInfo, Player minigameWinner){
    	this.world = world;
        this.mapInfo = mapInfo;
        this.minigameWinner = minigameWinner;
    }


	public HandlerList getHandlers() {
		return handlers;
	}
	public static HandlerList getHandlerList() {
        return handlers;
    }

    public void setMapInfo(MapInfo mapInfo)
    {
        this.mapInfo = mapInfo;
    }

    public World getWorld()
    {
        return world;
    }

    public MapInfo getMapInfo()
    {
        return mapInfo;
    }

    public Player getMinigameWinner() {
        return minigameWinner;
    }
}


