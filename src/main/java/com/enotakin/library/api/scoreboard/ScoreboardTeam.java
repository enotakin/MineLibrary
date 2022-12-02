package com.enotakin.library.api.scoreboard;

import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.enotakin.library.api.protocol.PacketWrapper;
import io.papermc.paper.adventure.PaperAdventure;
import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import com.enotakin.library.api.protocol.WrapperPlayServerScoreboardTeam;
import com.enotakin.library.api.util.Cmpt;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class ScoreboardTeam extends AbstractScoreboard implements Team {

    private final String name;

    private Component prefix;
    private WrappedChatComponent wrappedPrefix;

    private Component suffix;
    private WrappedChatComponent wrappedSuffix;

    private ChatColor color;
    private Visibility visibility;
    private Push push;

    private boolean allowFriendlyFire;
    private boolean canSeeFriendlyInvisibles;

    private final Set<String> entries = new HashSet<>();

    public ScoreboardTeam(String name) {
        this.name = name;

        setPrefix(null);
        setSuffix(null);

        setColor(ChatColor.RESET);
        setVisibility(Visibility.ALWAYS);
        setPush(Push.ALWAYS);

        setAllowFriendlyFire(true);
        setCanSeeFriendlyInvisibles(true);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Component getPrefix() {
        return prefix;
    }

    @Override
    public void setPrefix(Component prefix) {
        if (prefix == null || Cmpt.isEmpty(prefix)) {
            prefix = Cmpt.EMPTY;
        }

        this.prefix = prefix;
        this.wrappedPrefix = WrappedChatComponent.fromHandle(PaperAdventure.asVanilla(prefix));
        this.sendUpdatePacket();
    }

    @Override
    public Component getSuffix() {
        return suffix;
    }

    @Override
    public void setSuffix(Component suffix) {
        if (suffix == null || Cmpt.isEmpty(suffix)) {
            suffix = Cmpt.EMPTY;
        }

        this.suffix = suffix;
        this.wrappedSuffix = WrappedChatComponent.fromHandle(PaperAdventure.asVanilla(suffix));
        this.sendUpdatePacket();
    }

    @Override
    public ChatColor getColor() {
        return color;
    }

    @Override
    public void setColor(ChatColor color) {
        if (this.color != color) {
            this.color = color;
            this.sendUpdatePacket();
        }
    }

    @Override
    public Visibility getVisibility() {
        return visibility;
    }

    @Override
    public void setVisibility(Visibility visibility) {
        if (this.visibility != visibility) {
            this.visibility = visibility;
            this.sendUpdatePacket();
        }
    }

    @Override
    public Push getPush() {
        return push;
    }

    @Override
    public void setPush(Push push) {
        if (this.push != push) {
            this.push = push;
            this.sendUpdatePacket();
        }
    }

    @Override
    public boolean isAllowFriendlyFire() {
        return allowFriendlyFire;
    }

    @Override
    public void setAllowFriendlyFire(boolean allowFriendlyFire) {
        if (this.allowFriendlyFire != allowFriendlyFire) {
            this.allowFriendlyFire = allowFriendlyFire;
            this.sendUpdatePacket();
        }
    }

    @Override
    public boolean isCanSeeFriendlyInvisibles() {
        return canSeeFriendlyInvisibles;
    }

    @Override
    public void setCanSeeFriendlyInvisibles(boolean canSeeFriendlyInvisibles) {
        if (this.canSeeFriendlyInvisibles != canSeeFriendlyInvisibles) {
            this.canSeeFriendlyInvisibles = canSeeFriendlyInvisibles;
            this.sendUpdatePacket();
        }
    }

    @Override
    public boolean addEntry(String entry) {
        if (entries.add(entry)) {
            PacketWrapper wrapper = getTeamPacket(true, name, entry);
            players.forEach(wrapper::sendPacket);
            return true;
        }
        return false;
    }

    @Override
    public boolean removeEntry(String entry) {
        if (entries.remove(entry)) {
            PacketWrapper wrapper = getTeamPacket(false, name, entry);
            players.forEach(wrapper::sendPacket);
            return true;
        }
        return false;
    }

    @Override
    public Collection<String> getEntries() {
        return entries;
    }

    @Override
    protected void onShow(Player player) {
        PacketWrapper wrapper = getTeamPacket(true, name, wrappedPrefix, wrappedSuffix, color, visibility, push, allowFriendlyFire, canSeeFriendlyInvisibles, entries);
        wrapper.sendPacket(player);
    }

    @Override
    protected void onHide(Player player) {
        PacketWrapper wrapper = getTeamPacket(name);
        wrapper.sendPacket(player);
    }

    private void sendUpdatePacket() {
        if (players.size() > 0) {
            PacketWrapper wrapper = getTeamPacket(false, name, wrappedPrefix, wrappedSuffix, color, visibility, push, allowFriendlyFire, canSeeFriendlyInvisibles, entries);
            players.forEach(wrapper::sendPacket);
        }
    }

    private static PacketWrapper getTeamPacket(boolean createOrUpdate, String name, WrappedChatComponent prefix, WrappedChatComponent suffix, ChatColor color, Visibility visibility, Push push, boolean allowFriendlyFire, boolean canSeeFriendlyInvisibles, Collection<String> players) {
        int optionData = 0;
        if (allowFriendlyFire)
            optionData |= 0x01;
        if (canSeeFriendlyInvisibles)
            optionData |= 0x02;

        WrapperPlayServerScoreboardTeam wrapper = new WrapperPlayServerScoreboardTeam();
        wrapper.setMode(createOrUpdate ? WrapperPlayServerScoreboardTeam.Mode.TEAM_CREATED : WrapperPlayServerScoreboardTeam.Mode.TEAM_UPDATED);
        wrapper.setName(name);

        if (createOrUpdate) {
            wrapper.getPlayers().addAll(players);
        }

        wrapper.setPrefix(prefix);
        wrapper.setDisplayName(EMPTY_TEXT);
        wrapper.setSuffix(suffix);
        wrapper.setPackOptionData(optionData);
        wrapper.setNameTagVisibility(visibility.getName());
        wrapper.setCollisionRule(push.getName());
        wrapper.setColor(color);

        return wrapper;
    }

    private static PacketWrapper getTeamPacket(boolean addOrRemovePlayers, String name, String playerName) {
        // Add or remove players
        WrapperPlayServerScoreboardTeam wrapper = new WrapperPlayServerScoreboardTeam();
        wrapper.setName(name);
        wrapper.setMode(addOrRemovePlayers ? WrapperPlayServerScoreboardTeam.Mode.PLAYERS_ADDED : WrapperPlayServerScoreboardTeam.Mode.PLAYERS_REMOVED);
        wrapper.getPlayers().add(playerName);
        return wrapper;
    }

    private static PacketWrapper getTeamPacket(String name) {
        // Remove team
        WrapperPlayServerScoreboardTeam wrapper = new WrapperPlayServerScoreboardTeam();
        wrapper.setName(name);
        wrapper.setMode(WrapperPlayServerScoreboardTeam.Mode.TEAM_REMOVED);
        return wrapper;
    }

}
