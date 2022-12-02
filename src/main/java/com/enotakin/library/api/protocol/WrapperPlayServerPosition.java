package com.enotakin.library.api.protocol;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.reflect.StructureModifier;
import com.comphenix.protocol.utility.MinecraftReflection;
import com.comphenix.protocol.wrappers.EnumWrappers;

import java.util.Set;

public class WrapperPlayServerPosition extends PacketWrapper {

    private static final Class<?> FLAGS_CLASS = MinecraftReflection.getMinecraftClass("EnumPlayerTeleportFlags", "PacketPlayOutPosition$EnumPlayerTeleportFlags");

    public static final PacketType TYPE = PacketType.Play.Server.POSITION;

    public WrapperPlayServerPosition() {
        super(new PacketContainer(TYPE), TYPE);
        handle.getModifier().writeDefaults();
    }

    public WrapperPlayServerPosition(PacketContainer packet) {
        super(packet, TYPE);
    }

    public double getX() {
        return handle.getDoubles().read(0);
    }

    public void setX(double value) {
        handle.getDoubles().write(0, value);
    }

    public double getY() {
        return handle.getDoubles().read(1);
    }

    public void setY(double value) {
        handle.getDoubles().write(1, value);
    }

    public double getZ() {
        return handle.getDoubles().read(2);
    }

    public void setZ(double value) {
        handle.getDoubles().write(2, value);
    }

    public float getYaw() {
        return handle.getFloat().read(0);
    }

    public void setYaw(float value) {
        handle.getFloat().write(0, value);
    }

    public float getPitch() {
        return handle.getFloat().read(1);
    }

    public void setPitch(float value) {
        handle.getFloat().write(1, value);
    }

    public Set<PlayerTeleportFlag> getFlags() {
        return getFlagsModifier().read(0);
    }

    public void setFlags(Set<PlayerTeleportFlag> value) {
        getFlagsModifier().write(0, value);
    }

    private StructureModifier<Set<PlayerTeleportFlag>> getFlagsModifier() {
        return handle.getSets(EnumWrappers.getGenericConverter(FLAGS_CLASS, PlayerTeleportFlag.class));
    }

    public int getTeleportID() {
        return handle.getIntegers().read(0);
    }

    public void setTeleportID(int value) {
        handle.getIntegers().write(0, value);
    }

    public enum PlayerTeleportFlag {
        X,
        Y,
        Z,
        Y_ROT,
        X_ROT;
    }

}
