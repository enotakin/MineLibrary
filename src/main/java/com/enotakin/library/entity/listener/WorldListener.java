package com.enotakin.library.entity.listener;

import com.enotakin.library.entity.EntityManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.event.world.WorldUnloadEvent;

public class WorldListener implements Listener {

    private final EntityManager manager;

    public WorldListener(EntityManager manager) {
        this.manager = manager;
    }

    @EventHandler
    public void onWorldLoad(WorldLoadEvent event) {
        manager.createWorld(event.getWorld());
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onWorldUnload(WorldUnloadEvent event) {
        manager.removeWorld(event.getWorld());
    }

}
