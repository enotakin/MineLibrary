package com.enotakin.library.api;

import com.enotakin.library.api.entity.MineRenderedEntity;
import com.enotakin.library.api.nametag.NameTag;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.plugin.Plugin;

public interface MinePlugin {

    NameTag createTag(LivingEntity entity);

    <E extends MineRenderedEntity> E spawnEntity(Location location, Class<E> entityClass);

    <T> void registerCommand(Plugin plugin, Class<T> command);

}
