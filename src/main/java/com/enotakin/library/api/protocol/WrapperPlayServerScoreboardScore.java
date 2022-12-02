package com.enotakin.library.api.protocol;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.EnumWrappers.ScoreboardAction;

public class WrapperPlayServerScoreboardScore extends PacketWrapper {

    public static final PacketType TYPE = PacketType.Play.Server.SCOREBOARD_SCORE;

    public WrapperPlayServerScoreboardScore() {
        super(new PacketContainer(TYPE), TYPE);
        handle.getModifier().writeDefaults();
    }

    public WrapperPlayServerScoreboardScore(PacketContainer packet) {
        super(packet, TYPE);
    }

    public String getScoreName() {
        return handle.getStrings().read(0);
    }

    public void setScoreName(String value) {
        handle.getStrings().write(0, value);
    }

    public String getObjectiveName() {
        return handle.getStrings().read(1);
    }

    public void setObjectiveName(String value) {
        handle.getStrings().write(1, value);
    }

    public int getValue() {
        return handle.getIntegers().read(0);
    }

    public void setValue(int value) {
        handle.getIntegers().write(0, value);
    }

    public ScoreboardAction getScoreboardAction() {
        return handle.getScoreboardActions().read(0);
    }

    public void setScoreboardAction(ScoreboardAction value) {
        handle.getScoreboardActions().write(0, value);
    }

}
