package com.enotakin.library.nametag;

import com.enotakin.library.MineLibraryPlugin;
import com.enotakin.library.entity.type.LibLivingEntity;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;

public class PacketEntityNameTag extends LibNameTag {

    private final LibLivingEntity entity;

    public PacketEntityNameTag(MineLibraryPlugin plugin, LibLivingEntity entity) {
        super(plugin);
        this.entity = entity;
    }

    @Override
    public int getID() {
        return entity.getEntityID();
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
