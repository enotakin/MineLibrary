package com.enotakin.library.api.protocol;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;

import java.util.UUID;

public class WrapperPlayServerSpawnEntityLiving extends PacketWrapper {

    public static final PacketType TYPE = PacketType.Play.Server.SPAWN_ENTITY_LIVING;

    public WrapperPlayServerSpawnEntityLiving() {
        super(new PacketContainer(TYPE), TYPE);
        handle.getModifier().writeDefaults();
    }

    public WrapperPlayServerSpawnEntityLiving(PacketContainer packet) {
        super(packet, TYPE);
    }

    public int getEntityID() {
        return handle.getIntegers().read(0);
    }

    public void setEntityID(int value) {
        handle.getIntegers().write(0, value);
    }

    public UUID getUniqueID() {
        return handle.getUUIDs().read(0);
    }

    public void setUniqueID(UUID value) {
        handle.getUUIDs().write(0, value);
    }

    public int getTypeID() {
        return handle.getIntegers().read(1);
    }

    public void setTypeID(int typeID) {
        handle.getIntegers().write(1, typeID);
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

    public float getHeadPitch() {
        return (handle.getBytes().read(2) * 360F) / 256F;
    }

    public void setHeadPitch(float value) {
        handle.getBytes().write(2, (byte) (value * 256F / 360F));
    }

    public double getVelocityX() {
        return handle.getIntegers().read(2) / 8000D;
    }

    public void setVelocityX(double value) {
        handle.getIntegers().write(2, (int) (value * 8000D));
    }

    public double getVelocityY() {
        return handle.getIntegers().read(3) / 8000D;
    }

    public void setVelocityY(double value) {
        handle.getIntegers().write(3, (int) (value * 8000D));
    }

    public double getVelocityZ() {
        return handle.getIntegers().read(4) / 8000D;
    }

    public void setVelocityZ(double value) {
        handle.getIntegers().write(4, (int) (value * 8000D));
    }

}
