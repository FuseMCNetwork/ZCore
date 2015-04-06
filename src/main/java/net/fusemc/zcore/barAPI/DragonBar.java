package net.fusemc.zcore.barAPI;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

/**
 * Created by Marco on 13.10.2014.
 */
public class DragonBar extends EntityBar {

    private static final int MAX_HEALTH = 200;
    private static final int DISTANCE_TO_PLAYER = 300;

    public DragonBar(int entityID) {
        super(entityID);
    }

    @Override
    public void updateLocation(Player player) {
        Location playerLocation = player.getLocation();
        this.location = new Vector(playerLocation.getX(), playerLocation.getY() - DISTANCE_TO_PLAYER, playerLocation.getZ());
    }

    @Override
    public void setMessage(String message) {
        this.dataWatcher.setObject(10, message);
        this.changed = true;
        if(isDead()) {
            spawn();
        }
    }

    @Override
    public int getMaxHealth() {
        return MAX_HEALTH;
    }

    @Override
    public int getTypeID() {
        return 63;
    }
}
