package org.firstinspires.ftc.teamcode.config.modules.robot

import org.firstinspires.ftc.teamcode.config.modules.IGBSRobotModuleConfiguration
import org.firstinspires.ftc.teamcode.modules.actions.GBSAnalogAction
import org.firstinspires.ftc.teamcode.modules.actions.GBSAnalogBinding
import org.firstinspires.ftc.teamcode.modules.actions.GBSBinaryBinding
import org.firstinspires.ftc.teamcode.modules.actions.GBSModuleActions

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

    /**
     * How much do we need to move the sticks in order to start moving the robot
     */
    val STICK_THRESHOLD: Double

    /**
     * How much do we need to move the sticks in order to start moving the robot in fine adjust mode
     */
    val FINE_ADJUST_STICK_THRESHOLD: Double
}