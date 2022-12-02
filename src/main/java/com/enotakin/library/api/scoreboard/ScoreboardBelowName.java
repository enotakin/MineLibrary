package com.enotakin.library.api.scoreboard;

import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

public class ScoreboardBelowName extends AbstractValuableObjective implements ValuableObjective {

    public ScoreboardBelowName(String name, Component displayName) {
        super(name, displayName);
    }

    @Override
    public @NotNull Position getPosition() {
        return Position.BELOW_NAME;
    }

    @Override
    public @NotNull Type getType() {
        return Type.INTEGER;
    }

    @Override
    public void setType(@NotNull Type type) throws UnsupportedOperationException {
        throw new UnsupportedOperationException("BelowName should always be '" + getType() + "'");
    }

}
