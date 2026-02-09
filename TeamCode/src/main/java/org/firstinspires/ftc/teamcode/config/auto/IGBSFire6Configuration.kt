package org.firstinspires.ftc.teamcode.config.auto

import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles

/**
 * The configuration for a 3-artifact firing autonomous
 */
@Suppress("PropertyName")
interface IGBSFire6Configuration : IGBSFire3Configuration {
    val SPIKE_MOTOR_DISTANCES: Pair<Int, Int>

    val SPIKE_FORWARD_DISTANCE: Int

    val SPIKE_FORWARD_SPEED: Double
}