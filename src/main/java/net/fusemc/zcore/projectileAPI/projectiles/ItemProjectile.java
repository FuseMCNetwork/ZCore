package net.fusemc.zcore.projectileAPI.projectiles;

import net.fusemc.zcore.projectileAPI.ProjectileHorse;
import net.fusemc.zcore.projectileAPI.ProjectileItem;
import net.fusemc.zcore.projectileAPI.event.CustomProjectileEvent;
import net.fusemc.zcore.projectileAPI.event.ItemProjectileEvent;
import net.minecraft.server.v1_7_R4.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.v1_7_R4.CraftWorld;
import org.bukkit.craftbukkit.v1_7_R4.block.CraftBlock;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_7_R4.inventory.CraftItemStack;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
/**
 * Created by Marco on 20.07.2014.
 */

public class ItemProjectile extends EntityWitherSkull implements CustomProjectile, IProjectile {

    private ProjectileHorse horse;
    private ProjectileItem item;
    private int age = 0;

    public ItemProjectile(Player shooter, ItemStack itemstack, float speed) {
        this(shooter, shooter.getEyeLocation(), itemstack, speed);
    }

    public ItemProjectile(Player shooter, Location loc, ItemStack itemstack,
                          float speed) {
        super(((CraftWorld) shooter.getWorld()).getHandle());
        this.shooter = ((CraftPlayer) shooter).getHandle();
        this.setPositionRotation(loc.getX(), loc.getY() + 100, loc.getZ(),
                loc.getYaw(), loc.getPitch());
        this.world.addEntity(this);

        horse = new ProjectileHorse(this.world);
        horse.setAge(-4077000);
        horse.setPosition(this.locX, this.locY, this.locZ);
        this.world.addEntity(horse);

        item = new ProjectileItem(horse, itemstack);
        item.setPosition(this.locX, this.locY, this.locZ);
        this.world.addEntity(item);

        item.setPassengerOf(horse);
        horse.setPassengerOf(this);

        a(0.25F, 0.25F);
        this.locX -= MathHelper.cos(this.yaw / 180.0F * 3.141593F) * 0.16F;
        this.locY -= 0.1000000014901161D;
        this.locZ -= MathHelper.sin(this.yaw / 180.0F * 3.141593F) * 0.16F;
        this.height = 0.0F;
        float f = 0.4F;

        this.motX = (-MathHelper.sin(this.yaw / 180.0F * 3.141593F)
                * MathHelper.cos(this.pitch / 180.0F * 3.141593F) * f);
        this.motZ = (MathHelper.cos(this.yaw / 180.0F * 3.141593F)
                * MathHelper.cos(this.pitch / 180.0F * 3.141593F) * f);
        this.motY = (-MathHelper.sin(this.pitch / 180.0F * 3.141593F) * f);
        shoot(this.motX, this.motY, this.motZ, speed * 1.5F, 1.0F);
    }

    public void shoot(double d0, double d1, double d2, float f, float f1) {
        float f2 = MathHelper.sqrt(d0 * d0 + d1 * d1 + d2 * d2);

        d0 /= f2;
        d1 /= f2;
        d2 /= f2;
        d0 += this.random.nextGaussian() * 0.007499999832361937D * f1;
        d1 += this.random.nextGaussian() * 0.007499999832361937D * f1;
        d2 += this.random.nextGaussian() * 0.007499999832361937D * f1;
        d0 *= f;
        d1 *= f;
        d2 *= f;
        this.motX = d0;
        this.motY = d1;
        this.motZ = d2;
        float f3 = MathHelper.sqrt(d0 * d0 + d2 * d2);

        this.lastYaw = (this.yaw = (float) (Math.atan2(d0, d2) * 180.0D / 3.141592741012573D));
        this.lastPitch = (this.pitch = (float) (Math.atan2(d1, f3) * 180.0D / 3.141592741012573D));
    }

