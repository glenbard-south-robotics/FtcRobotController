package org.firstinspires.ftc.teamcode.config

import org.firstinspires.ftc.teamcode.config.modules.IGBSRobotModuleConfiguration
import org.firstinspires.ftc.teamcode.config.modules.robot.IGBSBaseModuleConfiguration
import org.firstinspires.ftc.teamcode.config.modules.robot.IGBSIntakeModuleConfiguration

object GBSBaseModuleConfiguration : IGBSBaseModuleConfiguration {
    override val DEBUG_TELEMETRY = true

    override val BASE_POWER_COEFFICIENT = 0.75
    override val FINE_ADJUST_POWER_COEFFICIENT = 0.33

    override val STICK_THRESHOLD = 0.2
    override val FINE_ADJUST_STICK_THRESHOLD = 0.05
}

object GBSIntakeModuleConfiguration : IGBSIntakeModuleConfiguration {
    override val DEBUG_TELEMETRY = true

    override val POWER = 1.0
    override val FORWARD_COEFFICIENT = 1.0
    override val SLOW_MODE_COEFFICIENT = 0.75
    override val REVERSE_COEFFICIENT = 0.5
}

object GBSFlywheelModuleConfiguration : IGBSRobotModuleConfiguration {
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

object GBSWebcamModuleConfig : IGBSRobotModuleConfiguration {
    override val DEBUG_TELEMETRY = true
}