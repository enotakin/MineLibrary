package com.enotakin.library.api.scoreboard;

import com.enotakin.library.api.protocol.PacketWrapper;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

public class ScoreboardList extends AbstractValuableObjective implements ValuableObjective {

    private Type type;

    public ScoreboardList(String name, Type type) {
        super(name, null);
        this.type = type;
    }

    @Override
    public @NotNull Component getDisplayName() {
        throw new UnsupportedOperationException("List do not have 'Display Name'");
    }

    @Override
    public void setDisplayName(Component displayName) {
        throw new UnsupportedOperationException("List do not have 'Display Name'");
    }

    @Override
    public @NotNull Position getPosition() {
        return Position.LIST;
    }

    @Override
    public @NotNull Type getType() {
        return type;
    }

    @Override
    public void setType(@NotNull Type type) {
        if (this.type != type) {
            this.type = type;

            if (players.size() > 0) {
                PacketWrapper wrapper = getObjectivePacket(name, wrappedDisplayName, getType(), false);
                players.forEach(wrapper::sendPacket);
            }
        }
    }

}
