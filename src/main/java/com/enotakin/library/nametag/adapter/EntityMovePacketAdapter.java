package com.enotakin.library.nametag.adapter;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.enotakin.library.api.protocol.WrapperPlayServerEntityTeleport;
import com.enotakin.library.api.protocol.WrapperPlayServerEntityVelocity;
import com.enotakin.library.api.protocol.WrapperPlayServerRelEntityMove;
import com.enotakin.library.api.protocol.WrapperPlayServerRelEntityMoveLook;
import com.enotakin.library.nametag.LibNameTag;
import com.enotakin.library.nametag.NameTagManager;

public class EntityMovePacketAdapter extends PacketAdapter {

    private final NameTagManager manager;

    public EntityMovePacketAdapter(NameTagManager manager) {
        super(manager.getPlugin(),
                PacketType.Play.Server.NAMED_ENTITY_SPAWN,
                PacketType.Play.Server.ENTITY_TELEPORT,
                PacketType.Play.Server.ENTITY_VELOCITY,
                PacketType.Play.Server.REL_ENTITY_MOVE,
                PacketType.Play.Server.REL_ENTITY_MOVE_LOOK
        );

        this.manager = manager;
    }

    @Override
    public void onPacketSending(PacketEvent event) {
        // This method runs on the main thread, so using the Bukkit API is safe,
        // but only if ListenerOptions.ASYNC is not enabled (disabled by default)

        PacketType type = event.getPacketType();
        if (type == PacketType.Play.Server.ENTITY_TELEPORT) {
            WrapperPlayServerEntityTeleport wrapper = new WrapperPlayServerEntityTeleport(event.getPacket());

            LibNameTag nameTag = manager.getNameTag(wrapper.getEntityID());
            if (nameTag != null) {
                nameTag.teleportTo(event.getPlayer(), wrapper.getX(), wrapper.getY(), wrapper.getZ());
            }
        } else if (type == PacketType.Play.Server.ENTITY_VELOCITY) {
            WrapperPlayServerEntityVelocity wrapper = new WrapperPlayServerEntityVelocity(event.getPacket());

            LibNameTag nameTag = manager.getNameTag(wrapper.getEntityID());
            if (nameTag != null) {
                nameTag.velocityTo(event.getPlayer(), wrapper.getNativeX(), wrapper.getNativeY(), wrapper.getNativeZ());
            }
        } else if (type == PacketType.Play.Server.REL_ENTITY_MOVE) {
            WrapperPlayServerRelEntityMove wrapper = new WrapperPlayServerRelEntityMove(event.getPacket());

            LibNameTag nameTag = manager.getNameTag(wrapper.getEntityID());
            if (nameTag != null) {
                nameTag.moveTo(event.getPlayer(), wrapper.getNativeX(), wrapper.getNativeY(), wrapper.getNativeZ());
            }
        } else if (type == PacketType.Play.Server.REL_ENTITY_MOVE_LOOK) {
            WrapperPlayServerRelEntityMoveLook wrapper = new WrapperPlayServerRelEntityMoveLook(event.getPacket());

            LibNameTag nameTag = manager.getNameTag(wrapper.getEntityID());
            if (nameTag != null) {
                nameTag.moveTo(event.getPlayer(), wrapper.getNativeX(), wrapper.getNativeY(), wrapper.getNativeZ());
            }
        }
    }

}
