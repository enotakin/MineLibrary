package com.enotakin.library.api.scoreboard;

import org.bukkit.entity.Player;

import java.util.Collection;

public interface Scoreboard {

    Collection<Player> getPlayers();

    boolean showTo(Player player);

    boolean hideTo(Player player);

    void clear();

}
