package org.firstinspires.ftc.teamcode.opmodes.auto.blue

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import org.firstinspires.ftc.teamcode.GBSCloseBlueConfiguration
import org.firstinspires.ftc.teamcode.modules.GBSModuleContext
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
        val context = GBSModuleContext(this)

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

        val config = GBSCloseBlueConfiguration()

        flywheel.autoFlywheelOn()

        base.autoDrive(
            config.BASE_POWER, config.MOTOR_DISTANCES.first, config.MOTOR_DISTANCES.second, 5000, {
                base.autoDrive(config.BASE_POWER, 12, -12, 5000, {
                    sleep(config.SPINUP_MS)
                    intake.autoIntakeForward(config.INTAKE_POWER)
                })
            })

        if (webcam2.aprilTagDetections.isNotEmpty()) {
            val aprilTag = webcam2.aprilTagDetections.first()
            val currentPose = aprilTag.robotPose

            val errorYaw = config.DESIRED_TAG_ORIENTATION.yaw - currentPose.orientation.yaw

            val turnPower = errorYaw * config.PID_K_P

            if (abs(errorYaw) > config.ERROR_EPSILON) {
                base.autoPower(config.ERROR_CORRECTION_SPEED, -turnPower, turnPower)
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