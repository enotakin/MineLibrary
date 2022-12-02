package com.enotakin.library.api.protocol;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import org.bukkit.World;
import org.bukkit.entity.Entity;

public class WrapperPlayServerEntityLook extends PacketWrapper {

    public static final PacketType TYPE = PacketType.Play.Server.ENTITY_LOOK;

    public WrapperPlayServerEntityLook() {
        super(new PacketContainer(TYPE), TYPE);
        handle.getModifier().writeDefaults();
    }

    public WrapperPlayServerEntityLook(PacketContainer packet) {
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
