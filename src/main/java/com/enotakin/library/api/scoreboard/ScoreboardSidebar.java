package com.enotakin.library.api.scoreboard;

import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.enotakin.library.api.protocol.PacketWrapper;
import io.papermc.paper.adventure.PaperAdventure;
import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import com.enotakin.library.api.protocol.WrapperPlayServerScoreboardTeam;
import com.enotakin.library.api.util.Cmpt;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ScoreboardSidebar extends AbstractObjective implements Sidebar {

    private final Map<Integer, SidebarLine> lines = new HashMap<>();

    public ScoreboardSidebar(String name, Component displayName) {
        super(name, displayName);
    }

    @Override
    public @NotNull Position getPosition() {
        return Position.SIDEBAR;
    }

    @Override
    public @NotNull Type getType() {
        return Type.INTEGER;
    }

    @Override
    public void setType(@NotNull Type type) throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Sidebar should always be '" + getType() + "'");
    }

    @Override
    public void setLine(int score, Component text) {
        if (text == null || Cmpt.isEmpty(text)) {
            text = Cmpt.EMPTY;
        }

        SidebarLine line = lines.get(score);
        if (line != null) {
            line.setText(text);

            if (players.size() > 0) {
                WrapperPlayServerScoreboardTeam wrapper = new WrapperPlayServerScoreboardTeam();
                wrapper.setMode(WrapperPlayServerScoreboardTeam.Mode.TEAM_UPDATED);
                wrapper.setName(line.name);
                wrapper.setPrefix(line.text);
                wrapper.setDisplayName(EMPTY_TEXT);
                wrapper.setColor(ChatColor.RESET);
                wrapper.setPackOptionData(0);

                players.forEach(wrapper::sendPacket);
            }
        } else {
            int size = lines.size();
            if (size >= 15) {
                throw new IllegalStateException("Maximum sidebar lines [" + (size + 1) + "/15]");
            }

            line = new SidebarLine(score);
            line.setText(text);
            lines.put(score, line);

            if (players.size() > 0) {
                PacketWrapper scoreWrapper = getScorePacket(line.name, getName(), line.score);

                WrapperPlayServerScoreboardTeam teamWrapper = new WrapperPlayServerScoreboardTeam();
                teamWrapper.setMode(WrapperPlayServerScoreboardTeam.Mode.TEAM_CREATED);
                teamWrapper.setName(line.name);
                teamWrapper.setPlayers(Collections.singletonList(line.name));
                teamWrapper.setPrefix(line.text);
                teamWrapper.setDisplayName(EMPTY_TEXT);
                teamWrapper.setColor(ChatColor.WHITE);
                teamWrapper.setPackOptionData(0);

                for (Player player : players) {
                    scoreWrapper.sendPacket(player);
                    teamWrapper.sendPacket(player);
                }
            }
        }
    }

    @Override
    public void removeLine(int score) {
        SidebarLine line = lines.remove(score);
        if (line != null && players.size() > 0) {
            PacketWrapper wrapper = getScorePacket(line.name, getName());
            players.forEach(wrapper::sendPacket);
        }
    }

    @Override
    protected void onShow(Player player) {
        super.onShow(player);

        for (SidebarLine line : lines.values()) {
            PacketWrapper scoreWrapper = getScorePacket(line.name, getName(), line.score);

            WrapperPlayServerScoreboardTeam teamWrapper = new WrapperPlayServerScoreboardTeam();
            teamWrapper.setMode(WrapperPlayServerScoreboardTeam.Mode.TEAM_CREATED);
            teamWrapper.setName(line.name);
            teamWrapper.setPlayers(Collections.singletonList(line.name));
            teamWrapper.setPrefix(line.text);
            teamWrapper.setDisplayName(EMPTY_TEXT);
            teamWrapper.setColor(ChatColor.WHITE);
            teamWrapper.setPackOptionData(0);

            scoreWrapper.sendPacket(player);
            teamWrapper.sendPacket(player);
        }
    }

    private static class SidebarLine {

        private final int score;
        private final String name;

        private WrappedChatComponent text;

        SidebarLine(int score) {
            this.score = score;
            this.name = ChatColor.COLOR_CHAR + "" + (char) score;
        }

        public void setText(Component text) {
            this.text = WrappedChatComponent.fromHandle(PaperAdventure.asVanilla(text));
        }

    }

}
