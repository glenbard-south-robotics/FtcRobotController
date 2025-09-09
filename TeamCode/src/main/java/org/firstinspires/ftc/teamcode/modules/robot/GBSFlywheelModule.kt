package org.firstinspires.ftc.teamcode.modules.robot

import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.DcMotorSimple
import com.qualcomm.robotcore.util.ElapsedTime
import org.firstinspires.ftc.teamcode.GBSFlywheelModuleConfiguration
import org.firstinspires.ftc.teamcode.exceptions.GBSHardwareMissingException
import org.firstinspires.ftc.teamcode.modules.GBSModuleContext
import org.firstinspires.ftc.teamcode.modules.GBSRobotModule
import kotlin.math.max

private const val BANK_VELOCITY = 1300.0
private const val FAR_VELOCITY = 1900.0
private const val MAX_VELOCITY = 2200.0

class GBSFlywheelModule(context: GBSModuleContext) : GBSRobotModule(context) {
    private lateinit var flywheel: DcMotorEx

    private val autoLaunchTimer = ElapsedTime()

    override fun initialize(): Result<Unit> {
        return try {
            val flywheel = context.hardwareMap.tryGet(DcMotorEx::class.java, "flywheel")
                ?: throw GBSHardwareMissingException("flywheel")

            this.flywheel = flywheel

            flywheel.mode = DcMotor.RunMode.RUN_USING_ENCODER
            flywheel.direction = DcMotorSimple.Direction.REVERSE

            Result.success(Unit)
        } catch (e: Exception) {
            context.telemetry.addLine("[ERR]: An exception was raised in GBSFlywheelModule::init: ${e.message}")
            context.telemetry.update()

            Result.failure(e)
        }
    }

    override fun run(): Result<Unit> {
        return try {
            val gamepad = context.gamepads.gamepad2
            val config = GBSFlywheelModuleConfiguration()

            flywheel.power = 0.0

            val triggerPower = max(gamepad.right_trigger, gamepad.left_trigger)

            when {
                // Apply flywheel brake when a trigger is held to safely stop the motor
                triggerPower >= config.TRIGGER_THRESHOLD -> {
                    if (flywheel.velocity > 0.0) {
                        flywheel.power = -1.0 * config.BRAKE_TRIGGER_COEFFICIENT
                        context.telemetry.addLine("[FLYWHEEL]: Braking...")
                        context.telemetry.update()
                    }
                }

                // Apply maximum velocity, for long shots
                gamepad.dpad_up -> {
                    flywheel.velocity = MAX_VELOCITY
                    context.telemetry.addLine("[FLYWHEEL]: Applied MAX_VELOCITY")
                    context.telemetry.update()
                }

                // Apply bank velocity, for shots from the tape line or very close
                gamepad.dpad_down -> {
                    flywheel.velocity = BANK_VELOCITY
                    context.telemetry.addLine("[FLYWHEEL]: Applied BANK_VELOCITY")
                    context.telemetry.update()
                }

                // Apply far velocity, for shots from a few feet away
                gamepad.dpad_left -> {
                    flywheel.velocity = FAR_VELOCITY
                    context.telemetry.addLine("[FLYWHEEL]: Applied FAR_VELOCITY")
                    context.telemetry.update()
                }
            }

            context.telemetry.addData("[FLYWHEEL]: Velocity", flywheel.velocity)
            context.telemetry.addData("[FLYWHEEL]: Power", flywheel.power)
            context.telemetry.update()

            Result.success(Unit)
        } catch (e: Exception) {
            context.telemetry.addLine("[ERR]: An exception was raised in GBSFlywheelModule::run: ${e.message}")
            context.telemetry.update()
            Result.failure(e)
        }
    }

    override fun shutdown(): Result<Unit> {
        flywheel.power = 0.0
        context.telemetry.addLine("[STDN]: GBSFlywheelModule shutdown.")
        context.telemetry.update()
        return super.shutdown()
    }

    fun autoLaunch(targetVelocity: Double, timeoutMs: Int = 5000): Result<Boolean> {
        return try {
            autoLaunchTimer.reset()
            flywheel.velocity = targetVelocity


            while (flywheel.velocity < targetVelocity && autoLaunchTimer.milliseconds() < timeoutMs) {
                context.opMode.idle()
            }

            // TODO: Launch a projectile using the hopper module

            flywheel.power = 0.0

            Result.success(autoLaunchTimer.milliseconds() >= timeoutMs)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}