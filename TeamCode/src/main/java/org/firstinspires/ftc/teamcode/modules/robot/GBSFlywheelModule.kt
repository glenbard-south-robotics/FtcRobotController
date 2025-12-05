package org.firstinspires.ftc.teamcode.modules.robot

import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorEx
import org.firstinspires.ftc.teamcode.GBSFlywheelModuleConfiguration
import org.firstinspires.ftc.teamcode.exceptions.GBSHardwareMissingException
import org.firstinspires.ftc.teamcode.modules.GBSModuleContext
import org.firstinspires.ftc.teamcode.modules.GBSRobotModule

enum class GBSFlywheelModuleState {
    IDLE,
    FORWARD,
}

class GBSFlywheelModule(context: GBSModuleContext) : GBSRobotModule(context) {
    private var state: GBSFlywheelModuleState = GBSFlywheelModuleState.IDLE

    private lateinit var flywheelMotor: DcMotorEx

    override fun initialize(): Result<Unit> {
        return try {
            val flywheel = context.hardwareMap.tryGet(DcMotorEx::class.java, "flywheel")
                ?: throw GBSHardwareMissingException("flywheel")

            flywheelMotor = flywheel

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
            else -> handleRunningState()
        }
    }

    override fun shutdown(): Result<Unit> {
        setMotorPower(0)
        context.telemetry.addLine("[STDN]: GBSFlywheelModule shutdown.")
        context.telemetry.update()
        return Result.success(Unit)
    }

    private fun handleIdleState(): Result<Unit> {
        val gamepad2 = context.gamepads.gamepad2

        if (gamepad2.triangleWasPressed()) {
            if (state == GBSFlywheelModuleState.FORWARD) {
                state = GBSFlywheelModuleState.IDLE
                return Result.success(Unit)
            }

            // IDLE, goto FORWARD
            state = GBSFlywheelModuleState.FORWARD
            setMotorPower(0)
        }

        setMotorPower(0)

        return Result.success(Unit)
    }

    private fun handleRunningState(): Result<Unit> {
        val config = GBSFlywheelModuleConfiguration()
        val gamepad2 = context.gamepads.gamepad2

        val power = config.FORWARD_TPS

        if (gamepad2.triangleWasPressed()) {
            if (state == GBSFlywheelModuleState.FORWARD) {
                state = GBSFlywheelModuleState.IDLE
                return Result.success(Unit)
            }

            // If IDLE, goto FORWARD
            state = GBSFlywheelModuleState.FORWARD
            setMotorPower(0)
        }

        setMotorPower(power)

        return Result.success(Unit)
    }

    fun setMotorPower(power: Int): Result<Unit> {
        context.telemetry.addLine("${power}")
        context.telemetry.update()
        flywheelMotor.velocity = power.toDouble()

        return Result.success(Unit)
    }
}