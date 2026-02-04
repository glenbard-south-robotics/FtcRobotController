package org.firstinspires.ftc.teamcode.opmodes.auto

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit
import org.firstinspires.ftc.robotcore.external.navigation.Orientation
import org.firstinspires.ftc.robotcore.external.navigation.Pose3D
import org.firstinspires.ftc.robotcore.external.navigation.Position
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles
import org.firstinspires.ftc.teamcode.GBSGamepadPair
import org.firstinspires.ftc.teamcode.magnitudePose3D
import org.firstinspires.ftc.teamcode.modules.GBSModuleContext
import org.firstinspires.ftc.teamcode.modules.robot.GBSBaseModule
import org.firstinspires.ftc.teamcode.modules.robot.GBSFlywheelModule
import org.firstinspires.ftc.teamcode.modules.robot.GBSIntakeModule
import org.firstinspires.ftc.teamcode.modules.robot.GBSWebcamModule
import org.firstinspires.ftc.teamcode.roundPose3D
import org.firstinspires.ftc.teamcode.subPose3D
import kotlin.math.abs

const val EPSILON_RED: Double = 0.5

@Autonomous(name = "GBSForwardFireAutoRed")
class GBSForwardFireAutoRed : LinearOpMode() {
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

        flywheelModule.autoFlywheelOn()

        baseModule.autoDrive(0.33, 48, 48, 5000, {
            baseModule.autoDrive(0.33, 12, -12, 5000, {
                Thread.sleep(3000)
                intakeModule.autoIntakeForward(0.5)
            })
        })

        val desiredPosition = Position(DistanceUnit.INCH, -32.0, -28.0, 15.0, 10)

        val desiredOrientation = YawPitchRollAngles(AngleUnit.DEGREES, 130.0, 80.0, 8.0, 10)

        val desiredRobotPose = Pose3D(desiredPosition, desiredOrientation)

        while (opModeIsActive()) {
            check(baseModule.run().isSuccess)
            check(flywheelModule.run().isSuccess)
            check(intakeModule.run().isSuccess)
            check(webcamModule.run().isSuccess)
            check(webcamModule2.run().isSuccess)

            telemetry.addLine("${webcamModule.aprilTagDetections.size}")
            telemetry.addLine("${webcamModule2.aprilTagDetections.size}")

            if (webcamModule2.aprilTagDetections.isNotEmpty()) {
                val aprilTag = webcamModule2.aprilTagDetections.first()
                val currentPose = aprilTag.robotPose

//                val errorX = desiredPosition.x - currentPose.position.x
                val errorYaw = desiredOrientation.yaw - currentPose.orientation.yaw

//                val Kp = 0.05
//                val turnPower = errorYaw * Kp
//
//                if (abs(errorYaw) > EPSILON_RED) {
//                    baseModule.autoPower(0.25, -turnPower, turnPower)
//                } else {
//                    baseModule.autoPower(0.0, 0.0, 0.0)
//                }

//                val forwardPower = errorX * Kp
//
//                if (abs(errorX) > EPSILON) {
//                    baseModule.autoPower(0.25, forwardPower, forwardPower)
//                } else {
//                    baseModule.autoPower(0.0, 0.0, 0.0)
//                }

            }

            telemetry.update()
        }


        flywheelModule.autoFlywheelOff()
        intakeModule.autoIntakeStop()

    }
}