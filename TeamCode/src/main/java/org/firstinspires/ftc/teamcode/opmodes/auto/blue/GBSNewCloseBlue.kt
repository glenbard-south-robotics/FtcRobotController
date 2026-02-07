package org.firstinspires.ftc.teamcode.opmodes.auto.blue

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles
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

        flywheel.autoFlywheelOn()

        base.autoDrive(0.33, 48, 48, 5000, {
            base.autoDrive(0.33, 12, -12, 5000, {
                sleep(3000)
                intake.autoIntakeForward(0.5)
            })
        })

        val desiredOrientation = YawPitchRollAngles(AngleUnit.DEGREES, 130.0, 80.0, 8.0, 10)

        if (webcam2.aprilTagDetections.isNotEmpty()) {
            val aprilTag = webcam2.aprilTagDetections.first()
            val currentPose = aprilTag.robotPose


            val errorYaw = desiredOrientation.yaw - currentPose.orientation.yaw

            val kP = 0.05
            val turnPower = errorYaw * kP

            if (abs(errorYaw) > EPSILON_FORWARD_FIRE) {
                base.autoPower(0.25, -turnPower, turnPower)
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