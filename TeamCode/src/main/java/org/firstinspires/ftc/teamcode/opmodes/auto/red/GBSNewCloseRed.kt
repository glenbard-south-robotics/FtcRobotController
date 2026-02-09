package org.firstinspires.ftc.teamcode.opmodes.auto.red

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import org.firstinspires.ftc.teamcode.config.GBSCloseRedConfiguration
import org.firstinspires.ftc.teamcode.modules.GBSModuleOpModeContext
import org.firstinspires.ftc.teamcode.modules.robot.GBSBaseModule
import org.firstinspires.ftc.teamcode.modules.robot.GBSFlywheelModule
import org.firstinspires.ftc.teamcode.modules.robot.GBSIntakeModule
import org.firstinspires.ftc.teamcode.modules.robot.GBSWebcamModule
import org.firstinspires.ftc.teamcode.opmodes.GBSOpMode
import kotlin.math.abs

@Suppress("unused")
@Autonomous(name = "GBSNewCloseRed", group = "Red")
class GBSNewCloseRed : GBSOpMode() {
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

    override fun run(): Result<Unit> {
        val base = this.getModule<GBSBaseModule>("base")
        val flywheel = this.getModule<GBSFlywheelModule>("flywheel")
        val intake = this.getModule<GBSIntakeModule>("intake")
        val webcam2 = this.getModule<GBSWebcamModule>("webcam2")

        flywheel.autoFlywheelOn()

        base.autoDrive(
            GBSCloseRedConfiguration.BASE_POWER,
            GBSCloseRedConfiguration.MOTOR_DISTANCES.first,
            GBSCloseRedConfiguration.MOTOR_DISTANCES.second,
            5000,
            {
                base.autoDrive(GBSCloseRedConfiguration.BASE_POWER, 12, -12, 5000, {
                    sleep(GBSCloseRedConfiguration.SPINUP_MS)
                    intake.autoIntakeForward()
                })
            })

        if (webcam2.aprilTagDetections.isNotEmpty()) {
            val aprilTag = webcam2.aprilTagDetections.first()
            val currentPose = aprilTag.robotPose

            val errorYaw =
                GBSCloseRedConfiguration.DESIRED_TAG_ORIENTATION.yaw - currentPose.orientation.yaw

            val turnPower = errorYaw * GBSCloseRedConfiguration.PID_K_P

            if (abs(errorYaw) > GBSCloseRedConfiguration.ERROR_EPSILON) {
                base.autoPower(
                    GBSCloseRedConfiguration.ERROR_CORRECTION_SPEED, -turnPower, turnPower
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