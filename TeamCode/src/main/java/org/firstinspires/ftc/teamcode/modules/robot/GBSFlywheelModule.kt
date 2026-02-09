package org.firstinspires.ftc.teamcode.modules.robot

import com.qualcomm.robotcore.hardware.DcMotorEx
import org.firstinspires.ftc.teamcode.config.GBSFlywheelModuleConfiguration
import org.firstinspires.ftc.teamcode.exceptions.GBSInvalidStateException
import org.firstinspires.ftc.teamcode.modules.GBSModuleOpModeContext
import org.firstinspires.ftc.teamcode.modules.GBSRobotModule
import org.firstinspires.ftc.teamcode.modules.actions.GBSGamepadID
import org.firstinspires.ftc.teamcode.modules.actions.GBSModuleActions
import org.firstinspires.ftc.teamcode.modules.telemetry.GBSTelemetryDebug
import kotlin.math.abs

enum class GBSFlywheelModuleState {
    IDLE, FORWARD, AUTO,
}

@Suppress("unused")
class GBSFlywheelModule(context: GBSModuleOpModeContext) :
    GBSRobotModule(context, GBSFlywheelModuleConfiguration) {

    private var state: GBSFlywheelModuleState = GBSFlywheelModuleState.IDLE
    private var lastRumbleMs: Long = 0
    private var slowMode: Boolean = false

    // TODO: Move autoVelocity into autoFlywheelOn
    private var autoVelocity: Double? = null

    private lateinit var flywheelMotor: DcMotorEx

    override fun initialize(): Result<Unit> {
        tryGetHardware<DcMotorEx>("flywheel").fold(
            { flywheelMotor = it },
            { return Result.failure(it) })

        lastRumbleMs = System.currentTimeMillis()

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

    private fun handleFlywheelToggle() {
        // We should not be able to modify the state in AUTO
        if (state == GBSFlywheelModuleState.AUTO) return

        if (readBinaryPressed(GBSModuleActions.FLYWHEEL_TOGGLE)) {
            state = if (state == GBSFlywheelModuleState.IDLE) {
                GBSFlywheelModuleState.FORWARD
            } else {
                GBSFlywheelModuleState.IDLE
            }
        }
    }

    private fun handleSlowModeToggle() {
        // We should not be able to modify the state in AUTO
        if (state == GBSFlywheelModuleState.AUTO) return

        if (readBinaryPressed(GBSModuleActions.FLYWHEEL_TOGGLE_SLOW_MODE)) {
            slowMode = !slowMode
        }
    }

    private fun handleAutoState(): Result<Unit> {
        if (this.autoVelocity == null) {
            return Result.failure(GBSInvalidStateException("Cannot setMotorPower when autoVelocity is null!"))
        }

        // This is safe since we checked if it was null above
        val velocity = -requireNotNull(this.autoVelocity)

        setMotorVelocity(velocity)
        return Result.success(Unit)
    }

    private fun handleIdleState(): Result<Unit> {
        handleFlywheelToggle()

        setMotorVelocity(0.0)
        return Result.success(Unit)
    }

    private fun handleRunningState(): Result<Unit> {
        handleFlywheelToggle()
        handleSlowModeToggle()

        val velocity = if (slowMode) {
            -GBSFlywheelModuleConfiguration.TELEOP_SLOW_VELOCITY
        } else {
            -GBSFlywheelModuleConfiguration.TELEOP_VELOCITY
        }

        val now = System.currentTimeMillis()

        // Rumble the gamepads when the flywheel is at its target speed and we haven't in the last 5 seconds
        if (abs(velocity - getVelocity()) <= GBSFlywheelModuleConfiguration.RUMBLE_ERROR_EPSILON && (now - lastRumbleMs) >= 5000) {
            opModeContext.inputManager.rumble(
                250,
                GBSGamepadID.GAMEPAD_ONE,
                GBSGamepadID.GAMEPAD_TWO
            )

            lastRumbleMs = System.currentTimeMillis()
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
    private fun setMotorVelocity(velocity: Double) {
        flywheelMotor.velocity = velocity
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

    //region Telemetry

    @GBSTelemetryDebug(group = "Flywheel")
    fun currentState(): String = state.name

    @GBSTelemetryDebug(group = "Flywheel")
    fun currentVelocity(): Double = flywheelMotor.velocity

    @GBSTelemetryDebug(group = "Flywheel")
    fun targetVelocity(): Double {
        return when (state) {
            GBSFlywheelModuleState.AUTO -> autoVelocity ?: 0.0
            GBSFlywheelModuleState.FORWARD -> if (slowMode) -GBSFlywheelModuleConfiguration.TELEOP_SLOW_VELOCITY else -GBSFlywheelModuleConfiguration.TELEOP_VELOCITY
            else -> 0.0
        }
    }

    @GBSTelemetryDebug(group = "Flywheel")
    fun isSlowMode(): Boolean = slowMode

    @GBSTelemetryDebug(group = "Flywheel")
    fun autoTargetVelocity(): Double? = autoVelocity

    //endregion
}
