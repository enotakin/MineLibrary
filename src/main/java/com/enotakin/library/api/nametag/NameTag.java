package com.enotakin.library.api.nametag;

import net.kyori.adventure.text.Component;

public interface NameTag {

    default void addLine(Component text) {
        setLine(size(), text);
    }

    void setLine(int index, Component text);

    void removeLine(int index);

    int size();

    void remove();

}
