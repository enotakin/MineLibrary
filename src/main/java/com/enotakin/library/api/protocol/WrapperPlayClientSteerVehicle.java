package com.enotakin.library.api.protocol;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;

public class WrapperPlayClientSteerVehicle extends PacketWrapper {

    public static final PacketType TYPE = PacketType.Play.Client.STEER_VEHICLE;

    public WrapperPlayClientSteerVehicle() {
        super(new PacketContainer(TYPE), TYPE);
        handle.getModifier().writeDefaults();
    }

    public WrapperPlayClientSteerVehicle(PacketContainer packet) {
        super(packet, TYPE);
    }

    public float getSideways() {
        return handle.getFloat().read(0);
    }

    public void setSideways(float value) {
        handle.getFloat().write(0, value);
    }

    public float getForward() {
        return handle.getFloat().read(1);
    }

    public void setForward(float value) {
        handle.getFloat().write(1, value);
    }

    public boolean isJump() {
        return handle.getBooleans().read(0);
    }

    public void setJump(boolean value) {
        handle.getBooleans().write(0, value);
    }

    public boolean isUnmount() {
        return handle.getBooleans().read(1);
    }

    public void setUnmount(boolean value) {
        handle.getBooleans().write(1, value);
    }

}
