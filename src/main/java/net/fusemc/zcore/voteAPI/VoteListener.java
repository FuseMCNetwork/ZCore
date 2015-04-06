package net.fusemc.zcore.voteAPI;

import net.fusemc.zcore.PlayerLeaveEvent;
import net.fusemc.zcore.ZCore;
import net.fusemc.zcore.util.Sounds;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

/**
 * Copyright by michidk
 * Created: 21.10.2014.
 */
public class VoteListener implements Listener {

    private Vote vote;

    public VoteListener(Vote vote) {
        this.vote = vote;
    }

    public void register() {
        Bukkit.getPluginManager().registerEvents(this, ZCore.getInstance());
    }

    public void unregister() {
        HandlerList.unregisterAll(this);
    }

    @EventHandler   //player click -> vote
    public void onInventoryClick(InventoryClickEvent e){
        if (e.getClickedInventory() == null || e.getClickedInventory().getTitle() == null) return;
        if (!(e.getClickedInventory().getTitle().equals(vote.getInventory().getChestTitle()))) return;
        if (e.getCurrentItem() == null || e.getCurrentItem().getItemMeta() == null || e.getCurrentItem().getType() == Material.AIR) return;

        Player p = (Player) e.getWhoClicked();
        String uuid = p.getUniqueId().toString();

        ItemStack current = e.getCurrentItem();
        String currentName = ChatColor.stripColor(current.getItemMeta().getDisplayName());

        if (vote.getVotes().containsKey(uuid)) {
            VoteItem old = vote.getVotes().get(uuid);
            if (old.getItemName().equals(currentName)) {   //already voted
                p.sendMessage("\u00A78\u00A7l[\u00A72\u2714\u00A78\u00A7l]\u00A7a Du hast bereits f\u00FCr\u00A76 " + currentName + " \u00A7agestimmt!");
                return;
            } else {    //changed vote
                p.sendMessage("\u00A78\u00A7l[\u00A72\u2714\u00A78\u00A7l]\u00A7a Auf\u00A76 " + currentName + " \u00A7aumgestimmt!");
            }
        } else {    //voted
            p.sendMessage("\u00A78\u00A7l[\u00A72\u2714\u00A78\u00A7l]\u00A7a F\u00FCr\u00A76 " + currentName + " \u00A7agestimmt!");
        }

        p.closeInventory();
        Sounds.SELECT.play(p);
        vote.vote(p, currentName);
    }

    @EventHandler   //player click -> open vote-inventory
    public void onPlayerInteract(PlayerInteractEvent e){
        Player p = e.getPlayer();

        if (e.getAction().equals(Action.PHYSICAL)) return;

        if (p.getItemInHand() == null) return;

        ItemStack clicked = p.getItemInHand();

        if (clicked.getItemMeta() == null) return;

        if (clicked.getItemMeta().getDisplayName() == null) return;

        String clickedName = clicked.getItemMeta().getDisplayName();

        if(clickedName.equals(vote.getVoteItemName())){
            e.setCancelled(true);
            p.openInventory(vote.getInventory().getChestInventory());
            Sounds.OPEN.play(p);
        }
    }

    @EventHandler   //remove votes on leave
    public void onLeave(PlayerLeaveEvent e) {
        Player player = e.getPlayer();
        String uuid = player.getUniqueId().toString();

        VoteItem old = vote.getVotes().get(uuid);
        if (old == null) return;
        old.removeVote(player);

        vote.getInventory().updateChestInventory();
        vote.getScoreboard().updateVotes(old);
    }

}
