package com.enotakin.library.api.entity;

import com.enotakin.library.api.nametag.NameTag;

public interface MineLivingEntity extends MineEntity {

    boolean isLookAtPlayer();

    void setLookAtPlayer(boolean lookAtPlayer);

    NameTag getNameTag();

}
