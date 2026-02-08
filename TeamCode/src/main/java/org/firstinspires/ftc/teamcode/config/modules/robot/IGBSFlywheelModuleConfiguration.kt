package org.firstinspires.ftc.teamcode.config.modules.robot

import org.firstinspires.ftc.teamcode.config.modules.IGBSRobotModuleConfiguration

@Suppress("PropertyName")
interface IGBSFlywheelModuleConfiguration : IGBSRobotModuleConfiguration {
    /**
     * The velocity of the motor in TeleOp
     */
    val TELEOP_VELOCITY: Double

    /**
     * The velocity of the motor in TeleOp with slow mode
     */
    val TELEOP_SLOW_VELOCITY: Double

    /**
     * The error in velocity allowed to rumble the gamepads
     */
    val RUMBLE_ERROR_EPSILON: Double
}