package com.enotakin.library.api;

import com.enotakin.library.api.entity.MineRenderedEntity;
import com.enotakin.library.api.nametag.NameTag;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;

public interface MinePlugin {

    NameTag createTag(LivingEntity entity);

    <E extends MineRenderedEntity> E spawnEntity(Location location, Class<E> entityClass);

}
