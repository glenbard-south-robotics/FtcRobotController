package org.firstinspires.ftc.teamcode.modules.robot

import com.qualcomm.robotcore.hardware.DcMotorEx
import org.firstinspires.ftc.teamcode.config.GBSIntakeModuleConfiguration
import org.firstinspires.ftc.teamcode.modules.GBSModuleOpModeContext
import org.firstinspires.ftc.teamcode.modules.GBSRobotModule

enum class GBSIntakeModuleState {
    IDLE, FORWARD, REVERSE
}

class GBSIntakeModule(context: GBSModuleOpModeContext, hardware: String = "intakeMotor") :
    GBSRobotModule(context, hardware) {
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
            else -> handleRunningState()
        }
    }

    override fun shutdown(): Result<Unit> {
        setMotorPower(0.0)
        return Result.success(Unit)
    }

    private fun handleIdleState(): Result<Unit> {
        val gamepad2 = opModeContext.gamepads.gamepad2

        if (gamepad2.leftBumperWasPressed()) {
            if (state == GBSIntakeModuleState.FORWARD) {
                state = GBSIntakeModuleState.IDLE
                return Result.success(Unit)
            }

            state = GBSIntakeModuleState.FORWARD
            setMotorPower(0.0)
        }

        if (gamepad2.rightBumperWasPressed()) {
            if (state == GBSIntakeModuleState.REVERSE) {
                state = GBSIntakeModuleState.IDLE
                return Result.success(Unit)
            }

            state = GBSIntakeModuleState.REVERSE
            setMotorPower(0.0)
        }

        if (gamepad2.crossWasPressed()) {
            slowMode = false
        }

        if (gamepad2.crossWasReleased()) {
            slowMode = true
        }

        setMotorPower(0.0)

        return Result.success(Unit)
    }

    private fun handleRunningState(): Result<Unit> {

        val coefficient: Double =
            if (state == GBSIntakeModuleState.FORWARD) GBSIntakeModuleConfiguration.FORWARD_COEFFICIENT else GBSIntakeModuleConfiguration.REVERSE_COEFFICIENT
        val gamepad2 = opModeContext.gamepads.gamepad2

        val modeCoefficient = if (state == GBSIntakeModuleState.FORWARD) -1 else 1
        val slowModeCoefficient =
            if (slowMode) GBSIntakeModuleConfiguration.SLOW_MODE_COEFFICIENT else 1.0

        val power =
            GBSIntakeModuleConfiguration.POWER * coefficient * modeCoefficient * slowModeCoefficient

        if (gamepad2.leftBumperWasPressed()) {
            if (state == GBSIntakeModuleState.FORWARD) {
                state = GBSIntakeModuleState.IDLE
                return Result.success(Unit)
            }

            state = GBSIntakeModuleState.FORWARD
            setMotorPower(0.0)
        }

        if (gamepad2.rightBumperWasPressed()) {
            if (state == GBSIntakeModuleState.REVERSE) {
                state = GBSIntakeModuleState.IDLE
                return Result.success(Unit)
            }

            state = GBSIntakeModuleState.REVERSE
            setMotorPower(0.0)
        }

        if (gamepad2.crossWasPressed()) {
            slowMode = false
        }

        if (gamepad2.crossWasReleased()) {
            slowMode = true
        }

        setMotorPower(power)

        return Result.success(Unit)
    }

    /**
     * Set the motor power to -power
     * TODO: Create a config key for this negative coefficient
     * @param power The target intake power
     */
    private fun setMotorPower(power: Double): Result<Unit> {
        intakeMotor.power = -power
        return Result.success(Unit)
    }

    /**
     * Set the motor velocity to -velocity
     * TODO: Create a config key for this negative coefficient
     * @param velocity The target intake velocity
     */
    private fun setMotorVelocity(velocity: Double): Result<Unit> {
        intakeMotor.velocity = -velocity
        return Result.success(Unit)
    }

    /**
     * Transitions to FORWARD and sets the motor velocity to velocity
     * @param velocity The target intake velocity
     */
    fun autoIntakeForward(velocity: Double): Result<Unit> {
        state = GBSIntakeModuleState.FORWARD
        return setMotorVelocity(velocity)
    }

    /**
     * Transitions to REVERSE and sets the motor velocity to velocity
     * @param velocity The target intake velocity
     */
    fun autoIntakeReverse(velocity: Double): Result<Unit> {
        state = GBSIntakeModuleState.REVERSE
        return setMotorVelocity(-velocity)
    }

    /**
     * Turn off the intake in AUTO, transitions to IDLE and sets the motor velocity to 0
     */
    fun autoIntakeStop(): Result<Unit> {
        state = GBSIntakeModuleState.IDLE
        return setMotorVelocity(0.0)
    }
}