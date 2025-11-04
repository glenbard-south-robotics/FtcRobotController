package org.firstinspires.ftc.teamcode.opmodes.test

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.CYCLE_MS
import org.firstinspires.ftc.teamcode.GBSGamepadPair
import org.firstinspires.ftc.teamcode.modules.GBSModuleContext
import org.firstinspires.ftc.teamcode.modules.robot.GBSBaseModule
import org.firstinspires.ftc.teamcode.modules.robot.GBSFlywheelModule
import org.firstinspires.ftc.teamcode.modules.robot.GBSHopperModule

@Suppress("unused")
@TeleOp(name = "GBSTestAllOp", group = "Tests")
class GBSTestFlywheelAllOp : LinearOpMode() {
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

        while (opModeIsActive()) {
            check(base.run().isSuccess)
            check(hopper.run().isSuccess)
            check(flywheel.run().isSuccess)
            idle()
            sleep(CYCLE_MS)
        }
    }
}