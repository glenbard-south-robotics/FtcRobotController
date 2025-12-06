package org.firstinspires.ftc.teamcode.opmodes.auto

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import org.firstinspires.ftc.teamcode.GBSGamepadPair
import org.firstinspires.ftc.teamcode.modules.GBSModuleContext
import org.firstinspires.ftc.teamcode.modules.robot.GBSBaseModule
import org.firstinspires.ftc.teamcode.modules.robot.GBSFlywheelModule
import org.firstinspires.ftc.teamcode.modules.robot.GBSIntakeModule

@Autonomous(name = "GBSForwardFireAuto")
class GBSForwardFireAuto : LinearOpMode() {
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

        check(baseModule.initialize().isSuccess)
        check(flywheelModule.initialize().isSuccess)
        check(intakeModule.initialize().isSuccess)

        waitForStart()

        flywheelModule.autoFlywheelOn()

        baseModule.autoDrive(0.5, 48, 48, 5000, {
            baseModule.autoDrive(0.5, 12, -12, 5000, {
                intakeModule.autoIntakeForward(0.5)
            })
        })

        while (opModeIsActive()) {
            check(baseModule.run().isSuccess)
            check(flywheelModule.run().isSuccess)
            check(intakeModule.run().isSuccess)
        }

        flywheelModule.autoFlywheelOff()
        intakeModule.autoIntakeStop()

    }
}