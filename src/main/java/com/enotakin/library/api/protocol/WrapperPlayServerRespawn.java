package com.enotakin.library.api.protocol;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.EnumWrappers.NativeGameMode;
import org.bukkit.World;

public class WrapperPlayServerRespawn extends PacketWrapper {

    public static final PacketType TYPE = PacketType.Play.Server.RESPAWN;

    public WrapperPlayServerRespawn() {
        super(new PacketContainer(TYPE), TYPE);
        handle.getModifier().writeDefaults();
    }

    public WrapperPlayServerRespawn(PacketContainer packet) {
        super(packet, TYPE);
    }

    public World getDimension() {
        return handle.getDimensionTypes().read(0);
    }

    public void setDimension(World value) {
        handle.getDimensionTypes().write(0, value);
    }

    public World getWorldName() {
        return handle.getWorldKeys().read(0);
    }

    public void setWorldName(World value) {
        handle.getWorldKeys().write(0, value);
    }

    public long getHashedSeed() {
        return handle.getLongs().read(0);
    }

    public void setHashedSeed(long value) {
        handle.getLongs().write(0, value);
    }

    public NativeGameMode getGameMode() {
        return handle.getGameModes().read(0);
    }

    public void setGameMode(NativeGameMode value) {
        handle.getGameModes().write(0, value);
    }

    public NativeGameMode getPreviousGameMode() {
        return handle.getGameModes().read(1);
    }

    public void setPreviousGameMode(NativeGameMode value) {
        handle.getGameModes().write(1, value);
    }

    public boolean getDebug() {
        return handle.getBooleans().read(0);
    }

    public void setDebug(boolean value) {
        handle.getBooleans().write(0, value);
    }

    public boolean getFlat() {
        return handle.getBooleans().read(1);
    }

    public void setFlat(boolean value) {
        handle.getBooleans().write(1, value);
    }

    public boolean getCopyMetadata() {
        return handle.getBooleans().read(2);
    }

    public void setCopyMetadata(boolean value) {
        handle.getBooleans().write(2, value);
    }

}
