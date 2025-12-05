package org.firstinspires.ftc.teamcode.modules.robot

import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorSimple
import com.qualcomm.robotcore.hardware.Gamepad
import com.qualcomm.robotcore.util.ElapsedTime
import org.firstinspires.ftc.teamcode.GBSBaseModuleConfiguration
import org.firstinspires.ftc.teamcode.exceptions.GBSHardwareMissingException
import org.firstinspires.ftc.teamcode.modules.GBSModuleContext
import org.firstinspires.ftc.teamcode.modules.GBSRobotModule
import kotlin.math.abs
import kotlin.math.roundToInt

private const val WHEELS_INCHES_TO_TICKS = 140.0 / Math.PI

enum class GBSBaseModuleState {
    IDLE,
    MANUAL,
    AUTO_DRIVE,
}

class GBSBaseModule(context: GBSModuleContext) : GBSRobotModule(context) {
    private var autoDriveTimer: ElapsedTime = ElapsedTime()
    private var state: GBSBaseModuleState = GBSBaseModuleState.IDLE

    private lateinit var leftDrive: DcMotor
    private lateinit var rightDrive: DcMotor

    private var autoSpeed: Double = 0.0
    private var autoTimeoutMs: Int = 0

    private var fineAdjustMode: Boolean = false

    override fun initialize(): Result<Unit> {
        return try {
            val left = context.hardwareMap .tryGet(DcMotor::class.java, "leftDrive")
                ?: throw GBSHardwareMissingException("leftDrive")
            val right = context.hardwareMap.tryGet(DcMotor::class.java, "rightDrive")
                ?: throw GBSHardwareMissingException("rightDrive")

            leftDrive = left
            rightDrive = right

            leftDrive.direction = DcMotorSimple.Direction.REVERSE
            leftDrive.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
            rightDrive.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER

            context.telemetry.addLine("[INIT]: GBSBaseModule initialized.")
            context.telemetry.update()
            Result.success(Unit)
        } catch (e: Exception) {
            context.telemetry.addLine("[ERR] An exception was raised in GBSBaseModule::init: ${e.message}")
            context.telemetry.update()
            Result.failure(e)
        }
    }

    override fun run(): Result<Unit> {
        return when (state) {
            GBSBaseModuleState.IDLE -> handleIdleState()
            GBSBaseModuleState.MANUAL -> handleManualState()
            GBSBaseModuleState.AUTO_DRIVE -> handleAutoDriveState()
        }
    }

    override fun shutdown(): Result<Unit> {
        setMotorPowers(0.0, 0.0)
        context.telemetry.addLine("[STDN]: GBSBaseModule shutdown.")
        context.telemetry.update()
        return Result.success(Unit)
    }

    /**
     * Get the manual stick threshold, depending on `fineAdjustMode` and `config`
     */
    fun getManualStickThreshold(config: GBSBaseModuleConfiguration): Double {
        return if (this.fineAdjustMode) config.STICK_THRESHOLD else config.FINE_ADJUST_STICK_THRESHOLD
    }

    /**
    * Check if the `y` values of any of the gamepad's sticks are over `config`'s `STICK_THRESHOLD`
     */
    fun sticksYOverThreshold(config: GBSBaseModuleConfiguration, gamepad: Gamepad): Boolean {
        val threshold = getManualStickThreshold(config)
        return abs(-gamepad.left_stick_y) > threshold || abs(-gamepad.right_stick_y) > threshold
    }

    /**
     * Get the manual power coefficient, depending on `fineAdjustMode` and `config`
     */
    fun getManualPowerCoefficient(config: GBSBaseModuleConfiguration): Double {
        return if (this.fineAdjustMode) config.FINE_ADJUST_POWER_COEFFICIENT else config.BASE_POWER_COEFFICIENT
    }

