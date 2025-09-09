package org.firstinspires.ftc.teamcode.opmodes.test

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.CYCLE_MS
import org.firstinspires.ftc.teamcode.GBSGamepadPair
import org.firstinspires.ftc.teamcode.modules.GBSModuleContext
import org.firstinspires.ftc.teamcode.modules.robot.GBSHopperModule

@Suppress("unused")
@TeleOp(name = "GBSTestHopperTeleOp", group = "Tests")
class GBSTestHopperTeleOp : LinearOpMode() {
    override fun runOpMode() {
        val context = GBSModuleContext(
            opMode = this,
            hardwareMap = this.hardwareMap,
            telemetry = this.telemetry,
            gamepads = GBSGamepadPair(this.gamepad1, this.gamepad2)
        )

        val hopper = GBSHopperModule(context)

        // Initialize modules, and make sure they succeeded
        check(hopper.initialize().isSuccess)

        waitForStart()

        while (opModeIsActive()) {
            check(hopper.run().isSuccess)
            idle()
            sleep(CYCLE_MS)
        }
    }
}