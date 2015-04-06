package net.fusemc.zcore.projectileAPI;

import net.minecraft.server.v1_7_R4.EntityItem;
import net.minecraft.server.v1_7_R4.MinecraftServer;
import org.bukkit.craftbukkit.v1_7_R4.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

/**
 * Created by Marco on 20.07.2014.
 */

public class ProjectileItem extends EntityItem {

    private ProjectileHorse horse;
    private int lasttick;

    public ProjectileItem(ProjectileHorse horse, ItemStack item) {
        super(horse.world, horse.locX, horse.locY, horse.locZ, CraftItemStack
                .asNMSCopy(item));
        this.pickupDelay = Integer.MAX_VALUE;
        this.lasttick = MinecraftServer.currentTick;
        this.horse = horse;
    }

    @Override
    public void h() {
        this.setPassengerOf(horse);
        int ticks = MinecraftServer.currentTick - this.lasttick;
        this.pickupDelay -= ticks;
        this.age += ticks;
        this.lasttick = MinecraftServer.currentTick;
    }
}
