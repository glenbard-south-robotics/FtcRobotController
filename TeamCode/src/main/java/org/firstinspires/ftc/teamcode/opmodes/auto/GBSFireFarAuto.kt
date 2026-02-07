package org.firstinspires.ftc.teamcode.opmodes.auto

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit
import org.firstinspires.ftc.robotcore.external.navigation.Pose3D
import org.firstinspires.ftc.robotcore.external.navigation.Position
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles
import org.firstinspires.ftc.teamcode.modules.GBSFlywheelModuleConfiguration
import org.firstinspires.ftc.teamcode.GBSGamepadPair
import org.firstinspires.ftc.teamcode.modules.GBSModuleContext
import org.firstinspires.ftc.teamcode.modules.robot.GBSBaseModule
import org.firstinspires.ftc.teamcode.modules.robot.GBSFlywheelModule
import org.firstinspires.ftc.teamcode.modules.robot.GBSIntakeModule
import org.firstinspires.ftc.teamcode.modules.robot.GBSWebcamModule
import kotlin.math.abs

const val EPSILON_FAR_AUTO: Double = 0.5

@Autonomous(name = "GBSFireFarAuto")
class GBSFireFarAuto : LinearOpMode() {
    override fun runOpMode() {

        val context = GBSModuleContext(
            opMode = this,
            hardwareMap = this.hardwareMap,
            telemetry = this.telemetry,
            gamepads = GBSGamepadPair(this.gamepad1, this.gamepad2)
        )

        val baseModule = GBSBaseModule(context)
        val flywheelModule = GBSFlywheelModule(context)
        val intakeModule = GBSIntakeModule(context)
        val webcamModule = GBSWebcamModule(context, "webcam")
        val webcamModule2 = GBSWebcamModule(context, "webcam2")

        check(baseModule.initialize().isSuccess)
        check(flywheelModule.initialize().isSuccess)
        check(intakeModule.initialize().isSuccess)
        check(webcamModule.initialize().isSuccess)
        check(webcamModule2.initialize().isSuccess)

        waitForStart()

        val flywheelConfig = GBSFlywheelModuleConfiguration()

        flywheelModule.autoTPS = flywheelConfig.AUTO_FAR_TPS
        flywheelModule.autoFlywheelOn()

        baseModule.autoDrive(0.25, 0, -5, 5000, {
            Thread.sleep(3000)
            intakeModule.autoIntakeForward(0.5)
        })

        val desiredPosition = Position(DistanceUnit.INCH, 37.9, -15.8, 13.5, 10)
        val desiredOrientation = YawPitchRollAngles(AngleUnit.DEGREES, 76.9, 86.9, 39.0, 10)
        val desiredRobotPose = Pose3D(desiredPosition, desiredOrientation)

        while (opModeIsActive()) {
            check(baseModule.run().isSuccess)
            check(flywheelModule.run().isSuccess)
            check(intakeModule.run().isSuccess)
            check(webcamModule.run().isSuccess)
            check(webcamModule2.run().isSuccess)

            telemetry.addLine("neue")
            telemetry.addLine("${flywheelModule.autoTPS}")
            telemetry.addLine("${webcamModule.aprilTagDetections.size}")
            telemetry.addLine("${webcamModule2.aprilTagDetections.size}")

            if (webcamModule2.aprilTagDetections.isNotEmpty()) {
                val aprilTag = webcamModule2.aprilTagDetections.first()
                val currentPose = aprilTag.robotPose

                val errorX = desiredPosition.x - currentPose.position.x
                val errorYaw = desiredOrientation.yaw - currentPose.orientation.yaw

                val Kp = 0.05
                val turnPower = errorYaw * Kp
//
                if (abs(errorYaw) > EPSILON_FAR_AUTO) {
                    baseModule.autoPower(0.25, -turnPower, turnPower)
                } else {
                    baseModule.autoPower(0.0, 0.0, 0.0)
                }

            }

            telemetry.update()
        }


        flywheelModule.autoFlywheelOff()
        intakeModule.autoIntakeStop()

    }
}