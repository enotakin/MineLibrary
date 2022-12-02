package com.enotakin.library.api.scoreboard;

import com.comphenix.protocol.wrappers.WrappedChatComponent;
import io.papermc.paper.adventure.PaperAdventure;
import org.bukkit.entity.Player;
import com.enotakin.library.api.util.Cmpt;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public abstract class AbstractScoreboard implements Scoreboard {

    protected static final WrappedChatComponent EMPTY_TEXT = WrappedChatComponent.fromHandle(PaperAdventure.asVanilla(Cmpt.EMPTY));

    protected final Set<Player> players = new HashSet<>();

    AbstractScoreboard() {
    }

    @Override
    public Collection<Player> getPlayers() {
        return players;
    }

    @Override
    public boolean showTo(Player player) {
        if (players.add(player)) {
            onShow(player);
            return true;
        }
        return false;
    }

    protected abstract void onShow(Player player);

    @Override
    public boolean hideTo(Player player) {
        if (players.remove(player)) {
            onHide(player);
            return true;
        }
        return false;
    }

    protected abstract void onHide(Player player);

    @Override
    public void clear() {
        players.forEach(this::onHide);
        players.clear();
    }

}
