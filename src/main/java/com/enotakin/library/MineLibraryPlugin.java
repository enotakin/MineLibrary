package com.enotakin.library;

import com.enotakin.library.api.MinePlugin;
import com.enotakin.library.api.entity.*;
import com.enotakin.library.api.nametag.NameTag;
import com.enotakin.library.entity.EntityManager;
import com.enotakin.library.entity.type.*;
import com.enotakin.library.menu.MenuListener;
import com.enotakin.library.nametag.BukkitEntityNameTag;
import com.enotakin.library.nametag.LibNameTag;
import com.enotakin.library.nametag.NameTagManager;
import com.google.common.base.Preconditions;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.entity.LivingEntity;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;

public class MineLibraryPlugin extends JavaPlugin implements MinePlugin {

    private final Map<Class<? extends LibraryManager>, LibraryManager> managers = new HashMap<>();

    @Override
    public void onEnable() {
        registerManager(new NameTagManager(this));
        registerManager(new EntityManager(this));

        Server server = getServer();

        PluginManager pluginManager = server.getPluginManager();
        pluginManager.registerEvents(new MenuListener(), this);
    }

    @Override
    public void onDisable() {
        for (LibraryManager manager : managers.values()) {
            manager.unregister();
        }
        managers.clear();
    }

    public void registerManager(LibraryManager manager) {
        LibraryManager previousManager = managers.put(manager.getClass(), manager);
        if (previousManager != null) {
            previousManager.unregister();
        }

        manager.register();
    }

    @SuppressWarnings("unchecked")
    public <M extends LibraryManager> M getManager(Class<M> managerClass) {
        return (M) managers.get(managerClass);
    }

    @Override
    public NameTag createTag(LivingEntity entity) {
        LibNameTag nameTag = new BukkitEntityNameTag(this, entity);
        NameTagManager nameTagManager = getManager(NameTagManager.class);
        nameTagManager.registerNameTag(nameTag);
        return nameTag;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <E extends MineRenderedEntity> E spawnEntity(Location location, Class<E> entityClass) {
        Preconditions.checkArgument(location.getWorld() != null, "");

        LibRenderedEntity entity = null;
        if (entityClass.isAssignableFrom(MineArmorStand.class)) {
            entity = new LibArmorStand(this, location);
        } else if (entityClass.isAssignableFrom(MineHologram.class)) {
            entity = new LibHologram(this, location);
        } else if (entityClass.isAssignableFrom(MineHuman.class)) {
            entity = new LibHuman(this, location);
        } else if (entityClass.isAssignableFrom(MineVillager.class)) {
            entity = new LibVillager(this, location);
        }

        if (entity != null) {
            EntityManager entityManager = getManager(EntityManager.class);
            entityManager.addEntity(entity);

            if (entity instanceof LibLivingEntity livingEntity) {
                NameTagManager nameTagManager = getManager(NameTagManager.class);
                nameTagManager.registerNameTag(livingEntity.getNameTag());
            }

            entity.setAutoVisible(true);

            return (E) entity;
        }

        throw new IllegalStateException("Unable to create '" + entityClass.getSimpleName() + "', unknown entity!");
    }

}
