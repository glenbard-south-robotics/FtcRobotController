package org.firstinspires.ftc.teamcode.modules.robot

import com.qualcomm.robotcore.hardware.CRServo
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorSimple
import org.firstinspires.ftc.teamcode.exceptions.GBSHardwareMissingException
import org.firstinspires.ftc.teamcode.modules.GBSModuleContext
import org.firstinspires.ftc.teamcode.modules.GBSRobotModule

private enum class HopperState {
    IDLE,
    MANUAL,
    FIRING
}

class GBSHopperModule(context: GBSModuleContext) : GBSRobotModule(context) {

    private lateinit var coreHex: DcMotor
    private lateinit var servo: CRServo
    private var state: HopperState = HopperState.IDLE

    override fun initialize(): Result<Unit> {
        return try {
            val coreHexMotor = context.hardwareMap.tryGet(DcMotor::class.java, "coreHex")
                ?: throw GBSHardwareMissingException("coreHex")
            val servo = context.hardwareMap.tryGet(CRServo::class.java, "servo")
                ?: throw GBSHardwareMissingException("servo")
            this.coreHex = coreHexMotor
            this.servo = servo
            coreHex.direction = DcMotorSimple.Direction.REVERSE
            servo.power = 0.0
            context.telemetry.addLine("[INIT]: GBSHopperModule initialized.")
            context.telemetry.update()
            Result.success(Unit)
        } catch (e: Exception) {
            context.telemetry.addLine("[ERR]: An exception was raised in GBSHopperModule::init: ${e.message}")
            context.telemetry.update()
            Result.failure(e)
        }
    }

    override fun run(): Result<Unit> {
        return when (state) {
            HopperState.IDLE -> handleIdleState()
            HopperState.MANUAL -> handleManualState()
            HopperState.FIRING -> handleFiringState()
        }
    }

    override fun shutdown(): Result<Unit> {
        coreHex.power = 0.0
        servo.power = 0.0
        context.telemetry.addLine("[STDN]: GBSHopperModule shutdown.")
        context.telemetry.update()
        return Result.success(Unit)
    }

    private fun handleIdleState(): Result<Unit> {
        val gamepad1 = context.gamepads.gamepad1
        coreHex.power = 0.0
        servo.power = 0.0
        context.telemetry.addLine("[HOPPER]: Idle")
        context.telemetry.update()

        // Transition to FIRING or MANUAL states
        return when {
            gamepad1.left_bumper || gamepad1.right_bumper -> {
                state = HopperState.FIRING
                Result.success(Unit)
            }
            gamepad1.dpad_left || gamepad1.dpad_right -> {
                state = HopperState.MANUAL
                Result.success(Unit)
            }
            else -> {
                Result.success(Unit)
            }
        }
    }

    private fun handleManualState(): Result<Unit> {
        val gamepad1 = context.gamepads.gamepad1
        return when {
            gamepad1.dpad_left -> {
                servo.power = 1.0
                coreHex.power = 0.0
                context.telemetry.addLine("[HOPPER]: Manual servo control (Dpad Left)")
                context.telemetry.update()
                Result.success(Unit)
            }
            gamepad1.dpad_right -> {
                servo.power = -1.0
                coreHex.power = 0.0
                context.telemetry.addLine("[HOPPER]: Manual servo control (Dpad Right)")
                context.telemetry.update()
                Result.success(Unit)
            }
            // Transition back to IDLE when dpad is released
            !gamepad1.dpad_left && !gamepad1.dpad_right -> {
                state = HopperState.IDLE
                Result.success(Unit)
            }
            else -> {
                Result.success(Unit)
            }
        }
    }

    private fun handleFiringState(): Result<Unit> {
        val gamepad = context.gamepads.gamepad1
        val isFiringCommandActive = gamepad.left_bumper || gamepad.right_bumper

        return if (isFiringCommandActive) {
            coreHex.power = 1.0
            servo.power = -1.0
            context.telemetry.addLine("[HOPPER]: Firing...")
            context.telemetry.update()
            Result.success(Unit)
        } else {
            coreHex.power = 0.0
            servo.power = 0.0
            state = HopperState.IDLE
            context.telemetry.addLine("[HOPPER]: Firing complete. Transitioning to IDLE.")
            context.telemetry.update()
            Result.success(Unit)
        }
    }

    fun fire(): Result<Unit> {
        if (state != HopperState.IDLE) {
            return Result.failure(IllegalStateException("Cannot start firing while not in IDLE state."))
        }
        state = HopperState.FIRING
        return Result.success(Unit)
    }
}