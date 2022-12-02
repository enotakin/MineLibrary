package com.enotakin.library.api.protocol;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import com.google.common.base.Objects;
import org.bukkit.entity.Player;

public abstract class PacketWrapper {

    protected final PacketContainer handle;

    protected PacketWrapper(PacketContainer handle, PacketType type) {
        if (handle == null) {
            throw new IllegalArgumentException("Packet handle cannot be NULL.");
        }

        if (!Objects.equal(handle.getType(), type)) {
            throw new IllegalArgumentException(handle.getHandle() + " is not a packet of type " + type);
        }

        this.handle = handle;
    }

    public PacketContainer getHandle() {
        return handle;
    }

    public void broadcastPacket() {
        try {
            ProtocolLibrary.getProtocolManager().broadcastServerPacket(getHandle());
        } catch (Exception ex) {
            throw new RuntimeException("Cannot broadcast packet: ", ex);
        }
    }

    public void sendPacket(Player receiver) {
        try {
            ProtocolLibrary.getProtocolManager().sendServerPacket(receiver, getHandle());
        } catch (Exception ex) {
            throw new RuntimeException("Cannot send packet: ", ex);
        }
    }

    public void receivePacket(Player sender) {
        try {
            ProtocolLibrary.getProtocolManager().recieveClientPacket(sender, getHandle());
        } catch (Exception ex) {
            throw new RuntimeException("Cannot receive packet: ", ex);
        }
    }

}
