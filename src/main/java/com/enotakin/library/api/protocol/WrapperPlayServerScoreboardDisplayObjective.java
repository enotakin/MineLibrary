package com.enotakin.library.api.protocol;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;

public class WrapperPlayServerScoreboardDisplayObjective extends PacketWrapper {

    public static final PacketType TYPE = PacketType.Play.Server.SCOREBOARD_DISPLAY_OBJECTIVE;

    public WrapperPlayServerScoreboardDisplayObjective() {
        super(new PacketContainer(TYPE), TYPE);
        handle.getModifier().writeDefaults();
    }

    public WrapperPlayServerScoreboardDisplayObjective(PacketContainer packet) {
        super(packet, TYPE);
    }

    public int getPosition() {
        return handle.getIntegers().read(0);
    }

    public void setPosition(int value) {
        handle.getIntegers().write(0, value);
    }

    public String getScoreName() {
        return handle.getStrings().read(0);
    }

    public void setScoreName(String value) {
        handle.getStrings().write(0, value);
    }

}
