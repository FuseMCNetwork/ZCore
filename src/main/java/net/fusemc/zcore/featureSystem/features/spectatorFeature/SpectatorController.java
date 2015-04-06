package net.fusemc.zcore.featureSystem.features.spectatorFeature;

import com.google.common.collect.Lists;
import fr.neatmonster.nocheatplus.checks.CheckType;
import fr.neatmonster.nocheatplus.hooks.NCPExemptionManager;
import net.fusemc.zcore.ZCore;
import net.fusemc.zcore.util.LocationUtil;
import net.fusemc.zcore.util.NCPUtils;
import net.fusemc.zcore.util.NamedItem;
import net.fusemc.zcore.util.ReflectionUtil;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.PotionEffect;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class SpectatorController {

    private List<Player> spectator;
    private List<Player> playingPlayers;
    private SpectatorListener listener;
    private Location spawn;

    private Map<Integer, ItemStack> spectatorItems = new HashMap<>();

    public static final String TELEPORTERNAME = "\u00A78Teleporter";
    public static final String LOBBYNAME = "\u00A76Zur\u00FCck zur Lobby";

    public SpectatorController() {
        this(null);
    }

    public SpectatorController(Location spawn) {
        this.spawn = spawn;
        spectator = new ArrayList<>();
        playingPlayers = new ArrayList<>();
        for (Player p : Bukkit.getOnlinePlayers()) {
            playingPlayers.add(p);
        }
        listener = new SpectatorListener(this);
        Bukkit.getPluginManager().registerEvents(listener, ZCore.getInstance());

        NamedItem teleItem = new NamedItem(Material.COMPASS, TELEPORTERNAME);
        spectatorItems.put(0, teleItem);
        NamedItem lobbyItem = new NamedItem(Material.WATCH, LOBBYNAME);
        spectatorItems.put(8, lobbyItem);
    }

    public void addSpectator(Player p, boolean showRespawnDialog) {
        if (spectator.contains(p)) {
            return;
        }

        SpectatorEvent event = new SpectatorEvent(p);
        Bukkit.getPluginManager().callEvent(event);
        if (event.isCancelled())
            return;

        if (playingPlayers.contains(p)) {
            playingPlayers.remove(p);
        }

        spectator.add(p);
        updateCompassInventory();
        NCPExemptionManager.unexempt(p, CheckType.MOVING);

        //if (!p.getGameMode().equals(GameMode.CREATIVE)) p.setGameMode(GameMode.CREATIVE);
        if (!showRespawnDialog) {
            p.getInventory().clear();
            p.getInventory().setArmorContents(null);

            p.setHealth(20D);
            p.setFoodLevel(20);
            p.setAllowFlight(true);
            p.setFlying(true);
            p.setLevel(0);
            p.setExp(0);

            if (spawn != null) {
                p.teleport(LocationUtil.centerLocation(spawn));
            }

            for (PotionEffect effect : p.getActivePotionEffects()) {
                p.removePotionEffect(effect.getType());
            }

            giveItems(p);
        }

        setCollideable(p, false);

        for (Player player : Bukkit.getOnlinePlayers())
            player.hidePlayer(p);
    }

    public void removeSpectator(Player p) {
        if (!spectator.contains(p))
            return;

        spectator.remove(p);
        NCPExemptionManager.unexempt(p, CheckType.MOVING);

        p.setFlying(false);
        p.setAllowFlight(false);

        setCollideable(p, true);

        for (Player player : Bukkit.getOnlinePlayers())
            player.showPlayer(p);
    }

    public void giveItems(Player p) {
        PlayerInventory inv = p.getInventory();
        for (int slot : spectatorItems.keySet()) {
            inv.setItem(slot, spectatorItems.get(slot));
            //Bukkit.broadcastMessage("giving " + p.getName() + " a " + spectatorItems.get(slot).getType().toString());
        }
        p.updateInventory();
    }

    private static final Method COLLIDE_METHOD = ReflectionUtil.getMethod(ReflectionUtil.getCraftBukkitClass("entity.CraftEntity"), "getHandle");
    private static final Field COLLIDE_FIELD = ReflectionUtil.getField(ReflectionUtil.getMinecraftClass("EntityPlayer"), "collidesWithEntities");

    private void setCollideable(Player player, boolean collide) {
        Object nmsPlayer = null;
        try {
            nmsPlayer = COLLIDE_METHOD.invoke(player);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        if (nmsPlayer == null) return;
        try {
            COLLIDE_FIELD.set(nmsPlayer, collide);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public void setSeeEachOther(boolean cansee) {
        for (Player p : getSpectators()) {
            for (Player pe : getSpectators()) {
                if (cansee) {
                    pe.showPlayer(p);
                } else {
                    pe.hidePlayer(p);
                }

            }
        }
    }

    public void setVisible(boolean visible) {
        for (Player p : Bukkit.getOnlinePlayers()) {
            for (Player pe : getSpectators()) {
                if (visible) {
                    p.showPlayer(pe);
                } else {
                    p.hidePlayer(pe);
                }
            }
        }
    }

    private Inventory compassInventory;
    public Inventory getCompassInventory() {
        if (spectatorItems != null) return compassInventory;
        updateCompassInventory();
        return compassInventory;
    }

    public void updateCompassInventory() {
        if (compassInventory == null) compassInventory = Bukkit.createInventory(null, 27, TELEPORTERNAME);
        compassInventory.clear();
        int c = 0;
        for (Player p:playingPlayers) {
            NamedItem ni = new NamedItem(Material.SKULL_ITEM, 1, (short)SkullType.PLAYER.ordinal(), ChatColor.DARK_AQUA + p.getName());
            SkullMeta skull = (SkullMeta) ni.getItemMeta();
            skull.setOwner(p.getName());
            ni.setItemMeta(skull);
            compassInventory.setItem(c,ni);
            c++;
        }
    }

    public static Player getRandomPlayerWithoutACertainPlayer(Player p) {
        List<Player> players = Lists.newArrayList(Bukkit.getOnlinePlayers());
        players.remove(p);
        if (players.size() <= 0) {
            return null;
        }
        int r = new Random().nextInt(players.size());
        return players.get(r);
    }

    public void setItem(int slot, ItemStack item) {
        spectatorItems.put(slot, item);
    }

    public void removeItem(int slot) {
        if (spectatorItems.containsKey(slot)) spectatorItems.remove(slot);
    }

    public void unregister() {
        HandlerList.unregisterAll(listener);
    }

    public List<Player> getSpectators() {
        return spectator;
    }

    public List<Player> getPlayingPlayers() {
        return playingPlayers;
    }

    public Location getSpawn() {
        return spawn;
    }
}
