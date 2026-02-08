package org.firstinspires.ftc.teamcode.modules.actions

import org.firstinspires.ftc.teamcode.GBSGamepadPair
import org.firstinspires.ftc.teamcode.config.modules.IGBSRobotModuleConfiguration

data class GBSInputManager(private val gamepadPair: GBSGamepadPair) {
    /**
     * Get the gamepad associated with this GBSGamepadID
     * @see GBSGamepadID
     */
    private fun gamepadFor(id: GBSGamepadID) = when (id) {
        GBSGamepadID.GAMEPAD_ONE -> gamepadPair.gamepad1
        GBSGamepadID.GAMEPAD_TWO -> gamepadPair.gamepad2
    }

    /**
     * Was the binding pressed since the last call of this function?
     */
    fun binaryWasPressed(binding: GBSBinaryBinding): Boolean {
        val gamepad = gamepadFor(binding.gamepad)
        return when (binding.button) {
            GBSBinaryAction.CROSS -> gamepad.crossWasPressed()
            GBSBinaryAction.CIRCLE -> gamepad.circleWasPressed()
            GBSBinaryAction.TRIANGLE -> gamepad.triangleWasPressed()
            GBSBinaryAction.SQUARE -> gamepad.squareWasPressed()
            GBSBinaryAction.DPAD_UP -> gamepad.dpadUpWasPressed()
            GBSBinaryAction.DPAD_DOWN -> gamepad.dpadDownWasPressed()
            GBSBinaryAction.DPAD_LEFT -> gamepad.dpadLeftWasPressed()
            GBSBinaryAction.DPAD_RIGHT -> gamepad.dpadRightWasPressed()
            GBSBinaryAction.LEFT_BUMPER -> gamepad.leftBumperWasPressed()
            GBSBinaryAction.RIGHT_BUMPER -> gamepad.rightBumperWasPressed()
            GBSBinaryAction.LEFT_STICK_BUTTON -> gamepad.leftStickButtonWasPressed()
            GBSBinaryAction.RIGHT_STICK_BUTTON -> gamepad.rightStickButtonWasPressed()
        }
    }

    /**
     * Was the binding released since the last call of this function?
     */
    fun binaryWasReleased(binding: GBSBinaryBinding): Boolean {
        val gamepad = gamepadFor(binding.gamepad)
        return when (binding.button) {
            GBSBinaryAction.CROSS -> gamepad.crossWasReleased()
            GBSBinaryAction.CIRCLE -> gamepad.circleWasReleased()
            GBSBinaryAction.TRIANGLE -> gamepad.triangleWasReleased()
            GBSBinaryAction.SQUARE -> gamepad.squareWasReleased()
            GBSBinaryAction.DPAD_UP -> gamepad.dpadUpWasReleased()
            GBSBinaryAction.DPAD_DOWN -> gamepad.dpadDownWasReleased()
            GBSBinaryAction.DPAD_LEFT -> gamepad.dpadLeftWasReleased()
            GBSBinaryAction.DPAD_RIGHT -> gamepad.dpadRightWasReleased()
            GBSBinaryAction.LEFT_BUMPER -> gamepad.leftBumperWasReleased()
            GBSBinaryAction.RIGHT_BUMPER -> gamepad.rightBumperWasReleased()
            GBSBinaryAction.LEFT_STICK_BUTTON -> gamepad.leftStickButtonWasReleased()
            GBSBinaryAction.RIGHT_STICK_BUTTON -> gamepad.rightStickButtonWasReleased()
        }
    }

    /**
     * Read the value of an analog binding
     */
    fun readAnalog(binding: GBSAnalogBinding): Float {
        val gamepad = gamepadFor(binding.gamepad)
        val raw = when (binding.analogAction) {
            GBSAnalogAction.LEFT_STICK_X -> gamepad.left_stick_x
            GBSAnalogAction.LEFT_STICK_Y -> -gamepad.left_stick_y
            GBSAnalogAction.RIGHT_STICK_X -> gamepad.right_stick_x
            GBSAnalogAction.RIGHT_STICK_Y -> -gamepad.right_stick_y
            GBSAnalogAction.LEFT_TRIGGER -> gamepad.left_trigger
            GBSAnalogAction.RIGHT_TRIGGER -> gamepad.right_trigger
        }
        val v = raw.coerceIn(-1f, 1f)
        return if (kotlin.math.abs(v) < binding.deadZone) 0f else v * binding.scale
    }

    /**
     * Check if the given GBSModuleAction was pressed
     * @return `false` if the binding does not exist
     */
    fun GBSModuleActions.wasPressed(config: IGBSRobotModuleConfiguration): Boolean {
        val binding = config.BINARY_BINDINGS[this] ?: return false
        return binaryWasPressed(binding)
    }

    /**
     * Get the value of the GBSAnalogAction
     * @return `0f` if the binding does not exist
     */
    fun GBSAnalogAction.read(config: IGBSRobotModuleConfiguration): Float {
        val binding = config.ANALOG_BINDINGS[this] ?: return 0f
        return readAnalog(binding)
    }
}
