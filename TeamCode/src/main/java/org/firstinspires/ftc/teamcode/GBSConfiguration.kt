package org.firstinspires.ftc.teamcode

const val CYCLE_MS: Long = 20L

@Suppress("PropertyName")
class GBSBaseModuleConfiguration {
    /**
     * The coefficient to the base's power in `auto` and `manual`
     */
    val BASE_POWER_COEFFICIENT = 0.75
    /**
     * The coefficient to the base's power in `auto` and `manual` while `fineAdjustMode` is active
     */
    val FINE_ADJUST_POWER_COEFFICIENT = 0.25

    /**
     * The value that a gamepad's stick Y needs to exceed for the motor to start
     */
    val STICK_THRESHOLD = 0.2
    /**
     * The value that a gamepad's stick Y needs to exceed for the motor to start in `fineAdjustMode`
     */
    val FINE_ADJUST_STICK_THRESHOLD = 0.05
}

@Suppress("PropertyName")
class GBSIntakeModuleConfiguration {
    val POWER = 1.0
    val FORWARD_COEFFICIENT = 1.0
    val SLOW_MODE_COEFFICIENT = 0.5
    val REVERSE_COEFFICIENT = 0.5

    val LAUNCH_REVERSE_DURATION_MS = 1000L
    val LAUNCH_FORWARD_DURATION_MS = 1000L
    val LAUNCH_FORWARD_COEFFICIENT = 1.0
}

@Suppress("PropertyName")
class GBSFlywheelModuleConfiguration {
    val FORWARD_TPS = 19600.0 * 0.7
}