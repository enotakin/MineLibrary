package com.enotakin.library.api.entity;

import org.bukkit.util.EulerAngle;

public interface MineArmorStand extends MineEquippableEntity {

    EulerAngle getHeadPose();

    void setHeadPose(EulerAngle headPose);

    EulerAngle getBodyPose();

    void setBodyPose(EulerAngle bodyPose);

    EulerAngle getLeftArmPose();

    void setLeftArmPose(EulerAngle leftArmPose);

    EulerAngle getRightArmPose();

    void setRightArmPose(EulerAngle rightArmPose);

    EulerAngle getLeftLegPose();

    void setLeftLegPose(EulerAngle leftLegPose);

    EulerAngle getRightLegPose();

    void setRightLegPose(EulerAngle rightLegPose);

    boolean isSmall();

    void setSmall(boolean small);

    boolean hasArms();

    void setArms(boolean arms);

    boolean hasBasePlate();

    void setBasePlate(boolean basePlate);

    boolean isMarker();

    void setMarker(boolean marker);

}
