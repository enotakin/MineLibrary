package com.enotakin.library.api.scoreboard;

import org.bukkit.entity.Player;

import java.util.Collection;

public interface ValuableObjective extends Objective {

    Collection<String> getEntries();

    default int getEntry(Player player) {
        return getEntry(player.getName());
    }

    int getEntry(String entry);

    default void setEntry(Player player, int value) {
        setEntry(player.getName(), value);
    }

    void setEntry(String entry, int value);

    default boolean removeEntry(Player player) {
        return removeEntry(player.getName());
    }

    boolean removeEntry(String entry);

}
