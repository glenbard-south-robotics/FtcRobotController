package org.firstinspires.ftc.teamcode.config.modules.robot

import org.firstinspires.ftc.teamcode.config.modules.IGBSRobotModuleConfiguration

@Suppress("PropertyName")
interface IGBSIntakeModuleConfiguration : IGBSRobotModuleConfiguration {
    /**
     * The power coefficient for the intake
     */
    val POWER: Double
    /**
     * The multiplier for forward mode
     */
    val FORWARD_COEFFICIENT: Double
    /**
     * The multiplier for reverse mode
     */
    val REVERSE_COEFFICIENT: Double
    /**
     * The multiplier for slow mode
     */
    val SLOW_MODE_COEFFICIENT: Double
}