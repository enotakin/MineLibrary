package com.enotakin.library.entity.type;

import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import com.comphenix.protocol.wrappers.WrappedVillagerData;
import com.enotakin.library.MineLibraryPlugin;
import com.enotakin.library.api.entity.MineVillager;
import com.google.common.base.Preconditions;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Villager;
import org.jetbrains.annotations.NotNull;

public class LibVillager extends LibEquippableEntity implements MineVillager {

    public static final WrappedDataWatcher.WrappedDataWatcherObject DATA_VILLAGER_DATA = new WrappedDataWatcher.WrappedDataWatcherObject(18, WrappedDataWatcher.Registry.get(WrappedVillagerData.getNmsClass()));

    private Villager.Type type;
    private Villager.Profession profession;
    private int level;

    public LibVillager(MineLibraryPlugin plugin, Location location) {
        super(plugin, location);

        setVillagerData(Villager.Type.PLAINS, Villager.Profession.NONE, 1, false);
    }

    @Override
    public EntityType getType() {
        return EntityType.VILLAGER;
    }

    @Override
    public @NotNull Villager.Type getVillagerType() {
        return type;
    }

    @Override
    public void setVillagerType(@NotNull Villager.Type type) {
        if (this.type != type) {
            setVillagerData(type, profession, level, true);
        }
    }

    @Override
    public @NotNull Villager.Profession getVillagerProfession() {
        return profession;
    }

    @Override
    public void setVillagerProfession(@NotNull Villager.Profession profession) {
        if (this.profession != profession) {
            setVillagerData(type, profession, level, true);
        }
    }

    @Override
    public int getVillagerLevel() {
        return level;
    }

    @Override
    public void setVillagerLevel(int level) {
        Preconditions.checkArgument(level >= 1 && level <= 5, "Level must be between [1, 5]");
        if (this.level != level) {
            setVillagerData(type, profession, level, true);
        }
    }

    private void setVillagerData(Villager.Type type, Villager.Profession profession, int level, boolean dirty) {
        this.profession = profession;
        this.type = type;
        this.level = level;

        WrappedVillagerData.Type protocolType = WrappedVillagerData.Type.valueOf(type.name());
        WrappedVillagerData.Profession protocolProfession = WrappedVillagerData.Profession.valueOf(profession.name());
        WrappedVillagerData villagerData = WrappedVillagerData.fromValues(protocolType, protocolProfession, level);

        dataWatcher.setObject(DATA_VILLAGER_DATA, villagerData.getHandle(), dirty);
        if (dirty) {
            dataWatcherDirty = true;
        }
    }

}
