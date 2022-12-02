package com.enotakin.library.api.entity;

import org.bukkit.entity.Player;

public interface Interact {

    void onUse(Player player, Action action);

    enum Action {
        INTERACT,
        ATTACK,
        INTERACT_AT
    }

}
