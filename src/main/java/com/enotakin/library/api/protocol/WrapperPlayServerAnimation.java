package com.enotakin.library.api.protocol;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import org.bukkit.World;
import org.bukkit.entity.Entity;

public class WrapperPlayServerAnimation extends PacketWrapper {

    public static final PacketType TYPE = PacketType.Play.Server.ANIMATION;

    public WrapperPlayServerAnimation() {
        super(new PacketContainer(TYPE), TYPE);
        handle.getModifier().writeDefaults();
    }

    public WrapperPlayServerAnimation(PacketContainer packet) {
        super(packet, TYPE);
    }

    public int getEntityID() {
        return handle.getIntegers().read(0);
    }

    public void setEntityID(int value) {
        handle.getIntegers().write(0, value);
    }

    public Entity getEntity(World world) {
        return handle.getEntityModifier(world).read(0);
    }

    public int getAnimation() {
        return handle.getIntegers().read(1);
    }

    public void setAnimation(int value) {
        handle.getIntegers().write(1, value);
    }

}
