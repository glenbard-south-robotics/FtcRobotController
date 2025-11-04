package org.firstinspires.ftc.teamcode.modules.robot

import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.DcMotorSimple
import com.qualcomm.robotcore.util.ElapsedTime
import org.firstinspires.ftc.teamcode.GBSFlywheelModuleConfiguration
import org.firstinspires.ftc.teamcode.exceptions.GBSHardwareMissingException
import org.firstinspires.ftc.teamcode.modules.GBSModuleContext
import org.firstinspires.ftc.teamcode.modules.GBSRobotModule
import kotlin.math.max

private enum class FlywheelState {
    OFF,
    MANUAL,
    BRAKING,
    AUTO_LAUNCH
}

private const val BANK_VELOCITY = 1300.0
private const val FAR_VELOCITY = 1900.0
private const val MAX_VELOCITY = 3200.0

class GBSFlywheelModule(context: GBSModuleContext) : GBSRobotModule(context) {
    private lateinit var flywheel: DcMotorEx
    private var state: FlywheelState = FlywheelState.OFF
    private val autoLaunchTimer = ElapsedTime()
    private var autoTargetVelocity: Double = 0.0
    private var autoTimeoutMs: Int = 0

    override fun initialize(): Result<Unit> {
        return try {
            val flywheel = context.hardwareMap.tryGet(DcMotorEx::class.java, "flywheel")
                ?: throw GBSHardwareMissingException("flywheel")
            this.flywheel = flywheel
            flywheel.mode = DcMotor.RunMode.RUN_USING_ENCODER
            flywheel.direction = DcMotorSimple.Direction.REVERSE
            Result.success(Unit)
        } catch (e: Exception) {
            context.telemetry.addLine("[ERR]: An exception was raised in GBSFlywheelModule::init: ${e.message}")
            context.telemetry.update()
            Result.failure(e)
        }
    }

    override fun run(): Result<Unit> {
        return when (state) {
            FlywheelState.OFF -> handleOffState()
            FlywheelState.MANUAL -> handleManualState()
            FlywheelState.BRAKING -> handleBrakingState()
            FlywheelState.AUTO_LAUNCH -> handleAutoLaunchState()
        }
    }

    override fun shutdown(): Result<Unit> {
        flywheel.power = 0.0
        context.telemetry.addLine("[STDN]: GBSFlywheelModule shutdown.")
        context.telemetry.update()
        return super.shutdown()
    }

    private fun handleOffState(): Result<Unit> {
        val gamepad = context.gamepads.gamepad2
        val config = GBSFlywheelModuleConfiguration()
        val triggerPower = max(gamepad.right_trigger, gamepad.left_trigger)

        return when {
            triggerPower >= config.TRIGGER_THRESHOLD -> {
                flywheel.power = -1.0 * config.BRAKE_TRIGGER_COEFFICIENT
                state = FlywheelState.BRAKING
                context.telemetry.addLine("[FLYWHEEL]: Transitioning to BRAKING state.")
                context.telemetry.update()
                Result.success(Unit)
            }
            gamepad.dpad_up || gamepad.dpad_down || gamepad.dpad_left -> {
                state = FlywheelState.MANUAL
                context.telemetry.addLine("[FLYWHEEL]: Transitioning to MANUAL state.")
                context.telemetry.update()
                Result.success(Unit)
            }
            else -> {
                flywheel.velocity = 0.0
                context.telemetry.addData("[FLYWHEEL]: Current Velocity", flywheel.velocity)
                context.telemetry.addData("[FLYWHEEL]: State", state)
                context.telemetry.update()
                Result.success(Unit)
            }
        }
    }

