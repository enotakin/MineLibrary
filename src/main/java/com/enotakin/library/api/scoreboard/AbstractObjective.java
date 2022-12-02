package com.enotakin.library.api.scoreboard;

import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.enotakin.library.api.protocol.PacketWrapper;
import com.enotakin.library.api.protocol.WrapperPlayServerScoreboardDisplayObjective;
import com.enotakin.library.api.protocol.WrapperPlayServerScoreboardObjective;
import com.enotakin.library.api.protocol.WrapperPlayServerScoreboardScore;
import io.papermc.paper.adventure.PaperAdventure;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import com.enotakin.library.api.util.Cmpt;

public abstract class AbstractObjective extends AbstractScoreboard implements Objective {

    protected final String name;

    protected Component displayName;
    protected WrappedChatComponent wrappedDisplayName;

    AbstractObjective(String name, Component displayName) {
        this.name = name;

        if (displayName == null || Cmpt.isEmpty(displayName)) {
            this.displayName = Cmpt.EMPTY;
            this.wrappedDisplayName = EMPTY_TEXT;
        } else {
            setDisplayName(displayName);
        }
    }

    @Override
    public @NotNull String getName() {
        return name;
    }

    @Override
    public @NotNull Component getDisplayName() {
        return displayName;
    }

    @Override
    public void setDisplayName(Component displayName) {
        if (displayName == null || Cmpt.isEmpty(displayName)) {
            displayName = Cmpt.EMPTY;
        }

        this.displayName = displayName;
        this.wrappedDisplayName = WrappedChatComponent.fromHandle(PaperAdventure.asVanilla(displayName));

        if (players.size() > 0) {
            PacketWrapper wrapper = getObjectivePacket(name, wrappedDisplayName, getType(), false);
            players.forEach(wrapper::sendPacket);
        }
    }

    @Override
    protected void onShow(Player player) {
        getObjectivePacket(name, wrappedDisplayName, getType(), true)
                .sendPacket(player);

        getDisplayObjectivePacket(getPosition(), name)
                .sendPacket(player);
    }

    @Override
    protected void onHide(Player player) {
        getObjectivePacket(name)
                .sendPacket(player);
    }

    protected static PacketWrapper getScorePacket(String scoreName, String objectiveName, int score) {
        // Create or update score
        WrapperPlayServerScoreboardScore wrapper = new WrapperPlayServerScoreboardScore();
        wrapper.setScoreName(scoreName);
        wrapper.setObjectiveName(objectiveName);
        wrapper.setValue(score);
        wrapper.setScoreboardAction(EnumWrappers.ScoreboardAction.CHANGE);
        return wrapper;
    }

    protected static PacketWrapper getScorePacket(String scoreName, String objectiveName) {
        // Remove score
        WrapperPlayServerScoreboardScore wrapper = new WrapperPlayServerScoreboardScore();
        wrapper.setScoreName(scoreName);
        wrapper.setObjectiveName(objectiveName);
        wrapper.setScoreboardAction(EnumWrappers.ScoreboardAction.REMOVE);
        return wrapper;
    }

    protected static PacketWrapper getObjectivePacket(String objectiveName, WrappedChatComponent objectiveDisplayName, Type type, boolean createOrUpdate) {
        // Create or update objective
        WrapperPlayServerScoreboardObjective wrapper = new WrapperPlayServerScoreboardObjective();
        wrapper.setName(objectiveName);
        wrapper.setDisplayName(objectiveDisplayName);
        wrapper.setHealthDisplay(WrapperPlayServerScoreboardObjective.HealthDisplay.valueOf(type.ordinal()));
        wrapper.setMode(createOrUpdate ? WrapperPlayServerScoreboardObjective.Mode.ADD_OBJECTIVE : WrapperPlayServerScoreboardObjective.Mode.UPDATE_VALUE);
        return wrapper;
    }

    protected static PacketWrapper getObjectivePacket(String objectiveName) {
        // Remove objective
        WrapperPlayServerScoreboardObjective wrapper = new WrapperPlayServerScoreboardObjective();
        wrapper.setName(objectiveName);
        wrapper.setMode(WrapperPlayServerScoreboardObjective.Mode.REMOVE_OBJECTIVE);
        return wrapper;
    }

    protected static PacketWrapper getDisplayObjectivePacket(Position position, String objectiveName) {
        // Create display objective
        WrapperPlayServerScoreboardDisplayObjective wrapper = new WrapperPlayServerScoreboardDisplayObjective();
        wrapper.setPosition(position.ordinal());
        wrapper.setScoreName(objectiveName);
        return wrapper;
    }

}
