package com.enotakin.library.api.protocol;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.utility.MinecraftReflection;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import org.bukkit.ChatColor;

import java.util.Collection;
import java.util.List;

public class WrapperPlayServerScoreboardTeam extends PacketWrapper {

    public static final PacketType TYPE = PacketType.Play.Server.SCOREBOARD_TEAM;

    public WrapperPlayServerScoreboardTeam() {
        super(new PacketContainer(TYPE), TYPE);
        handle.getModifier().writeDefaults();
    }

    public WrapperPlayServerScoreboardTeam(PacketContainer packet) {
        super(packet, TYPE);
    }

    public static class Mode {
        public static final int TEAM_CREATED = 0;
        public static final int TEAM_REMOVED = 1;
        public static final int TEAM_UPDATED = 2;
        public static final int PLAYERS_ADDED = 3;
        public static final int PLAYERS_REMOVED = 4;
    }

    public String getName() {
        return handle.getStrings().read(0);
    }

    public void setName(String value) {
        handle.getStrings().write(0, value);
    }

    public WrappedChatComponent getDisplayName() {
        return handle.getChatComponents().read(0);
    }

    public void setDisplayName(WrappedChatComponent value) {
        handle.getChatComponents().write(0, value);
    }

    public WrappedChatComponent getPrefix() {
        return handle.getChatComponents().read(1);
    }

    public void setPrefix(WrappedChatComponent value) {
        handle.getChatComponents().write(1, value);
    }

    public WrappedChatComponent getSuffix() {
        return handle.getChatComponents().read(2);
    }

    public void setSuffix(WrappedChatComponent value) {
        handle.getChatComponents().write(2, value);
    }

    public String getNameTagVisibility() {
        return handle.getStrings().read(1);
    }

    public void setNameTagVisibility(String value) {
        handle.getStrings().write(1, value);
    }

    public ChatColor getColor() {
        return handle.getEnumModifier(ChatColor.class, MinecraftReflection.getMinecraftClass("EnumChatFormat")).read(0);
    }

    public void setColor(ChatColor value) {
        handle.getEnumModifier(ChatColor.class, MinecraftReflection.getMinecraftClass("EnumChatFormat")).write(0, value);
    }

    public String getCollisionRule() {
        return handle.getStrings().read(2);
    }

    public void setCollisionRule(String value) {
        handle.getStrings().write(2, value);
    }

    @SuppressWarnings("unchecked")
    public List<String> getPlayers() {
        return (List<String>) handle.getSpecificModifier(Collection.class)
                .read(0);
    }

    public void setPlayers(List<String> value) {
        handle.getSpecificModifier(Collection.class).write(0, value);
    }

    public int getMode() {
        return handle.getIntegers().read(0);
    }

    public void setMode(int value) {
        handle.getIntegers().write(0, value);
    }

    public int getPackOptionData() {
        return handle.getIntegers().read(1);
    }

    public void setPackOptionData(int value) {
        handle.getIntegers().write(1, value);
    }

}
