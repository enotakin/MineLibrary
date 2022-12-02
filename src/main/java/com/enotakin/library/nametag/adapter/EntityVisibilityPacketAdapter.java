package com.enotakin.library.nametag.adapter;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.enotakin.library.api.protocol.WrapperPlayServerEntityDestroy;
import com.enotakin.library.api.protocol.WrapperPlayServerNamedEntitySpawn;
import com.enotakin.library.api.protocol.WrapperPlayServerSpawnEntityLiving;
import com.enotakin.library.nametag.LibNameTag;
import com.enotakin.library.nametag.NameTagManager;

public class EntityVisibilityPacketAdapter extends PacketAdapter {

    private final NameTagManager manager;

    public EntityVisibilityPacketAdapter(NameTagManager manager) {
        super(manager.getPlugin(),
                PacketType.Play.Server.NAMED_ENTITY_SPAWN,
                PacketType.Play.Server.SPAWN_ENTITY_LIVING,
                PacketType.Play.Server.ENTITY_DESTROY
        );

        this.manager = manager;
    }

    @Override
    public void onPacketSending(PacketEvent event) {
        // This method runs on the main thread, so using the Bukkit API is safe,
        // but only if ListenerOptions.ASYNC is not enabled (disabled by default)

        PacketType type = event.getPacketType();
        if (type == PacketType.Play.Server.NAMED_ENTITY_SPAWN) {
            WrapperPlayServerNamedEntitySpawn wrapper = new WrapperPlayServerNamedEntitySpawn(event.getPacket());

            LibNameTag nameTag = manager.getNameTag(wrapper.getEntityID());
            if (nameTag != null) {
                nameTag.addViewer(event.getPlayer(), wrapper.getX(), wrapper.getY(), wrapper.getZ());
            }
        } else if (type == PacketType.Play.Server.SPAWN_ENTITY_LIVING) {
            WrapperPlayServerSpawnEntityLiving wrapper = new WrapperPlayServerSpawnEntityLiving();

            LibNameTag nameTag = manager.getNameTag(wrapper.getEntityID());
            if (nameTag != null) {
                nameTag.addViewer(event.getPlayer(), wrapper.getX(), wrapper.getY(), wrapper.getZ());
            }
        } else if (type == PacketType.Play.Server.ENTITY_DESTROY) {
            WrapperPlayServerEntityDestroy wrapper = new WrapperPlayServerEntityDestroy(event.getPacket());

            for (int entityID : wrapper.getEntityIDs()) {
                LibNameTag nameTag = manager.getNameTag(entityID);
                if (nameTag != null) {
                    nameTag.removeViewer(event.getPlayer());
                }
            }
        }
    }

}
