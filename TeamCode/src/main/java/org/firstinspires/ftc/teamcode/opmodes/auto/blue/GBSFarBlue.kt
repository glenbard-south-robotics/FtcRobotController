package org.firstinspires.ftc.teamcode.opmodes.auto.blue

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import org.firstinspires.ftc.teamcode.GBSFarBlueConfiguration
import org.firstinspires.ftc.teamcode.modules.GBSModuleOpModeContext
import org.firstinspires.ftc.teamcode.modules.robot.GBSBaseModule
import org.firstinspires.ftc.teamcode.modules.robot.GBSFlywheelModule
import org.firstinspires.ftc.teamcode.modules.robot.GBSIntakeModule
import org.firstinspires.ftc.teamcode.modules.robot.GBSWebcamModule
import kotlin.math.abs

@Suppress("unused")
@Autonomous(name = "GBSFarBlue", group = "Blue")
class GBSFarBlue : LinearOpMode() {
    override fun runOpMode() {
        val context = GBSModuleOpModeContext(this)

        val baseModule = GBSBaseModule(context)
        val flywheelModule = GBSFlywheelModule(context)
        val intakeModule = GBSIntakeModule(context)
        val webcamModule2 = GBSWebcamModule(context, "webcam2")

        check(baseModule.initialize().isSuccess)
        check(flywheelModule.initialize().isSuccess)
        check(intakeModule.initialize().isSuccess)
        check(webcamModule2.initialize().isSuccess)

        waitForStart()

        flywheelModule.setAutoVelocity(GBSFarBlueConfiguration.FLYWHEEL_VELOCITY)
        flywheelModule.autoFlywheelOn()

        baseModule.autoDrive(
            GBSFarBlueConfiguration.BASE_POWER,
            GBSFarBlueConfiguration.MOTOR_DISTANCES.first,
            GBSFarBlueConfiguration.MOTOR_DISTANCES.second,
            5000,
            {
                sleep(GBSFarBlueConfiguration.SPINUP_MS)
                intakeModule.autoIntakeForward(GBSFarBlueConfiguration.INTAKE_POWER)
            })

        while (opModeIsActive()) {
            check(baseModule.run().isSuccess)
            check(flywheelModule.run().isSuccess)
            check(intakeModule.run().isSuccess)
            check(webcamModule2.run().isSuccess)

            if (webcamModule2.aprilTagDetections.isNotEmpty()) {
                val aprilTag = webcamModule2.aprilTagDetections.first()
                val currentPose = aprilTag.robotPose

                val errorYaw = GBSFarBlueConfiguration.DESIRED_TAG_ORIENTATION.yaw - currentPose.orientation.yaw

                val turnInches = errorYaw * GBSFarBlueConfiguration.PID_K_P

                if (abs(errorYaw) > GBSFarBlueConfiguration.ERROR_EPSILON) {
                    baseModule.autoPower(GBSFarBlueConfiguration.ERROR_CORRECTION_SPEED, -turnInches, turnInches)
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