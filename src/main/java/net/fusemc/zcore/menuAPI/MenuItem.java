package net.fusemc.zcore.menuAPI;

import org.bukkit.inventory.ItemStack;

/**
 * Created by Niklas on 26.08.2014.
 */
public abstract class MenuItem {
    private int slot;

    public MenuItem(int slot) {
        this.slot = slot;
    }

    public abstract void onClick();
    public abstract ItemStack getItem();

    public int getSlot() {
        return slot;
    }

    void setSlot(int slot) {
        this.slot = slot;
    }
}
