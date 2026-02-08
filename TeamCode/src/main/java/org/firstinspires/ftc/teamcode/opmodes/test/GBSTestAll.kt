package org.firstinspires.ftc.teamcode.opmodes.test

import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.modules.GBSModuleOpModeContext
import org.firstinspires.ftc.teamcode.modules.robot.GBSBaseModule
import org.firstinspires.ftc.teamcode.modules.robot.GBSFlywheelModule
import org.firstinspires.ftc.teamcode.modules.robot.GBSIntakeModule
import org.firstinspires.ftc.teamcode.modules.robot.GBSWebcamModule
import org.firstinspires.ftc.teamcode.opmodes.GBSOpMode

@Suppress("unused")
@TeleOp(name = "GBSTestAllOp", group = "Tests")
class GBSTestAll() : GBSOpMode() {
    override fun initialize(): Result<Unit> {
        val context = GBSModuleOpModeContext(this)
        registerModules(
            "base" to GBSBaseModule(context),
            "flywheel" to GBSFlywheelModule(context),
            "intake" to GBSIntakeModule(context),
            "webcam" to GBSWebcamModule(context, "webcam"),
            "webcam2" to GBSWebcamModule(context, "webcam2"),
        )
        return Result.success(Unit)
    }

    override fun run(): Result<Unit> {
        return Result.success(Unit)
    }

    override fun shutdown(): Result<Unit> {
        return Result.success(Unit)
    }
}