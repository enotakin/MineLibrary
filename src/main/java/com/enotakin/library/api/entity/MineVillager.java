package com.enotakin.library.api.entity;

import org.bukkit.entity.Villager;

public interface MineVillager extends MineEquippableEntity {

    Villager.Type getVillagerType();

    void setVillagerType(Villager.Type type);

    Villager.Profession getVillagerProfession();

    void setVillagerProfession(Villager.Profession profession);

    int getVillagerLevel();

    void setVillagerLevel(int level);

}
