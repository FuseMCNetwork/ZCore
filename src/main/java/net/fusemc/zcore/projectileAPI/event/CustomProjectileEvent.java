package net.fusemc.zcore.projectileAPI.event;

import net.fusemc.zcore.projectileAPI.HitType;
import net.fusemc.zcore.projectileAPI.projectiles.CustomProjectile;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;

/**
 * Created by Marco on 20.07.2014.
 */

public abstract class CustomProjectileEvent extends Event implements Cancellable{

    private CustomProjectile projectile;
    private boolean cancel;
    private HitType type;
    private Block block;
    private BlockFace face;
    private LivingEntity entity;

    public CustomProjectileEvent(CustomProjectile projectile, Block block, BlockFace face, LivingEntity entity){
        this.projectile = projectile;
        this.type = entity == null? HitType.BLOCK: HitType.ENTITY;
        this.block = block;
        this.face = face;
        this.entity = entity;
    }

    public CustomProjectile getProjectile(){
        return projectile;
    }

    public HitType getHitType(){
        return type;
    }

    public Block getHitBlock(){
        return block;
    }

    public BlockFace getHitFace(){
        return face;
    }

    public LivingEntity getHitEntity(){
        return entity;
    }

    @Override
    public boolean isCancelled(){
        return cancel;
    }

    @Override
    public void setCancelled(boolean cancel){
        this.cancel = cancel;
    }
}
