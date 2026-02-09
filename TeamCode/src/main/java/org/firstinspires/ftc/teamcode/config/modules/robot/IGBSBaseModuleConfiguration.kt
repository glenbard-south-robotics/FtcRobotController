package org.firstinspires.ftc.teamcode.config.modules.robot

import org.firstinspires.ftc.teamcode.config.modules.IGBSRobotModuleConfiguration
import org.firstinspires.ftc.teamcode.GBSMotorDirection

@Suppress("PropertyName")
interface IGBSBaseModuleConfiguration : IGBSRobotModuleConfiguration {
    /**
     * The direction of the leftDrive motor
     */
    val LEFT_MOTOR_DIRECTION: GBSMotorDirection

    /**
     * The direction of the rightDrive motor
     */
    val RIGHT_MOTOR_DIRECTION: GBSMotorDirection

    /**
     * What should we power given by the sticks in TeleOp by?
     */
    val BASE_POWER_COEFFICIENT: Double

    /**
     * What should we power given by the sticks in TeleOp by in slow mode?
     */
    val FINE_ADJUST_POWER_COEFFICIENT: Double
}