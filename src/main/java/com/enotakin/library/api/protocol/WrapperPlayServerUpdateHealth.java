package com.enotakin.library.api.protocol;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;

public class WrapperPlayServerUpdateHealth extends PacketWrapper {

    public static final PacketType TYPE = PacketType.Play.Server.UPDATE_HEALTH;

    public WrapperPlayServerUpdateHealth() {
        super(new PacketContainer(TYPE), TYPE);
        handle.getModifier().writeDefaults();
    }

    public WrapperPlayServerUpdateHealth(PacketContainer packet) {
        super(packet, TYPE);
    }

    public float getHealth() {
        return handle.getFloat().read(0);
    }

    public void setHealth(float value) {
        handle.getFloat().write(0, value);
    }

    public int getFood() {
        return handle.getIntegers().read(0);
    }

    public void setFood(int value) {
        handle.getIntegers().write(0, value);
    }

    public float getFoodSaturation() {
        return handle.getFloat().read(1);
    }

    public void setFoodSaturation(float value) {
        handle.getFloat().write(1, value);
    }

}
