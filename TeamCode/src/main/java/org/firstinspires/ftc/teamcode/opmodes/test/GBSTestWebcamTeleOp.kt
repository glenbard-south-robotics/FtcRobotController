package org.firstinspires.ftc.teamcode.opmodes.test

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.CYCLE_MS
import org.firstinspires.ftc.teamcode.GBSGamepadPair
import org.firstinspires.ftc.teamcode.modules.GBSModuleContext
import org.firstinspires.ftc.teamcode.modules.robot.GBSWebcamModule

@Suppress("unused")
@TeleOp(name = "GBSTestWebcamTeleOp", group = "Tests")
class GBSTestWebcamTeleOp : LinearOpMode() {
    override fun runOpMode() {
        val context = GBSModuleContext(
            opMode = this,
            hardwareMap = this.hardwareMap,
            telemetry = this.telemetry,
            gamepads = GBSGamepadPair(this.gamepad1, this.gamepad2)
        )

//        val base = GBSBaseModule(context)
        val webcam = GBSWebcamModule(context, "webcam")
        val webcam2 = GBSWebcamModule(context, "webcam2")

        // Initialize modules, and make sure they succeeded
//        check(base.initialize().isSuccess)
        check(webcam.initialize().isSuccess)
        check(webcam2.initialize().isSuccess)

        waitForStart()

        while (opModeIsActive()) {
            check(webcam.run().isSuccess)
            check(webcam2.run().isSuccess)
            context.telemetry.update()
            idle()
            sleep(CYCLE_MS)
        }
    }
}