    /**
     * Enable or disables `fineAdjustMode` depending if the respective buttons are pressed
     * Rumbles the controller in different durations for haptic feedback
     */
    fun enableOrDisableFineAdjustMode(gamepad: Gamepad) {
        if (gamepad.crossWasPressed()) {
            fineAdjustMode = !fineAdjustMode
            gamepad.rumble(250)
        }
    }

    private fun handleIdleState(): Result<Unit> {
        val gamepad1 = context.gamepads.gamepad1
        val config = GBSBaseModuleConfiguration()

        if (sticksYOverThreshold(config, gamepad1)) {
            state = GBSBaseModuleState.MANUAL
        } else {
            setMotorPowers(0.0, 0.0)
        }

        return Result.success(Unit)
    }

    private fun handleManualState(): Result<Unit> {
        val gamepad1 = context.gamepads.gamepad1

        val leftStickY = -gamepad1.left_stick_y.toDouble()
        val rightStickY = -gamepad1.right_stick_y.toDouble()

        val config = GBSBaseModuleConfiguration()
        val stickThreshold = getManualStickThreshold(config)

        enableOrDisableFineAdjustMode(gamepad1)

        // Transition to `idle` if both sticks are lower than the threshold needed to make the motors move
        if (abs(leftStickY) <= stickThreshold && abs(rightStickY) <= stickThreshold) {
            state = GBSBaseModuleState.IDLE
            setMotorPowers(0.0, 0.0)
            return Result.success(Unit)
        }

        val powerCoefficient = getManualPowerCoefficient(config)

        val leftPower = if (abs(leftStickY) > stickThreshold)
            leftStickY * powerCoefficient else 0.0
        val rightPower = if (abs(rightStickY) > stickThreshold)
            rightStickY * powerCoefficient else 0.0

        return setMotorPowers(leftPower, rightPower)
    }

    /**
     * Handle `auto` state. Transitions to `idle` if both motors are stopped, or the timeout is exceeded.
     */
    private fun handleAutoDriveState(): Result<Unit> {
        if ((!leftDrive.isBusy && !rightDrive.isBusy) || autoDriveTimer.milliseconds() >= autoTimeoutMs) {
            setMotorPowers(0.0, 0.0)
            leftDrive.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
            rightDrive.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
            state = GBSBaseModuleState.IDLE
            return Result.success(Unit)
        }
        return Result.success(Unit)
    }

    fun setMotorPowers(leftPower: Double, rightPower: Double): Result<Unit> {
        try {
            leftDrive.power = -leftPower
            rightDrive.power = -rightPower
        } catch (e: Exception) {
            return Result.failure(e)
        }
        return Result.success(Unit)
    }

    /**
     * Attempt to drive so many `leftDistanceInches` and `rightDistanceInches`, errors if we aren't in `idle`
     */
    fun autoDrive(
        speed: Double,
        leftDistanceInches: Int,
        rightDistanceInches: Int,
        timeoutMs: Int = 5000
    ): Result<Boolean> {
        if (state != GBSBaseModuleState.IDLE) {
            return Result.failure(IllegalStateException("[ERR]: GBSBaseModule cannot autoDrive while not IDLE."))
        }
        try {
            autoDriveTimer.reset()

            state = GBSBaseModuleState.AUTO_DRIVE

            autoSpeed = speed
            autoTimeoutMs = timeoutMs

            leftDrive.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
            rightDrive.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER

            leftDrive.targetPosition = (leftDistanceInches * WHEELS_INCHES_TO_TICKS).roundToInt()
            rightDrive.targetPosition =
                (rightDistanceInches * WHEELS_INCHES_TO_TICKS).roundToInt()

            leftDrive.mode = DcMotor.RunMode.RUN_TO_POSITION
            rightDrive.mode = DcMotor.RunMode.RUN_TO_POSITION

            leftDrive.power = abs(autoSpeed)
            rightDrive.power = abs(autoSpeed)
            return Result.success(false)
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }
}