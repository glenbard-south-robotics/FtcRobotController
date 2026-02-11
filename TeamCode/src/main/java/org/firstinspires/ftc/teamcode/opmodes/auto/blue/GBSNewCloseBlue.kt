package org.firstinspires.ftc.teamcode.opmodes.auto.blue

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import org.firstinspires.ftc.teamcode.config.GBSCloseBlueConfiguration
import org.firstinspires.ftc.teamcode.modules.GBSModuleOpModeContext
import org.firstinspires.ftc.teamcode.modules.robot.GBSBaseModule
import org.firstinspires.ftc.teamcode.modules.robot.GBSFlywheelModule
import org.firstinspires.ftc.teamcode.modules.robot.GBSIntakeModule
import org.firstinspires.ftc.teamcode.modules.robot.GBSWebcamModule
import org.firstinspires.ftc.teamcode.opmodes.GBSOpMode
import kotlin.math.abs

@Suppress("unused")
@Autonomous(name = "GBSNewCloseBlue", group = "Blue")
class GBSNewCloseBlue : GBSOpMode() {
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

        flywheel.setAutoVelocity(GBSCloseBlueConfiguration.FLYWHEEL_VELOCITY)
        flywheel.autoFlywheelOn()



        base.autoDrive(
            GBSCloseBlueConfiguration.BASE_POWER,
            GBSCloseBlueConfiguration.MOTOR_DISTANCES.first,
            GBSCloseBlueConfiguration.MOTOR_DISTANCES.second,
            5000,
            {
                base.autoDrive(GBSCloseBlueConfiguration.BASE_POWER, 12, -12, 5000, {
                    sleep(GBSCloseBlueConfiguration.SPINUP_MS)
                    intake.autoIntakeForward()
                })
            })

        return Result.success(Unit)
    }

    override fun runLoop(): Result<Unit> {
        val base = this.getModule<GBSBaseModule>("base")
        val webcam2 = this.getModule<GBSWebcamModule>("webcam2")

        if (webcam2.aprilTagDetections.isNotEmpty()) {
            val aprilTag = webcam2.aprilTagDetections.first()
            val currentPose = aprilTag.robotPose

            val errorYaw =
                GBSCloseBlueConfiguration.DESIRED_TAG_ORIENTATION.yaw - currentPose.orientation.yaw

            val turnPower = errorYaw * GBSCloseBlueConfiguration.PID_K_P

            if (abs(errorYaw) > GBSCloseBlueConfiguration.ERROR_EPSILON) {
                base.autoPower(
                    GBSCloseBlueConfiguration.ERROR_CORRECTION_SPEED, -turnPower, turnPower
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