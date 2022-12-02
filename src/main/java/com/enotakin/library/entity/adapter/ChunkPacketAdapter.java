package com.enotakin.library.entity.adapter;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.enotakin.library.api.protocol.WrapperPlayServerMapChunk;
import com.enotakin.library.api.protocol.WrapperPlayServerUnloadChunk;
import com.enotakin.library.entity.EntityManager;
import com.enotakin.library.entity.WorldRepository;
import org.bukkit.entity.Player;

public class ChunkPacketAdapter extends PacketAdapter {

    private final EntityManager manager;

    public ChunkPacketAdapter(EntityManager manager) {
        super(manager.getPlugin(),
                PacketType.Play.Server.MAP_CHUNK,
                PacketType.Play.Server.UNLOAD_CHUNK
        );

        this.manager = manager;
    }

    @Override
    public void onPacketSending(PacketEvent event) {
        // This method runs on the main thread, so using the Bukkit API is safe,
        // but only if ListenerOptions.ASYNC is not enabled (disabled by default)

        Player player = event.getPlayer();
        WorldRepository worldRepository = manager.getWorld(player.getWorld());

        PacketType type = event.getPacketType();
        if (type == PacketType.Play.Server.MAP_CHUNK) {
            WrapperPlayServerMapChunk wrapper = new WrapperPlayServerMapChunk(event.getPacket());
            WorldRepository.ChunkRepository chunkRepository = worldRepository.getChunkRepository(wrapper.getChunkX(), wrapper.getChunkZ(), true);
            chunkRepository.addPlayer(player);
        } else if (type == PacketType.Play.Server.UNLOAD_CHUNK) {
            WrapperPlayServerUnloadChunk wrapper = new WrapperPlayServerUnloadChunk(event.getPacket());

            WorldRepository.ChunkRepository chunkRepository = worldRepository.getChunkRepository(wrapper.getChunkX(), wrapper.getChunkZ(), false);
            if (chunkRepository != null) {
                chunkRepository.removePlayer(player, true);
            }
        }
    }

}
