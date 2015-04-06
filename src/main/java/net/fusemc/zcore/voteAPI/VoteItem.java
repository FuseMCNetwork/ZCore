package net.fusemc.zcore.voteAPI;

import net.fusemc.zcore.util.NamedItem;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright by michidk
 * Created: 21.10.2014.
 */
public class VoteItem extends NamedItem {

    private static final String ITEM_COLOR = ChatColor.BLUE + "";
    private static final String SB_COLOR = ChatColor.AQUA + "";
    private static final String SB_PREFIX = SB_COLOR + "\u2022 ";

    private String rawName;
    private int votes = 0;

    private List<String> doubleVoted = new ArrayList<>();

    /**
     * Instatiate a new VoteItem with paper as material
     * @param name      the name of the option
     */
    public VoteItem(String name) {
        this(Material.PAPER, ITEM_COLOR +name);
    }

    /**
     * Instatiate a new VoteItem
     * @param material  the material in the chestInventory
     * @param name      the name of the option
     */
    public VoteItem(Material material, String name) {
        super(material, ITEM_COLOR + name);
        rawName = name;

        updateAmount();
    }

    /**
     * add a single vote
     */
    protected void addVote() {
        votes ++;
        updateAmount();
    }

    /**
     * add a vote that counts double
     * we need that for the reason, the player will be removed from premium list while voting
     * @param player    the player that voted
     */
    protected void addDoubleVote(Player player) {
        String uuid = player.getUniqueId().toString();
        votes += 2;
        doubleVoted.add(uuid);
        updateAmount();
    }

    /**
     * removed a single vote
     * WARNING: this won't remove double-votes correctly
     */
    protected void removeVote() {
        if (votes >= 1) {
            votes --;
            updateAmount();
        }
    }

    /**
     * removed a vote, catches the case that the player voted double
     * @param player    the player that unvoted
     */
    protected void removeVote(Player player) {
        String uuid = player.getUniqueId().toString();
        if (doubleVoted.contains(uuid)) {
            doubleVoted.remove(uuid);
            if (votes >= 2) {
                votes -= 2;
            } else if (votes >= 1) {
                votes --;
            } else {
                return;
            }
            updateAmount();
        } else {
            removeVote();
        }
    }

    protected void updateAmount() {
        if (votes <= 0) {
            setAmount(1);
        } else {
            setAmount(votes);
        }
        this.setLore(0, ChatColor.DARK_PURPLE + "Votes: " + ChatColor.LIGHT_PURPLE + votes);
    }

    /**
     * @return      the name displayed in the scoreboard
     */
    public String getScoreboardName() {
        return SB_PREFIX + rawName;
    }

    /**
     * @return      the displayname of the item
     */
    public String getItemName() {
        return getName();
    }

    /**
     * @return      the unformatted name
     */
    public String getName() {
        return rawName;
    }

    public int getVotes() {
        return votes;
    }

    public List<String> getDoubleVoted() {
        return doubleVoted;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof VoteItem)) return false;
        if (obj == this) return true;

        VoteItem other = (VoteItem) obj;
        if (other.getName().equalsIgnoreCase(this.getName())) {
            return true;
        } else {
            return false;
        }
    }

}
