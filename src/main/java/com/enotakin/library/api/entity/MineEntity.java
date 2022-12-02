package com.enotakin.library.api.entity;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.EntityType;

import java.util.UUID;

public interface MineEntity extends MineRenderedEntity {

    int getEntityID();

    UUID getUniqueID();

    EntityType getType();

    boolean isGlowing();

    void setGlowing(boolean glowing);

    boolean isInvisible();

    void setInvisible(boolean invisible);

    Component getCustomName();

    default void setCustomName(String customName) {
        setCustomName(customName != null ? Component.text(customName) : null);
    }

    void setCustomName(Component customName);

    boolean isCustomNameVisible();

    void setCustomNameVisible(boolean customNameVisible);

}
