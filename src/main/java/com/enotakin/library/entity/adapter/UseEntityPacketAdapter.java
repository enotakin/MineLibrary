package com.enotakin.library.entity.adapter;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.enotakin.library.api.protocol.WrapperPlayClientUseEntity;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import com.enotakin.library.api.entity.Interact;
import com.enotakin.library.entity.EntityManager;

public class UseEntityPacketAdapter extends PacketAdapter {

    private final EntityManager manager;

    public UseEntityPacketAdapter(EntityManager manager) {
        super(manager.getPlugin(), PacketType.Play.Client.USE_ENTITY);
        this.manager = manager;
    }

    @Override
    public void onPacketReceiving(PacketEvent event) {
        // This code may be running on a different thread,
        // so be sure to check the thread to work

        if (event.getPacketType() == PacketType.Play.Client.USE_ENTITY) {
            WrapperPlayClientUseEntity wrapper = new WrapperPlayClientUseEntity(event.getPacket());

            Player player = event.getPlayer();
            int targetID = wrapper.getTargetID();
            Interact.Action action = Interact.Action.valueOf(wrapper.getType().name());

            if (Bukkit.isPrimaryThread()) {
                manager.onInteract(player, targetID, action);
            } else {
                plugin.getServer().getScheduler().runTask(plugin, () -> manager.onInteract(player, targetID, action));
            }
        }
    }

}
