package net.fusemc.zcore.featureSystem.features.borderFeature;

import me.michidk.DKLib.BlockLocation;
import org.bukkit.Location;
import org.bukkit.Material;

import java.util.HashSet;
import java.util.Set;

public final class BorderSettings {
    private int borderRadius;
    private Shape borderShape;
    private int playerRadius;
    private Shape playerShape;
    private BlockLocation center;
    private Material borderMaterial;
    private byte borderData;


    //for json
    public BorderSettings()
    {

    }

    public BorderSettings(int borderRadius, Shape borderShape, int playerRadius, Shape playerShape, BlockLocation center, Material borderMaterial, byte borderData) {
        this.borderRadius = borderRadius;
        this.borderShape = borderShape;
        this.playerRadius = playerRadius;
        this.playerShape = playerShape;
        this.center = center;
        this.borderMaterial = borderMaterial;
        this.borderData = borderData;
    }

    public BorderSettings(int borderRadius, Shape borderShape, int playerRadius, Shape playerShape, BlockLocation center) {
        this(borderRadius, borderShape, 5, Shape.BALL, center, Material.GLASS, (byte) 0);
    }

    public BorderSettings(int borderRadius, Shape borderShape, BlockLocation center, Material material) {
        this(borderRadius, borderShape, 5, Shape.BALL, center, material, (byte) 0);
    }

    public BorderSettings(int borderRadius, BlockLocation center) {
        this(borderRadius, Shape.BALL, 5, Shape.BALL, center, Material.GLASS, (byte) 0);
    }

    public int getBorderRadius() {
        return borderRadius;
    }

    public Shape getBorderShape() {
        return borderShape;
    }

    public int getPlayerRadius() {
        return playerRadius;
    }

    public Shape getPlayerShape() {
        return playerShape;
    }

    public BlockLocation getCenter() {
        return center;
    }

    public byte getBorderData() {
        return borderData;
    }

    public Material getBorderMaterial() {
        return borderMaterial;
    }

