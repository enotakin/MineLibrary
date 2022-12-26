package com.enotakin.library.api;

import com.enotakin.library.api.entity.MineRenderedEntity;
import com.enotakin.library.api.nametag.NameTag;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.plugin.Plugin;

public final class MineAPI {

    private static MinePlugin plugin;

    private MineAPI() {
    }

    public static MinePlugin getPlugin() {
        if (MineAPI.plugin == null) {
            Plugin bukkitPlugin = Bukkit.getPluginManager().getPlugin("MineLibrary");
            if (bukkitPlugin instanceof MinePlugin plugin) {
                MineAPI.plugin = plugin;
            } else {
                throw new IllegalStateException("MineLibrary has not loaded yet");
            }
        }
        return MineAPI.plugin;
    }

    public static <E extends MineRenderedEntity> E spawnEntity(Location location, Class<E> entityClass) {
        return getPlugin().spawnEntity(location, entityClass);
    }

    public static NameTag createTag(LivingEntity entity) {
        return getPlugin().createTag(entity);
    }

    public static  <T> void registerCommand(Plugin plugin, Class<T> command) {
        getPlugin().registerCommand(plugin, command);
    }

}
