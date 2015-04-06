package net.fusemc.zcore.menuAPI;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

/**
 * Created by Niklas on 26.08.2014.
 */
public class SimpleMenuItem extends MenuItem {
    private ItemStack item;

    public SimpleMenuItem(int slot, ItemStack item) {
        super(slot);
        this.item = item;
    }

    public SimpleMenuItem(ItemStack item) {
        this(-1, item);
    }

    public SimpleMenuItem(Material material) {
        this(-1, material);
    }

    public SimpleMenuItem(Material material, int amount) {
        this(-1, material, amount, (short) 0);
    }

    public SimpleMenuItem(Material material, int amount, short damage) {
        this(-1, material, amount, damage);
    }

    public SimpleMenuItem(int slot, Material material) {
        this(slot, material, 1);
    }

    public SimpleMenuItem(int slot, Material material, int amount) {
        this(slot, material, amount, (short) 0);
    }

    public SimpleMenuItem(int slot, Material material, int amount, short damage) {
        this(slot, new ItemStack(material, amount, damage));
    }

    @Override
    public void onClick() {}

    @Override
    public ItemStack getItem() {
        return item;
    }
}
