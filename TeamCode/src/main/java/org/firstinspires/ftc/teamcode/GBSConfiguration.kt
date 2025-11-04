package org.firstinspires.ftc.teamcode

const val CYCLE_MS: Long = 20L

@Suppress("PropertyName")
class GBSBaseModuleConfiguration {
    val BASE_POWER = 0.75
    val FINE_ADJUST_POWER_COEFFICIENT = 0.33
    val STICK_THRESHOLD = 0.2
    val FINE_ADJUST_STICK_THRESHOLD = 0.05
}

@Suppress("PropertyName")
class GBSFlywheelModuleConfiguration {
    val TRIGGER_THRESHOLD = 0.2
    var BRAKE_TRIGGER_COEFFICIENT = 0.5
}

