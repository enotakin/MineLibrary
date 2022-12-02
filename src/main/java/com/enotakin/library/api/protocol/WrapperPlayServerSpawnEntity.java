package com.enotakin.library.api.protocol;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

import java.util.UUID;

public class WrapperPlayServerSpawnEntity extends PacketWrapper {

    public static final PacketType TYPE = PacketType.Play.Server.SPAWN_ENTITY;

    public WrapperPlayServerSpawnEntity() {
        super(new PacketContainer(TYPE), TYPE);
        handle.getModifier().writeDefaults();
    }

    public WrapperPlayServerSpawnEntity(PacketContainer packet) {
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

    public UUID getUniqueID() {
        return handle.getUUIDs().read(0);
    }

    public void setUniqueID(UUID value) {
        handle.getUUIDs().write(0, value);
    }

    public EntityType getType() {
        return handle.getEntityTypeModifier().read(0);
    }

    public void setType(EntityType value) {
        handle.getEntityTypeModifier().write(0, value);
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

    public double getOptionalSpeedX() {
        return handle.getIntegers().read(1) / 8000D;
    }

    public void setOptionalSpeedX(double value) {
        handle.getIntegers().write(1, (int) (value * 8000D));
    }

    public double getOptionalSpeedY() {
        return handle.getIntegers().read(2) / 8000D;
    }

    public void setOptionalSpeedY(double value) {
        handle.getIntegers().write(2, (int) (value * 8000D));
    }

    public double getOptionalSpeedZ() {
        return handle.getIntegers().read(3) / 8000D;
    }

    public void setOptionalSpeedZ(double value) {
        handle.getIntegers().write(3, (int) (value * 8000D));
    }

    public float getPitch() {
        return (handle.getIntegers().read(4) * 360F) / 256F;
    }

    public void setPitch(float value) {
        handle.getIntegers().write(4, (int) (value * 256F / 360F));
    }

    public float getYaw() {
        return (handle.getIntegers().read(5) * 360F) / 256F;
    }

    public void setYaw(float value) {
        handle.getIntegers().write(5, (int) (value * 256F / 360F));
    }

    public int getObjectData() {
        return handle.getIntegers().read(6);
    }

    public void setObjectData(int value) {
        handle.getIntegers().write(6, value);
    }

}
