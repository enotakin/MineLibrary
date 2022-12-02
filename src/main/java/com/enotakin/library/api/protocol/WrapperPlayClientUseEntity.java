package com.enotakin.library.api.protocol;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.EnumWrappers.EntityUseAction;
import org.bukkit.util.Vector;

public class WrapperPlayClientUseEntity extends PacketWrapper {

    public static final PacketType TYPE = PacketType.Play.Client.USE_ENTITY;

    public WrapperPlayClientUseEntity() {
        super(new PacketContainer(TYPE), TYPE);
        handle.getModifier().writeDefaults();
    }

    public WrapperPlayClientUseEntity(PacketContainer packet) {
        super(packet, TYPE);
    }

    public int getTargetID() {
        return handle.getIntegers().read(0);
    }

    public void setTargetID(int value) {
        handle.getIntegers().write(0, value);
    }

    public EntityUseAction getType() {
        return handle.getEntityUseActions().read(0);
    }

    public void setType(EntityUseAction value) {
        handle.getEntityUseActions().write(0, value);
    }

    public Vector getTargetVector() {
        return handle.getVectors().read(0);
    }

    public void setTargetVector(Vector value) {
        handle.getVectors().write(0, value);
    }

    public EnumWrappers.Hand getHand() {
        return handle.getHands().read(0);
    }

    public void setHand(EnumWrappers.Hand value) {
        handle.getHands().write(0, value);
    }

    public boolean getSneaking() {
        return handle.getBooleans().read(0);
    }

    public void setSneaking(boolean value) {
        handle.getBooleans().write(0, value);
    }

}
