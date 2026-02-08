package org.firstinspires.ftc.teamcode.config.modules.robot

import org.firstinspires.ftc.teamcode.config.modules.IGBSRobotModuleConfiguration

@Suppress("PropertyName")
interface IGBSBaseModuleConfiguration : IGBSRobotModuleConfiguration {
    /**
     * What should we power given by the sticks in TeleOp by?
     */
    val BASE_POWER_COEFFICIENT: Double

    /**
     * What should we power given by the sticks in TeleOp by in slow mode?
     */
    val FINE_ADJUST_POWER_COEFFICIENT: Double
}