package org.firstinspires.ftc.teamcode.opmodes.test

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.CYCLE_MS
import org.firstinspires.ftc.teamcode.modules.GBSGamepadPair
import org.firstinspires.ftc.teamcode.modules.GBSModuleContext
import org.firstinspires.ftc.teamcode.modules.robot.GBSBaseModule

@Suppress("unused")
@TeleOp(name = "GBSTestTeleop", group = "Tests")
class GBSTestTeleop : LinearOpMode() {
    override fun runOpMode() {
        val context = GBSModuleContext(
            opMode = this,
            hardwareMap = this.hardwareMap,
            telemetry = this.telemetry,
            gamepads = GBSGamepadPair(this.gamepad1, this.gamepad2)
        )

        val base = GBSBaseModule(context)

        // Initialize modules, and make sure they succeeded
        check(base.initialize().isSuccess)

        waitForStart()

        while (opModeIsActive()) {
            check(base.run().isSuccess)
            idle()
            sleep(CYCLE_MS)
        }
    }
}