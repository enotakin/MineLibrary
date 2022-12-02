package com.enotakin.library.api.scoreboard;

import net.kyori.adventure.text.Component;

public interface Sidebar extends Objective {

    void setLine(int score, Component text);

    void removeLine(int score);

}
