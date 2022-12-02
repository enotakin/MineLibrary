package com.enotakin.library.api.protocol;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.EnumWrappers;

public class WrapperPlayClientArmAnimation extends PacketWrapper {

    public static final PacketType TYPE = PacketType.Play.Client.ARM_ANIMATION;

    public WrapperPlayClientArmAnimation() {
        super(new PacketContainer(TYPE), TYPE);
        handle.getModifier().writeDefaults();
    }

    public WrapperPlayClientArmAnimation(PacketContainer packet) {
        super(packet, TYPE);
    }

    public EnumWrappers.Hand getHand() {
        return handle.getHands().read(0);
    }

    public void setHand(EnumWrappers.Hand value) {
        handle.getHands().write(0, value);
    }

}
