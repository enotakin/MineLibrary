package com.enotakin.library.api.entity;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface MineHologram extends MineRenderedEntity {

    double TEXT_LINE_HEIGHT = 0.25D;
    double ITEM_LINE_HEIGHT = 0.6D;

    default void addLine(String text) {
        if (text == null || text.isEmpty()) {
            addLine(TEXT_LINE_HEIGHT);
        } else {
            addLine(Component.text(text));
        }
    }

    void addLine(Component text);

    void addLine(ItemStack itemStack);

    void addLine(double height);

    default void setLine(int index, String text) {
        if (text == null || text.isEmpty()) {
            setLine(index, TEXT_LINE_HEIGHT);
        } else {
            setLine(index, Component.text(text));
        }
    }

    void setLine(int index, Component text);

    void setLine(int index, ItemStack itemStack);

    void setLine(int index, double height);

    default void insertLine(int index, String text) {
        if (text == null || text.isEmpty()) {
            insertLine(index, TEXT_LINE_HEIGHT);
        } else {
            insertLine(index, Component.text(text));
        }
    }

    void insertLine(int index, Component text);

    void insertLine(int index, ItemStack itemStack);

    void insertLine(int index, double height);

    void removeLine(int index);

    void playPickupTo(Player player, int index);

    void clear();

}
