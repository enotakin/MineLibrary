package com.enotakin.library.api.protocol;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import org.bukkit.World;
import org.bukkit.entity.Entity;

public class WrapperPlayServerRelEntityMove extends PacketWrapper {

    public static final PacketType TYPE = PacketType.Play.Server.REL_ENTITY_MOVE;

    public WrapperPlayServerRelEntityMove() {
        super(new PacketContainer(TYPE), TYPE);
        handle.getModifier().writeDefaults();
    }

    public WrapperPlayServerRelEntityMove(PacketContainer packet) {
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

    public double getDeltaX() {
        return getNativeX() / 4096D;
    }

    public short getNativeX() {
        return handle.getShorts().read(0);
    }

    public void setDeltaX(double value) {
        setNativeX((short) (value * 4096));
    }

    public void setNativeX(short value) {
        handle.getShorts().write(0, value);
    }

    public double getDeltaY() {
        return getNativeY() / 4096D;
    }

    public short getNativeY() {
        return handle.getShorts().read(1);
    }

    public void setDeltaY(double value) {
        setNativeY((short) (value * 4096));
    }

    public void setNativeY(short value) {
        handle.getShorts().write(1, value);
    }

    public double getDeltaZ() {
        return getNativeZ() / 4096D;
    }

    public short getNativeZ() {
        return handle.getShorts().read(2);
    }

    public void setDeltaZ(double value) {
        setNativeZ((short) (value * 4096));
    }

    public void setNativeZ(short value) {
        handle.getShorts().write(2, value);
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

    public boolean getOnGround() {
        return handle.getBooleans().read(0);
    }

    public void setOnGround(boolean value) {
        handle.getBooleans().write(0, value);
    }

}
