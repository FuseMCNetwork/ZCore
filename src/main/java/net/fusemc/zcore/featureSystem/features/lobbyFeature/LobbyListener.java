package net.fusemc.zcore.featureSystem.features.lobbyFeature;

import net.fusemc.zcore.ZCore;
import net.fusemc.zcore.voteAPI.Vote;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.Inventory;

/**
 * @author michidk
 */

public class LobbyListener implements Listener
{

    private LobbySettings settings;
    private Vote vote;

    public void enable(LobbySettings settings, Vote vote)
    {
    	this.vote = vote;
        this.settings = settings;
        Bukkit.getPluginManager().registerEvents(this, ZCore.getInstance());

    }

    public void disable()
    {
        HandlerList.unregisterAll(this);
    }

    @EventHandler
    public void onPlayerLogin(PlayerLoginEvent e)
    {
        if (Bukkit.getOnlinePlayers().length + 1 >= settings.getMaxPlayers())
        {
            e.disallow(PlayerLoginEvent.Result.KICK_OTHER, "\u00A78\u00A7l[\u00A74\u2716\u00A78\u00A7l] \u00A7cDieser Server ist voll. Bitte versuche es erneut.");
            return;
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e)
    {
        Player p = e.getPlayer();

        ((CraftPlayer) p).getHandle().reset();

        //creative, but no fly
        if (!p.getGameMode().equals(GameMode.ADVENTURE)) p.setGameMode(GameMode.ADVENTURE);
        p.setAllowFlight(false);
        p.setFlying(false);

        p.setExp(0);
        p.setLevel(0);
        p.closeInventory();
        p.getInventory().clear();
        p.getInventory().setArmorContents(null);
        p.getActivePotionEffects().removeAll(p.getActivePotionEffects());
        p.setWalkSpeed(0.2f);
        p.setMaxHealth(20D);
        p.setHealth(20D);
        p.setHealth(20D);
        p.setFoodLevel(20);
        p.getInventory().setHeldItemSlot(0);

        p.setCompassTarget(settings.getWorld().getSpawnLocation());

        p.teleport(settings.getWorld().getSpawnLocation().getBlock().getLocation().add(0.5,0,0.5));

        setItems(p);
    }

	@SuppressWarnings("deprecation")
	public void setItems(Player p)
    {
        if (!settings.isEnableVote()) return;

        Inventory inv = p.getInventory();

        if(LobbyManager.guideManager.book != null) inv.setItem(0, LobbyManager.guideManager.book);
        inv.setItem(4, vote.getVoteItem());
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e)
    {
        e.setCancelled(true);
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageEvent e)
    {
        if (!e.getEntityType().equals(EntityType.PLAYER))
        {
            return;
        }

        if (e.getCause().equals(EntityDamageEvent.DamageCause.VOID) || e.getCause().equals(EntityDamageEvent.DamageCause.SUFFOCATION))
        {
            e.getEntity().teleport(settings.getWorld().getSpawnLocation());
        }

        e.setCancelled(true);
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e)
    {
        e.setDeathMessage("");
        e.setKeepLevel(false);
        e.setNewExp(0);
        e.setNewLevel(0);
        e.setNewTotalExp(0);
        e.setDroppedExp(0);

        if (!(e.getEntity() instanceof Player)) return;
        Player p = e.getEntity();
        p.setFlying(false);
        p.setAllowFlight(false);

    }

    @EventHandler
    public void onAttach(EntityDamageByEntityEvent e)
    {
        e.setCancelled(true);
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e)
    {
        if (e.getTo().getBlock().getType() == Material.PORTAL) e.getPlayer().kickPlayer("lobby");
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent e)
    {
        e.setRespawnLocation(settings.getWorld().getSpawnLocation());
        setItems(e.getPlayer());
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e)
    {
        e.setCancelled(true);
        e.getWhoClicked().closeInventory();
    }

    @EventHandler
    public void onPlayerDrop(PlayerDropItemEvent e)
    {
        e.getItemDrop().remove();
        setItems(e.getPlayer());
    }

    @EventHandler
    public void onPickUp(PlayerPickupItemEvent e)
    {
        e.setCancelled(true);
    }

    @EventHandler
    public void onFoodLevel(FoodLevelChangeEvent e)
    {
        e.setCancelled(true);
    }

    @EventHandler
    public void onPortal(PlayerPortalEvent e) {
        e.setCancelled(true);
    }

}
