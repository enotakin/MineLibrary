package com.enotakin.library.api.scoreboard;

import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface Objective extends Scoreboard {

    @NotNull String getName();

    @NotNull Position getPosition();

    @NotNull Type getType();

    void setType(@NotNull Type type) throws UnsupportedOperationException;

    @NotNull Component getDisplayName();

    void setDisplayName(@Nullable Component displayName) throws UnsupportedOperationException;

    enum Position {
        LIST,
        SIDEBAR,
        BELOW_NAME;
    }

    enum Type {
        INTEGER,
        HEARTS;
    }

}
