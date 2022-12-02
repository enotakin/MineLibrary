package com.enotakin.library.nametag;

import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import com.enotakin.library.MineLibraryPlugin;
import com.enotakin.library.api.nametag.NameTag;
import com.enotakin.library.api.protocol.*;
import com.enotakin.library.api.util.Cmpt;
import com.enotakin.library.api.util.MCHelper;
import com.enotakin.library.entity.type.LibArmorStand;
import com.enotakin.library.entity.type.LibEntity;
import io.papermc.paper.adventure.PaperAdventure;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.*;

public abstract class LibNameTag implements NameTag {

    private final MineLibraryPlugin plugin;

    private boolean sneaking;
    private boolean sleeping;
    private boolean swimming;

    private final Set<Player> viewers = new HashSet<>();
    private final Int2ObjectMap<NameTagLine> lines = new Int2ObjectOpenHashMap<>();

    protected LibNameTag(MineLibraryPlugin plugin) {
        this.plugin = plugin;
    }

    public abstract EntityType getType();

    public abstract Location getLocation();

    public abstract int getID();

    public void addViewer(Player player, double x, double y, double z) {
        if (viewers.add(player)) {
            spawnTo(player, x, y, z);
        }
    }

    public void removeViewer(Player player) {
        if (viewers.remove(player)) {
            destroyTo(player);
        }
    }

    public void setSneaking(boolean sneaking) {
        if (this.sneaking != sneaking) {
            this.sneaking = sneaking;

            Location location = getLocation();
            double x = location.getX();
            double y = location.getY();
            double z = location.getZ();

            for (NameTagLine line : lines.values()) {
                line.setSneaking(sneaking);

                WrapperPlayServerEntityMetadata metadataWrapper = new WrapperPlayServerEntityMetadata();
                metadataWrapper.setEntityID(line.entityID);
                metadataWrapper.setMetadata(Collections.singletonList(line.dataWatcher.getWatchableObject(LibEntity.DATA_ENTITY_FLAGS.getIndex())));

                WrapperPlayServerEntityTeleport teleportWrapper = new WrapperPlayServerEntityTeleport();
                teleportWrapper.setEntityID(line.entityID);
                teleportWrapper.setX(x);
                teleportWrapper.setY(y + line.getOffset());
                teleportWrapper.setZ(z);

                for (Player viewer : viewers) {
                    metadataWrapper.sendPacket(viewer);
                    teleportWrapper.sendPacket(viewer);
                }
            }
        }
    }

    public void spawnTo(Player viewer, double x, double y, double z) {
        for (NameTagLine line : lines.values()) {
            line.spawnTo(viewer, x, y, z);
        }
    }

    public void teleportTo(Player viewer, double x, double y, double z) {
        for (NameTagLine line : lines.values()) {
            WrapperPlayServerEntityTeleport wrapper = new WrapperPlayServerEntityTeleport();
            wrapper.setEntityID(line.entityID);
            wrapper.setX(x);
            wrapper.setY(y + line.getOffset());
            wrapper.setZ(z);
            wrapper.sendPacket(viewer);
        }
    }

    public void moveTo(Player viewer, short x, short y, short z) {
        for (NameTagLine line : lines.values()) {
            WrapperPlayServerRelEntityMove wrapper = new WrapperPlayServerRelEntityMove();
            wrapper.setEntityID(line.entityID);
            wrapper.setNativeX(x);
            wrapper.setNativeY(y);
            wrapper.setNativeZ(z);
            wrapper.sendPacket(viewer);
        }
    }

    public void velocityTo(Player viewer, int x, int y, int z) {
        for (NameTagLine line : lines.values()) {
            WrapperPlayServerEntityVelocity wrapper = new WrapperPlayServerEntityVelocity();
            wrapper.setEntityID(line.entityID);
            wrapper.setNativeX(x);
            wrapper.setNativeY(y);
            wrapper.setNativeZ(z);
            wrapper.sendPacket(viewer);
        }
    }

    public void destroyTo(Player viewer) {
        PacketWrapper wrapper = createDestroyPacket();
        wrapper.sendPacket(viewer);
    }

    private PacketWrapper createDestroyPacket() {
        IntList entityIDs = new IntArrayList();
        for (NameTagLine line : lines.values()) {
            entityIDs.add(line.entityID);
        }

        WrapperPlayServerEntityDestroy wrapper = new WrapperPlayServerEntityDestroy();
        wrapper.setEntityIDs(entityIDs.toIntArray());
        return wrapper;
    }

