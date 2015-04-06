package net.fusemc.zcore.featureSystem.features.lobbyFeature;


import org.bukkit.World;

/**
 * Class to define settings for the lobbymanager
 *
 * @author michidk
 */
public class LobbySettings
{

    /**
     * the count of players that must join the server to start the lobby coutdown
     */
    private int minPlayers;

    /**
     * the max players that can join
     */
    private int maxPlayers;

    /**
     * the coutdown time
     */
    private int timeToStart;

    /**
     * the world, where the lobby is located
     */
    private World world;

    /**
     * if true, the lobbymanager will manage the inventory and the map-voting system
     */
    private boolean enableVote = true;

    /**
     * main constructor
     */
    public LobbySettings(int minPlayers, int maxPlayers, int timeToStart, World world)
    {
        this.minPlayers = minPlayers;
        this.maxPlayers = maxPlayers;
        this.timeToStart = timeToStart;

        this.world = world;
    }

    /**
     * use this constructor, to disable voting
     */
    public LobbySettings(int minPlayers, int maxPlayers, int timeToStart, World world, boolean enableVote)
    {
        this.minPlayers = minPlayers;
        this.maxPlayers = maxPlayers;
        this.timeToStart = timeToStart;

        this.world = world;
        this.enableVote = enableVote;
    }

    public int getMinPlayers()
    {
        return minPlayers;
    }

    public int getMaxPlayers()
    {
        return maxPlayers;
    }

    public int getTimeToStart()
    {
        return timeToStart;
    }

    public World getWorld()
    {
        return world;
    }

    public boolean isEnableVote()
    {
        return enableVote;
    }

}
