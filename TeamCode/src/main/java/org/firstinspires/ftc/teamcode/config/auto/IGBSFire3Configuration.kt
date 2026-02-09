package org.firstinspires.ftc.teamcode.config.auto

import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles

/**
 * The configuration for a 3-artifact firing autonomous
 */
@Suppress("PropertyName")
interface IGBSFire3Configuration {
    /**
     * Distance to drive before stopping in inches
     * The first number is the left motor
     * The second number is the right motor
     */
    val MOTOR_DISTANCES: Pair<Int, Int>

    /**
     * The speed of the flywheel in ticks / second
     */
    val FLYWHEEL_VELOCITY: Double
    /**
     * How long should we wait to let the flywheel spin-up before turning on the intake in milliseconds
     */
    val SPINUP_MS: Long

    /**
     * The speed we should drive at
     */
    val BASE_POWER: Double

    /**
     * What orientation do we want to see in the AprilTag
     */
    val DESIRED_TAG_ORIENTATION: YawPitchRollAngles
    /**
     * What amount of error is acceptable?
     */
    val ERROR_EPSILON: Double
    /**
     * The power of the base motors when correcting error
     */
    val ERROR_CORRECTION_SPEED: Double
    /**
     * The proportional constant of the PID, a coefficient to the current error
     */
    val PID_K_P: Double
}