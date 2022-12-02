package com.enotakin.library.api.protocol;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedChatComponent;

public class WrapperPlayServerScoreboardObjective extends PacketWrapper {

    public static final PacketType TYPE = PacketType.Play.Server.SCOREBOARD_OBJECTIVE;

    public WrapperPlayServerScoreboardObjective() {
        super(new PacketContainer(TYPE), TYPE);
        handle.getModifier().writeDefaults();
    }

    public WrapperPlayServerScoreboardObjective(PacketContainer packet) {
        super(packet, TYPE);
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

    public HealthDisplay getHealthDisplay() {
        return handle.getEnumModifier(HealthDisplay.class, 2).read(0);
    }

    public void setHealthDisplay(HealthDisplay value) {
        handle.getEnumModifier(HealthDisplay.class, 2).write(0, value);
    }

    public int getMode() {
        return handle.getIntegers().read(0);
    }

    public void setMode(int value) {
        handle.getIntegers().write(0, value);
    }

    public static class Mode {
        public static final int ADD_OBJECTIVE = 0;
        public static final int REMOVE_OBJECTIVE = 1;
        public static final int UPDATE_VALUE = 2;
    }

    public enum HealthDisplay {

        INTEGER,
        HEARTS;

        private static final HealthDisplay[] VALUES = values();

        public static HealthDisplay valueOf(int ordinal) {
            return VALUES[ordinal];
        }

    }

}
