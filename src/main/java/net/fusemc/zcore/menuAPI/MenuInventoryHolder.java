package net.fusemc.zcore.menuAPI;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

/**
 * Created by Niklas on 26.08.2014.
 */
public class MenuInventoryHolder implements InventoryHolder {
    private Menu menu;

    MenuInventoryHolder(Menu menu) {
        this.menu = menu;
    }

    public Menu getMenu() {
        return menu;
    }

    @Override
    public Inventory getInventory() {
        return null;
    }
}
