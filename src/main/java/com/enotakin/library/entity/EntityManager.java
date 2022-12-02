package com.enotakin.library.entity;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.enotakin.library.LibraryManager;
import com.enotakin.library.MineLibraryPlugin;
import com.enotakin.library.api.entity.Interact;
import com.enotakin.library.entity.adapter.ChunkPacketAdapter;
import com.enotakin.library.entity.adapter.UseEntityPacketAdapter;
import com.enotakin.library.entity.listener.PlayerListener;
import com.enotakin.library.entity.listener.WorldListener;
import com.enotakin.library.entity.task.EntityTickTask;
import com.enotakin.library.entity.type.LibRenderedEntity;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.HashMap;
import java.util.Map;

public class EntityManager implements LibraryManager {

    private final MineLibraryPlugin plugin;

    private final Map<World, WorldRepository> worlds = new HashMap<>();
    private final Int2ObjectMap<Interact> interacts = new Int2ObjectOpenHashMap<>();
    private final Map<Player, PlayerCooldown> cooldowns = new HashMap<>();

    public EntityManager(MineLibraryPlugin plugin) {
        this.plugin = plugin;
    }

    public MineLibraryPlugin getPlugin() {
        return plugin;
    }

    @Override
    public void register() {
        Server server = plugin.getServer();

        PluginManager pluginManager = server.getPluginManager();
        pluginManager.registerEvents(new PlayerListener(this), plugin);
        pluginManager.registerEvents(new WorldListener(this), plugin);

        BukkitScheduler bukkitScheduler = server.getScheduler();
        bukkitScheduler.runTaskTimer(plugin, new EntityTickTask(this), 0L, 1L);

        ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();
        protocolManager.addPacketListener(new ChunkPacketAdapter(this));
        protocolManager.addPacketListener(new UseEntityPacketAdapter(this));

        for (World world : server.getWorlds()) {
            createWorld(world);
        }
    }

    @Override
    public void unregister() {
    }

    public void onTick() {
        for (WorldRepository repository : worlds.values()) {
            repository.onTick();
        }

        for (PlayerCooldown cooldown : cooldowns.values()) {
            if (cooldown.cooldown > 0) {
                cooldown.cooldown--;
            }
        }
    }

    public WorldRepository createWorld(World world) {
        WorldRepository repository = new WorldRepository();
        worlds.put(world, repository);
        return repository;
    }

    public WorldRepository getWorld(World world) {
        return worlds.get(world);
    }

    public WorldRepository removeWorld(World world) {
        WorldRepository repository = worlds.remove(world);

        // TODO: remove entities

        return repository;
    }

    public void addInteract(int entityID, Interact interact) {
        interacts.put(entityID, interact);
    }

    public void onInteract(Player player, int targetID, Interact.Action action) {
        Interact interact = interacts.get(targetID);
        if (interact == null) {
            return;
        }

        PlayerCooldown cooldown = cooldowns.get(player);
        if (cooldown == null || cooldown.cooldown > 0) {
            return;
        }

        interact.onUse(player, action);
        cooldown.cooldown = 3;
    }

    public void removeInteract(int entityID) {
        interacts.remove(entityID);
    }

    public void addEntity(LibRenderedEntity entity) {
        WorldRepository worldRepository = getWorld(entity.getWorld());
        worldRepository.addEntity(entity);
    }

    public void removeEntity(LibRenderedEntity entity) {
        WorldRepository worldRepository = getWorld(entity.getWorld());
        worldRepository.removeEntity(entity);
    }

    public void createPlayer(Player player) {
        cooldowns.put(player, new PlayerCooldown(player));

        WorldRepository worldRepository = getWorld(player.getWorld());
        worldRepository.createPlayerRepository(player);
    }

    public void updatePlayer(Player player, World fromWorld, World toWorld) {
        WorldRepository worldRepository = getWorld(fromWorld);
        WorldRepository.PlayerRepository playerRepository = worldRepository.removePlayerRepository(player);

        worldRepository = getWorld(toWorld);
        worldRepository.addPlayerRepository(playerRepository);
    }

    public void removePlayer(Player player) {
        WorldRepository worldRepository = getWorld(player.getWorld());
        worldRepository.removePlayerRepository(player);

        cooldowns.remove(player);
    }

    private static class PlayerCooldown {

        private final Player player;
        private int cooldown;

        public PlayerCooldown(Player player) {
            this.player = player;
        }

    }

}