    private fun handleManualState(): Result<Unit> {
        val gamepad = context.gamepads.gamepad2
        val config = GBSFlywheelModuleConfiguration()
        val triggerPower = max(gamepad.right_trigger, gamepad.left_trigger)

        return when {
            triggerPower >= config.TRIGGER_THRESHOLD -> {
                flywheel.power = -1.0 * config.BRAKE_TRIGGER_COEFFICIENT
                state = FlywheelState.BRAKING
                context.telemetry.addLine("[FLYWHEEL]: Transitioning to BRAKING state.")
                context.telemetry.update()
                Result.success(Unit)
            }
            // Transition back to OFF if no dpad buttons are pressed
            !gamepad.dpad_up && !gamepad.dpad_down && !gamepad.dpad_left -> {
                state = FlywheelState.OFF
                context.telemetry.addLine("[FLYWHEEL]: No dpad pressed. Transitioning to OFF state.")
                context.telemetry.update()
                Result.success(Unit)
            }
            gamepad.dpad_up -> {
                flywheel.velocity = MAX_VELOCITY
                context.telemetry.addData("[FLYWHEEL]: Target Velocity", MAX_VELOCITY)
                context.telemetry.addData("[FLYWHEEL]: State", state)
                context.telemetry.update()
                Result.success(Unit)
            }
            gamepad.dpad_down -> {
                flywheel.velocity = BANK_VELOCITY
                context.telemetry.addData("[FLYWHEEL]: Target Velocity", BANK_VELOCITY)
                context.telemetry.addData("[FLYWHEEL]: State", state)
                context.telemetry.update()
                Result.success(Unit)
            }
            gamepad.dpad_left -> {
                flywheel.velocity = FAR_VELOCITY
                context.telemetry.addData("[FLYWHEEL]: Target Velocity", FAR_VELOCITY)
                context.telemetry.addData("[FLYWHEEL]: State", state)
                context.telemetry.update()
                Result.success(Unit)
            }
            else -> {
                // This case should not be reached due to the previous checks
                Result.success(Unit)
            }
        }
    }

    private fun handleBrakingState(): Result<Unit> {
        val gamepad = context.gamepads.gamepad2
        val config = GBSFlywheelModuleConfiguration()
        val triggerPower = max(gamepad.right_trigger, gamepad.left_trigger)

        return if (triggerPower < config.TRIGGER_THRESHOLD) {
            flywheel.power = 0.0
            state = FlywheelState.OFF
            context.telemetry.addLine("[FLYWHEEL]: Trigger released. Transitioning to OFF state.")
            context.telemetry.update()
            Result.success(Unit)
        } else {
            flywheel.power = -1.0 * config.BRAKE_TRIGGER_COEFFICIENT
            context.telemetry.addLine("[FLYWHEEL]: Braking...")
            context.telemetry.update()
            Result.success(Unit)
        }
    }

    private fun handleAutoLaunchState(): Result<Unit> {
        val launchComplete =
            (flywheel.velocity >= autoTargetVelocity) || (autoLaunchTimer.milliseconds() >= autoTimeoutMs)
        if (launchComplete) {
            // TODO: Launch a projectile using the hopper module
            flywheel.velocity = 0.0
            state = FlywheelState.OFF
            context.telemetry.addLine("[FLYWHEEL]: Auto-launch complete. Transitioning to OFF state.")
            context.telemetry.update()
            return Result.success(Unit)
        }
        context.telemetry.addData("[FLYWHEEL]: Auto-launching...", autoTargetVelocity)
        context.telemetry.addData("[FLYWHEEL]: Current Velocity", flywheel.velocity)
        context.telemetry.addData("[FLYWHEEL]: State", state)
        context.telemetry.update()
        return Result.success(Unit)
    }

    fun autoLaunch(targetVelocity: Double, timeoutMs: Int = 5000): Result<Boolean> {
        if (state != FlywheelState.OFF) {
            return Result.failure(IllegalStateException("Cannot start auto-launch while not in OFF state."))
        }
        try {
            autoLaunchTimer.reset()
            autoTargetVelocity = targetVelocity
            autoTimeoutMs = timeoutMs
            state = FlywheelState.AUTO_LAUNCH
            flywheel.velocity = targetVelocity
            return Result.success(false)
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }
}