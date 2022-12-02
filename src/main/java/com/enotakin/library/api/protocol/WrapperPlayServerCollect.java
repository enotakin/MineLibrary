package com.enotakin.library.api.protocol;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;

public class WrapperPlayServerCollect extends PacketWrapper {

    public static final PacketType TYPE = PacketType.Play.Server.COLLECT;

    public WrapperPlayServerCollect() {
        super(new PacketContainer(TYPE), TYPE);
        handle.getModifier().writeDefaults();
    }

    public WrapperPlayServerCollect(PacketContainer packet) {
        super(packet, TYPE);
    }

    public int getCollectedEntityID() {
        return handle.getIntegers().read(0);
    }

    public void setCollectedEntityID(int value) {
        handle.getIntegers().write(0, value);
    }

    public int getCollectorEntityID() {
        return handle.getIntegers().read(1);
    }

    public void setCollectorEntityID(int value) {
        handle.getIntegers().write(1, value);
    }

    public int getPickupItemCount() {
        return handle.getIntegers().read(2);
    }

    public void setPickupItemCount(int value) {
        handle.getIntegers().write(2, value);
    }

}
