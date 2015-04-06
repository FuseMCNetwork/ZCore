package net.fusemc.zcore.barAPI;

import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

/**
 * Created by Marco on 13.10.2014.
 */
public class WitherBar extends EntityBar {

    private static final int MAX_HEALTH = 300;
    private static final int DISTANCE_TO_PLAYER = 30;

    public WitherBar(int entityID) {
        super(entityID);

        this.dataWatcher.setObject(0, (byte) 32);
        this.dataWatcher.setObject(8, (byte) 0);
        this.dataWatcher.setObject(15, (byte) 1);
        this.dataWatcher.setObject(20, 875);
    }

    @Override
    public void updateLocation(Player player) {
        Vector direction = player.getEyeLocation().getDirection().multiply(DISTANCE_TO_PLAYER);
        this.location = player.getEyeLocation().toVector().add(direction);
    }

    @Override
    public int getMaxHealth() {
        return MAX_HEALTH;
    }

    @Override
    public int getTypeID() {
        return 64;
    }
}
