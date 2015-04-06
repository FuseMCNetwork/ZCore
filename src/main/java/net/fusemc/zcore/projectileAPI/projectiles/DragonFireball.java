package net.fusemc.zcore.projectileAPI.projectiles;

import net.minecraft.server.v1_7_R4.*;

/**
 * Created by Marco on 20.07.2014.
 */

public class DragonFireball extends EntitySmallFireball {

    public DragonFireball(World world) {
        super(world);
    }

    public DragonFireball(World world, EntityLiving entityliving, double dirX, double dirY, double dirZ) {
        super(world, entityliving, dirX, dirY, dirZ);
    }

    public DragonFireball(World world, double locX, double locY, double locZ, double dirX, double dirY, double dirZ) {
        super(world, locX, locY, locZ, dirX, dirY, dirZ);
    }

    @Override
    public void a(MovingObjectPosition movingobjectposition) {
        if (!this.world.isStatic) {
            if (movingobjectposition.entity != null)
                movingobjectposition.entity.damageEntity(DamageSource.fireball(this, this.shooter), 5.0F);
            die();
        }
    }
}
