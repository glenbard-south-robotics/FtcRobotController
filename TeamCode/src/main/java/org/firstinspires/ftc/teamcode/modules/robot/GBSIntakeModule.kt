package org.firstinspires.ftc.teamcode.modules.robot

import com.qualcomm.robotcore.hardware.DcMotor
import org.firstinspires.ftc.teamcode.GBSIntakeModuleConfiguration
import org.firstinspires.ftc.teamcode.exceptions.GBSHardwareMissingException
import org.firstinspires.ftc.teamcode.modules.GBSModuleContext
import org.firstinspires.ftc.teamcode.modules.GBSRobotModule

enum class GBSIntakeModuleState {
    IDLE,
    FORWARD,
    REVERSE,
    LAUNCHING
}

class GBSIntakeModule(context: GBSModuleContext) : GBSRobotModule(context) {
    private var state: GBSIntakeModuleState = GBSIntakeModuleState.IDLE

    private lateinit var intakeMotor: DcMotor
    private var slowMode: Boolean = true

    private var launchStartTimeMs: Long = 0

    override fun initialize(): Result<Unit> {
        return try {
            val intake = context.hardwareMap.tryGet(DcMotor::class.java, "intakeMotor")
                ?: throw GBSHardwareMissingException("intakeMotor")

            intakeMotor = intake

            context.telemetry.addLine("[INIT]: GBSIntakeModule initialized.")
            context.telemetry.update()
            Result.success(Unit)
        } catch (e: Exception) {
            context.telemetry.addLine("[ERR] An exception was raised in GBSIntakeModule::init: ${e.message}")
            context.telemetry.update()
            Result.failure(e)
        }
    }

    fun startLaunch(): Result<Unit> {
        if (state == GBSIntakeModuleState.IDLE) {
            state = GBSIntakeModuleState.LAUNCHING
            launchStartTimeMs = context.opMode.runtime.toLong()
            context.telemetry.addLine("[INTAKE]: Launch sequence started.")
        }
        return Result.success(Unit)
    }

    override fun run(): Result<Unit> {
        return when (state) {
            GBSIntakeModuleState.IDLE -> handleIdleState()
            GBSIntakeModuleState.LAUNCHING -> handleLaunchState()
            else -> handleRunningState()
        }
    }

    override fun shutdown(): Result<Unit> {
        setMotorPower(0.0)
        context.telemetry.addLine("[STDN]: GBSIntakeModule shutdown.")
        context.telemetry.update()
        return Result.success(Unit)
    }

    private fun handleIdleState(): Result<Unit> {
        val gamepad2 = context.gamepads.gamepad2

        if (gamepad2.leftBumperWasPressed()) {
            if (state == GBSIntakeModuleState.FORWARD) {
                state = GBSIntakeModuleState.IDLE
                return Result.success(Unit)
            }

            // If IDLE or REVERSE, goto FORWARD
            state = GBSIntakeModuleState.FORWARD
            setMotorPower(0.0)
        }

        if (gamepad2.rightBumperWasPressed()) {
            if (state == GBSIntakeModuleState.REVERSE) {
                state = GBSIntakeModuleState.IDLE
                return Result.success(Unit)
            }

            // If IDLE or FORWARD, goto REVERSE
            state = GBSIntakeModuleState.REVERSE
            setMotorPower(0.0)
        }

        if (gamepad2.touchpadWasPressed()) {
            slowMode = false
        }

        if (gamepad2.touchpadWasReleased()) {
            slowMode = true
        }

        setMotorPower(0.0)

        return Result.success(Unit)
    }

    private fun handleRunningState(): Result<Unit> {
        val config = GBSIntakeModuleConfiguration()
        val coefficient: Double = if (state == GBSIntakeModuleState.FORWARD) config.FORWARD_COEFFICIENT else config.REVERSE_COEFFICIENT;
        val gamepad2 = context.gamepads.gamepad2

        val modeCoefficient = if (state == GBSIntakeModuleState.FORWARD) -1 else 1
        val slowModeCoefficent = if (slowMode) config.SLOW_MODE_COEFFICIENT else 1.0

        val power = config.POWER * coefficient * modeCoefficient * slowModeCoefficent

        if (gamepad2.leftBumperWasPressed()) {
            if (state == GBSIntakeModuleState.FORWARD) {
                state = GBSIntakeModuleState.IDLE
                return Result.success(Unit)
            }

            // If IDLE or REVERSE, goto FORWARD
            state = GBSIntakeModuleState.FORWARD
            setMotorPower(0.0)
        }

        if (gamepad2.rightBumperWasPressed()) {
            if (state == GBSIntakeModuleState.REVERSE) {
                state = GBSIntakeModuleState.IDLE
                return Result.success(Unit)
            }

            // If IDLE or FORWARD, goto REVERSE
            state = GBSIntakeModuleState.REVERSE
            setMotorPower(0.0)
        }

        setMotorPower(power)

        return Result.success(Unit)
    }

    private fun setMotorPower(power: Double): Result<Unit> {
        intakeMotor.power = power

        return Result.success(Unit)
    }

    private fun handleLaunchState(): Result<Unit> {
        val currentTimeMs = context.opMode.runtime.toLong()
        val elapsedTimeMs = currentTimeMs - launchStartTimeMs
        val config = GBSIntakeModuleConfiguration()

        if (elapsedTimeMs < config.LAUNCH_REVERSE_DURATION_MS) {
            val power = config.POWER * config.REVERSE_COEFFICIENT
            setMotorPower(power)
            context.telemetry.addData("[INTAKE/LAUNCH]", "REVERSE (%.2f/%.2f sec)",
                elapsedTimeMs / 1000.0, config.LAUNCH_REVERSE_DURATION_MS / 1000.0)
        } else if (elapsedTimeMs < config.LAUNCH_REVERSE_DURATION_MS + config.LAUNCH_FORWARD_DURATION_MS) {
            val power = config.POWER * config.LAUNCH_FORWARD_COEFFICIENT
            setMotorPower(power)
            context.telemetry.addData(
                "[INTAKE/LAUNCH]",
                "FAST FORWARD (%.2f/%.2f sec)",
                (elapsedTimeMs -config. LAUNCH_REVERSE_DURATION_MS) / 1000.0,
                config.LAUNCH_FORWARD_DURATION_MS / 1000.0
            )
        } else {
            context.telemetry.addLine("[INTAKE/LAUNCH]: Launch sequence complete. Resuming FORWARD.")

            state = GBSIntakeModuleState.FORWARD
        }

        return Result.success(Unit)
    }
}