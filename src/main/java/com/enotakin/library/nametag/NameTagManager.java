package com.enotakin.library.nametag;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.enotakin.library.LibraryManager;
import com.enotakin.library.MineLibraryPlugin;
import com.enotakin.library.nametag.adapter.EntityMovePacketAdapter;
import com.enotakin.library.nametag.adapter.EntityVisibilityPacketAdapter;
import com.enotakin.library.nametag.listener.PlayerListener;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import org.bukkit.Server;
import org.bukkit.plugin.PluginManager;

public class NameTagManager implements LibraryManager {

    private final MineLibraryPlugin plugin;

    private final Int2ObjectMap<LibNameTag> nameTags = new Int2ObjectOpenHashMap<>();

    public NameTagManager(MineLibraryPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void register() {
        Server server = plugin.getServer();

        PluginManager pluginManager = server.getPluginManager();
        pluginManager.registerEvents(new PlayerListener(this), plugin);

        ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();
        protocolManager.addPacketListener(new EntityVisibilityPacketAdapter(this));
        protocolManager.addPacketListener(new EntityMovePacketAdapter(this));
    }

    @Override
    public void unregister() {
    }

    public MineLibraryPlugin getPlugin() {
        return plugin;
    }

    public void registerNameTag(LibNameTag nameTag) {
        nameTags.put(nameTag.getID(), nameTag);
    }

    public LibNameTag getNameTag(int id) {
        return nameTags.get(id);
    }

    public void unregisterNameTag(int id) {
        nameTags.remove(id);
    }

}
