package org.firstinspires.ftc.teamcode.opmodes.teleop

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.modules.CYCLE_MS
import org.firstinspires.ftc.teamcode.GBSGamepadPair
import org.firstinspires.ftc.teamcode.modules.GBSModuleContext
import org.firstinspires.ftc.teamcode.modules.robot.GBSBaseModule
import org.firstinspires.ftc.teamcode.modules.robot.GBSFlywheelModule
import org.firstinspires.ftc.teamcode.modules.robot.GBSIntakeModule

@Suppress("unused")
@TeleOp(name = "GBSDecodeTeleOp", group = "TeleOp")
class GBSDecodeTeleOp : LinearOpMode() {
    override fun runOpMode() {
        val context = GBSModuleContext(this)

        val base = GBSBaseModule(context)
        val intake = GBSIntakeModule(context)
        val flywheel = GBSFlywheelModule(context)

        check(base.initialize().isSuccess)
        check(intake.initialize().isSuccess)
        check(flywheel.initialize().isSuccess)

        waitForStart()

        while (opModeIsActive()) {
            check(base.run().isSuccess)
            check(intake.run().isSuccess)
            check(flywheel.run().isSuccess)

            telemetry.update()
            idle()
        }
    }

}