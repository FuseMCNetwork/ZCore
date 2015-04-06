package net.fusemc.zcore.projectileAPI.event;

import net.fusemc.zcore.projectileAPI.projectiles.ItemProjectile;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

/**
 * Created by Marco on 20.07.2014.
 */

public class ItemProjectileEvent extends CustomProjectileEvent{

    public static HandlerList handlers = new HandlerList();

    private ItemStack item;

    public ItemProjectileEvent(ItemProjectile projectile, Block block, BlockFace face, LivingEntity entity, ItemStack item) {
        super(projectile, block, face, entity);
        this.item = item;
    }

    public ItemStack getItemStack(){
        return item;
    }

    public static HandlerList getHandlerList(){
        return handlers;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    @Override
    public ItemProjectile getProjectile(){
        return (ItemProjectile) super.getProjectile();
    }
}