    @Override
    public void setLine(int index, Component text) {
        if (text == null || Cmpt.isEmpty(text)) {
            text = Cmpt.EMPTY;
        }

        NameTagLine line = lines.get(index);
        if (line != null) {
            line.setText(text);

            WrapperPlayServerEntityMetadata wrapper = new WrapperPlayServerEntityMetadata();
            wrapper.setEntityID(line.entityID);
            wrapper.setMetadata(Collections.singletonList(line.dataWatcher.getWatchableObject(LibEntity.DATA_CUSTOM_NAME.getIndex())));
            for (Player viewer : viewers) {
                wrapper.sendPacket(viewer);
            }
        } else {
            line = new NameTagLine(index);
            line.setText(text);
            lines.put(index, line);

            Location location = getLocation();
            double x = location.getX();
            double y = location.getY();
            double z = location.getZ();

            for (Player viewer : viewers) {
                line.spawnTo(viewer, x, y, z);
            }
        }
    }

    @Override
    public void removeLine(int index) {
        lines.remove(index);

        // TODO: update
    }

    @Override
    public int size() {
        return lines.size();
    }

    @Override
    public void remove() {
        NameTagManager manager = plugin.getManager(NameTagManager.class);
        manager.unregisterNameTag(getID());

        if (viewers.size() > 0) {
            PacketWrapper wrapper = createDestroyPacket();
            for (Player viewer : viewers) {
                wrapper.sendPacket(viewer);
            }
            viewers.clear();
        }
    }

    private class NameTagLine {

        private final int index;

        private final int entityID = MCHelper.nextEntityID();
        private final UUID uniqueID = UUID.randomUUID();
        private final WrappedDataWatcher dataWatcher = new WrappedDataWatcher();

        public NameTagLine(int index) {
            this.index = index;

            dataWatcher.setObject(LibEntity.DATA_ENTITY_FLAGS, (byte) 0x00);
            dataWatcher.setObject(LibEntity.DATA_CUSTOM_NAME, Optional.empty());
            dataWatcher.setObject(LibEntity.DATA_CUSTOM_NAME_VISIBLE, true);
            dataWatcher.setObject(LibArmorStand.DATA_STAND_FLAGS, (byte) 0x00);

            setInvisible(true);
            setMarker(true);
        }

        public void setSneaking(boolean sneaking) {
            setEntityFlag(1, sneaking);
        }

        public void setInvisible(boolean invisible) {
            setEntityFlag(5, invisible);
        }

        private void setEntityFlag(int index, boolean value) {
            byte flags = dataWatcher.getByte(LibEntity.DATA_ENTITY_FLAGS.getIndex());
            if (value) {
                dataWatcher.setObject(LibEntity.DATA_ENTITY_FLAGS, (byte) (flags | 1 << index));
            } else {
                dataWatcher.setObject(LibEntity.DATA_ENTITY_FLAGS, (byte) (flags & ~(1 << index)));
            }
        }

        public void setMarker(boolean marker) {
            setStandFlag(16, marker);
        }

        private void setStandFlag(int index, boolean value) {
            byte flags = dataWatcher.getByte(LibArmorStand.DATA_STAND_FLAGS.getIndex());
            if (value) {
                dataWatcher.setObject(LibArmorStand.DATA_STAND_FLAGS, (byte) (flags | index));
            } else {
                dataWatcher.setObject(LibArmorStand.DATA_STAND_FLAGS, (byte) (flags & ~index));
            }
        }

        public double getOffset() {
            return getHeight(getType(), sneaking, sleeping, swimming) + index * 0.27D;
        }

        public void setText(Component text) {
            dataWatcher.setObject(LibEntity.DATA_CUSTOM_NAME, text == null || Cmpt.isEmpty(text) ? Optional.empty() : Optional.of(PaperAdventure.asVanilla(text)));
        }

        public void spawnTo(Player viewer, double x, double y, double z) {
            WrapperPlayServerSpawnEntity spawnWrapper = new WrapperPlayServerSpawnEntity();
            spawnWrapper.setEntityID(entityID);
            spawnWrapper.setUniqueID(uniqueID);
            spawnWrapper.setType(EntityType.ARMOR_STAND);
            spawnWrapper.setX(x);
            spawnWrapper.setY(y + getOffset());
            spawnWrapper.setZ(z);
            spawnWrapper.sendPacket(viewer);

            WrapperPlayServerEntityMetadata metadataWrapper = new WrapperPlayServerEntityMetadata();
            metadataWrapper.setEntityID(entityID);
            metadataWrapper.setMetadata(dataWatcher.getWatchableObjects());
            metadataWrapper.sendPacket(viewer);
        }

    }

    private static double getHeight(EntityType type, boolean sneaking, boolean sleeping, boolean swimming) {
        switch (type) {
            case PLAYER:
                if (sneaking) return 1.35D;
                if (sleeping) return 0D;
                if (swimming) return 0D;
                return 1.8D;
            default:
                return 0D;
        }
    }

}
