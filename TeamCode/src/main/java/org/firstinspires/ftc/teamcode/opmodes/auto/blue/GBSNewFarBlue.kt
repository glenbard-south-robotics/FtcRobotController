package org.firstinspires.ftc.teamcode.opmodes.auto.blue

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import org.firstinspires.ftc.teamcode.config.GBSFarBlueConfiguration
import org.firstinspires.ftc.teamcode.modules.GBSModuleOpModeContext
import org.firstinspires.ftc.teamcode.modules.robot.GBSBaseModule
import org.firstinspires.ftc.teamcode.modules.robot.GBSFlywheelModule
import org.firstinspires.ftc.teamcode.modules.robot.GBSIntakeModule
import org.firstinspires.ftc.teamcode.modules.robot.GBSWebcamModule
import org.firstinspires.ftc.teamcode.opmodes.GBSOpMode
import kotlin.math.abs

@Suppress("unused")
@Autonomous(name = "GBSNewFarBlue", group = "Blue")
class GBSNewFarBlue : GBSOpMode() {
    override fun initialize(): Result<Unit> {
        val context = GBSModuleOpModeContext(this)

        this.registerModules(
            "base" to GBSBaseModule(context),
            "flywheel" to GBSFlywheelModule(context),
            "intake" to GBSIntakeModule(context),
            "webcam2" to GBSWebcamModule(context, "webcam2")
        )

        return Result.success(Unit)
    }

    override fun runLinear(): Result<Unit> {
        val base = this.getModule<GBSBaseModule>("base")
        val flywheel = this.getModule<GBSFlywheelModule>("flywheel")
        val intake = this.getModule<GBSIntakeModule>("intake")

        flywheel.setAutoVelocity(GBSFarBlueConfiguration.FLYWHEEL_VELOCITY)
        flywheel.autoFlywheelOn()

        base.autoDrive(
            GBSFarBlueConfiguration.BASE_POWER,
            GBSFarBlueConfiguration.MOTOR_DISTANCES.first,
            GBSFarBlueConfiguration.MOTOR_DISTANCES.second,
            5000, {
                sleep(2000)
                intake.autoIntakeForward()
            }
        )

        return Result.success(Unit)
    }

    override fun runLoop(): Result<Unit> {
        val base = this.getModule<GBSBaseModule>("base")
        val webcam2 = this.getModule<GBSWebcamModule>("webcam2")

        if (webcam2.aprilTagDetections.isNotEmpty()) {
            val aprilTag = webcam2.aprilTagDetections.first()
            val currentPose = aprilTag.robotPose

            val errorYaw =
                GBSFarBlueConfiguration.DESIRED_TAG_ORIENTATION.yaw - currentPose.orientation.yaw

            val turnPower = errorYaw * GBSFarBlueConfiguration.PID_K_P

            if (abs(errorYaw) > GBSFarBlueConfiguration.ERROR_EPSILON) {
                base.autoPower(
                    GBSFarBlueConfiguration.ERROR_CORRECTION_SPEED, -turnPower, turnPower
                )
            } else {
                base.autoPower(0.0, 0.0, 0.0)
            }
        }

        return Result.success(Unit)
    }

    override fun shutdown(): Result<Unit> {
        return Result.success(Unit)
    }
}