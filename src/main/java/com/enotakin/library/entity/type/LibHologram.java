package com.enotakin.library.entity.type;

import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import com.comphenix.protocol.wrappers.WrappedWatchableObject;
import com.enotakin.library.MineLibraryPlugin;
import com.enotakin.library.api.entity.Interact;
import com.enotakin.library.api.entity.MineHologram;
import com.enotakin.library.api.protocol.*;
import com.enotakin.library.api.util.Cmpt;
import com.enotakin.library.api.util.MCHelper;
import io.papermc.paper.adventure.PaperAdventure;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class LibHologram extends LibRenderedEntity implements MineHologram {

    private final List<HologramLine> lines = new ArrayList<>();

    public LibHologram(MineLibraryPlugin plugin, Location location) {
        super(plugin, location);
    }

    @Override
    public void addLine(Component text) {
        if (text == null || Cmpt.isEmpty(text)) {
            addLine(MineHologram.TEXT_LINE_HEIGHT);
            return;
        }

        TextLine line = new TextLine();
        line.setText(text, false);
        addLine(line);
    }

    @Override
    public void addLine(ItemStack itemStack) {
        if (itemStack == null || itemStack.getType() == Material.AIR) {
            addLine(ITEM_LINE_HEIGHT);
            return;
        }

        ItemLine line = new ItemLine();
        line.setItemStack(itemStack, false);
        addLine(line);
    }

    @Override
    public void addLine(double height) {
        EmptyLine line = new EmptyLine();
        line.setHeight(height);
        lines.add(line);
    }

    private void addLine(HologramLine line) {
        lines.add(line);

        double indent = 0D;
        for (HologramLine currentLine : lines) {
            indent += currentLine.getHeight();
        }

        line.onShow(x, y - indent, z);
    }

    @Override
    public void setLine(int index, Component text) {
        if (index < 0 || index >= lines.size()) {
            throw new IndexOutOfBoundsException();
        }

        if (text == null || Cmpt.isEmpty(text)) {
            setLine(index, TEXT_LINE_HEIGHT);
            return;
        }

        HologramLine line = lines.get(index);

        TextLine textLine;
        if (line instanceof TextLine) {
            textLine = (TextLine) line;
            textLine.setText(text, true);
        } else {
            textLine = new TextLine();
            textLine.setText(text, false);
            setLine(index, line, textLine);
        }
    }

    @Override
    public void setLine(int index, ItemStack itemStack) {
        if (index < 0 || index >= lines.size()) {
            throw new IndexOutOfBoundsException();
        }

        if (itemStack == null || itemStack.getType() == Material.AIR) {
            setLine(index, ITEM_LINE_HEIGHT);
            return;
        }

        HologramLine line = lines.get(index);

        ItemLine itemLine;
        if (line instanceof ItemLine) {
            itemLine = (ItemLine) line;
            itemLine.setItemStack(itemStack, true);
        } else {
            itemLine = new ItemLine();
            itemLine.setItemStack(itemStack, false);
            setLine(index, line, itemLine);
        }
    }

    @Override
    public void setLine(int index, double height) {
        if (index < 0 || index >= lines.size()) {
            throw new IndexOutOfBoundsException();
        }

        HologramLine line = lines.get(index);

        EmptyLine emptyLine;
        if (line instanceof EmptyLine) {
            emptyLine = (EmptyLine) line;
            emptyLine.setHeight(height);

            double indent = 0D;
            for (int i = 0; i < lines.size(); i++) {
                HologramLine currentLine = lines.get(i);
                indent += currentLine.getHeight();
                if (i > index) {
                    currentLine.onTeleport(x, y - indent, z);
                }
            }
        } else {
            emptyLine = new EmptyLine();
            emptyLine.setHeight(height);
            setLine(index, line, emptyLine);
        }
    }

    private void setLine(int index, HologramLine oldLine, HologramLine newLine) {
        if (oldLine == null) {
            setLine(index, newLine);
        } else {
            IntList entityIDs = new IntArrayList();
            oldLine.getEntityIDs(entityIDs);

            if (entityIDs.size() > 0) {
                WrapperPlayServerEntityDestroy wrapper = new WrapperPlayServerEntityDestroy();
                wrapper.setEntityIDs(entityIDs.toIntArray());
                sendPacket(wrapper);
            }

            setLine(index, newLine);
        }
    }

    private void setLine(int index, HologramLine newLine) {
        lines.set(index, newLine);

        double indent = 0D;
        for (int i = 0; i < lines.size(); i++) {
            HologramLine line = lines.get(i);
            indent += line.getHeight();

            if (i == index) {
                line.onShow(x, y - indent, z);
            } else if (i > index) {
                line.onTeleport(x, y - indent, z);
            }
        }
    }

    @Override
    public void insertLine(int index, Component text) {
        if (index < 0 || index > lines.size()) {
            throw new IndexOutOfBoundsException();
        }

        if (text == null || Cmpt.isEmpty(text)) {
            insertLine(index, TEXT_LINE_HEIGHT);
            return;
        }

        TextLine line = new TextLine();
        line.setText(text, false);
        insertLine(index, line);
    }

    @Override
    public void insertLine(int index, ItemStack itemStack) {
        if (index < 0 || index > lines.size()) {
            throw new IndexOutOfBoundsException();
        }

        if (itemStack == null || itemStack.getType() == Material.AIR) {
            insertLine(index, ITEM_LINE_HEIGHT);
            return;
        }

        ItemLine line = new ItemLine();
        line.setItemStack(itemStack, false);
        insertLine(index, line);
    }

    @Override
    public void insertLine(int index, double height) {
        if (index < 0 || index > lines.size()) {
            throw new IndexOutOfBoundsException();
        }

        EmptyLine line = new EmptyLine();
        line.setHeight(height);
        insertLine(index, line);
    }

    private void insertLine(int index, HologramLine newLine) {
        lines.add(index, newLine);

        double indent = 0D;
        for (int i = 0; i < lines.size(); i++) {
            HologramLine line = lines.get(i);
            indent += line.getHeight();

            if (i == index) {
                line.onShow(x, y - indent, z);
            } else if (i > index) {
                line.onTeleport(x, y - indent, z);
            }
        }
    }

    @Override
    public void removeLine(int index) {
        if (index < 0 || index >= lines.size()) {
            throw new IndexOutOfBoundsException();
        }

        HologramLine oldLine = lines.remove(index);

        IntList entityIDs = new IntArrayList();
        oldLine.getEntityIDs(entityIDs);

        if (entityIDs.size() > 0) {
            WrapperPlayServerEntityDestroy wrapper = new WrapperPlayServerEntityDestroy();
            wrapper.setEntityIDs(entityIDs.toIntArray());
            sendPacket(wrapper);
        }

        double indent = 0D;
        for (int i = 0; i < lines.size(); i++) {
            HologramLine line = lines.get(i);
            indent += line.getHeight();

            if (i >= index) {
                line.onTeleport(x, y - indent, z);
            }
        }
    }

    @Override
    public void playPickupTo(Player player, int index) {
        HologramLine line = getLine(index);
        if (line instanceof ItemLine itemLine) {
            WrapperPlayServerCollect wrapper = new WrapperPlayServerCollect();
            wrapper.setCollectedEntityID(itemLine.dropEntityID);
            wrapper.setCollectorEntityID(player.getEntityId());
            wrapper.setPickupItemCount(1);
            sendPacket(wrapper);
        }
    }

    private HologramLine getLine(int index) {
        return index >= 0 && index < lines.size() ? lines.get(index) : null;
    }

    @Override
    public void clear() {
        if (lines.size() > 0) {
            if (hasViewers()) {
                IntList entityIDs = new IntArrayList();

                for (HologramLine line : lines) {
                    line.getEntityIDs(entityIDs);
                }

                if (entityIDs.size() > 0) {
                    WrapperPlayServerEntityDestroy wrapper = new WrapperPlayServerEntityDestroy();
                    wrapper.setEntityIDs(entityIDs.toIntArray());
                    sendPacket(wrapper);
                }
            }
            lines.clear();
        }
    }

    @Override
    public void setInteract(Interact interact) {
        super.setInteract(interact);

        if (interact != null) {
            // TODO: add all interacts
        } else {
            // TODO: remove all interacts
        }
    }

    @Override
    public void onTick() {
        super.onTick();

        for (HologramLine line : lines) {
            line.onTick();
        }
    }

    @Override
    public void onShowTo(Player player) {
        double indent = 0D;
        for (HologramLine line : lines) {
            indent += line.getHeight();
            line.onShowTo(player, x, y - indent, z);
        }
    }

    @Override
    public void onHideTo(Player player) {
        IntList entityIDs = new IntArrayList();

        for (HologramLine line : lines) {
            line.getEntityIDs(entityIDs);
        }

        if (entityIDs.size() > 0) {
            WrapperPlayServerEntityDestroy wrapper = new WrapperPlayServerEntityDestroy();
            wrapper.setEntityIDs(entityIDs.toIntArray());
            wrapper.sendPacket(player);
        }
    }

    @Override
    public void onMove(short deltaX, short deltaY, short deltaZ) {
        for (HologramLine line : lines) {
            line.onMove(deltaX, deltaY, deltaZ);
        }
    }

    @Override
    public void onMoveAndLook(short deltaX, short deltaY, short deltaZ, float yaw, float pitch) {
        for (HologramLine line : lines) {
            line.onMove(deltaX, deltaY, deltaZ);
        }
    }

    @Override
    public void onTeleport(double x, double y, double z, float yaw, float pitch) {
        double indent = 0D;
        for (HologramLine line : lines) {
            indent += line.getHeight();
            line.onTeleport(x, y - indent, z);
        }
    }

    private class HologramBox {

        private static final int SLIME_TYPE_ID = MCHelper.getEntityTypeID(EntityType.SLIME);
        private static final WrappedDataWatcher.WrappedDataWatcherObject DATA_SIZE = new WrappedDataWatcher.WrappedDataWatcherObject(15, WrappedDataWatcher.Registry.get(Integer.class));

        private final int entityID = MCHelper.nextEntityID();
        private final UUID uniqueID = UUID.randomUUID();

        private final WrappedDataWatcher dataWatcher = new WrappedDataWatcher();

        public HologramBox() {
            dataWatcher.setObject(LibEntity.DATA_ENTITY_FLAGS, (byte) 0x20); // Invisibility = true
            dataWatcher.setObject(DATA_SIZE, 1); // Slime Size = 1
        }

        public void onShowTo(Player player, double x, double y, double z) {
            WrapperPlayServerSpawnEntityLiving spawnWrapper = new WrapperPlayServerSpawnEntityLiving();
            spawnWrapper.setEntityID(entityID);
            spawnWrapper.setUniqueID(uniqueID);
            spawnWrapper.setTypeID(SLIME_TYPE_ID);
            spawnWrapper.setX(x);
            spawnWrapper.setY(y);
            spawnWrapper.setZ(z);
            spawnWrapper.setYaw(0F);
            spawnWrapper.setPitch(0F);
            spawnWrapper.setHeadPitch(0F);
            spawnWrapper.setVelocityX(0D);
            spawnWrapper.setVelocityY(0D);
            spawnWrapper.setVelocityZ(0D);
            spawnWrapper.sendPacket(player);

            WrapperPlayServerEntityMetadata metadataWrapper = new WrapperPlayServerEntityMetadata();
            metadataWrapper.setEntityID(entityID);
            metadataWrapper.setMetadata(dataWatcher.getWatchableObjects());
            metadataWrapper.sendPacket(player);
        }

        public void onTeleport(double x, double y, double z) {
            WrapperPlayServerEntityTeleport wrapper = new WrapperPlayServerEntityTeleport();
            wrapper.setEntityID(entityID);
            wrapper.setX(x);
            wrapper.setY(y);
            wrapper.setZ(z);
            wrapper.setYaw(0F);
            wrapper.setPitch(0F);
            wrapper.setOnGround(false);
            sendPacket(wrapper);
        }

        public void onMove(short deltaX, short deltaY, short deltaZ) {
            WrapperPlayServerRelEntityMove wrapper = new WrapperPlayServerRelEntityMove();
            wrapper.setEntityID(entityID);
            wrapper.setNativeX(deltaX);
            wrapper.setNativeY(deltaY);
            wrapper.setNativeZ(deltaZ);
            wrapper.setOnGround(false);
            sendPacket(wrapper);
        }

    }

    private interface HologramLine {

        double getHeight();

        void getEntityIDs(IntList entityIDs);

        void onShow(double x, double y, double z);

        void onShowTo(Player player, double x, double y, double z);

        void onTeleport(double x, double y, double z);

        void onMove(short deltaX, short deltaY, short deltaZ);

        void onTick();

    }

    private abstract class AbstractFilledLine implements HologramLine {

        private static final int ARMOR_STAND_TYPE_ID = MCHelper.getEntityTypeID(EntityType.ARMOR_STAND);

        protected final int standEntityID = MCHelper.nextEntityID();
        protected final UUID standUniqueID = UUID.randomUUID();

        protected final WrappedDataWatcher standDataWatcher = new WrappedDataWatcher();
        protected boolean standDataWatcherDirty;

        private HologramBox box;

        public AbstractFilledLine() {
            standDataWatcher.setObject(LibArmorStand.DATA_ENTITY_FLAGS, (byte) 0x20); // Invisibility = true
            standDataWatcher.setObject(LibArmorStand.DATA_STAND_FLAGS, (byte) 0x10); // Marker = true
            standDataWatcher.setObject(LibArmorStand.DATA_CUSTOM_NAME, Optional.empty());
        }

        @Override
        public void getEntityIDs(IntList entityIDs) {
            entityIDs.add(standEntityID);
        }

        @Override
        public void onShow(double x, double y, double z) {
            WrapperPlayServerSpawnEntityLiving spawnWrapper = new WrapperPlayServerSpawnEntityLiving();
            spawnWrapper.setEntityID(standEntityID);
            spawnWrapper.setUniqueID(standUniqueID);
            spawnWrapper.setTypeID(ARMOR_STAND_TYPE_ID);
            spawnWrapper.setX(x);
            spawnWrapper.setY(y);
            spawnWrapper.setZ(z);
            spawnWrapper.setYaw(0F);
            spawnWrapper.setPitch(0F);
            spawnWrapper.setHeadPitch(0F);
            spawnWrapper.setVelocityX(0D);
            spawnWrapper.setVelocityY(0D);
            spawnWrapper.setVelocityZ(0D);
            sendPacket(spawnWrapper);

            WrapperPlayServerEntityMetadata metadataWrapper = new WrapperPlayServerEntityMetadata();
            metadataWrapper.setEntityID(standEntityID);
            metadataWrapper.setMetadata(standDataWatcher.getWatchableObjects());
            sendPacket(metadataWrapper);
        }

        @Override
        public void onShowTo(Player player, double x, double y, double z) {
            WrapperPlayServerSpawnEntityLiving spawnWrapper = new WrapperPlayServerSpawnEntityLiving();
            spawnWrapper.setEntityID(standEntityID);
            spawnWrapper.setUniqueID(standUniqueID);
            spawnWrapper.setTypeID(ARMOR_STAND_TYPE_ID);
            spawnWrapper.setX(x);
            spawnWrapper.setY(y);
            spawnWrapper.setZ(z);
            spawnWrapper.setYaw(0F);
            spawnWrapper.setPitch(0F);
            spawnWrapper.setHeadPitch(0F);
            spawnWrapper.setVelocityX(0D);
            spawnWrapper.setVelocityY(0D);
            spawnWrapper.setVelocityZ(0D);
            spawnWrapper.sendPacket(player);

            WrapperPlayServerEntityMetadata metadataWrapper = new WrapperPlayServerEntityMetadata();
            metadataWrapper.setEntityID(standEntityID);
            metadataWrapper.setMetadata(standDataWatcher.getWatchableObjects());
            metadataWrapper.sendPacket(player);
        }

        @Override
        public void onTeleport(double x, double y, double z) {
            if (hasViewers()) {
                WrapperPlayServerEntityTeleport wrapper = new WrapperPlayServerEntityTeleport();
                wrapper.setEntityID(standEntityID);
                wrapper.setX(x);
                wrapper.setY(y);
                wrapper.setZ(z);
                wrapper.setYaw(0F);
                wrapper.setPitch(0F);
                wrapper.setOnGround(false);
                sendPacket(wrapper);
            }
        }

        @Override
        public void onMove(short deltaX, short deltaY, short deltaZ) {
            if (hasViewers()) {
                WrapperPlayServerRelEntityMove wrapper = new WrapperPlayServerRelEntityMove();
                wrapper.setEntityID(standEntityID);
                wrapper.setNativeX(deltaX);
                wrapper.setNativeY(deltaY);
                wrapper.setNativeZ(deltaZ);
                wrapper.setOnGround(false);
                sendPacket(wrapper);
            }
        }

        @Override
        public void onTick() {
            if (standDataWatcherDirty) {
                List<WrappedWatchableObject> watchableObjects = null;

                for (WrappedWatchableObject watchableObject : standDataWatcher.getWatchableObjects()) {
                    if (watchableObject.getDirtyState()) {
                        if (watchableObjects == null) {
                            watchableObjects = new ArrayList<>();
                        }
                        watchableObject.setDirtyState(false);
                        watchableObjects.add(watchableObject);
                    }
                }

                if (watchableObjects != null) {
                    WrapperPlayServerEntityMetadata wrapper = new WrapperPlayServerEntityMetadata();
                    wrapper.setEntityID(standEntityID);
                    wrapper.setMetadata(watchableObjects);
                    sendPacket(wrapper);
                }

                standDataWatcherDirty = false;
            }
        }

    }

    private class TextLine extends AbstractFilledLine {

        public TextLine() {
            standDataWatcher.setObject(LibArmorStand.DATA_CUSTOM_NAME_VISIBLE, true);
        }

        public void setText(Component text, boolean dirty) {
            standDataWatcher.setObject(LibArmorStand.DATA_CUSTOM_NAME, text == null || Cmpt.isEmpty(text) ? Optional.empty() : Optional.of(PaperAdventure.asVanilla(text)), dirty);
            if (dirty) {
                standDataWatcherDirty = true;
            }
        }

        @Override
        public double getHeight() {
            return MineHologram.TEXT_LINE_HEIGHT;
        }

    }

    private class ItemLine extends AbstractFilledLine {

        private static final double VEHICLE_OFFSET = 0.2D;

        private static final WrappedDataWatcher.WrappedDataWatcherObject DATA_ITEM = new WrappedDataWatcher.WrappedDataWatcherObject(7, WrappedDataWatcher.Registry.getItemStackSerializer(false));

        private final int dropEntityID = MCHelper.nextEntityID();
        private final UUID dropUniqueID = UUID.randomUUID();

        private final WrappedDataWatcher dropDataWatcher = new WrappedDataWatcher();
        private boolean dropDataWatcherDirty;

        public void setItemStack(ItemStack itemStack, boolean dirty) {
            dropDataWatcher.setObject(DATA_ITEM, itemStack, dirty);
            if (dirty) {
                dropDataWatcherDirty = true;
            }
        }

        @Override
        public double getHeight() {
            return MineHologram.ITEM_LINE_HEIGHT;
        }

        @Override
        public void getEntityIDs(IntList entityIDs) {
            super.getEntityIDs(entityIDs);
            entityIDs.add(dropEntityID);
        }

        @Override
        public void onShow(double x, double y, double z) {
            super.onShow(x, y, z);

            WrapperPlayServerSpawnEntity spawnWrapper = new WrapperPlayServerSpawnEntity();
            spawnWrapper.setEntityID(dropEntityID);
            spawnWrapper.setUniqueID(dropUniqueID);
            spawnWrapper.setType(EntityType.DROPPED_ITEM);
            spawnWrapper.setObjectData(0);
            spawnWrapper.setX(x);
            spawnWrapper.setY(y);
            spawnWrapper.setZ(z);
            spawnWrapper.setYaw(0F);
            spawnWrapper.setPitch(0F);
            spawnWrapper.setOptionalSpeedX(0D);
            spawnWrapper.setOptionalSpeedY(0D);
            spawnWrapper.setOptionalSpeedZ(0D);
            sendPacket(spawnWrapper);

            WrapperPlayServerEntityMetadata metadataWrapper = new WrapperPlayServerEntityMetadata();
            metadataWrapper.setEntityID(dropEntityID);
            metadataWrapper.setMetadata(dropDataWatcher.getWatchableObjects());
            sendPacket(metadataWrapper);

            WrapperPlayServerMount mountWrapper = new WrapperPlayServerMount();
            mountWrapper.setEntityID(standEntityID);
            mountWrapper.setPassengerIds(dropEntityID);
            sendPacket(mountWrapper);
        }

        @Override
        public void onShowTo(Player player, double x, double y, double z) {
            super.onShowTo(player, x, y + VEHICLE_OFFSET, z);

            WrapperPlayServerSpawnEntity spawnWrapper = new WrapperPlayServerSpawnEntity();
            spawnWrapper.setEntityID(dropEntityID);
            spawnWrapper.setUniqueID(dropUniqueID);
            spawnWrapper.setType(EntityType.DROPPED_ITEM);
            spawnWrapper.setObjectData(0);
            spawnWrapper.setX(x);
            spawnWrapper.setY(y);
            spawnWrapper.setZ(z);
            spawnWrapper.setYaw(0F);
            spawnWrapper.setPitch(0F);
            spawnWrapper.setOptionalSpeedX(0D);
            spawnWrapper.setOptionalSpeedY(0D);
            spawnWrapper.setOptionalSpeedZ(0D);
            spawnWrapper.sendPacket(player);

            WrapperPlayServerEntityMetadata metadataWrapper = new WrapperPlayServerEntityMetadata();
            metadataWrapper.setEntityID(dropEntityID);
            metadataWrapper.setMetadata(dropDataWatcher.getWatchableObjects());
            metadataWrapper.sendPacket(player);

            WrapperPlayServerMount mountWrapper = new WrapperPlayServerMount();
            mountWrapper.setEntityID(standEntityID);
            mountWrapper.setPassengerIds(dropEntityID);
            mountWrapper.sendPacket(player);
        }

        @Override
        public void onTick() {
            super.onTick();

            if (dropDataWatcherDirty) {
                List<WrappedWatchableObject> watchableObjects = null;

                for (WrappedWatchableObject watchableObject : dropDataWatcher.getWatchableObjects()) {
                    if (watchableObject.getDirtyState()) {
                        if (watchableObjects == null) {
                            watchableObjects = new ArrayList<>();
                        }
                        watchableObject.setDirtyState(false);
                        watchableObjects.add(watchableObject);
                    }
                }

                if (watchableObjects != null) {
                    WrapperPlayServerEntityMetadata wrapper = new WrapperPlayServerEntityMetadata();
                    wrapper.setEntityID(dropEntityID);
                    wrapper.setMetadata(watchableObjects);
                    sendPacket(wrapper);
                }

                dropDataWatcherDirty = false;
            }
        }

    }

    private static class EmptyLine implements HologramLine {

        private double height;

        @Override
        public double getHeight() {
            return height;
        }

        public void setHeight(double height) {
            this.height = height;
        }

        @Override
        public void getEntityIDs(IntList entityIDs) {
        }

        @Override
        public void onShowTo(Player player, double x, double y, double z) {
        }

        @Override
        public void onShow(double x, double y, double z) {
        }

        @Override
        public void onTeleport(double x, double y, double z) {
        }

        @Override
        public void onMove(short deltaX, short deltaY, short deltaZ) {
        }

        @Override
        public void onTick() {
        }

    }

}
