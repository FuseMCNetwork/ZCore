package net.fusemc.zcore.featureSystem.features.jumppadFeature;

import org.bukkit.entity.Player;

/**
 * Created by Marco on 08.10.2014.
 */
public class JumpPadScheduler implements Runnable {

    private Player player;
    private float height;
    private float length;

    public JumpPadScheduler(Player player, float height, float length) {
        this.player = player;
        this.height = height;
        this.length = length;
    }

    @Override
    public void run() {
        if(player.isOnline()) {
            player.setVelocity(player.getLocation().getDirection().setY(height).multiply(length));
        }
    }
}
