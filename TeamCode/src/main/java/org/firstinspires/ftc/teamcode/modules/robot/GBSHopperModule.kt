package org.firstinspires.ftc.teamcode.modules.robot

import com.qualcomm.robotcore.hardware.CRServo
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorSimple
import org.firstinspires.ftc.teamcode.exceptions.GBSHardwareMissingException
import org.firstinspires.ftc.teamcode.modules.GBSModuleContext
import org.firstinspires.ftc.teamcode.modules.GBSRobotModule
import kotlin.math.max

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
//            servo.power = 0.0
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
//        servo.power = 0.0
        context.telemetry.addLine("[STDN]: GBSHopperModule shutdown.")
        context.telemetry.update()
        return Result.success(Unit)
    }

    private fun handleIdleState(): Result<Unit> {
        val gamepad2 = context.gamepads.gamepad2
        coreHex.power = 0.0
//        servo.power = 0.0
        context.telemetry.addLine("[HOPPER]: Idle")
        context.telemetry.update()

        // Transition to FIRING (Bumpers) or MANUAL (Triggers) states
        return when {
            // New MANUAL input: Check if triggers are pressed for manual control
            gamepad2.left_trigger > 0.1 || gamepad2.right_trigger > 0.1 -> {
                state = HopperState.MANUAL
                Result.success(Unit)
            }
            // FIRING input (remains the same): Check for bumpers
            gamepad2.left_bumper || gamepad2.right_bumper -> {
                state = HopperState.FIRING
                Result.success(Unit)
            }
            else -> {
                Result.success(Unit)
            }
        }
    }

    private fun handleManualState(): Result<Unit> {
        val gamepad2 = context.gamepads.gamepad2
        val leftTrigger = gamepad2.left_trigger
        val rightTrigger = gamepad2.right_trigger

        return when {
            gamepad2.left_bumper || gamepad2.right_bumper -> {
                state = HopperState.FIRING
                Result.success(Unit)
            }
            gamepad2.dpad_right || gamepad2.dpad_left -> {
                state = HopperState.FIRING
                Result.success(Unit)
            }
            leftTrigger <= 0.1 && rightTrigger <= 0.1 && !gamepad2.left_bumper && !gamepad2.right_bumper -> {
                state = HopperState.IDLE
                Result.success(Unit)
            }
            else -> {
                Result.success(Unit)
            }
        }
    }

    private fun handleFiringState(): Result<Unit> {
        val gamepad = context.gamepads.gamepad2
        val isFiringCommandActive = gamepad.left_bumper || gamepad.right_bumper

        return if (isFiringCommandActive) {
            coreHex.power = -1.0
            context.telemetry.addLine("Servo -1")
//            servo.power = -1.0
            context.telemetry.update()
            Result.success(Unit)
        } else {
            coreHex.power = 0.0
            if (gamepad.b) {
//                servo.power = -1.0
            } else if (gamepad.x) {
//                servo.power = 1.0
            }
            state = HopperState.IDLE
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