package net.fusemc.zcore.selectAPI;

import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.ItemStack;

/**
 * Copyright by michidk
 * Created: 21.09.2014.
 */
public class BasicSelection extends Selection {

    public BasicSelection(String title, ItemStack open, SelectItem[] parts) {
        super(title, open, parts);
    }

    @Override
    public SelectEvent call(String title, HumanEntity he, SelectItem select, SelectItem unselect) {
        BasicSelectEvent event = new BasicSelectEvent(title, he, select, unselect);
        Bukkit.getPluginManager().callEvent(event);
        return event;
    }

}
