package org.firstinspires.ftc.teamcode.opmodes.auto.blue

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import org.firstinspires.ftc.teamcode.GBSFarBlueConfiguration
import org.firstinspires.ftc.teamcode.modules.GBSModuleContext
import org.firstinspires.ftc.teamcode.modules.robot.GBSBaseModule
import org.firstinspires.ftc.teamcode.modules.robot.GBSFlywheelModule
import org.firstinspires.ftc.teamcode.modules.robot.GBSIntakeModule
import org.firstinspires.ftc.teamcode.modules.robot.GBSWebcamModule
import kotlin.math.abs

@Suppress("unused")
@Autonomous(name = "GBSFarBlue", group = "Blue")
class GBSFarBlue : LinearOpMode() {
    override fun runOpMode() {
        val context = GBSModuleContext(this)

        val baseModule = GBSBaseModule(context)
        val flywheelModule = GBSFlywheelModule(context)
        val intakeModule = GBSIntakeModule(context)
        val webcamModule2 = GBSWebcamModule(context, "webcam2")

        check(baseModule.initialize().isSuccess)
        check(flywheelModule.initialize().isSuccess)
        check(intakeModule.initialize().isSuccess)
        check(webcamModule2.initialize().isSuccess)

        waitForStart()

        val config = GBSFarBlueConfiguration()

        flywheelModule.setAutoVelocity(config.FLYWHEEL_VELOCITY)
        flywheelModule.autoFlywheelOn()

        baseModule.autoDrive(
            config.BASE_POWER,
            config.MOTOR_DISTANCES.first,
            config.MOTOR_DISTANCES.second,
            5000,
            {
                sleep(config.SPINUP_MS)
                intakeModule.autoIntakeForward(config.INTAKE_POWER)
            })

        while (opModeIsActive()) {
            check(baseModule.run().isSuccess)
            check(flywheelModule.run().isSuccess)
            check(intakeModule.run().isSuccess)
            check(webcamModule2.run().isSuccess)

            if (webcamModule2.aprilTagDetections.isNotEmpty()) {
                val aprilTag = webcamModule2.aprilTagDetections.first()
                val currentPose = aprilTag.robotPose

                val errorYaw = config.DESIRED_TAG_ORIENTATION.yaw - currentPose.orientation.yaw

                val turnInches = errorYaw * config.PID_K_P

                if (abs(errorYaw) > config.ERROR_EPSILON) {
                    baseModule.autoPower(config.ERROR_CORRECTION_SPEED, -turnInches, turnInches)
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