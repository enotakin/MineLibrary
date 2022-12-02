package com.enotakin.library.entity.type;

import com.comphenix.protocol.wrappers.Vector3F;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import com.enotakin.library.api.entity.MineArmorStand;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.util.EulerAngle;
import com.enotakin.library.MineLibraryPlugin;

public class LibArmorStand extends LibEquippableEntity implements MineArmorStand {

    public static final WrappedDataWatcher.WrappedDataWatcherObject DATA_STAND_FLAGS = new WrappedDataWatcher.WrappedDataWatcherObject(14, WrappedDataWatcher.Registry.get(Byte.class));
    public static final WrappedDataWatcher.WrappedDataWatcherObject DATA_HEAD_POSE = new WrappedDataWatcher.WrappedDataWatcherObject(15, WrappedDataWatcher.Registry.getVectorSerializer());
    public static final WrappedDataWatcher.WrappedDataWatcherObject DATA_BODY_POSE = new WrappedDataWatcher.WrappedDataWatcherObject(16, WrappedDataWatcher.Registry.getVectorSerializer());
    public static final WrappedDataWatcher.WrappedDataWatcherObject DATA_LEFT_ARM_POSE = new WrappedDataWatcher.WrappedDataWatcherObject(17, WrappedDataWatcher.Registry.getVectorSerializer());
    public static final WrappedDataWatcher.WrappedDataWatcherObject DATA_RIGHT_ARM_POSE = new WrappedDataWatcher.WrappedDataWatcherObject(18, WrappedDataWatcher.Registry.getVectorSerializer());
    public static final WrappedDataWatcher.WrappedDataWatcherObject DATA_LEFT_LEG_POSE = new WrappedDataWatcher.WrappedDataWatcherObject(19, WrappedDataWatcher.Registry.getVectorSerializer());
    public static final WrappedDataWatcher.WrappedDataWatcherObject DATA_RIGHT_LEG_POSE = new WrappedDataWatcher.WrappedDataWatcherObject(20, WrappedDataWatcher.Registry.getVectorSerializer());

    public LibArmorStand(MineLibraryPlugin plugin, Location location) {
        super(plugin, location);

        dataWatcher.setObject(DATA_STAND_FLAGS, (byte) 0x00);

        setPose(DATA_HEAD_POSE, toAngle(0F, 0F, 0F), false);
        setPose(DATA_BODY_POSE, toAngle(0F, 0F, 0F), false);
        setPose(DATA_LEFT_ARM_POSE, toAngle(-10F, 0F, -10F), false);
        setPose(DATA_RIGHT_ARM_POSE, toAngle(-15F, 0F, 10F), false);
        setPose(DATA_LEFT_LEG_POSE, toAngle(-1F, 0F, -1F), false);
        setPose(DATA_RIGHT_LEG_POSE, toAngle(1F, 0F, 1F), false);
    }

    @Override
    public EntityType getType() {
        return EntityType.ARMOR_STAND;
    }

    @Override
    public EulerAngle getHeadPose() {
        return getPose(DATA_HEAD_POSE);
    }

    @Override
    public void setHeadPose(EulerAngle headPose) {
        setPose(DATA_HEAD_POSE, headPose, true);
    }

    @Override
    public EulerAngle getBodyPose() {
        return getPose(DATA_BODY_POSE);
    }

    @Override
    public void setBodyPose(EulerAngle bodyPose) {
        setPose(DATA_BODY_POSE, bodyPose, true);
    }

    @Override
    public EulerAngle getLeftArmPose() {
        return getPose(DATA_LEFT_ARM_POSE);
    }

    @Override
    public void setLeftArmPose(EulerAngle leftArmPose) {
        setPose(DATA_LEFT_ARM_POSE, leftArmPose, true);
    }

    @Override
    public EulerAngle getRightArmPose() {
        return getPose(DATA_RIGHT_ARM_POSE);
    }

    @Override
    public void setRightArmPose(EulerAngle rightArmPose) {
        setPose(DATA_RIGHT_ARM_POSE, rightArmPose, true);
    }

    @Override
    public EulerAngle getLeftLegPose() {
        return getPose(DATA_LEFT_LEG_POSE);
    }

    @Override
    public void setLeftLegPose(EulerAngle leftLegPose) {
        setPose(DATA_LEFT_LEG_POSE, leftLegPose, true);
    }

    @Override
    public EulerAngle getRightLegPose() {
        return getPose(DATA_RIGHT_LEG_POSE);
    }

    @Override
    public void setRightLegPose(EulerAngle rightLegPose) {
        setPose(DATA_RIGHT_LEG_POSE, rightLegPose, true);
    }

    private EulerAngle getPose(WrappedDataWatcher.WrappedDataWatcherObject dataPose) {
        Vector3F vector = (Vector3F) dataWatcher.getObject(dataPose);
        return toAngle(vector.getX(), vector.getY(), vector.getZ());
    }

    private void setPose(WrappedDataWatcher.WrappedDataWatcherObject dataPose, EulerAngle angle, boolean dirty) {
        dataWatcher.setObject(dataPose, toVector(angle), dirty);
        if (dirty) {
            dataWatcherDirty = true;
        }
    }

    @Override
    public boolean isSmall() {
        return getStandFlag(1);
    }

    @Override
    public void setSmall(boolean small) {
        setStandFlag(1, small);
    }

    @Override
    public boolean hasArms() {
        return getStandFlag(4);
    }

    @Override
    public void setArms(boolean arms) {
        setStandFlag(4, arms);
    }

    @Override
    public boolean hasBasePlate() {
        return getStandFlag(8);
    }

    @Override
    public void setBasePlate(boolean basePlate) {
        setStandFlag(8, basePlate);
    }

    @Override
    public boolean isMarker() {
        return getStandFlag(16);
    }

    @Override
    public void setMarker(boolean marker) {
        setStandFlag(16, marker);
    }

    private boolean getStandFlag(int index) {
        return (dataWatcher.getByte(DATA_STAND_FLAGS.getIndex()) & index) != 0;
    }

    private void setStandFlag(int index, boolean value) {
        byte flags = dataWatcher.getByte(DATA_STAND_FLAGS.getIndex());
        if (value) {
            dataWatcher.setObject(DATA_STAND_FLAGS, (byte) (flags | index), true);
        } else {
            dataWatcher.setObject(DATA_STAND_FLAGS, (byte) (flags & ~index), true);
        }
        dataWatcherDirty = true;
    }

    private static EulerAngle toAngle(float x, float y, float z) {
        return new EulerAngle(
                Math.toRadians(x),
                Math.toRadians(y),
                Math.toRadians(z)
        );
    }

    private static Vector3F toVector(EulerAngle angle) {
        return new Vector3F(
                (float) Math.toDegrees(angle.getX()),
                (float) Math.toDegrees(angle.getY()),
                (float) Math.toDegrees(angle.getZ())
        );
    }

}
