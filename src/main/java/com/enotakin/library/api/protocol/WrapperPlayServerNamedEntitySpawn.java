package com.enotakin.library.api.protocol;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import org.bukkit.World;
import org.bukkit.entity.Entity;

import java.util.UUID;

public class WrapperPlayServerNamedEntitySpawn extends PacketWrapper {

    public static final PacketType TYPE = PacketType.Play.Server.NAMED_ENTITY_SPAWN;

    public WrapperPlayServerNamedEntitySpawn() {
        super(new PacketContainer(TYPE), TYPE);
        handle.getModifier().writeDefaults();
    }

    public WrapperPlayServerNamedEntitySpawn(PacketContainer packet) {
        super(packet, TYPE);
    }

    public int getEntityID() {
        return handle.getIntegers().read(0);
    }

    public void setEntityID(int value) {
        handle.getIntegers().write(0, value);
    }

    public Entity getEntity(World world) {
        return handle.getEntityModifier(world).read(0);
    }

    public UUID getPlayerUUID() {
        return handle.getUUIDs().read(0);
    }

    public void setPlayerUUID(UUID value) {
        handle.getUUIDs().write(0, value);
    }

    public double getX() {
        return handle.getDoubles().read(0);
    }

    public void setX(double value) {
        handle.getDoubles().write(0, value);
    }

    public double getY() {
        return handle.getDoubles().read(1);
    }

    public void setY(double value) {
        handle.getDoubles().write(1, value);
    }

    public double getZ() {
        return handle.getDoubles().read(2);
    }

    public void setZ(double value) {
        handle.getDoubles().write(2, value);
    }

    public float getYaw() {
        return (handle.getBytes().read(0) * 360F) / 256F;
    }

    public void setYaw(float value) {
        handle.getBytes().write(0, (byte) (value * 256F / 360F));
    }

    public float getPitch() {
        return (handle.getBytes().read(1) * 360F) / 256F;
    }

    public void setPitch(float value) {
        handle.getBytes().write(1, (byte) (value * 256F / 360F));
    }

}
