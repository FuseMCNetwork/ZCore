package net.fusemc.zcore.menuAPI;

import com.google.common.collect.Lists;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Niklas on 26.08.2014.
 */
public class Menu {
    private final Player player;
    private final Map<Integer, MenuItem> menuItems;
    private final Inventory inventory;

    public Menu(Player player, MenuItem[] items, String title, int lines) {
        this(player, Lists.newArrayList(items), title, lines);
    }

    public Menu(Player player, List<MenuItem> items, String title, int lines) {
        this.player = player;
        this.menuItems = new HashMap<>(lines*9);
        this.inventory = Bukkit.createInventory(new MenuInventoryHolder(this), lines*9, title);
        addItems(items);
    }

    public void openInventory() {
        player.openInventory(inventory);
    }

    public void addItem(MenuItem item) {
        if (item.getSlot() < 0) {
            int slot = freeSlot();
            if (slot >= 0) {
                item.setSlot(slot);
                menuItems.put(slot, item);
            }
        } else {
            menuItems.put(Integer.valueOf(item.getSlot()), item);
        }
        updateInventory();
    }

    public void addItems(List<MenuItem> items) {
        for (MenuItem item : items) {
            if (item.getSlot() < 0) {
                int slot = freeSlot();
                if (slot >= 0) {
                    item.setSlot(slot);
                    menuItems.put(slot, item);
                }
            } else {
                menuItems.put(Integer.valueOf(item.getSlot()), item);
            }
        }
        updateInventory();
    }

    public void removeItem(MenuItem item) {
        menuItems.remove(item);
        updateInventory();
    }

    public void clearInventory() {
        menuItems.clear();
        updateInventory();
    }

    public MenuItem getItem(int slot) {
        return menuItems.get(Integer.valueOf(slot));
    }

    public MenuItem[] items() {
        return menuItems.values().toArray(new MenuItem[menuItems.size()]);
    }

    public Player getPlayer() {
        return player;
    }

    private void updateInventory() {
        inventory.clear();
        for (MenuItem item : menuItems.values()) {
            inventory.setItem(item.getSlot(), item.getItem());
        }
    }

    private int freeSlot() {
        for (int i = 0; i < menuItems.size(); i++) {
            if (menuItems.get(Integer.valueOf(i)) == null) return i;
        }
        return -1;
    }
}