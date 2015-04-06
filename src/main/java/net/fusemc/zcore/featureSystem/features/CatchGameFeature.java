package net.fusemc.zcore.featureSystem.features;

import com.google.common.collect.Lists;
import me.michidk.DKLib.Cooldown;
import me.michidk.DKLib.effects.ParticleEffect;
import net.fusemc.zcore.ZCore;
import net.fusemc.zcore.featureSystem.Feature;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

/**
 * @author michidk
 */

public class CatchGameFeature extends Feature {

    private static final int COOLDOWN = 30;

    private Player owner;
    private HashMap<String, Cooldown> cooldownMap = new HashMap<>();
    public static final String HEADNAME = "\u00A76Der goldene Kopf";


    @Override
    public boolean enable() {
        Bukkit.getWorlds().get(0).setPVP(true);
        return super.enable();
    }

    @Override
    public boolean disable() {
        return super.disable();
    }

    public Player getWinner() {
        return owner;
    }

    private static ItemStack block = null;
    public static ItemStack getBlock() {
        if (block == null) {
            ItemStack i = new ItemStack(Material.GOLD_BLOCK);
            ItemMeta m = i.getItemMeta();
            m.setDisplayName(HEADNAME);

            m.setLore(Arrays.asList(
                    "\u00A78idea by CrushedPixel.eu"
            ));

            i.setItemMeta(m);
            return i;
        } else {
            return block;
        }
    }

    private static ItemStack invBlock = null;
    public static ItemStack getInvBlock() {
        if (invBlock == null) {
            ItemStack i = new ItemStack(Material.GOLD_BLOCK);
            ItemMeta m = i.getItemMeta();
            m.setDisplayName(HEADNAME);

            m.setLore(Arrays.asList(
                    "\u00A73Du besitzt gerade den goldenen Kopf!"
            ));

            i.setItemMeta(m);
            return i;
        } else {
            return invBlock;
        }
    }

    /*  no rightclick because of clickspam
    @EventHandler
    public void onInteract(PlayerInteractEntityEvent e) {
        if (!(e.getRightClicked() instanceof Player)) return;

        if (e.getRightClicked().equals(owner)) {
            changeOwner(e.getPlayer(), false);
        }
    }
    */

    //change owner
    @EventHandler
    public void onDamage(EntityDamageByEntityEvent e) {
        if (!(e.getEntity() instanceof Player)) return;
        if (!(e.getDamager() instanceof Player)) return;

        Player victim = (Player) e.getEntity();
        Player attacker = (Player) e.getDamager();

        if (victim.equals(owner)) {
            changeOwner(attacker, false);
        }

    }

    //if no one have the block, give it the owner
    @EventHandler(priority = EventPriority.HIGH)
    public void onJoin(PlayerJoinEvent e) {
        if (owner != null) return;

        changeOwner(e.getPlayer(), true);

    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        if (!(e.getPlayer().equals(owner))) return;
        //System.out.println("in quit event: " + Bukkit.getOnlinePlayers().length + " players online");
        //result: 2 players were online, one left: 2 players on
        if (Bukkit.getOnlinePlayers().length <= 1) {
            changeOwner(null, true);
        } else {
            changeOwner(getRandomPlayerWithoutACertainPlayer(e.getPlayer()), true);
        }
    }

    @EventHandler
    public void onKick(PlayerKickEvent e)
    {
        if (!(e.getPlayer().equals(owner))) return;

        if (Bukkit.getOnlinePlayers().length <= 1) {
            changeOwner(null, true);
        } else {
            changeOwner(getRandomPlayerWithoutACertainPlayer(e.getPlayer()), true);
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onDrop(PlayerDropItemEvent e)
    {
        if (e.getItemDrop().getItemStack().getItemMeta() == null) return;
        if (!e.getItemDrop().getItemStack().getItemMeta().getDisplayName().equals(HEADNAME)) return;

        Player p = e.getPlayer();
        e.getItemDrop().remove();
        p.getInventory().setItem(8, getInvBlock());
    }

    @EventHandler
    public void onPlace(PlayerInteractEntityEvent e) {
        if (e.getRightClicked() == null || e.getPlayer() == null || e.getPlayer().getItemInHand() == null || e.getPlayer().getItemInHand().getItemMeta() == null || e.getPlayer().getItemInHand().getItemMeta().getDisplayName() == null) return;
        if (!(e.getRightClicked() instanceof Player)) return;

        if (e.getPlayer().getItemInHand().getItemMeta().getDisplayName().equals(HEADNAME)) {
            e.setCancelled(true);
        }
    }

    //change the owner
    public void changeOwner(Player newOwner, boolean ignoreCooldown) {

        if (owner == newOwner) {
            //System.out.println("[CatchGame] owner == newOwner: " + newOwner.getName());
            return;
        }

        //check for cooldown
        if (!ignoreCooldown) {
            if (newOwner != null && cooldownMap.containsKey(newOwner.getUniqueId().toString())) {
                if (!cooldownMap.get(newOwner.getUniqueId().toString()).isOver()) {
                    //System.out.println("[CatchGame] cooldown not over: " + newOwner.getName());
                    return;
                }
            }
        }

        //debug
        String oldOwnerString;
        String newOwnerString;
        if (owner == null) oldOwnerString = "null";
        else oldOwnerString = owner.getName();
        if (newOwner == null) newOwnerString = "null";
        else newOwnerString = newOwner.getName();

        //System.out.println("[CatchGame] owner was: " + oldOwnerString + "; new owner is: " + newOwnerString);


        //if another player was the owner before restore the head from slot 27
        if (owner != null) {
            PlayerInventory inv = owner.getInventory();

            //27 to helment
            ItemStack head = inv.getItem(27);
            if (head == null) head = new ItemStack(Material.AIR);
            inv.setHelmet(head);
            inv.setItem(8, new ItemStack(Material.AIR));

            owner.getWorld().playSound(owner.getLocation(), Sound.CHICKEN_EGG_POP, 3, 0);
            ParticleEffect.ANGRY_VILLAGER.play(owner.getLocation().add(0, 2, 0), 0, 0, 0, 1, 3);
            owner = null;
        }

        //place old helmet in slot 27, and set goldblock
        if (newOwner != null) {

            PlayerInventory inv = newOwner.getInventory();

            //helmet to 27
            ItemStack head = inv.getHelmet();
            if (head == null) head = new ItemStack(Material.AIR);
            inv.setItem(27, head);

            inv.setHelmet(getBlock());
            inv.setItem(8, getInvBlock());

            owner = newOwner;
            cooldownMap.put(newOwner.getUniqueId().toString(), new Cooldown(ZCore.getInstance(), COOLDOWN));
        } else {
            owner = null;
        }

    }

    public static Player getRandomPlayer() {
        int r = new Random().nextInt(Bukkit.getOnlinePlayers().length);
        return Bukkit.getOnlinePlayers()[r];
    }

    public static Player getRandomPlayerWithoutACertainPlayer(Player p) {
        List<Player> players = Lists.newArrayList(Bukkit.getOnlinePlayers());
        players.remove(p);
        int r = new Random().nextInt(players.size());
        return players.get(r);
    }

}
