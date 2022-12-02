package com.enotakin.library.api.menu;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

public interface Menu extends InventoryHolder {

    default void showTo(Player player) {
        player.openInventory(getInventory());
    }

    default void onOpen(Player player) {
    }

    void onClick(@Nullable ItemStack itemStack, Player player, int slot, ClickType type);

    default void onClose(Player player) {
    }

}
