package net.fusemc.zcore.featureSystem.features.jumppadFeature;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
 
public class JumpPadEvent extends PlayerEvent implements Cancellable
{
 
    private static final HandlerList handlers = new HandlerList();

    private double height;
    private double length;
    private boolean cancel;
 
    public JumpPadEvent(Player who, double length,double height)
    {
        super(who);
 
        this.height = height;
        this.length = length;
    }
 
    @Override
    public boolean isCancelled()
    {
        return cancel;
    }
 
    @Override
    public void setCancelled(boolean cancel)
    {
        this.cancel = cancel;
    }
 
    @Override
    public HandlerList getHandlers()
    {
        return handlers;
    }
 
    public static HandlerList getHandlerList()
    {
        return handlers;
    }

    public double getHeight()
    {
        return height;
    }

    public double getLength()
    {
        return length;
    }
    
    public void setHeight(double height){
    	this.height = height;
    }
    
    public void setLength(double length){
    	this.length = length;
    }
}