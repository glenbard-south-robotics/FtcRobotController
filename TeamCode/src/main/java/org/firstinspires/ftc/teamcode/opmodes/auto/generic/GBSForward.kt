package org.firstinspires.ftc.teamcode.opmodes.auto.generic

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import org.firstinspires.ftc.teamcode.modules.GBSModuleOpModeContext
import org.firstinspires.ftc.teamcode.modules.robot.GBSBaseModule

@Suppress("unused")
@Autonomous(name = "GBSForward", group = "Generic")
class GBSForward : LinearOpMode() {
    override fun runOpMode() {
        val context = GBSModuleOpModeContext(this)

        val baseModule = GBSBaseModule(context)

        waitForStart()

        baseModule.autoDrive(0.33, 12, 12, 5000, {
            stop()
        })

        while (opModeIsActive()) {
            check(baseModule.run().isSuccess)

            telemetry.update()
            idle()
        }
    }
}