package com.enotakin.library.api.entity;

import org.bukkit.ChatColor;

import java.util.Objects;

public interface MineHuman extends MineEquippableEntity {

    String getName();

    Skin getSkin();

    default void setSkin(String value, String signature) {
        setSkin(new Skin(value, signature));
    }

    void setSkin(Skin skin);

    ChatColor getGlowColor();

    void setGlowColor(ChatColor glowColor);

    class Skin {

        private final String value;
        private final String signature;

        public Skin(String value, String signature) {
            this.value = value;
            this.signature = signature;
        }

        public String getValue() {
            return value;
        }

        public String getSignature() {
            return signature;
        }

        @Override
        public boolean equals(Object object) {
            if (this == object) return true;
            if (object == null || getClass() != object.getClass()) return false;

            Skin that = (Skin) object;
            return Objects.equals(value, that.value) && Objects.equals(signature, that.signature);
        }

        @Override
        public int hashCode() {
            return Objects.hash(value, signature);
        }

    }

}
