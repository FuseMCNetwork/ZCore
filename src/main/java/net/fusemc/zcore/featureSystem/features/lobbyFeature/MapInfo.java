package net.fusemc.zcore.featureSystem.features.lobbyFeature;

import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;

/**
 * @author michidk
 */

public class MapInfo
{

    /**
     * the name of the folder of the world
     */
    private transient String worldName;
    /**
     * the display name that is shown in the vote menu
     */
    private String displayName;
    /**
     * one or multiple authors
     */
    private String author;
    /**
     * a link that will be broadcasted to the if the game starts
     */
    private String authorLink;
    /**
     * the material of the item, that is shown in the vote menu
     */
    private Material material;


    public static List<MapInfo> infos = new ArrayList<>();

    public MapInfo()
    {

    }

    /**
     * called after loaded by json
     */
    public void init()
    {
        infos.add(this);
    }

    /**
     * @return the map info to the world name
     */
    public static MapInfo getByWorld(String world)
    {
        for (MapInfo info: infos)
        {
            if (info.getWorldName().equalsIgnoreCase(world))
            {
                return info;
            }
        }
        return null;
    }

    public static MapInfo getByDisplayName(String displayName)
    {
        for (MapInfo info: infos)
        {
            if (info.getDisplayName().equalsIgnoreCase(displayName))
            {
                return info;
            }
        }
        return null;
    }


    public void setWorldName(String worldName)
    {
        this.worldName = worldName;
    }

    public String getWorldName()
    {
        return worldName;
    }

    public String getDisplayName()
    {
        return displayName;
    }

    public String getAuthor()
    {
        return author;
    }

    public Material getMaterial()
    {
        return material;
    }

    public String getAuthorLink()
    {
        return authorLink;
    }
}
