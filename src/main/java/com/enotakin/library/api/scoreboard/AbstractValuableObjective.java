package com.enotakin.library.api.scoreboard;

import com.enotakin.library.api.protocol.PacketWrapper;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractValuableObjective extends AbstractObjective implements ValuableObjective {

    private final Map<String, Integer> entries = new HashMap<>();

    AbstractValuableObjective(String name, Component displayName) {
        super(name, displayName);
    }

    @Override
    public Collection<String> getEntries() {
        return entries.keySet();
    }

    @Override
    public int getEntry(String entry) {
        return entries.get(entry);
    }

    @Override
    public void setEntry(String entry, int value) {
        Integer oldValue = entries.put(entry, value);
        if (oldValue != null && oldValue != value) {
            PacketWrapper wrapper = getScorePacket(entry, getName(), value);
            players.forEach(wrapper::sendPacket);
        }
    }

    @Override
    public boolean removeEntry(String entry) {
        if (entries.remove(entry) != null) {
            PacketWrapper wrapper = getScorePacket(entry, getName());
            players.forEach(wrapper::sendPacket);
            return true;
        }
        return false;
    }

    @Override
    protected void onShow(Player player) {
        super.onShow(player);

        for (Map.Entry<String, Integer> entry : entries.entrySet()) {
            PacketWrapper wrapper = getScorePacket(entry.getKey(), getName(), entry.getValue());
            wrapper.sendPacket(player);
        }
    }

}
