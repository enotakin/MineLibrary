package com.enotakin.library.api.entity;

import org.bukkit.inventory.ItemStack;

public interface MineEquippableEntity extends MineLivingEntity {

    ItemStack getItemInMainHand();

    void setItemInMainHand(ItemStack itemInMainHand);

    ItemStack getItemInOffHand();

    void setItemInOffHand(ItemStack itemInOffHand);

    ItemStack getHelmet();

    void setHelmet(ItemStack helmet);

    ItemStack getChestplate();

    void setChestplate(ItemStack chestplate);

    ItemStack getLeggings();

    void setLeggings(ItemStack leggings);

    ItemStack getBoots();

    void setBoots(ItemStack boots);

    default void clearHands() {
        setItemInMainHand(null);
        setItemInOffHand(null);
    }

    default void clearArmor() {
        setHelmet(null);
        setChestplate(null);
        setLeggings(null);
        setBoots(null);
    }

}