    public static enum Shape {
        VERTICAL_CYLINDER { //without floor and ceiling
            @Override
            public Set<BlockLocation> calculateCollidingBlocks(BlockLocation centerLocation, int radius, Set<BlockLocation> checkedBlocks) {
                int radiusSquared = radius * radius;
                Set<BlockLocation> locations = new HashSet<>();
                for (BlockLocation bl : checkedBlocks) {
                    int deltaX = bl.getX()-centerLocation.getX();
                    int deltaZ = bl.getZ()-centerLocation.getZ();
                    int distanceSquared = deltaX*deltaX + deltaZ*deltaZ;
                    int xMod = deltaX>0?1:-1;
                    int zMod = deltaZ>0?1:-1;
                    if (distanceSquared <= radiusSquared &&
                            (lengthSq(deltaX+xMod, deltaZ) > radiusSquared || lengthSq(deltaX, deltaZ+zMod) > radiusSquared)) {
                        locations.add(bl);
                    }
                }
                return locations;
            }

            @Override
            public Set<BlockLocation> calculateContainedBlocks(BlockLocation centerLocation, int radius) {
                int radiusSquared = radius*radius;
                Set<BlockLocation> locations = new HashSet<>();
                for (int xPos = -1*radius; xPos <= radius; xPos++) {
                    for (int zPos = -1*radius; zPos <= radius; zPos++) {
                        int realX = xPos + centerLocation.getX();
                        int realZ = zPos + centerLocation.getZ();
                        int distanceSquared = xPos*xPos+zPos*zPos;
                        if (distanceSquared <= radiusSquared) {
                            for (int i = -1*radius; i <= radius; i++) {
                                locations.add(new BlockLocation(realX, i+centerLocation.getY(), realZ, centerLocation.getWorldName()));
                            }
                        }
                    }
                }
                return locations;
            }

            @Override
            public boolean inArea(BlockLocation centerLocation, int radius, Location location){
                int locX = centerLocation.getX() - location.getBlockX();
                int locZ = centerLocation.getZ() - location.getBlockZ();
                int radius2 = radius * radius;
                int loc2 = (locX * locX) + (locZ * locZ);
                if(loc2 > radius2){
                    return false;
                }
                return true;
            }
        }, BALL { //players cant fall into void, because floor und ceiling
            @Override
            public Set<BlockLocation> calculateCollidingBlocks(BlockLocation centerLocation, int radius, Set<BlockLocation> checkedBlocks) {
                int radiusSquared = radius * radius;
                Set<BlockLocation> locations = new HashSet<>();
                for (BlockLocation bl : checkedBlocks) {
                    int deltaX = bl.getX()-centerLocation.getX();
                    int deltaY = bl.getY()-centerLocation.getY();
                    int deltaZ = bl.getZ()-centerLocation.getZ();
                    int distanceSquared = deltaX*deltaX + deltaY*deltaY + deltaZ*deltaZ;
                    if (distanceSquared <= radiusSquared) {
                        int xMod = deltaX>0?1:-1;
                        int yMod = deltaY>0?1:-1;
                        int zMod = deltaZ>0?1:-1;
                        if     (lengthSq(deltaX+xMod, deltaY, deltaZ) > radiusSquared ||
                                lengthSq(deltaX, deltaY+yMod, deltaZ) > radiusSquared ||
                                lengthSq(deltaX, deltaY, deltaZ+zMod) > radiusSquared) {
                            locations.add(bl);
                        }
                    }
                }
                return locations;
            }

            @Override
            public Set<BlockLocation> calculateContainedBlocks(BlockLocation centerLocation, int radius) {
                int radiusSquared = radius*radius;
                Set<BlockLocation> locations = new HashSet<>();
                for (int xPos = -1*radius; xPos <= radius; xPos++) {
                    for (int yPos = -1*radius; yPos <= radius; yPos++) {
                        for (int zPos = -1 * radius; zPos <= radius; zPos++) {
                            int realX = xPos + centerLocation.getX();
                            int realY = yPos + centerLocation.getY();
                            int realZ = zPos + centerLocation.getZ();
                            int distanceSquared = xPos * xPos + zPos * zPos + yPos * yPos;
                            if (distanceSquared <= radiusSquared) {
                                locations.add(new BlockLocation(realX, realY, realZ, centerLocation.getWorldName()));
                            }
                        }
                    }
                }
                return locations;
            }

            @Override
            public boolean inArea(BlockLocation centerLocation, int radius, Location location){
                int locX = centerLocation.getX() - location.getBlockX();
                int locY = centerLocation.getY() - location.getBlockY();
                int locZ = centerLocation.getZ() - location.getBlockZ();
                int radius2 = radius * radius;
                int loc2 = (locX * locX) + (locY * locY) + (locZ * locZ);
                if(loc2 > radius2){
                    return false;
                }
                return true;
            }
        }, VERTICAL_SQUARE_TUBE {   //without floor and ceiling
            @Override
            public Set<BlockLocation> calculateCollidingBlocks(BlockLocation centerLocation, int radius, Set<BlockLocation> checkedBlocks) {
                Set<BlockLocation> locations = new HashSet<>();
                int minX = centerLocation.getX()-radius;
                int maxX = centerLocation.getX()+radius;
                int minZ = centerLocation.getZ()-radius;
                int maxZ = centerLocation.getZ()+radius;
                for (BlockLocation bl : checkedBlocks) {
                    if (bl.getX() == minX || bl.getX() == maxX || bl.getZ() == minZ || bl.getZ() == maxZ) {
                        if (bl.getX() >= minX && bl.getX() <= maxX && bl.getZ() >= minZ && bl.getZ() <= maxZ) {
                            // inside tube
                            locations.add(bl);
                        }
                    }
                }
                return locations;
            }

            @Override
            public Set<BlockLocation> calculateContainedBlocks(BlockLocation centerLocation, int radius) {
                Set<BlockLocation> locations = new HashSet<>();
                for (int xPos = -1*radius; xPos <= radius; xPos++) {
                    for (int yPos = -1*radius; yPos <= radius; yPos++) {
                        for (int zPos = -1*radius; zPos <= radius; zPos++) {
                            int realX = xPos + centerLocation.getX();
                            int realY = yPos + centerLocation.getY();
                            int realZ = zPos + centerLocation.getZ();
                            locations.add(new BlockLocation(realX, realY, realZ, centerLocation.getWorldName()));
                        }
                    }
                }
                return locations;
            }

            @Override
            public boolean inArea(BlockLocation centerLocation, int radius, Location location){
                int minX = centerLocation.getX()-radius;
                int maxX = centerLocation.getX()+radius;
                int minZ = centerLocation.getZ()-radius;
                int maxZ = centerLocation.getZ()+radius;
                if(location.getX() < minX || location.getZ() < minZ || location.getX() > maxX || location.getZ() > maxZ){
                    return false;
                }
                return true;
            }
        };

        public abstract Set<BlockLocation> calculateCollidingBlocks(BlockLocation centerLocation, int radius, Set<BlockLocation> checkedBlocks);
        public abstract Set<BlockLocation> calculateContainedBlocks(BlockLocation centerLocation, int radius);
        public abstract boolean inArea(BlockLocation centerLocation, int radius, Location location);
        private static double lengthSq(double x, double y) {
            return x*x+y*y;
        }
        private static double lengthSq(double x, double y, double z) {
            return x*x+y*y+z*z;
        }
    }
}
