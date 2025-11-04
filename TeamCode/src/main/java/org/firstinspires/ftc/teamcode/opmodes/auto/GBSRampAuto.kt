package org.firstinspires.ftc.teamcode.opmodes.test

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.CYCLE_MS
import org.firstinspires.ftc.teamcode.GBSGamepadPair
import org.firstinspires.ftc.teamcode.modules.GBSModuleContext
import org.firstinspires.ftc.teamcode.modules.robot.GBSBaseModule
import org.firstinspires.ftc.teamcode.modules.robot.GBSFlywheelModule
import org.firstinspires.ftc.teamcode.modules.robot.GBSHopperModule

@Suppress("unused")
@Autonomous(name = "GBSRampAuto", group = "Auto")
class GBSRampAuto : LinearOpMode() {
    override fun runOpMode() {
        val context = GBSModuleContext(
            opMode = this,
            hardwareMap = this.hardwareMap,
            telemetry = this.telemetry,
            gamepads = GBSGamepadPair(this.gamepad1, this.gamepad2)
        )

        val base = GBSBaseModule(context)
        val hopper = GBSHopperModule(context)
        val flywheel = GBSFlywheelModule(context)

        // Initialize modules, and make sure they succeeded
        check(base.initialize().isSuccess)
        check(hopper.initialize().isSuccess)
        check(flywheel.initialize().isSuccess)

        waitForStart()

        check(base.autoDrive(1.0, 72, 72, 2500).isSuccess)
        check(hopper.fire().isSuccess)


        while (opModeIsActive()) {
            check(base.run().isSuccess)
            check(hopper.run().isSuccess)
            check(flywheel.run().isSuccess)
            check(hopper.fire().isSuccess)
            idle()
            sleep(CYCLE_MS)
        }
    }
}