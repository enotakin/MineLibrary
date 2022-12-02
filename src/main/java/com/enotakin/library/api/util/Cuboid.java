package com.enotakin.library.api.util;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;

public class Cuboid {

    protected final World world;

    protected final int minX;
    protected final int minY;
    protected final int minZ;

    protected final int maxX;
    protected final int maxY;
    protected final int maxZ;

    protected final int width;
    protected final int height;
    protected final int length;

    public Cuboid(Location fromLocation, Location toLocation) {
        this(
                equal(fromLocation.getWorld(), toLocation.getWorld()),
                fromLocation.getBlockX(), fromLocation.getBlockY(), fromLocation.getBlockZ(),
                toLocation.getBlockX(), toLocation.getBlockY(), toLocation.getBlockZ()
        );
    }

    public Cuboid(World world, int fromX, int fromY, int fromZ, int toX, int toY, int toZ) {
        this.world = world;

        minX = Math.min(fromX, toX);
        minY = Math.min(fromY, toY);
        minZ = Math.min(fromZ, toZ);

        maxX = Math.max(fromX, toX);
        maxY = Math.max(fromY, toY);
        maxZ = Math.max(fromZ, toZ);

        width = maxX - minX + 1;
        height = maxY - minY + 1;
        length = maxZ - minZ + 1;
    }

    public World getWorld() {
        return world;
    }

    public int getMinX() {
        return minX;
    }

    public int getMinY() {
        return minY;
    }

    public int getMinZ() {
        return minZ;
    }

    public int getMaxX() {
        return maxX;
    }

    public int getMaxY() {
        return maxY;
    }

    public int getMaxZ() {
        return maxZ;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getLength() {
        return length;
    }

    public Location getMinLocation() {
        return new Location(world, minX, minY, minZ);
    }

    public Location getMaxLocation() {
        return new Location(world, maxX, maxY, maxZ);
    }

    public Location getCenterLocation() {
        return new Location(world, minX + (width / 2D) + 0.5D, minY + (height / 2D) + 0.5D, minZ + (length / 2D) + 0.5D);
    }

    public boolean contains(Block block) {
        return contains(block.getWorld(), block.getX(), block.getY(), block.getZ());
    }

    public boolean contains(Entity entity) {
        return contains(entity.getLocation());
    }

    public boolean contains(Location location) {
        return contains(location.getWorld(), location.getBlockX(), location.getBlockY(), location.getBlockZ());
    }

    public boolean contains(World world, int x, int y, int z) {
        return this.world.equals(world) && x >= minX && x <= maxX && y >= minY && y <= maxY && z >= minZ && z <= maxZ;
    }

    private static World equal(World fromWorld, World toWorld) {
        if (!fromWorld.equals(toWorld)) {
            throw new IllegalArgumentException();
        }
        return fromWorld;
    }

}
