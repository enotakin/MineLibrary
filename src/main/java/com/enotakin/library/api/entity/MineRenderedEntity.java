package com.enotakin.library.api.entity;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

public interface MineRenderedEntity {

    boolean isAutoVisible();

    void setAutoVisible(boolean autoVisible);

    double getRenderDistance();

    void setRenderDistance(double renderDistance);

    Interact getInteract();

    void setInteract(Interact interact);

    boolean showTo(Player player);

    boolean isVisibleTo(Player player);

    boolean hideTo(Player player);

    World getWorld();

    double getX();

    double getY();

    double getZ();

    float getYaw();

    float getPitch();

    Location getLocation();

    Location getLocation(Location location);

    default void teleport(Location location) {
        teleport(
                location.getWorld(),
                location.getX(),
                location.getY(),
                location.getZ(),
                location.getYaw(),
                location.getPitch()
        );
    }

    void teleport(World world, double x, double y, double z, float yaw, float pitch);

    boolean isRemoved();

    boolean remove();

}
