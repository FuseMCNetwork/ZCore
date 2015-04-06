package net.fusemc.zcore.selectAPI;

import org.bukkit.entity.HumanEntity;
import org.bukkit.event.HandlerList;

/**
 * Copyright by michidk
 * Created: 21.09.2014.
 */
public class BasicSelectEvent extends SelectEvent {

    private static HandlerList handlers = new HandlerList();

    public BasicSelectEvent(String title, HumanEntity who, SelectItem selected, SelectItem unselected) {
        super(title, who, selected, unselected);
    }

    @Override
    public SelectItem getSelected() {
        return null;
    }

    @Override
    public SelectItem getUnselected() {
        return null;
    }

    @Override
    public HandlerList getHandlers() {
        return null;
    }

    public static HandlerList getHandlerList(){
        return handlers;
    }

}
