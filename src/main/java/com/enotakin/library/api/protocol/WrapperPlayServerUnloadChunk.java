package com.enotakin.library.api.protocol;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;

public class WrapperPlayServerUnloadChunk extends PacketWrapper {

    public static final PacketType TYPE = PacketType.Play.Server.UNLOAD_CHUNK;

    public WrapperPlayServerUnloadChunk() {
        super(new PacketContainer(TYPE), TYPE);
        handle.getModifier().writeDefaults();
    }

    public WrapperPlayServerUnloadChunk(PacketContainer packet) {
        super(packet, TYPE);
    }

    public int getChunkX() {
        return handle.getIntegers().read(0);
    }

    public void setChunkX(int value) {
        handle.getIntegers().write(0, value);
    }

    public int getChunkZ() {
        return handle.getIntegers().read(1);
    }

    public void setChunkZ(int value) {
        handle.getIntegers().write(1, value);
    }

}
