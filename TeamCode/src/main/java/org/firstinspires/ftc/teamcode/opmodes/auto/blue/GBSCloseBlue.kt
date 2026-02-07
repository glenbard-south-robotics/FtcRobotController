package org.firstinspires.ftc.teamcode.opmodes.auto.blue

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles
import org.firstinspires.ftc.teamcode.GBSGamepadPair
import org.firstinspires.ftc.teamcode.modules.GBSModuleContext
import org.firstinspires.ftc.teamcode.modules.robot.GBSBaseModule
import org.firstinspires.ftc.teamcode.modules.robot.GBSFlywheelModule
import org.firstinspires.ftc.teamcode.modules.robot.GBSIntakeModule
import org.firstinspires.ftc.teamcode.modules.robot.GBSWebcamModule
import kotlin.math.abs

const val EPSILON_FORWARD_FIRE: Double = 0.5

@Autonomous(name = "GBSCloseBlue", group = "Blue")
class GBSCloseBlue : LinearOpMode() {
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
        val webcamModule2 = GBSWebcamModule(context, "webcam2")

        waitForStart()

        flywheelModule.autoFlywheelOn()

        baseModule.autoDrive(0.33, 48, 48, 5000, {
            baseModule.autoDrive(0.33, 12, -12, 5000, {
                Thread.sleep(3000)
                intakeModule.autoIntakeForward(0.5)
            })
        })

        val desiredOrientation = YawPitchRollAngles(AngleUnit.DEGREES, 130.0, 80.0, 8.0, 10)

        while (opModeIsActive()) {
            check(baseModule.run().isSuccess)
            check(flywheelModule.run().isSuccess)
            check(intakeModule.run().isSuccess)
            check(webcamModule2.run().isSuccess)

            if (webcamModule2.aprilTagDetections.isNotEmpty()) {
                val aprilTag = webcamModule2.aprilTagDetections.first()
                val currentPose = aprilTag.robotPose

                val errorYaw = desiredOrientation.yaw - currentPose.orientation.yaw

                val Kp = 0.05
                val turnPower = errorYaw * Kp

                if (abs(errorYaw) > EPSILON_FORWARD_FIRE) {
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