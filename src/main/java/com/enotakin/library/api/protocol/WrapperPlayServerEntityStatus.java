package com.enotakin.library.api.protocol;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;

public class WrapperPlayServerEntityStatus extends PacketWrapper {

    public static final PacketType TYPE = PacketType.Play.Server.ENTITY_STATUS;

    public WrapperPlayServerEntityStatus() {
        super(new PacketContainer(TYPE), TYPE);
        handle.getModifier().writeDefaults();
    }

    public WrapperPlayServerEntityStatus(PacketContainer packet) {
        super(packet, TYPE);
    }

    public int getEntityID() {
        return handle.getIntegers().read(0);
    }

    public void setEntityID(int value) {
        handle.getIntegers().write(0, value);
    }

    public byte getEntityStatus() {
        return handle.getBytes().read(0);
    }

    public void setEntityStatus(byte value) {
        handle.getBytes().write(0, value);
    }

}
