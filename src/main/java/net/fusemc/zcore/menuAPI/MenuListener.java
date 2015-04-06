package net.fusemc.zcore.menuAPI;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

/**
 * Created by Niklas on 26.08.2014.
 */
public class MenuListener implements Listener {
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Inventory inventory = event.getInventory();

        if (inventory.getHolder() instanceof MenuInventoryHolder) {
            MenuInventoryHolder inventoryHolder = (MenuInventoryHolder)inventory.getHolder();
            MenuItem item = inventoryHolder.getMenu().getItem(event.getSlot());
            if (item != null) item.onClick();
            event.setCancelled(true);
        }
    }
}
