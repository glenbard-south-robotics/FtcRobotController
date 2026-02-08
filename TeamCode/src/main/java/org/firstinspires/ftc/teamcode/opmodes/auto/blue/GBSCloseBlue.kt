package org.firstinspires.ftc.teamcode.opmodes.auto.blue

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import org.firstinspires.ftc.teamcode.config.GBSCloseBlueConfiguration
import org.firstinspires.ftc.teamcode.modules.GBSModuleOpModeContext
import org.firstinspires.ftc.teamcode.modules.robot.GBSBaseModule
import org.firstinspires.ftc.teamcode.modules.robot.GBSFlywheelModule
import org.firstinspires.ftc.teamcode.modules.robot.GBSIntakeModule
import org.firstinspires.ftc.teamcode.modules.robot.GBSWebcamModule
import kotlin.math.abs

@Suppress("unused")
@Autonomous(name = "GBSCloseBlue", group = "Blue")
class GBSCloseBlue : LinearOpMode() {
    override fun runOpMode() {
        val context = GBSModuleOpModeContext(this)

        val baseModule = GBSBaseModule(context)
        val flywheelModule = GBSFlywheelModule(context)
        val intakeModule = GBSIntakeModule(context)
        val webcamModule2 = GBSWebcamModule(context, "webcam2")

        waitForStart()

        flywheelModule.setAutoVelocity(GBSCloseBlueConfiguration.FLYWHEEL_VELOCITY)
        flywheelModule.autoFlywheelOn()

        baseModule.autoDrive(
            GBSCloseBlueConfiguration.BASE_POWER,
            GBSCloseBlueConfiguration.MOTOR_DISTANCES.first,
            GBSCloseBlueConfiguration.MOTOR_DISTANCES.second,
            5000,
            {
                baseModule.autoDrive(GBSCloseBlueConfiguration.BASE_POWER, 12, -12, 5000, {
                    sleep(GBSCloseBlueConfiguration.SPINUP_MS)
                    intakeModule.autoIntakeForward(GBSCloseBlueConfiguration.INTAKE_POWER)
                })
            })

        while (opModeIsActive()) {
            check(baseModule.run().isSuccess)
            check(flywheelModule.run().isSuccess)
            check(intakeModule.run().isSuccess)
            check(webcamModule2.run().isSuccess)

            if (webcamModule2.aprilTagDetections.isNotEmpty()) {
                val aprilTag = webcamModule2.aprilTagDetections.first()
                val currentPose = aprilTag.robotPose

                val errorYaw =
                    GBSCloseBlueConfiguration.DESIRED_TAG_ORIENTATION.yaw - currentPose.orientation.yaw

                val turnPower = errorYaw * GBSCloseBlueConfiguration.PID_K_P

                if (abs(errorYaw) > GBSCloseBlueConfiguration.ERROR_EPSILON) {
                    baseModule.autoPower(
                        GBSCloseBlueConfiguration.ERROR_CORRECTION_SPEED,
                        -turnPower,
                        turnPower
                    )
                } else {
                    baseModule.autoPower(0.0, 0.0, 0.0)
                }
            }

            telemetry.update()
            idle()
        }

        flywheelModule.autoFlywheelOff()
        intakeModule.autoIntakeStop()
    }
}