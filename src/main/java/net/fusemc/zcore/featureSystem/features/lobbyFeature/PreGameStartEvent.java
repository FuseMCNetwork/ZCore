package net.fusemc.zcore.featureSystem.features.lobbyFeature;

import org.bukkit.ChatColor;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.ArrayList;
import java.util.List;

/**
 * @author michidk
 */

public class PreGameStartEvent extends Event
{

    private static final HandlerList handlers = new HandlerList();


    public boolean loadWorld = true;
    private boolean broadcastMessages = true;
    private List<String> messages = new ArrayList<String>();


    public PreGameStartEvent(List<String> messages)
    {
        this.messages = messages;
    }



    public HandlerList getHandlers() {
        return handlers;
    }
    public static HandlerList getHandlerList() {
        return handlers;
    }

    public void setLoadWorld(boolean loadWorld)
    {
        this.loadWorld = loadWorld;
    }
    public boolean isLoadWorld()
    {
        return loadWorld;
    }

    public void setBroadcastMessages(boolean broadcastMessages)
    {
        this.broadcastMessages = broadcastMessages;
    }

    public boolean isBroadcastMessages()
    {
        return broadcastMessages;
    }

    public List<String> getMessages()
    {
        return messages;
    }

    public void addMessage(String message)
    {
        this.messages.add(ChatColor.translateAlternateColorCodes('&', message));
    }
}
