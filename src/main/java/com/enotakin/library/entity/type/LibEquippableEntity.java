package com.enotakin.library.entity.type;

import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.Pair;
import com.enotakin.library.api.entity.MineEquippableEntity;
import com.enotakin.library.api.protocol.WrapperPlayServerEntityEquipment;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;
import com.enotakin.library.MineLibraryPlugin;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public abstract class LibEquippableEntity extends LibLivingEntity implements MineEquippableEntity {

    private static final ItemStack AIR = new ItemStack(Material.AIR);
    private static final EnumWrappers.ItemSlot[] SLOTS = EnumWrappers.ItemSlot.values();

    private final Map<EnumWrappers.ItemSlot, EntityEquipment> equipments = new EnumMap<>(EnumWrappers.ItemSlot.class);
    private boolean equipmentDirty;

    public LibEquippableEntity(MineLibraryPlugin plugin, Location location) {
        super(plugin, location);

        for (EnumWrappers.ItemSlot slot : SLOTS) {
            setEquipment(slot, AIR, false);
        }
    }

    @Override
    public @Nullable ItemStack getItemInMainHand() {
        return getEquipment(EnumWrappers.ItemSlot.MAINHAND);
    }

    @Override
    public void setItemInMainHand(@Nullable ItemStack itemInMainHand) {
        setEquipment(EnumWrappers.ItemSlot.MAINHAND, itemInMainHand, true);
    }

    @Override
    public @Nullable ItemStack getItemInOffHand() {
        return getEquipment(EnumWrappers.ItemSlot.OFFHAND);
    }

    @Override
    public void setItemInOffHand(@Nullable ItemStack itemInOffHand) {
        setEquipment(EnumWrappers.ItemSlot.OFFHAND, itemInOffHand, true);
    }

    @Override
    public @Nullable ItemStack getHelmet() {
        return getEquipment(EnumWrappers.ItemSlot.HEAD);
    }

    @Override
    public void setHelmet(@Nullable ItemStack helmet) {
        setEquipment(EnumWrappers.ItemSlot.HEAD, helmet, true);
    }

    @Override
    public @Nullable ItemStack getChestplate() {
        return getEquipment(EnumWrappers.ItemSlot.CHEST);
    }

    @Override
    public void setChestplate(@Nullable ItemStack chestplate) {
        setEquipment(EnumWrappers.ItemSlot.CHEST, chestplate, true);
    }

    @Override
    public @Nullable ItemStack getLeggings() {
        return getEquipment(EnumWrappers.ItemSlot.LEGS);
    }

    @Override
    public void setLeggings(@Nullable ItemStack leggings) {
        setEquipment(EnumWrappers.ItemSlot.LEGS, leggings, true);
    }

    @Override
    public @Nullable ItemStack getBoots() {
        return getEquipment(EnumWrappers.ItemSlot.FEET);
    }

    @Override
    public void setBoots(@Nullable ItemStack boots) {
        setEquipment(EnumWrappers.ItemSlot.FEET, boots, true);
    }

    @Override
    public void onShowTo(Player player) {
        super.onShowTo(player);
        onShowEquipmentTo(player);
    }

    public void onShowEquipmentTo(Player player) {
        List<Pair<EnumWrappers.ItemSlot, ItemStack>> slotStackPairs = new ArrayList<>();
        for (EntityEquipment equipment : equipments.values()) {
            slotStackPairs.add(new Pair<>(equipment.slot, equipment.itemStack));
        }

        WrapperPlayServerEntityEquipment wrapper = new WrapperPlayServerEntityEquipment();
        wrapper.setEntityID(entityID);
        wrapper.setSlotStackPairs(slotStackPairs);
        wrapper.sendPacket(player);
    }

    @Override
    public void onTick() {
        super.onTick();

        if (equipmentDirty) {
            if (hasViewers()) {
                List<Pair<EnumWrappers.ItemSlot, ItemStack>> slotStackPairs = null;

                for (EntityEquipment equipment : equipments.values()) {
                    if (equipment.dirty) {
                        if (slotStackPairs == null) {
                            slotStackPairs = new ArrayList<>();
                        }
                        slotStackPairs.add(new Pair<>(equipment.slot, equipment.itemStack));
                        equipment.dirty = false;
                    }
                }

                if (slotStackPairs != null) {
                    WrapperPlayServerEntityEquipment wrapper = new WrapperPlayServerEntityEquipment();
                    wrapper.setEntityID(entityID);
                    wrapper.setSlotStackPairs(slotStackPairs);
                    sendPacket(wrapper);
                }
            }

            equipmentDirty = false;
        }
    }

    private ItemStack getEquipment(EnumWrappers.ItemSlot slot) {
        EntityEquipment equipment = equipments.get(slot);
        if (equipment != null) {
            ItemStack itemStack = equipment.itemStack;
            if (itemStack != null) {
                return itemStack.clone();
            }
        }

        return null;
    }

    private void setEquipment(EnumWrappers.ItemSlot slot, ItemStack itemStack, boolean dirty) {
        if (itemStack == null) {
            itemStack = AIR;
        }

        EntityEquipment equipment = equipments.get(slot);
        if (equipment == null) {
            equipment = new EntityEquipment(slot);
            equipments.put(slot, equipment);
        }

        equipment.itemStack = itemStack.clone();
        equipment.dirty = dirty;

        if (dirty) {
            equipmentDirty = true;
        }
    }

    private static class EntityEquipment {

        private final EnumWrappers.ItemSlot slot;
        private ItemStack itemStack;
        private boolean dirty;

        public EntityEquipment(EnumWrappers.ItemSlot slot) {
            this.slot = slot;
        }

    }

}
