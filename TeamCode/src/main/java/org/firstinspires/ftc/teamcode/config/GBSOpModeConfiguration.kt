package org.firstinspires.ftc.teamcode.config

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles
import org.firstinspires.ftc.teamcode.config.auto.IGBSFire3Configuration

// TODO: Test these values
object GBSCloseBlueConfiguration : IGBSFire3Configuration {
    override val MOTOR_DISTANCES = Pair(48, 48)

    override val FLYWHEEL_VELOCITY = 2000.0
    override val SPINUP_MS = 3000L

    override val BASE_POWER = 0.33

    override val DESIRED_TAG_ORIENTATION =
        YawPitchRollAngles(AngleUnit.DEGREES, 76.9, 86.9, 39.0, 10)
    override val ERROR_EPSILON = 0.5
    override val ERROR_CORRECTION_SPEED = 0.25
    override val PID_K_P = 0.05
}

// TODO: Test these values
object GBSFarBlueConfiguration : IGBSFire3Configuration {
    override val MOTOR_DISTANCES = Pair(0, -5)

    override val FLYWHEEL_VELOCITY = 2500.0
    override val SPINUP_MS = 3000L

    override val BASE_POWER = 0.33

    override val DESIRED_TAG_ORIENTATION =
        YawPitchRollAngles(AngleUnit.DEGREES, 82.5, 86.8, 33.5, 10)
    override val ERROR_EPSILON = 0.5
    override val ERROR_CORRECTION_SPEED = 0.25
    override val PID_K_P = 0.05
}

// TODO: Test these values
object GBSCloseRedConfiguration : IGBSFire3Configuration {
    override val MOTOR_DISTANCES = Pair(48, 48)

    override val FLYWHEEL_VELOCITY = 2000.0
    override val SPINUP_MS = 3000L

    override val BASE_POWER = 0.33

    override val DESIRED_TAG_ORIENTATION =
        YawPitchRollAngles(AngleUnit.DEGREES, 76.9, 86.9, 39.0, 10)
    override val ERROR_EPSILON = 0.5
    override val ERROR_CORRECTION_SPEED = 0.25
    override val PID_K_P = 0.05
}

// TODO: Test these values
object GBSFarRedConfiguration : IGBSFire3Configuration {
    override val MOTOR_DISTANCES = Pair(-5, 0)

    override val FLYWHEEL_VELOCITY = 2500.0
    override val SPINUP_MS = 3000L

    override val BASE_POWER = 0.33

    override val DESIRED_TAG_ORIENTATION =
        YawPitchRollAngles(AngleUnit.DEGREES, 82.5, 86.8, 33.5, 10)
    override val ERROR_EPSILON = 0.5
    override val ERROR_CORRECTION_SPEED = 0.25
    override val PID_K_P = 0.05
}
