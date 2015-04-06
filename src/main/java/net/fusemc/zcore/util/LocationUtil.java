package net.fusemc.zcore.util;

import org.bukkit.Location;

/**
 * @author michidk
 */

public class LocationUtil
{

    /**
     *
     * @return a centered location
     */
    public static Location centerLocation(Location location)
    {
        return location.getBlock().getLocation().add(0.5,0,0.5);
    }

}
