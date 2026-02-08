package org.firstinspires.ftc.teamcode

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles
import org.firstinspires.ftc.teamcode.modules.GBSRobotModuleConfiguration

//region MODULES

@Suppress("PropertyName")
object GBSBaseModuleConfiguration : GBSRobotModuleConfiguration() {
    override val DEBUG_TELEMETRY = true

    const val BASE_POWER_COEFFICIENT = 0.75
    const val FINE_ADJUST_POWER_COEFFICIENT = 0.33

    const val STICK_THRESHOLD = 0.2
    const val FINE_ADJUST_STICK_THRESHOLD = 0.05
}

@Suppress("PropertyName")
object GBSIntakeModuleConfiguration : GBSRobotModuleConfiguration() {
    override val DEBUG_TELEMETRY = true

    const val POWER = 1.0
    const val FORWARD_COEFFICIENT = 1.0
    const val SLOW_MODE_COEFFICIENT = 0.75
    const val REVERSE_COEFFICIENT = 0.5
}

@Suppress("PropertyName")
object GBSFlywheelModuleConfiguration : GBSRobotModuleConfiguration() {
    override val DEBUG_TELEMETRY = true

    /**
     * The velocity of the motor in TeleOp
     */
    const val TELEOP_VELOCITY = 2500.0
    /**
     * The velocity of the motor in TeleOp with slow mode
     */
    const val TELEOP_SLOW_VELOCITY = 2000.0

    /**
     * The error in velocity allowed to rumble the gamepads
     */
    const val RUMBLE_ERROR_EPSILON_TPS = 10.0
}

object GBSWebcamModuleConfig : GBSRobotModuleConfiguration() {
    override val DEBUG_TELEMETRY = true
}

//endregion

//region AUTO

//region BLUE

object GBSCloseBlueConfiguration {
    /**
     * The speed of the flywheel in ticks / second
     */
    const val FLYWHEEL_VELOCITY = 2000.0

    /**
     * The speed we should drive at
     */
    const val BASE_POWER = 0.33

    /**
     * Distance to drive before stopping in inches
     * The first number is the left motor
     * The second number is the right motor
     */
    val MOTOR_DISTANCES = Pair(48, 48)

    /**
     * How long should we wait to let the flywheel spin-up before turning on the intake in milliseconds
     */
    const val SPINUP_MS = 3000L

    /**
     * What orientation do we want to see in the AprilTag
     * TODO: Re-tune before Saturday!!
     */
    val DESIRED_TAG_ORIENTATION = YawPitchRollAngles(AngleUnit.DEGREES, 76.9, 86.9, 39.0, 10)

    /**
     * Speed of the intake after spin-up
     */
    const val INTAKE_POWER = 0.5

    /**
     * What amount of error is acceptable?
     */
    const val ERROR_EPSILON = 0.5

    /**
     * The power of the base motors when correcting error
     */
    const val ERROR_CORRECTION_SPEED = 0.25

    /**
     * The proportional constant of the PID, a coefficient to the current error
     */
    const val PID_K_P = 0.05
}

object GBSFarBlueConfiguration {
    /**
     * The speed of the flywheel in ticks / second
     */
    const val FLYWHEEL_VELOCITY = 2000.0

    /**
     * The speed we should drive at
     */
    const val BASE_POWER = 0.33

    /**
     * Distance to drive before stopping in inches
     * The first number is the left motor
     * The second number is the right motor
     */
    val MOTOR_DISTANCES = Pair(0, -5)

    /**
     * How long should we wait to let the flywheel spin-up before turning on the intake in milliseconds
     */
    const val SPINUP_MS = 3000L

    /**
     * What orientation do we want to see in the AprilTag
     * TODO: Re-tune before Saturday!!
     */
    val DESIRED_TAG_ORIENTATION = YawPitchRollAngles(AngleUnit.DEGREES, 82.5, 86.8, 33.5, 10)

    /**
     * Speed of the intake after spin-up
     */
    const val INTAKE_POWER = 0.5

    /**
     * What amount of error is acceptable?
     */
    const val ERROR_EPSILON = 0.5

    /**
     * The power of the base motors when correcting error
     */
    const val ERROR_CORRECTION_SPEED = 0.25

    /**
     * The proportional constant of the PID, a coefficient to the current error
     */
    const val PID_K_P = 0.05
}

//endregion

//region RED

//endregion

//endregion