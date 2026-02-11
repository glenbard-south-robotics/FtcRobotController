package org.firstinspires.ftc.teamcode.modules.robot

import com.qualcomm.robotcore.hardware.DcMotorEx
import org.firstinspires.ftc.teamcode.config.GBSIntakeModuleConfiguration
import org.firstinspires.ftc.teamcode.getCoefficient
import org.firstinspires.ftc.teamcode.modules.GBSModuleOpModeContext
import org.firstinspires.ftc.teamcode.modules.GBSRobotModule
import org.firstinspires.ftc.teamcode.modules.actions.GBSModuleActions
import org.firstinspires.ftc.teamcode.modules.telemetry.GBSTelemetryDebug

enum class GBSIntakeModuleState {
    IDLE, FORWARD, REVERSE, AUTO_FORWARD, AUTO_REVERSE
}

@Suppress("unused")
class GBSIntakeModule(context: GBSModuleOpModeContext) :
    GBSRobotModule(context, GBSIntakeModuleConfiguration) {

    private var state: GBSIntakeModuleState = GBSIntakeModuleState.IDLE

    private lateinit var intakeMotor: DcMotorEx
    private var slowMode: Boolean = true

    override fun initialize(): Result<Unit> {
        tryGetHardware<DcMotorEx>("intakeMotor").fold({
            intakeMotor = it
        }, { return Result.failure(it) })

        return Result.success(Unit)
    }

    override fun run(): Result<Unit> {
        return when (state) {
            GBSIntakeModuleState.IDLE -> handleIdleState()
            GBSIntakeModuleState.AUTO_REVERSE -> handleAutoState()
            GBSIntakeModuleState.AUTO_FORWARD -> handleAutoState()
            else -> handleRunningState()
        }
    }

    override fun shutdown(): Result<Unit> {
        setMotorPower(0.0)
        return Result.success(Unit)
    }

    private fun handleInputs() {
        // We should not modify the state in AUTO
        if (state == GBSIntakeModuleState.AUTO_FORWARD || state == GBSIntakeModuleState.AUTO_REVERSE) return

        if (readBinaryPressed(GBSModuleActions.INTAKE_FORWARD)) {
            state = when (state) {
                GBSIntakeModuleState.FORWARD -> GBSIntakeModuleState.IDLE
                else -> GBSIntakeModuleState.FORWARD
            }
        }

        if (readBinaryPressed(GBSModuleActions.INTAKE_REVERSE)) {
            state = when (state) {
                GBSIntakeModuleState.REVERSE -> GBSIntakeModuleState.IDLE
                else -> GBSIntakeModuleState.REVERSE
            }
        }

        if (readBinaryPressed(GBSModuleActions.INTAKE_SLOW_TOGGLE)) {
            slowMode = !slowMode
        }
    }

    private fun handleAutoState(): Result<Unit> {
        val power = getTargetPower()

        setMotorPower(power)

        return Result.success(Unit)
    }

    private fun handleIdleState(): Result<Unit> {
        handleInputs()

        setMotorPower(0.0)

        return Result.success(Unit)
    }

    /**
     * The power of the mode, such as 0.5, or 1.0
     */
    private fun modePower(): Double {
        return when (state) {
            GBSIntakeModuleState.IDLE -> 0.0
            GBSIntakeModuleState.FORWARD -> GBSIntakeModuleConfiguration.FORWARD_POWER
            GBSIntakeModuleState.REVERSE -> GBSIntakeModuleConfiguration.REVERSE_POWER
            GBSIntakeModuleState.AUTO_FORWARD -> GBSIntakeModuleConfiguration.FORWARD_POWER
            GBSIntakeModuleState.AUTO_REVERSE -> GBSIntakeModuleConfiguration.REVERSE_POWER
        }
    }

    /**
     * The direction of the mode (-1.0 | 1.0)
     */
    private fun modeDirectionCoefficient(): Double {
        return when (state) {
            GBSIntakeModuleState.IDLE -> 1.0
            GBSIntakeModuleState.FORWARD -> 1.0
            GBSIntakeModuleState.REVERSE -> -1.0
            GBSIntakeModuleState.AUTO_FORWARD -> 1.0
            GBSIntakeModuleState.AUTO_REVERSE -> -1.0
        }
    }

    private fun motorDirectionCoefficient(): Double {
        return GBSIntakeModuleConfiguration.MOTOR_DIRECTION.getCoefficient()
    }

    /**
     * Calculate the target power, multiplies modePower, modeDirectionCoefficient, motorDirectionCoefficient
     */
    fun getTargetPower(): Double {
        val modePower = modePower()
        val modeDirectionCoefficient = modeDirectionCoefficient()
        val motorDirectionCoefficient = motorDirectionCoefficient()

        return modePower * modeDirectionCoefficient * motorDirectionCoefficient
    }

    private fun handleRunningState(): Result<Unit> {
        handleInputs()

        val power = getTargetPower()

        setMotorPower(power)

        return Result.success(Unit)
    }

    /**
     * Set the motor power
     * @param power The target intake power
     */
    private fun setMotorPower(power: Double) {
        intakeMotor.power = power
    }

    /**
     * Set the motor velocity
     * @param velocity The target intake velocity
     */
    private fun setMotorVelocity(velocity: Double) {
        intakeMotor.velocity = velocity
    }

    /**
     * Transitions to AUTO_FORWARD
     */
    fun autoIntakeForward() {
        state = GBSIntakeModuleState.AUTO_FORWARD
    }

    /**
     * Transitions to AUTO_REVERSE
     */
    fun autoIntakeReverse(velocity: Double) {
        state = GBSIntakeModuleState.AUTO_REVERSE
    }

    fun autoIntakeStop() {
        state = GBSIntakeModuleState.IDLE
        setMotorVelocity(0.0)
    }

    //region Telemetry

    @GBSTelemetryDebug(group = "Intake")
    fun currentState(): String = state.name

    @GBSTelemetryDebug(group = "Intake")
    fun isSlowMode(): Boolean = slowMode

    @GBSTelemetryDebug(group = "Intake")
    fun motorPower(): Double = intakeMotor.power

    @GBSTelemetryDebug(group = "Intake")
    fun motorVelocity(): Double = intakeMotor.velocity

    @GBSTelemetryDebug(group = "Intake")
    fun targetPower(): Double {
        return getTargetPower()
    }

    //endregion
}