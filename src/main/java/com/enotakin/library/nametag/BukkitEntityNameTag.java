package com.enotakin.library.nametag;

import com.enotakin.library.MineLibraryPlugin;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;

public class BukkitEntityNameTag extends LibNameTag {

    private final LivingEntity entity;

    public BukkitEntityNameTag(MineLibraryPlugin plugin, LivingEntity entity) {
        super(plugin);
        this.entity = entity;
    }

    @Override
    public int getID() {
        return entity.getEntityId();
    }

    @Override
    public EntityType getType() {
        return entity.getType();
    }

    @Override
    public Location getLocation() {
        return entity.getLocation();
    }

}
