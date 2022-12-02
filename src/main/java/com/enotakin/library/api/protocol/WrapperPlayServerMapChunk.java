package com.enotakin.library.api.protocol;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.nbt.NbtBase;

import java.util.List;

public class WrapperPlayServerMapChunk extends PacketWrapper {

    public static final PacketType TYPE = PacketType.Play.Server.MAP_CHUNK;

    public WrapperPlayServerMapChunk() {
        super(new PacketContainer(TYPE), TYPE);
        handle.getModifier().writeDefaults();
    }

    public WrapperPlayServerMapChunk(PacketContainer packet) {
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

    public boolean getGroundUpContinuous() {
        return handle.getBooleans().read(0);
    }

    public void setGroundUpContinuous(boolean value) {
        handle.getBooleans().write(0, value);
    }

    public int getBitmask() {
        return handle.getIntegers().read(2);
    }

    public void setBitmask(int value) {
        handle.getIntegers().write(2, value);
    }

    public byte[] getData() {
        return handle.getByteArrays().read(0);
    }

    public void setData(byte[] value) {
        handle.getByteArrays().write(0, value);
    }

    public List<NbtBase<?>> getTileEntities() {
        return handle.getListNbtModifier().read(0);
    }

    public void setTileEntities(List<NbtBase<?>> value) {
        handle.getListNbtModifier().write(0, value);
    }

}
