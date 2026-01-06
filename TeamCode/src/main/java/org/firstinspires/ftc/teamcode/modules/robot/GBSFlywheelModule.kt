package org.firstinspires.ftc.teamcode.modules.robot

import android.os.Build
import androidx.annotation.RequiresApi
import com.qualcomm.robotcore.hardware.DcMotorEx
import org.firstinspires.ftc.teamcode.GBSFlywheelModuleConfiguration
import org.firstinspires.ftc.teamcode.exceptions.GBSHardwareMissingException
import org.firstinspires.ftc.teamcode.modules.GBSModuleContext
import org.firstinspires.ftc.teamcode.modules.GBSRobotModule

import kotlin.math.abs

enum class GBSFlywheelModuleState {
    IDLE,
    FORWARD,
    AUTO,
}

class GBSFlywheelModule(context: GBSModuleContext, hardware: String = "flywheel") : GBSRobotModule(context, hardware) {
    private var state: GBSFlywheelModuleState = GBSFlywheelModuleState.IDLE
    private var debounce: Long = 0

    private lateinit var flywheelMotor: DcMotorEx

    override fun initialize(): Result<Unit> {
        return try {
            val flywheel = context.hardwareMap.tryGet(DcMotorEx::class.java, hardware)
                ?: throw GBSHardwareMissingException(hardware)

            flywheelMotor = flywheel
            debounce = System.currentTimeMillis()

            context.telemetry.addLine("[INIT]: GBSFlywheelModule initialized.")
            context.telemetry.update()
            Result.success(Unit)
        } catch (e: Exception) {
            context.telemetry.addLine("[ERR] An exception was raised in GBSFlywheelModule::init: ${e.message}")
            context.telemetry.update()
            Result.failure(e)
        }
    }

    override fun run(): Result<Unit> {
        return when (state) {
            GBSFlywheelModuleState.IDLE -> handleIdleState()
            GBSFlywheelModuleState.AUTO -> handleAutoState()
            else -> handleRunningState()
        }
    }

    override fun shutdown(): Result<Unit> {
        setMotorPower(0.0)
        context.telemetry.addLine("[STDN]: GBSFlywheelModule shutdown.")
        context.telemetry.update()
        return Result.success(Unit)
    }

    private fun handleAutoState(): Result<Unit> {
        val config = GBSFlywheelModuleConfiguration()
        val power = -config.AUTO_FORWARD_TPS

        setMotorPower(power)
        return Result.success(Unit)
    }

    private fun handleIdleState(): Result<Unit> {
        val gamepad2 = context.gamepads.gamepad2

        if (gamepad2.triangleWasPressed()) {
            state = GBSFlywheelModuleState.FORWARD
            return Result.success(Unit)
        }

        setMotorPower(0.0)
        return Result.success(Unit)
    }

    private fun handleRunningState(): Result<Unit> {
        val config = GBSFlywheelModuleConfiguration()
        val gamepad2 = context.gamepads.gamepad2
        val power = -config.FORWARD_TPS

        if (gamepad2.triangleWasPressed()) {
            state = GBSFlywheelModuleState.IDLE
            return Result.success(Unit)
        }

        val EPSILON: Double = 10.0
        context.telemetry.addLine("$power")
        context.telemetry.addLine("${getMotorPower()}")
        context.telemetry.addLine("${abs(power - getMotorPower())}, $EPSILON")
        val now = System.currentTimeMillis()
        if (abs(power - getMotorPower()) <= EPSILON && (now - debounce) >= 5000) {
            context.gamepads.gamepad2.rumble(1000)
            context.gamepads.gamepad1.rumble(1000)
            debounce = System.currentTimeMillis()
        }

        setMotorPower(power)
        return Result.success(Unit)
    }

    fun autoFlywheelOn(): Result<Unit> {
        state = GBSFlywheelModuleState.AUTO
        return Result.success(Unit)
    }

    fun autoFlywheelOff(): Result<Unit> {
        state = GBSFlywheelModuleState.IDLE
        return Result.success(Unit)
    }

    fun setMotorPower(power: Double): Result<Unit> {
        flywheelMotor.velocity = power.toDouble()
        return Result.success(Unit)
    }

    fun getMotorPower(): Double {
        return flywheelMotor.velocity.toDouble()
    }
}
