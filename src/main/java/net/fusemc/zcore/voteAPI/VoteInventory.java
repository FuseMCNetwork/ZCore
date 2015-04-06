package net.fusemc.zcore.voteAPI;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

/**
 * Copyright by michidk
 * Created: 21.10.2014.
 */
public class VoteInventory {

    private String title;
    private Vote vote;

    private int size;
    private Inventory chestInventory;

    public VoteInventory(String title, Vote vote) {
        this.title = title;
        this.vote = vote;

        size = (((vote.getVoteItems().size() -1)/5) * 2) + 1;
    }

    public Inventory getChestInventory() {
        if (chestInventory == null) {
            updateChestInventory();
        }
        return chestInventory;
    }

    public void updateChestInventory() {
        chestInventory = Bukkit.createInventory(null, size * 9, title);
        for (VoteItem option : vote.getVoteItems()) {
            int slot = getSlot(vote.getVoteItems().indexOf(option), vote.getVoteItems().size());
            chestInventory.setItem(slot, option);
        }
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (p.getOpenInventory().getTopInventory().getTitle().equals(title)) {
                p.getOpenInventory().close();
                p.openInventory(chestInventory);
            }
        }
    }

    /**
     * slot algorithm
     * @author johnking
     * @param pos   the current slot
     * @param max   the sum of all slots
     * @return      the slot, the item should be placed in
     */
    public int getSlot(int pos, int max){
        int size = ((max-1) / 5) + 1;
        int per = (max+size-1)/size;
        int y = (pos/per);
        int row = pos - (y*per);
        int x = (5 - per) + (row * 2);
        return (y*18) + x;
    }

    public String getTitle() {
        return title;
    }

    public String getChestTitle() {
        return title;
    }

    public Vote getVote() {
        return vote;
    }

    public int getSize() {
        return size;
    }
}
