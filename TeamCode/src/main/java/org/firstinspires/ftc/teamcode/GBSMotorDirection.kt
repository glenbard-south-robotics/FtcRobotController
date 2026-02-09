package org.firstinspires.ftc.teamcode

enum class GBSMotorDirection {
    FORWARD, REVERSE
}

fun GBSMotorDirection.getCoefficient(): Double {
    return when (this) {
        GBSMotorDirection.FORWARD -> 1.0
        GBSMotorDirection.REVERSE -> -1.0
    }
}