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

class GBSBaseModule(context: GBSModuleContext) : GBSRobotModule(context) {
    private var autoDriveTimer: ElapsedTime = ElapsedTime()

    private lateinit var leftDrive: DcMotor
    private lateinit var rightDrive: DcMotor

    override fun initialize(): Result<Unit> {
        return try {
            val left = context.hardwareMap.tryGet(DcMotor::class.java, "leftDrive")
                ?: throw GBSHardwareMissingException("leftDrive")

            val right = context.hardwareMap.tryGet(DcMotor::class.java, "rightDrive")
                ?: throw GBSHardwareMissingException("rightDrive")

            leftDrive = left
            rightDrive = right

            leftDrive.direction = DcMotorSimple.Direction.REVERSE

            Result.success(Unit)
        } catch (e: Exception) {
            context.telemetry.addLine("An exception was raised in GBSBaseModule: ${e.message}")
            context.telemetry.update()
            Result.failure(e)
        }
    }

    override fun run(): Result<Unit> {
        val gamepad1 = context.gamepads.gamepad1

        val leftStickY = -gamepad1.left_stick_y.toDouble()
        val rightStickY = -gamepad1.right_stick_y.toDouble()

        // Fine adjust mode allows for more granular control (e.g for parking)
        val fineAdjustMode = gamepad1.a

        val config = GBSBaseModuleConfiguration()

        val stickThreshold = if (fineAdjustMode) {
            config.FINE_ADJUST_STICK_THRESHOLD
        } else {
            config.STICK_THRESHOLD
        }

        val powerCoefficient = if (fineAdjustMode) {
            config.FINE_ADJUST_POWER_COEFFICIENT
        } else {
            1.0
        }

        val leftPower = if (abs(leftStickY) > stickThreshold) {
            leftStickY * config.BASE_POWER * powerCoefficient
        } else {
            0.0
        }

        val rightPower = if (abs(rightStickY) > stickThreshold) {
            rightStickY * config.BASE_POWER * powerCoefficient
        } else {
            0.0
        }

        setMotorPowers(leftPower, rightPower)

        return Result.success(Unit)
    }

    override fun shutdown(): Result<Unit> {
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
        try {
            autoDriveTimer.reset()

            leftDrive.targetPosition =
                (leftDrive.currentPosition + leftDistanceInches * WHEELS_INCHES_TO_TICKS).toInt()
            rightDrive.targetPosition =
                (rightDrive.currentPosition + rightDistanceInches * WHEELS_INCHES_TO_TICKS).toInt()

            leftDrive.mode = DcMotor.RunMode.RUN_TO_POSITION
            rightDrive.mode = DcMotor.RunMode.RUN_TO_POSITION

            leftDrive.power = Math.abs(speed)
            rightDrive.power = Math.abs(speed)

            // Idle until both motors are done, or we've reached the timeout
            while ((leftDrive.isBusy || rightDrive.isBusy) && autoDriveTimer.milliseconds() < timeoutMs) {
                context.opMode.idle()
            }

            leftDrive.power = 0.0
            rightDrive.power = 0.0

            leftDrive.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
            rightDrive.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER

            return Result.success(autoDriveTimer.milliseconds() >= timeoutMs)

        } catch (e: Exception) {
            return Result.failure(e)
        }
    }
}
