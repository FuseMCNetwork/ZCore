package net.fusemc.zcore.raytracingAPI;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.LivingEntity;

public class RayTraceResult {
    private Type type;
    private LivingEntity hitEntity;
    private Block hitBlock;
    private BlockFace hitBlockFace;
    private Location hitLocation;

    public RayTraceResult(Block hitBlock, BlockFace hitBlockFace, Location hitLocation) {
        this.type = Type.BLOCK;
        this.hitBlock = hitBlock;
        this.hitBlockFace = hitBlockFace;
        this.hitLocation = hitLocation;
    }

    public RayTraceResult(LivingEntity hitEntity, Location hitLocation) {
        this.type = Type.ENTITY;
        this.hitEntity = hitEntity;
        this.hitLocation = hitLocation;
    }

    public Type getType() {
        return type;
    }

    public LivingEntity getHitEntity() {
        return hitEntity;
    }

    public Block getHitBlock() {
        return hitBlock;
    }

    public BlockFace getHitBlockFace() {
        return hitBlockFace;
    }

    public Location getHitLocation() {
        return hitLocation;
    }

    public static enum Type {
        ENTITY, BLOCK
    }
}
