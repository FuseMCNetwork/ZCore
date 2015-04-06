package net.fusemc.zcore.voteAPI;

import net.fusemc.zcore.rankSystem.Rank;
import net.fusemc.zcore.util.NamedItem;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class Vote implements Listener {
	
	private String title;
    private boolean useScoreboard;
    private boolean useRandom;

    private static final String VOTEITEM_PREFIX =  "\u00A7a";
    private static final String VOTEITEM_SUFFIX = "";
    private static final String RANDOM = "Zufall";

    private ItemStack voteItem;

    private List<VoteItem> allItems;    //only used if useRandom = true
    private List<VoteItem> voteItems;
	private Map<String, VoteItem> votes = new HashMap<>();

    private VoteListener listener;
    private VoteScoreboard scoreboard;
    private VoteInventory inventory;

    /**
     * Creates a new full-managed vote
     * @param title             the title of the vote (will be displayed in the scoreboard, inventory and itemname)
     * @param voteItemArray     the items for which you can vote AS ARRAY
     * @param useScoreboard     should we show the stats in the scoreboard
     * @param useRandom         should we provide an extra option to vote for a random map
     */
    public Vote(String title, VoteItem[] voteItemArray, boolean useScoreboard, boolean useRandom) {
        if(voteItemArray.length <= 0) {
            throw new IllegalArgumentException("Not enough parts [have to >0]!");
        }

        this.title = title;

        this.useScoreboard = useScoreboard;
        this.useRandom = useRandom;

        List<VoteItem> voteItemList = Arrays.asList(voteItemArray);
        //shuffle the list
        Collections.shuffle(voteItems);
        //create & clone list
        this.allItems = new ArrayList<>(voteItems);

        if (useRandom) {
            //add the random item
            voteItems.add(0, new VoteItem(Material.EYE_OF_ENDER, RANDOM));
            //cut the list
            this.voteItems = voteItems.subList(0, 4);   //4 because of the random item
        } else {
            this.voteItems = voteItemList;
        }

        init();
    }

    /**
     * Creates a new full-managed vote
     * @param title             the title of the vote (will be displayed in the scoreboard, inventory and itemname)
     * @param voteItems           the voteItems for which you can vote AS LIST
     * @param useScoreboard     should we show the stats in the scoreboard
     * @param useRandom         should we provide an extra option to vote for a random map
     */
    public Vote(String title, List<VoteItem> voteItems, boolean useScoreboard, boolean useRandom) {
        if(voteItems.size() <= 0) {
            throw new IllegalArgumentException("Not enough parts [have to >0]!");
        }

        this.title = title;
        this.useScoreboard = useScoreboard;
        this.useRandom = useRandom;

        //shuffle the list
        Collections.shuffle(voteItems);
        //create & clone list
        this.allItems = new ArrayList<>(voteItems);

        if (useRandom) {
            //create the random item
            VoteItem random = new VoteItem(Material.EYE_OF_ENDER, RANDOM);
            random.setLore(1, "  \u00A7r\u00A77\u00A7oWenn dir keine von den drei Maps gef\u00E4llt");
            random.setLore(2, "  \u00A7r\u00A77\u00A7ostimme f\u00FCr \u00A7r\u00A79\u00A7oZufall\u00A7r\u00A77\u00A7o um eine zuf\u00E4llige Map");
            random.setLore(3, "  \u00A7r\u00A77\u00A7oaus allen existierenden Maps zu w\u00E4hlen.");
            //add the random item
            voteItems.add(0, random);
            //cut the list
            this.voteItems = voteItems.subList(0, Math.min(voteItems.size(), 4));   //4 because of the random item
        } else {
            this.voteItems = voteItems;
        }

        init();
    }

    private void init() {
        inventory = new VoteInventory(title, this);

        scoreboard = new VoteScoreboard(title, this);
        scoreboard.register();

        listener = new VoteListener(this);
        listener.register();

        voteItem = new NamedItem(Material.PAPER, getVoteItemName());
    }

    private void end() {
        listener.unregister();
        scoreboard.unregister();

        //close open vote-inventorys
        for(Player p : Bukkit.getOnlinePlayers()) {
            if (p.getInventory().getName() != null && p.getInventory().getTitle().equals(inventory.getChestTitle())) {
                p.closeInventory();
            }
        }
    }

    /**
     * let a player vote for an option
     * @param player    the player that voted
     * @param title     the title of the option, he voted
     */
    public void vote(Player player, String title) {
        vote(player, getVoteItem(title));
    }

    /**
     * let a player vote for an item
     * removes old votes
     * @param player    the player that voted
     * @param item    the item he voted
     */
    public void vote(Player player, VoteItem item) {
        String uuid = player.getUniqueId().toString();

        //player have already voted -> unvote old
        if (votes.containsKey(uuid)) {
            VoteItem old = votes.get(uuid);

            if (old.equals(item)) return;
            old.removeVote(player);
            scoreboard.updateVotes(old);
        }

        if (Rank.PREMIUM.isRank(player)) {
            item.addDoubleVote(player);
        } else {
            item.addVote();
        }
        votes.put(uuid, item);

        inventory.updateChestInventory();
        scoreboard.updateVotes(item);
    }

    /**
     * searches for the vote-item with a given title
     * @param title     the title of the option
     * @return          the vote-item, if nothing found then throws an IllegalArgumentException
     */
    public VoteItem getVoteItem(String title) {
        for (VoteItem item : voteItems) {
            if (item.getName().equalsIgnoreCase(title)) {
                return item;
            }
        }
        throw new IllegalArgumentException("VoteItem " + title + " not found!");
    }

    /**
     * @return      the vote-item with the most votes
     */
    public VoteItem getResult() {
        end();

        VoteItem winner = null;
        int highestVotes = 0;

        List<VoteItem> shuffledList = getVoteItems();   //because if two items have the same vote count, it will pick the first
        Collections.shuffle(shuffledList);

        for (VoteItem item : shuffledList) {
            if (item.getVotes() > highestVotes) {
                winner = item;
                highestVotes = item.getVotes();
                Bukkit.broadcastMessage(item.getName());
            }
        }

        //no one voted? -> pick random
        if (winner == null || (winner != null && winner.getName().equals(RANDOM))) {
            winner = allItems.get(0);
        }

        return winner;
    }

    public String getVoteItemName() {
        return VOTEITEM_PREFIX + " " + getTitle() + " " + VOTEITEM_SUFFIX;
    }

    public String getTitle() {
        return title;
    }

    public boolean isUseScoreboard() {
        return useScoreboard;
    }

    public boolean isUseRandom() {
        return useRandom;
    }

    public ItemStack getVoteItem() {
        return voteItem;
    }

    public List<VoteItem> getVoteItems() {
        return voteItems;
    }

    public Map<String, VoteItem> getVotes() {
        return votes;
    }

    public VoteListener getListener() {
        return listener;
    }

    public VoteInventory getInventory() {
        return inventory;
    }

    public VoteScoreboard getScoreboard() {
        return scoreboard;
    }
}
