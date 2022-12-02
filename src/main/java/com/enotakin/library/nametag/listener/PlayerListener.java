package com.enotakin.library.nametag.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import com.enotakin.library.nametag.LibNameTag;
import com.enotakin.library.nametag.NameTagManager;

public class PlayerListener implements Listener {

    private final NameTagManager manager;

    public PlayerListener(NameTagManager manager) {
        this.manager = manager;
    }

    @EventHandler
    public void onPlayerToggleSneak(PlayerToggleSneakEvent event) {
        LibNameTag nameTag = manager.getNameTag(event.getPlayer().getEntityId());
        if (nameTag != null) {
            nameTag.setSneaking(event.isSneaking());
        }
    }

}
