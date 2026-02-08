package org.firstinspires.ftc.teamcode.modules.robot

import com.qualcomm.robotcore.hardware.DcMotorEx
import org.firstinspires.ftc.teamcode.GBSFlywheelModuleConfiguration
import org.firstinspires.ftc.teamcode.exceptions.GBSInvalidStateException
import org.firstinspires.ftc.teamcode.modules.GBSModuleContext
import org.firstinspires.ftc.teamcode.modules.GBSRobotModule
import kotlin.math.abs

enum class GBSFlywheelModuleState {
    IDLE, FORWARD, AUTO,
}

class GBSFlywheelModule(context: GBSModuleContext, hardware: String = "flywheel") :
    GBSRobotModule(context, hardware) {
    private var state: GBSFlywheelModuleState = GBSFlywheelModuleState.IDLE
    private var debounce: Long = 0
    private var slowMode: Boolean = false

    private var autoVelocity: Double? = null

    private lateinit var flywheelMotor: DcMotorEx

    override fun initialize(): Result<Unit> {
        tryGetHardware<DcMotorEx>("flywheel").fold(
            { flywheelMotor = it },
            { return Result.failure(it) })

        debounce = System.currentTimeMillis()

        return Result.success(Unit)
    }

    override fun run(): Result<Unit> {
        return when (state) {
            GBSFlywheelModuleState.IDLE -> handleIdleState()
            GBSFlywheelModuleState.AUTO -> handleAutoState()
            else -> handleRunningState()
        }
    }

    override fun shutdown(): Result<Unit> {
        setMotorVelocity(0.0)
        return Result.success(Unit)
    }

    private fun handleAutoState(): Result<Unit> {
        if (this.autoVelocity == null) {
            return Result.failure(GBSInvalidStateException("Cannot setMotorPower when autoVelocity is null!"))
        }

        // This is safe since we checked if it was null above
        val velocity = -this.autoVelocity!!

        setMotorVelocity(velocity)
        return Result.success(Unit)
    }

    private fun handleIdleState(): Result<Unit> {
        val gamepad2 = context.gamepads.gamepad2

        if (gamepad2.triangleWasPressed()) {
            state = GBSFlywheelModuleState.FORWARD
            return Result.success(Unit)
        }

        setMotorVelocity(0.0)
        return Result.success(Unit)
    }

    private fun handleRunningState(): Result<Unit> {
        val config = GBSFlywheelModuleConfiguration()
        val gamepad2 = context.gamepads.gamepad2

        if (gamepad2.triangleWasPressed()) {
            state = GBSFlywheelModuleState.IDLE
            return Result.success(Unit)
        }

        if (gamepad2.squareWasPressed()) {
            slowMode = !slowMode
        }

        val velocity = if (slowMode) {
            -config.TELEOP_SLOW_VELOCITY
        } else {
            -config.TELEOP_VELOCITY
        }

        val now = System.currentTimeMillis()

        // Rumble the gamepads when the flywheel is at its target speed and we haven't in the last 5 seconds
        if (abs(velocity - getVelocity()) <= config.RUMBLE_ERROR_EPSILON_TPS && (now - debounce) >= 5000) {
            context.gamepads.gamepad2.rumble(1000)
            context.gamepads.gamepad1.rumble(1000)
            debounce = System.currentTimeMillis()
        }

        setMotorVelocity(velocity)
        return Result.success(Unit)
    }

    /**
     * Turn the flywheel on, transitions to AUTO
     */
    fun autoFlywheelOn(): Result<Unit> {
        state = GBSFlywheelModuleState.AUTO
        return Result.success(Unit)
    }

    /**
     * Turn the flywheel off, transitions to IDLE
     */
    fun autoFlywheelOff(): Result<Unit> {
        state = GBSFlywheelModuleState.IDLE
        return Result.success(Unit)
    }

    /**
     * Attempt to spin the flywheel at the given velocity
     * @param velocity The target motor velocity
     */
    fun setMotorVelocity(velocity: Double): Result<Unit> {
        flywheelMotor.velocity = velocity
        return Result.success(Unit)
    }

    /**
     * Set the auto velocity target
     * @param velocity The auto velocity
     */
    fun setAutoVelocity(velocity: Double) {
        this.autoVelocity = velocity
    }

    /**
     * Get the current velocity of the flywheel
     */
    fun getVelocity(): Double {
        return flywheelMotor.velocity
    }
}
