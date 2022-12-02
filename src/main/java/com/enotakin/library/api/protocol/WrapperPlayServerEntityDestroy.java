package com.enotakin.library.api.protocol;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;

public class WrapperPlayServerEntityDestroy extends PacketWrapper {

    public static final PacketType TYPE = PacketType.Play.Server.ENTITY_DESTROY;

    public WrapperPlayServerEntityDestroy() {
        super(new PacketContainer(TYPE), TYPE);
        handle.getModifier().writeDefaults();
    }

    public WrapperPlayServerEntityDestroy(PacketContainer packet) {
        super(packet, TYPE);
    }

    public int[] getEntityIDs() {
        return handle.getIntegerArrays().read(0);
    }

    public void setEntityIDs(int... value) {
        handle.getIntegerArrays().write(0, value);
    }

}
