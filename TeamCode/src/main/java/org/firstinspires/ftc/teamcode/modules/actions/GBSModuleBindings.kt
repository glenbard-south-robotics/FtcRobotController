package org.firstinspires.ftc.teamcode.modules.actions

enum class GBSGamepadID { GAMEPAD_ONE, GAMEPAD_TWO }

enum class GBSBinaryAction {
    CROSS, CIRCLE, TRIANGLE, SQUARE, DPAD_UP, DPAD_DOWN, DPAD_LEFT, DPAD_RIGHT, LEFT_BUMPER, RIGHT_BUMPER, LEFT_STICK_BUTTON, RIGHT_STICK_BUTTON
}

enum class GBSAnalogAction {
    LEFT_STICK_X, LEFT_STICK_Y, RIGHT_STICK_X, RIGHT_STICK_Y, LEFT_TRIGGER, RIGHT_TRIGGER
}

data class GBSBinaryBinding(val gamepad: GBSGamepadID, val button: GBSBinaryAction)
data class GBSAnalogBinding(
    val gamepad: GBSGamepadID,
    val analogAction: GBSAnalogAction,
    val deadZone: Float = 0f,
    val scale: Float = 1f
)

