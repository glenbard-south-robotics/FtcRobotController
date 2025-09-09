package org.firstinspires.ftc.teamcode.modules.robot

import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorSimple
import com.qualcomm.robotcore.util.ElapsedTime
import org.firstinspires.ftc.teamcode.GBSBaseModuleConfiguration
import org.firstinspires.ftc.teamcode.exceptions.GBSHardwareMissingException
import org.firstinspires.ftc.teamcode.modules.GBSModuleContext
import org.firstinspires.ftc.teamcode.modules.GBSRobotModule
import kotlin.math.abs

private const val WHEELS_INCHES_TO_TICKS = 140 / Math.PI

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

    override fun initialize(): Result<Unit> {
        return try {
            val left = context.hardwareMap.tryGet(DcMotor::class.java, "leftDrive")
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

    private fun handleIdleState(): Result<Unit> {
        // If in the IDLE state, check for a transition to MANUAL.

        val gamepad1 = context.gamepads.gamepad1
        val leftStickY = -gamepad1.left_stick_y.toDouble()
        val rightStickY = -gamepad1.right_stick_y.toDouble()

        val config = GBSBaseModuleConfiguration()
        val stickThreshold = config.STICK_THRESHOLD

        if (abs(leftStickY) > stickThreshold || abs(rightStickY) > stickThreshold) {
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

        // Check for a transition back to IDLE

        val config = GBSBaseModuleConfiguration()
        val stickThreshold = config.STICK_THRESHOLD
        if (abs(leftStickY) <= stickThreshold && abs(rightStickY) <= stickThreshold) {
            state = GBSBaseModuleState.IDLE
            setMotorPowers(0.0, 0.0)
            return Result.success(Unit)
        }

        val fineAdjustMode = gamepad1.a

        val fineAdjustStickThreshold = config.FINE_ADJUST_STICK_THRESHOLD
        val powerCoefficient = if (fineAdjustMode) config.FINE_ADJUST_POWER_COEFFICIENT else 1.0

        val stickThresholdToUse = if (fineAdjustMode) fineAdjustStickThreshold else stickThreshold

        val leftPower = if (abs(leftStickY) > stickThresholdToUse) {
            leftStickY * config.BASE_POWER * powerCoefficient
        } else {
            0.0
        }

        val rightPower = if (abs(rightStickY) > stickThresholdToUse) {
            rightStickY * config.BASE_POWER * powerCoefficient
        } else {
            0.0
        }

        return setMotorPowers(leftPower, rightPower)
    }

    private fun handleAutoDriveState(): Result<Unit> {
        // Transition to IDLE after motors are done, or the timeout was exceeded
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
            leftDrive.power = leftPower
            rightDrive.power = rightPower
        } catch (e: Exception) {
            return Result.failure(e)
        }

        return Result.success(Unit)
    }

    fun autoDrive(
        speed: Double,
        leftDistanceInches: Int,
        rightDistanceInches: Int,
        timeoutMs: Int = 5000
    ): Result<Boolean> {
        // Only start a new autonomous action if we are currently IDLE
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

            leftDrive.targetPosition = leftDistanceInches * WHEELS_INCHES_TO_TICKS.toInt()
            rightDrive.targetPosition = rightDistanceInches * WHEELS_INCHES_TO_TICKS.toInt()

            leftDrive.mode = DcMotor.RunMode.RUN_TO_POSITION
            rightDrive.mode = DcMotor.RunMode.RUN_TO_POSITION

            leftDrive.power = Math.abs(autoSpeed)
            rightDrive.power = Math.abs(autoSpeed)

            return Result.success(false)
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }
}