    @SuppressWarnings("rawtypes")
    @Override
    public void h() {
        this.lastX = this.locX;
        this.lastY = this.locY;
        this.lastZ = this.locZ;

        this.boundingBox.d(this.motX, this.motY, this.motZ);
        this.locX = ((this.boundingBox.a + this.boundingBox.d) / 2.0D);
        this.locY = (this.boundingBox.b + this.height - this.W);
        this.locZ = ((this.boundingBox.c + this.boundingBox.f) / 2.0D);

        if (this.age > 200)
            die();
        age++;

        if (this.onGround) {
            this.motY *= -0.5D;
            this.motX *= this.random.nextFloat() * 0.2F;
            this.motY *= this.random.nextFloat() * 0.2F;
            this.motZ *= this.random.nextFloat() * 0.2F;
        }

        Vec3D vec3d = Vec3D.a(this.locX, this.locY -100, this.locZ);
        Vec3D vec3d1 = Vec3D.a(this.locX + this.motX, this.locY + this.motY -100,
                this.locZ + this.motZ);
        MovingObjectPosition movingobjectposition = this.world.a(vec3d, vec3d1);

        vec3d = Vec3D.a(this.locX, this.locY -100, this.locZ);
        vec3d1 = Vec3D.a(this.locX + this.motX, this.locY + this.motY -100,
                this.locZ + this.motZ);
        if (movingobjectposition != null) {
            vec3d1 = Vec3D.a(movingobjectposition.pos.a,
                    movingobjectposition.pos.b, movingobjectposition.pos.c);
        }

        if (!this.world.isStatic) {
            Entity entity = null;
            List list = this.world.getEntities(this, this.boundingBox.a(this.motX, this.motY, this.motZ).d(0, -100, 0).grow(1.0D, 1.0D, 1.0D));
            double d0 = 0.0D;
            EntityLiving entityliving = shooter;

            for (int i = 0; i < list.size(); i++) {
                Entity entity1 = (Entity) list.get(i);

                if (entity1.R() && (entity1 != entityliving)) {
                    float f = 0.3F;
                    AxisAlignedBB axisalignedbb = entity1.boundingBox.grow(f, f, f);
                    MovingObjectPosition movingobjectposition1 = axisalignedbb.a(vec3d, vec3d1);

                    if (movingobjectposition1 != null) {
                        double d1 = vec3d.distanceSquared(movingobjectposition1.pos);

                        if ((d1 < d0) || (d0 == 0.0D)) {
                            entity = entity1;
                            d0 = d1;
                        }
                    }
                }
            }

            if (entity != null) {
                movingobjectposition = new MovingObjectPosition(entity);
            }
        }
        if (movingobjectposition != null) {
            if (movingobjectposition.type == EnumMovingObjectType.BLOCK) {
                CustomProjectileEvent event = call(
                        world.getWorld().getBlockAt(movingobjectposition.b,
                                movingobjectposition.c, movingobjectposition.d),
                        CraftBlock.notchToBlockFace(movingobjectposition.face),
                        null, CraftItemStack.asBukkitCopy(item.getItemStack()));
                if (!event.isCancelled()) {
                    die();
                }
            } else if (movingobjectposition.entity != null
                    && movingobjectposition.entity instanceof EntityLiving) {
                LivingEntity living = (LivingEntity) movingobjectposition.entity
                        .getBukkitEntity();
                CustomProjectileEvent event = call(null, null, living,
                        CraftItemStack.asBukkitCopy(item.getItemStack()));
                if (!event.isCancelled()) {
                    die();
                }
            } else if (this.onGround) {
                CustomProjectileEvent event = call(getBukkitEntity()
                                .getLocation().getBlock().getRelative(BlockFace.DOWN),
                        BlockFace.UP, null,
                        CraftItemStack.asBukkitCopy(item.getItemStack()));
                if (!event.isCancelled()) {
                    die();
                }
            }
        }

        EntityTracker tracker = ((WorldServer) this.world).tracker;
        tracker.untrackEntity(this);
        tracker.addEntity(this, 64, 10);
    }

    public CustomProjectileEvent call(Block block, BlockFace face,
                                      LivingEntity shooter, ItemStack itemStack) {
        ItemProjectileEvent event = new ItemProjectileEvent(this, block, face,
                shooter, itemStack);
        Bukkit.getPluginManager().callEvent(event);
        return event;
    }

    @Override
    public void die() {
        this.dead = true;
        this.horse.die();
        this.item.die();
    }

    @Override
    public Player getShooter() {
        return (Player) shooter.getBukkitEntity();
    }
}
