package com.enotakin.library.entity.type;

import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import com.comphenix.protocol.wrappers.WrappedWatchableObject;
import com.enotakin.library.MineLibraryPlugin;
import com.enotakin.library.api.entity.Interact;
import com.enotakin.library.api.entity.MineEntity;
import com.enotakin.library.api.protocol.*;
import com.enotakin.library.api.util.Cmpt;
import com.enotakin.library.api.util.MCHelper;
import com.enotakin.library.entity.EntityManager;
import io.papermc.paper.adventure.PaperAdventure;
import net.kyori.adventure.text.Component;
import net.minecraft.server.v1_16_R3.IChatBaseComponent;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public abstract class LibEntity extends LibRenderedEntity implements MineEntity {

    public static final WrappedDataWatcher.WrappedDataWatcherObject DATA_ENTITY_FLAGS = new WrappedDataWatcher.WrappedDataWatcherObject(0, WrappedDataWatcher.Registry.get(Byte.class));
    public static final WrappedDataWatcher.WrappedDataWatcherObject DATA_CUSTOM_NAME = new WrappedDataWatcher.WrappedDataWatcherObject(2, WrappedDataWatcher.Registry.getChatComponentSerializer(true));
    public static final WrappedDataWatcher.WrappedDataWatcherObject DATA_CUSTOM_NAME_VISIBLE = new WrappedDataWatcher.WrappedDataWatcherObject(3, WrappedDataWatcher.Registry.get(Boolean.class));

    protected final int entityID = MCHelper.nextEntityID();
    protected final UUID uniqueID = UUID.randomUUID();

    protected final WrappedDataWatcher dataWatcher = new WrappedDataWatcher();
    protected boolean dataWatcherDirty;

    public LibEntity(MineLibraryPlugin plugin, Location location) {
        super(plugin, location);

        dataWatcher.setObject(DATA_ENTITY_FLAGS, (byte) 0x00);
        dataWatcher.setObject(DATA_CUSTOM_NAME, Optional.empty());
        dataWatcher.setObject(DATA_CUSTOM_NAME_VISIBLE, false);
    }

    @Override
    public int getEntityID() {
        return entityID;
    }

    @Override
    public UUID getUniqueID() {
        return uniqueID;
    }

    @Override
    public void setInteract(Interact interact) {
        super.setInteract(interact);

        EntityManager manager = plugin.getManager(EntityManager.class);
        if (interact != null) {
            manager.addInteract(entityID, interact);
        } else {
            manager.removeInteract(entityID);
        }
    }

    @Override
    public boolean isInvisible() {
        return getEntityFlag(5);
    }

    @Override
    public void setInvisible(boolean invisible) {
        setEntityFlag(5, invisible);
    }

    @Override
    public boolean isGlowing() {
        return getEntityFlag(6);
    }

    @Override
    public void setGlowing(boolean glowing) {
        setEntityFlag(6, glowing);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Component getCustomName() {
        Optional<IChatBaseComponent> optional = (Optional<IChatBaseComponent>) dataWatcher.getObject(DATA_CUSTOM_NAME);
        return optional
                .map(PaperAdventure::asAdventure)
                .orElse(null);
    }

    @Override
    public void setCustomName(Component customName) {
        dataWatcher.setObject(DATA_CUSTOM_NAME, customName == null || Cmpt.isEmpty(customName) ? Optional.empty() : Optional.of(PaperAdventure.asVanilla(customName)), true);
        dataWatcherDirty = true;
    }

    @Override
    public boolean isCustomNameVisible() {
        return (Boolean) dataWatcher.getObject(DATA_CUSTOM_NAME_VISIBLE);
    }

    @Override
    public void setCustomNameVisible(boolean customNameVisible) {
        dataWatcher.setObject(DATA_CUSTOM_NAME_VISIBLE, customNameVisible, true);
        dataWatcherDirty = true;
    }

    protected boolean getEntityFlag(int index) {
        return (dataWatcher.getByte(DATA_ENTITY_FLAGS.getIndex()) & 1 << index) != 0;
    }

    protected void setEntityFlag(int index, boolean value) {
        byte flags = dataWatcher.getByte(DATA_ENTITY_FLAGS.getIndex());
        if (value) {
            dataWatcher.setObject(DATA_ENTITY_FLAGS, (byte) (flags | 1 << index), true);
        } else {
            dataWatcher.setObject(DATA_ENTITY_FLAGS, (byte) (flags & ~(1 << index)), true);
        }
        dataWatcherDirty = true;
    }

    @Override
    public void onShowTo(Player player) {
        WrapperPlayServerSpawnEntity spawnWrapper = new WrapperPlayServerSpawnEntity();
        spawnWrapper.setEntityID(entityID);
        spawnWrapper.setUniqueID(uniqueID);
        spawnWrapper.setType(getType());
        spawnWrapper.setX(x);
        spawnWrapper.setY(y);
        spawnWrapper.setZ(z);
        spawnWrapper.setPitch(pitch);
        spawnWrapper.setYaw(yaw);
        spawnWrapper.setObjectData(0);
        spawnWrapper.sendPacket(player);

        WrapperPlayServerEntityMetadata metadataWrapper = new WrapperPlayServerEntityMetadata();
        metadataWrapper.setEntityID(entityID);
        metadataWrapper.setMetadata(dataWatcher.getWatchableObjects());
        metadataWrapper.sendPacket(player);
    }

    @Override
    public void onHideTo(Player player) {
        WrapperPlayServerEntityDestroy wrapper = new WrapperPlayServerEntityDestroy();
        wrapper.setEntityIDs(entityID);
        wrapper.sendPacket(player);
    }

    @Override
    public void onTick() {
        super.onTick();

        if (dataWatcherDirty) {
            if (hasViewers()) {
                List<WrappedWatchableObject> watchableObjects = null;

                for (WrappedWatchableObject watchableObject : dataWatcher.getWatchableObjects()) {
                    if (watchableObject.getDirtyState()) {
                        if (watchableObjects == null) {
                            watchableObjects = new ArrayList<>();
                        }
                        watchableObjects.add(watchableObject);
                        watchableObject.setDirtyState(false);
                    }
                }

                if (watchableObjects != null) {
                    WrapperPlayServerEntityMetadata wrapper = new WrapperPlayServerEntityMetadata();
                    wrapper.setEntityID(entityID);
                    wrapper.setMetadata(watchableObjects);
                    sendPacket(wrapper);
                }
            }

            dataWatcherDirty = false;
        }
    }

    @Override
    public void onHead(float yaw) {
        if (hasViewers()) {
            WrapperPlayServerEntityHeadRotation wrapper = new WrapperPlayServerEntityHeadRotation();
            wrapper.setEntityID(entityID);
            wrapper.setHeadYaw(yaw);
            sendPacket(wrapper);
        }
    }

    @Override
    public void onLook(float yaw, float pitch) {
        if (hasViewers()) {
            WrapperPlayServerEntityLook wrapper = new WrapperPlayServerEntityLook();
            wrapper.setEntityID(entityID);
            wrapper.setYaw(yaw);
            wrapper.setPitch(pitch);
            wrapper.setOnGround(onGround);
            sendPacket(wrapper);
        }
    }

    @Override
    public void onMove(short deltaX, short deltaY, short deltaZ) {
        if (hasViewers()) {
            WrapperPlayServerRelEntityMove wrapper = new WrapperPlayServerRelEntityMove();
            wrapper.setEntityID(entityID);
            wrapper.setNativeX(deltaX);
            wrapper.setNativeY(deltaY);
            wrapper.setNativeZ(deltaZ);
            wrapper.setOnGround(onGround);
            sendPacket(wrapper);
        }
    }

    @Override
    public void onMoveAndLook(short deltaX, short deltaY, short deltaZ, float yaw, float pitch) {
        if (hasViewers()) {
            WrapperPlayServerRelEntityMoveLook wrapper = new WrapperPlayServerRelEntityMoveLook();
            wrapper.setEntityID(entityID);
            wrapper.setNativeX(deltaX);
            wrapper.setNativeY(deltaY);
            wrapper.setNativeZ(deltaZ);
            wrapper.setYaw(yaw);
            wrapper.setPitch(pitch);
            wrapper.setOnGround(onGround);
            sendPacket(wrapper);
        }
    }

    @Override
    public void onTeleport(double x, double y, double z, float yaw, float pitch) {
        if (hasViewers()) {
            WrapperPlayServerEntityTeleport wrapper = new WrapperPlayServerEntityTeleport();
            wrapper.setEntityID(entityID);
            wrapper.setX(x);
            wrapper.setY(y);
            wrapper.setZ(z);
            wrapper.setYaw(yaw);
            wrapper.setPitch(pitch);
            wrapper.setOnGround(onGround);
            sendPacket(wrapper);
        }
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;

        LibEntity that = (LibEntity) object;
        return entityID == that.entityID;
    }

    @Override
    public int hashCode() {
        return entityID;
    }

}
