package com.enotakin.library.entity.type;

import com.enotakin.library.api.entity.MineLivingEntity;
import com.enotakin.library.api.protocol.WrapperPlayServerEntityHeadRotation;
import com.enotakin.library.api.protocol.WrapperPlayServerEntityLook;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.NumberConversions;
import com.enotakin.library.MineLibraryPlugin;
import com.enotakin.library.api.util.Mth;
import com.enotakin.library.nametag.PacketEntityNameTag;

import java.util.HashMap;
import java.util.Map;

public abstract class LibLivingEntity extends LibEntity implements MineLivingEntity {

    private static final double LOOK_AT_PLAYER_DISTANCE_SQ = 5 * 5;

    protected final PacketEntityNameTag nameTag;
    protected final Map<Player, EntityView> views = new HashMap<>();

    private boolean lookAtPlayer;

    public LibLivingEntity(MineLibraryPlugin plugin, Location location) {
        super(plugin, location);

        nameTag = new PacketEntityNameTag(plugin, this);
    }

    @Override
    public boolean isLookAtPlayer() {
        return lookAtPlayer;
    }

    @Override
    public void setLookAtPlayer(boolean lookAtPlayer) {
        this.lookAtPlayer = lookAtPlayer;
    }

    @Override
    public PacketEntityNameTag getNameTag() {
        return nameTag;
    }

    @Override
    public void onShowTo(Player player) {
        super.onShowTo(player);
        onShowViewTo(player);
    }

    public void onShowViewTo(Player player) {
        views.put(player, new EntityView(yaw, pitch));
    }

    @Override
    public void onHideTo(Player player) {
        super.onHideTo(player);
        onHideViewTo(player);
    }

    public void onHideViewTo(Player player) {
        views.remove(player);
    }

    @Override
    public void onTick() {
        super.onTick();

        if (lookAtPlayer && ticks % 3 == 0) {
            if (!hasViewers()) {
                return;
            }

            Location location = null;
            for (Player viewer : viewers) {
                location = location != null ? viewer.getLocation(location) : viewer.getLocation();

                EntityView view = views.get(viewer);
                if (distanceSq(location) <= LOOK_AT_PLAYER_DISTANCE_SQ) {
                    double directionX = location.getX() - x;
                    double directionY = location.getY() - y;
                    double directionZ = location.getZ() - z;

                    double length = Math.sqrt(Mth.square(directionX) + Mth.square(directionY) + Mth.square(directionZ));
                    if (length > 0D) {
                        directionX /= length;
                        directionY /= length;
                        directionZ /= length;
                    }

                    float yaw;
                    float pitch;
                    if (directionX == 0D && directionZ == 0D) {
                        yaw = 0F;
                        pitch = directionY > 0D ? -90F : 90F;
                    } else {
                        double theta = Math.atan2(-directionX, directionZ);
                        yaw = (float) Math.toDegrees((theta + Mth.PI_2) % Mth.PI_2);

                        double x2 = NumberConversions.square(directionX);
                        double z2 = NumberConversions.square(directionZ);
                        double xz = Math.sqrt(x2 + z2);
                        pitch = (float) Math.toDegrees(Math.atan(-directionY / xz));
                    }

                    if (view.yaw != yaw || view.pitch != pitch) {
                        WrapperPlayServerEntityLook lookWrapper = new WrapperPlayServerEntityLook();
                        lookWrapper.setEntityID(entityID);
                        lookWrapper.setYaw(yaw);
                        lookWrapper.setPitch(pitch);
                        lookWrapper.setOnGround(onGround);
                        lookWrapper.sendPacket(viewer);
                    }

                    if (view.yaw != yaw) {
                        WrapperPlayServerEntityHeadRotation rotationWrapper = new WrapperPlayServerEntityHeadRotation();
                        rotationWrapper.setEntityID(entityID);
                        rotationWrapper.setHeadYaw(yaw);
                        rotationWrapper.sendPacket(viewer);
                    }

                    view.yaw = yaw;
                    view.pitch = pitch;
                    view.enabled = true;
                } else {
                    if (view.enabled) {
                        if (view.yaw != yaw || view.pitch != pitch) {
                            WrapperPlayServerEntityLook lookWrapper = new WrapperPlayServerEntityLook();
                            lookWrapper.setEntityID(entityID);
                            lookWrapper.setYaw(yaw);
                            lookWrapper.setPitch(pitch);
                            lookWrapper.setOnGround(onGround);
                            lookWrapper.sendPacket(viewer);
                        }

                        if (view.yaw != yaw) {
                            WrapperPlayServerEntityHeadRotation rotationWrapper = new WrapperPlayServerEntityHeadRotation();
                            rotationWrapper.setEntityID(entityID);
                            rotationWrapper.setHeadYaw(yaw);
                            rotationWrapper.sendPacket(viewer);
                        }

                        view.yaw = yaw;
                        view.pitch = pitch;
                        view.enabled = false;
                    }
                }
            }
        }
    }

    @Override
    public boolean remove() {
        boolean removed = super.remove();
        if (removed) {
            nameTag.remove();
        }
        return removed;
    }

    private static class EntityView {

        private float yaw;
        private float pitch;

        private boolean enabled;

        public EntityView(float yaw, float pitch) {
            this.yaw = yaw;
            this.pitch = pitch;
        }

    }

}
