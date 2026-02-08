package org.firstinspires.ftc.teamcode

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles

//region MODULES

@Suppress("PropertyName")
class GBSBaseModuleConfiguration {
    val BASE_POWER_COEFFICIENT = 0.75
    val FINE_ADJUST_POWER_COEFFICIENT = 0.33

    val STICK_THRESHOLD = 0.2
    val FINE_ADJUST_STICK_THRESHOLD = 0.05

    val DEBUG_TELEMETRY = true
}

@Suppress("PropertyName")
class GBSIntakeModuleConfiguration {
    val POWER = 1.0
    val FORWARD_COEFFICIENT = 1.0
    val SLOW_MODE_COEFFICIENT = 0.75
    val REVERSE_COEFFICIENT = 0.5

    val DEBUG_TELEMETRY = true
}

@Suppress("PropertyName")
class GBSFlywheelModuleConfiguration {
    /**
     * The velocity of the motor in TeleOp
     */
    val TELEOP_VELOCITY = 2500.0
    /**
     * The velocity of the motor in TeleOp with slow mode
     */
    val TELEOP_SLOW_VELOCITY = 2000.0

    /**
     * The error in velocity allowed to rumble the gamepads
     */
    val RUMBLE_ERROR_EPSILON_TPS = 10.0

    val DEBUG_TELEMETRY = true
}

@Suppress("PropertyName")
class GBSWebcamModuleConfig() {
    val DEBUG_TELEMETRY = true
}

//endregion

//region AUTO

//region BLUE

@Suppress("PropertyName")
class GBSCloseBlueConfiguration {
    /**
     * The speed of the flywheel in ticks / second
     */
    val FLYWHEEL_VELOCITY = 2000.0

    /**
     * The speed we should drive at
     */
    val BASE_POWER = 0.33

    /**
     * Distance to drive before stopping in inches
     * The first number is the left motor
     * The second number is the right motor
     */
    val MOTOR_DISTANCES = Pair(48, 48)

    /**
     * How long should we wait to let the flywheel spin-up before turning on the intake in milliseconds
     */
    val SPINUP_MS = 3000L

    /**
     * What orientation do we want to see in the AprilTag
     * TODO: Re-tune before Saturday!!
     */
    val DESIRED_TAG_ORIENTATION = YawPitchRollAngles(AngleUnit.DEGREES, 76.9, 86.9, 39.0, 10)

    /**
     * Speed of the intake after spin-up
     */
    val INTAKE_POWER = 0.5

    /**
     * What amount of error is acceptable?
     */
    val ERROR_EPSILON = 0.5

    /**
     * The power of the base motors when correcting error
     */
    val ERROR_CORRECTION_SPEED = 0.25

    /**
     * The proportional constant of the PID, a coefficient to the current error
     */
    val PID_K_P = 0.05
}

@Suppress("PropertyName")
class GBSFarBlueConfiguration {
    /**
     * The speed of the flywheel in ticks / second
     */
    val FLYWHEEL_VELOCITY = 2000.0

    /**
     * The speed we should drive at
     */
    val BASE_POWER = 0.33

    /**
     * Distance to drive before stopping in inches
     * The first number is the left motor
     * The second number is the right motor
     */
    val MOTOR_DISTANCES = Pair(0, -5)

    /**
     * How long should we wait to let the flywheel spin-up before turning on the intake in milliseconds
     */
    val SPINUP_MS = 3000L

    /**
     * What orientation do we want to see in the AprilTag
     * TODO: Re-tune before Saturday!!
     */
    val DESIRED_TAG_ORIENTATION = YawPitchRollAngles(AngleUnit.DEGREES, 82.5, 86.8, 33.5, 10)

    /**
     * Speed of the intake after spin-up
     */
    val INTAKE_POWER = 0.5

    /**
     * What amount of error is acceptable?
     */
    val ERROR_EPSILON = 0.5

    /**
     * The power of the base motors when correcting error
     */
    val ERROR_CORRECTION_SPEED = 0.25

    /**
     * The proportional constant of the PID, a coefficient to the current error
     */
    val PID_K_P = 0.05
}

//endregion

//region RED

//endregion

//endregion