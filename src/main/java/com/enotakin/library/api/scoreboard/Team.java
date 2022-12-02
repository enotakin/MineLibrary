package com.enotakin.library.api.scoreboard;

import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;

import java.util.Collection;

public interface Team extends Scoreboard {

    String getName();

    Component getPrefix();

    void setPrefix(Component prefix);

    Component getSuffix();

    void setSuffix(Component suffix);

    ChatColor getColor();

    void setColor(ChatColor color);

    Visibility getVisibility();

    void setVisibility(Visibility visibility);

    Push getPush();

    void setPush(Push push);

    boolean isAllowFriendlyFire();

    void setAllowFriendlyFire(boolean allowFriendlyFire);

    boolean isCanSeeFriendlyInvisibles();

    void setCanSeeFriendlyInvisibles(boolean canSeeFriendlyInvisibles);

    boolean addEntry(String entry);

    boolean removeEntry(String entry);

    Collection<String> getEntries();

    enum Visibility {

        ALWAYS("always"),
        HIDE_FOR_OTHER_TEAMS("hideForOtherTeams"),
        HIDE_FOR_OWN_TEAM("hideForOwnTeam"),
        NEVER("never");

        private final String name;

        Visibility(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

    }

    enum Push {

        ALWAYS("always"),
        NEVER("never"),
        PUSH_OTHER_TEAMS("pushOtherTeams"),
        PUSH_OWN_TEAM("pushOwnTeam");

        private final String name;

        Push(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

    }

}
