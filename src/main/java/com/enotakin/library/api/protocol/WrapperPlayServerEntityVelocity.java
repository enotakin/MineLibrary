package com.enotakin.library.api.protocol;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import org.bukkit.World;
import org.bukkit.entity.Entity;

public class WrapperPlayServerEntityVelocity extends PacketWrapper {

    public static final PacketType TYPE = PacketType.Play.Server.ENTITY_VELOCITY;

    public WrapperPlayServerEntityVelocity() {
        super(new PacketContainer(TYPE), TYPE);
        handle.getModifier().writeDefaults();
    }

    public WrapperPlayServerEntityVelocity(PacketContainer packet) {
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

    public double getVelocityX() {
        return getNativeX() / 8000D;
    }

    public int getNativeX() {
        return handle.getIntegers().read(1);
    }

    public void setVelocityX(double value) {
        setNativeX((int) (value * 8000D));
    }

    public void setNativeX(int value) {
        handle.getIntegers().write(1, value);
    }

    public double getVelocityY() {
        return getNativeY() / 8000D;
    }

    public int getNativeY() {
        return handle.getIntegers().read(2);
    }

    public void setVelocityY(double value) {
        setNativeY((int) (value * 8000D));
    }

    public void setNativeY(int value) {
        handle.getIntegers().write(2, value);
    }

    public double getVelocityZ() {
        return getNativeZ() / 8000D;
    }

    public int getNativeZ() {
        return handle.getIntegers().read(3);
    }

    public void setVelocityZ(double value) {
        setNativeZ((int) (value * 8000D));
    }

    public void setNativeZ(int value) {
        handle.getIntegers().write(3, value);
    }

}
