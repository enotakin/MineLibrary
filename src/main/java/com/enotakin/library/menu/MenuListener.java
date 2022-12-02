package com.enotakin.library.menu;

import com.enotakin.library.api.menu.Menu;
import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;

import java.util.logging.Level;

public class MenuListener implements Listener {

    @EventHandler
    public void onInventoryOpen(InventoryOpenEvent event) {
        if (event.getPlayer() instanceof Player player && event.getInventory().getHolder() instanceof Menu menu) {
            try {
                menu.onOpen(player);
            } catch (Exception exception) {
                Bukkit.getLogger().log(Level.WARNING, "Error when opening menu: " + menu.getClass().getName(), exception);
            }
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getWhoClicked() instanceof Player player && event.getView().getTopInventory().getHolder() instanceof Menu menu) {
            if (event.getClickedInventory() == event.getView().getTopInventory()) {
                try {
                    menu.onClick(event.getCurrentItem(), player, event.getSlot(), event.getClick());
                } catch (Exception exception) {
                    Bukkit.getLogger().log(Level.WARNING, "Error when clicking menu: " + menu.getClass().getName(), exception);
                }
            }
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent event) {
        if (event.getWhoClicked().getType() == EntityType.PLAYER && event.getView().getTopInventory().getHolder() instanceof Menu) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (event.getPlayer() instanceof Player player && event.getInventory().getHolder() instanceof Menu menu) {
            try {
                menu.onClose(player);
            } catch (Exception exception) {
                Bukkit.getLogger().log(Level.WARNING, "Error when closing menu: " + menu.getClass().getName(), exception);
            }
        }
    }

}
