package net.fusemc.zcore.featureSystem.features.spectatorFeature;

import com.google.common.collect.Lists;
import net.fusemc.zcore.util.LocationUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;

import java.util.List;
import java.util.Random;

public class SpectatorListener implements Listener {

    private SpectatorController controller;

    public SpectatorListener(SpectatorController controller) {
        this.controller = controller;
    }

    @EventHandler
    public void onHunger(FoodLevelChangeEvent e) {
        if (controller.getSpectators().contains(e.getEntity())) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onGetDamage(EntityDamageEvent e) {
        if (controller.getSpectators().contains(e.getEntity())) {
            e.setCancelled(true);

            if (e.getCause() == EntityDamageEvent.DamageCause.VOID) {
                Player random = SpectatorController.getRandomPlayerWithoutACertainPlayer((Player) e.getEntity());
                if (controller.getSpawn() != null || random == null) {
                    e.getEntity().teleport(controller.getSpawn());
                } else {
                    e.getEntity().teleport(random);
                }
            }
        }
    }

    @EventHandler
    public void onHitPlayer(EntityDamageByEntityEvent e) {
        if (controller.getSpectators().contains(e.getDamager())){
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        if (controller.getSpectators().contains(e.getPlayer())) {
            e.setCancelled(true);

            if (e.getItem() == null || e.getItem().getItemMeta() == null || e.getItem().getItemMeta().getDisplayName() == null) return;
            String itemName = e.getItem().getItemMeta().getDisplayName();
            if (itemName.equals(SpectatorController.LOBBYNAME)) {
                e.getPlayer().kickPlayer("lobby");
            } else if (itemName.equals(SpectatorController.TELEPORTERNAME)) {
                e.getPlayer().openInventory(controller.getCompassInventory());
            }
        }
    }

    @EventHandler
    public void onItemDrop(PlayerDropItemEvent e) {
        if (controller.getSpectators().contains(e.getPlayer())) {
            e.getItemDrop().remove();
            controller.giveItems(e.getPlayer());
        }
    }

    @EventHandler
    public void onPickUp(PlayerPickupItemEvent e) {
        if (controller.getSpectators().contains(e.getPlayer()))
            e.setCancelled(true);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (controller.getSpectators().contains((Player) e.getWhoClicked())) {
            if (e.getClickedInventory().getName() != null && e.getClickedInventory().getName().equals(SpectatorController.TELEPORTERNAME)) {
                if (e.getCurrentItem() != null && e.getCurrentItem().getItemMeta() != null && e.getCurrentItem().getItemMeta().getDisplayName() != null) {
                    e.getWhoClicked().teleport(Bukkit.getPlayerExact(ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName())).getLocation());
                }
            }
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onItemConsume(PlayerItemConsumeEvent e)
    {
        if (controller.getSpectators().contains(e.getPlayer())) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerXPChange(PlayerExpChangeEvent e)
    {
        if (controller.getSpectators().contains(e.getPlayer())) {
            e.setAmount(0);
        }
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent e) {
        if (controller.getSpectators().contains(e.getPlayer())) {
            Player p = e.getPlayer();

            p.setAllowFlight(true);
            p.setFlying(true);

            controller.giveItems(p);

            if (controller.getSpawn() != null) {
                e.setRespawnLocation(LocationUtil.centerLocation(controller.getSpawn()));
            } else {
                e.setRespawnLocation(p.getLocation());
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerQuit(PlayerQuitEvent e) {
        if (controller.getSpectators().contains(e.getPlayer())) {
            controller.getSpectators().remove(e.getPlayer());
        }
        if (controller.getPlayingPlayers().contains(e.getPlayer())) {
            controller.getPlayingPlayers().remove(e.getPlayer());
        }
        controller.updateCompassInventory();
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onKick(PlayerKickEvent e) {
        if (controller.getSpectators().contains(e.getPlayer())) {
            controller.getSpectators().remove(e.getPlayer());
        }
        if (controller.getPlayingPlayers().contains(e.getPlayer())) {
            controller.getPlayingPlayers().remove(e.getPlayer());
        }
        controller.updateCompassInventory();
    }

    //if player joins hide all spetators for him
    // BUT
    //he is a playing player
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        controller.getPlayingPlayers().add(e.getPlayer());
        for (Player p : controller.getSpectators()) {
            e.getPlayer().hidePlayer(p);
        }
    }

    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        if(controller.getSpectators().contains(event.getPlayer())) {
            event.setCancelled(true);
        }
    }

}
