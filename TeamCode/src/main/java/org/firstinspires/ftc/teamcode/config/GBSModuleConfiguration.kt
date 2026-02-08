package org.firstinspires.ftc.teamcode.config

import org.firstinspires.ftc.teamcode.config.modules.robot.IGBSBaseModuleConfiguration
import org.firstinspires.ftc.teamcode.config.modules.robot.IGBSFlywheelModuleConfiguration
import org.firstinspires.ftc.teamcode.config.modules.robot.IGBSIntakeModuleConfiguration
import org.firstinspires.ftc.teamcode.config.modules.robot.IGBSWebcamModuleConfiguration

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

object GBSFlywheelModuleConfiguration : IGBSFlywheelModuleConfiguration {
    override val DEBUG_TELEMETRY = true

    override val TELEOP_VELOCITY = 2500.0
    override val TELEOP_SLOW_VELOCITY = 2000.0
    override val RUMBLE_ERROR_EPSILON = 10.0
}

object GBSWebcamModuleConfig : IGBSWebcamModuleConfiguration {
    override val DEBUG_TELEMETRY = true

    override val DECIMATION: Float = 3.0F
}