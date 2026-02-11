package org.firstinspires.ftc.teamcode.config

import org.firstinspires.ftc.teamcode.GBSMotorDirection
import org.firstinspires.ftc.teamcode.config.modules.robot.IGBSBaseModuleConfiguration
import org.firstinspires.ftc.teamcode.config.modules.robot.IGBSFlywheelModuleConfiguration
import org.firstinspires.ftc.teamcode.config.modules.robot.IGBSIntakeModuleConfiguration
import org.firstinspires.ftc.teamcode.config.modules.robot.IGBSWebcamModuleConfiguration
import org.firstinspires.ftc.teamcode.modules.actions.GBSAnalogAction
import org.firstinspires.ftc.teamcode.modules.actions.GBSAnalogBinding
import org.firstinspires.ftc.teamcode.modules.actions.GBSBinaryAction
import org.firstinspires.ftc.teamcode.modules.actions.GBSBinaryBinding
import org.firstinspires.ftc.teamcode.modules.actions.GBSGamepadID
import org.firstinspires.ftc.teamcode.modules.actions.GBSModuleActions

object GBSBaseModuleConfiguration : IGBSBaseModuleConfiguration {
    override val DEBUG_TELEMETRY = true

    override val LEFT_MOTOR_DIRECTION: GBSMotorDirection = GBSMotorDirection.FORWARD
    override val RIGHT_MOTOR_DIRECTION: GBSMotorDirection = GBSMotorDirection.REVERSE

    override val BASE_POWER_COEFFICIENT = 0.75
    override val FINE_ADJUST_POWER_COEFFICIENT = 0.33

    override val BINARY_BINDINGS = mapOf(
        GBSModuleActions.BASE_SLOW_TOGGLE to GBSBinaryBinding(
            GBSGamepadID.GAMEPAD_ONE, GBSBinaryAction.CROSS
        )
    )
    override val ANALOG_BINDINGS = mapOf(
        GBSAnalogAction.LEFT_STICK_X to GBSAnalogBinding(
            GBSGamepadID.GAMEPAD_ONE, GBSAnalogAction.LEFT_STICK_X, deadZone = 0.2F
        ),
        GBSAnalogAction.LEFT_STICK_Y to GBSAnalogBinding(
            GBSGamepadID.GAMEPAD_ONE, GBSAnalogAction.LEFT_STICK_Y, deadZone = 0.2F
        ),
        GBSAnalogAction.RIGHT_STICK_X to GBSAnalogBinding(
            GBSGamepadID.GAMEPAD_ONE, GBSAnalogAction.RIGHT_STICK_X, deadZone = 0.2F
        ),
        GBSAnalogAction.RIGHT_STICK_Y to GBSAnalogBinding(
            GBSGamepadID.GAMEPAD_ONE, GBSAnalogAction.RIGHT_STICK_Y, deadZone = 0.2F
        ),
    )
}

object GBSIntakeModuleConfiguration : IGBSIntakeModuleConfiguration {
    override val DEBUG_TELEMETRY = true

    override val MOTOR_DIRECTION: GBSMotorDirection = GBSMotorDirection.FORWARD

    override val FORWARD_POWER = 0.675
    override val REVERSE_POWER = 0.5
    override val SLOW_MODE_COEFFICIENT = 0.75

    override val BINARY_BINDINGS: Map<GBSModuleActions, GBSBinaryBinding> = mapOf(
        GBSModuleActions.INTAKE_FORWARD to GBSBinaryBinding(
            GBSGamepadID.GAMEPAD_TWO,
            GBSBinaryAction.LEFT_BUMPER
        ),
        GBSModuleActions.INTAKE_REVERSE to GBSBinaryBinding(
            GBSGamepadID.GAMEPAD_TWO,
            GBSBinaryAction.RIGHT_BUMPER
        ),
        GBSModuleActions.INTAKE_SLOW_TOGGLE to GBSBinaryBinding(
            GBSGamepadID.GAMEPAD_TWO,
            GBSBinaryAction.CROSS
        )
    )
    override val ANALOG_BINDINGS: Map<GBSAnalogAction, GBSAnalogBinding> = mapOf()
}

object GBSFlywheelModuleConfiguration : IGBSFlywheelModuleConfiguration {
    override val DEBUG_TELEMETRY = true

    override val MOTOR_DIRECTION: GBSMotorDirection = GBSMotorDirection.REVERSE

    override val TELEOP_VELOCITY = 2500.0
    override val TELEOP_SLOW_VELOCITY = 2000.0
    override val RUMBLE_ERROR_EPSILON = 10.0

    override val BINARY_BINDINGS: Map<GBSModuleActions, GBSBinaryBinding> = mapOf(
        GBSModuleActions.FLYWHEEL_TOGGLE to GBSBinaryBinding(
            GBSGamepadID.GAMEPAD_TWO, GBSBinaryAction.TRIANGLE
        ), GBSModuleActions.FLYWHEEL_TOGGLE_SLOW_MODE to GBSBinaryBinding(
            GBSGamepadID.GAMEPAD_TWO, GBSBinaryAction.SQUARE
        )
    )
    override val ANALOG_BINDINGS: Map<GBSAnalogAction, GBSAnalogBinding> = mapOf()
}

object GBSWebcamModuleConfig : IGBSWebcamModuleConfiguration {
    override val DEBUG_TELEMETRY = true

    override val DECIMATION: Float = 3.0F

    override val BINARY_BINDINGS: Map<GBSModuleActions, GBSBinaryBinding> = mapOf()
    override val ANALOG_BINDINGS: Map<GBSAnalogAction, GBSAnalogBinding> = mapOf()
}