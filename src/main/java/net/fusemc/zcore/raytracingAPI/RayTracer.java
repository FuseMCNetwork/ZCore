package net.fusemc.zcore.raytracingAPI;

import net.minecraft.server.v1_7_R4.*;
import net.minecraft.server.v1_7_R4.Entity;
import net.minecraft.server.v1_7_R4.World;
import org.bukkit.*;
import org.bukkit.craftbukkit.v1_7_R4.CraftWorld;
import org.bukkit.craftbukkit.v1_7_R4.block.CraftBlock;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftEntity;
import org.bukkit.entity.*;
import org.bukkit.util.Vector;

import java.util.Collection;
import java.util.List;

import static com.google.common.base.Preconditions.checkState;

public class RayTracer {
    public static RayTraceResult trace(Location start, float length, Collection<org.bukkit.entity.LivingEntity> excludes) {
        Vector direction = start.getDirection();
        direction.multiply(length);
        Location end = start.clone().add(direction);
        return trace(start, end, excludes);
    }

    public static RayTraceResult trace(Location start, Location end, Collection<org.bukkit.entity.LivingEntity> excludes) {
        checkState(start.getWorld().getName().equals(end.getWorld().getName()), "Both locations have to be in the same world.");


        org.bukkit.World world = start.getWorld();
        CraftWorld craftWorld = (CraftWorld) world;
        World nmsWorld = craftWorld.getHandle();
        Vec3D nmsStart = Vec3D.a(start.getX(), start.getY(), start.getZ());
        Vec3D nmsEnd = Vec3D.a(end.getX(), end.getY(), end.getZ());
        Location smallLocation = new Location(world,
                Math.min(start.getX(), end.getX()), Math.min(start.getY(), end.getY()), Math.min(start.getZ(), end.getZ()));
        Location largeLocation = new Location(world,
                Math.max(start.getX(), end.getX()), Math.max(start.getY(), end.getY()), Math.max(start.getZ(), end.getZ()));

        // This is totally not a moving object position :(
        MovingObjectPosition nmsResult = nmsWorld.a(nmsStart, nmsEnd);

        // reset vectors
        nmsStart = Vec3D.a(start.getX(), start.getY(), start.getZ());
        nmsEnd = Vec3D.a(end.getX(), end.getY(), end.getZ());

        // try to get entity
        AxisAlignedBB aabb = AxisAlignedBB.a(smallLocation.getX(), smallLocation.getY(), smallLocation.getZ(),
                largeLocation.getX(), largeLocation.getY(), largeLocation.getZ()).grow(1, 1, 1);

        MovingObjectPosition entity = null;
        List list = nmsWorld.getEntities(null, aabb);
        double minDistance = nmsStart.distanceSquared(nmsResult.pos);

        outer:
        for (Object aList : list) {
            Entity e = (Entity) aList;

            for (org.bukkit.entity.Entity exclude : excludes) {
                if (((CraftEntity) exclude).getHandle() == e) {
                    // this entity is excluded
                    continue outer;
                }
            }

            if (e.R()) {
                float f = 0.3f;
                AxisAlignedBB axisAlignedBB = e.boundingBox.grow(f, f, f);
                MovingObjectPosition collide = axisAlignedBB.a(nmsStart, nmsEnd);

                if (collide != null) {
                    // we got a collision!
                    double dSqr = nmsStart.distanceSquared(collide.pos);

                    if ((dSqr < minDistance) || (minDistance == 0)) {
                        collide.type = EnumMovingObjectType.ENTITY;
                        collide.entity = e;
                        entity = collide;
                        minDistance = dSqr;
                    }
                }
            }
        }

        if (entity != null) {
            nmsResult = entity;
        }

        if (nmsResult != null) {
            // result gotten!
            Location hitLocation = new Location(world, nmsResult.pos.a, nmsResult.pos.b, nmsResult.pos.c);

            if (nmsResult.type == EnumMovingObjectType.BLOCK) {
                return new RayTraceResult(world.getBlockAt(nmsResult.b, nmsResult.c, nmsResult.d),
                        CraftBlock.notchToBlockFace(nmsResult.face), hitLocation);
            } else if (nmsResult.type == EnumMovingObjectType.ENTITY) {
                LivingEntity living = (LivingEntity) nmsResult.entity.getBukkitEntity();
                return new RayTraceResult(living, hitLocation);
            }
        }

        // no result :(
        return null;
    }
}
