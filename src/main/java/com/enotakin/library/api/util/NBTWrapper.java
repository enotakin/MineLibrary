package com.enotakin.library.api.util;

import net.minecraft.server.v1_16_R3.*;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftItemStack;

import java.util.ArrayList;
import java.util.List;

public final class NBTWrapper {

    private static final NBTTagCompound EMPTY_TAG = new NBTTagCompound();

    private final ItemStack nmsItem;

    private NBTWrapper(org.bukkit.inventory.ItemStack bukkitItem) {
        nmsItem = CraftItemStack.asNMSCopy(bukkitItem);
    }

    public static NBTWrapper wrap(org.bukkit.inventory.ItemStack bukkitItem) {
        return new NBTWrapper(bukkitItem);
    }

    private NBTTagCompound getTag(String path, boolean write) {
        if (!nmsItem.hasTag()) {
            if (!write) {
                return EMPTY_TAG;
            }
            nmsItem.setTag(new NBTTagCompound());
        }

        if (path.contains(".")) {
            String[] parts = path.split("\\.");
            NBTTagCompound t = nmsItem.getTag();

            for (int i = 0; i < parts.length - 1; ++i) {
                NBTTagCompound t0 = t.getCompound(parts[i]);
                if (write && t.hasKey(parts[i])) {
                    t.set(parts[i], t0);
                }
                t = t0;
            }

            return t;
        } else {
            return nmsItem.getTag();
        }
    }

    private String getKey(String path) {
        int index = path.lastIndexOf(46);
        return index == -1 ? path : path.substring(index + 1);
    }

    public NBTTagCompound getHandle() {
        return nmsItem.getTag();
    }

    public NBTBase get(String path) {
        return getTag(path, false).get(getKey(path));
    }

    public boolean contains(String path) {
        return getTag(path, false).hasKey(getKey(path));
    }

    public NBTWrapper set(String path, NBTBase value) {
        getTag(path, true).set(getKey(path), value);
        return this;
    }

    public NBTWrapper remove(String path) {
        getTag(path, false).remove(getKey(path));
        return this;
    }

    public boolean getBoolean(String path) {
        return getTag(path, false).getBoolean(getKey(path));
    }

    public NBTWrapper setBoolean(String path, boolean value) {
        getTag(path, true).setBoolean(getKey(path), value);
        return this;
    }

    public byte getByte(String path) {
        return getTag(path, false).getByte(getKey(path));
    }

    public NBTWrapper setByte(String path, byte value) {
        getTag(path, true).setByte(getKey(path), value);
        return this;
    }

    public short getShort(String path) {
        return getTag(path, false).getShort(getKey(path));
    }

    public NBTWrapper setShort(String path, short value) {
        getTag(path, true).setShort(getKey(path), value);
        return this;
    }

    public int getInt(String path) {
        return getTag(path, false).getInt(getKey(path));
    }

    public NBTWrapper setInt(String path, int value) {
        getTag(path, true).setInt(getKey(path), value);
        return this;
    }

    public long getLong(String path) {
        return getTag(path, false).getLong(getKey(path));
    }

    public NBTWrapper setLong(String path, long value) {
        getTag(path, true).setLong(getKey(path), value);
        return this;
    }

    public float getFloat(String path) {
        return getTag(path, false).getFloat(getKey(path));
    }

    public NBTWrapper setFloat(String path, float value) {
        getTag(path, true).setFloat(getKey(path), value);
        return this;
    }

    public double getDouble(String path) {
        return getTag(path, false).getDouble(getKey(path));
    }

    public NBTWrapper setDouble(String path, double value) {
        getTag(path, true).setDouble(getKey(path), value);
        return this;
    }

    public String getString(String path) {
        return getTag(path, false).getString(getKey(path));
    }

    public NBTWrapper setString(String path, String value) {
        getTag(path, true).setString(getKey(path), value);
        return this;
    }

    public byte[] getByteArray(String path) {
        return getTag(path, false).getByteArray(getKey(path));
    }

    public NBTWrapper setByteArray(String path, byte[] value) {
        getTag(path, true).setByteArray(getKey(path), value);
        return this;
    }

    public int[] getIntArray(String path) {
        return getTag(path, false).getIntArray(getKey(path));
    }

    public NBTWrapper setIntArray(String path, int[] value) {
        getTag(path, true).setIntArray(getKey(path), value);
        return this;
    }

    public List<String> getStringList(String path) {
        NBTTagList nbtList = (NBTTagList) get(path);
        if (nbtList != null && nbtList.size() != 0) {
            List<String> list = new ArrayList<>(nbtList.size());
            for (int i = 0; i < nbtList.size(); ++i) {
                list.add(nbtList.getString(i));
            }
            return list;
        } else {
            return new ArrayList<>();
        }
    }

    public NBTWrapper setStringList(String path, List<String> value) {
        NBTTagList nbtList = new NBTTagList();
        for (String s : value) {
            nbtList.add(NBTTagString.create(s));
        }
        getTag(path, true).set(getKey(path), nbtList);
        return this;
    }

    public org.bukkit.inventory.ItemStack build() {
        return CraftItemStack.asCraftMirror(nmsItem);
    }

}
