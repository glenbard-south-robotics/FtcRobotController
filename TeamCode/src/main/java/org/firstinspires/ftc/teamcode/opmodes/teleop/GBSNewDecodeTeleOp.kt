package org.firstinspires.ftc.teamcode.opmodes.teleop

import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.modules.GBSModuleContext
import org.firstinspires.ftc.teamcode.modules.robot.GBSBaseModule
import org.firstinspires.ftc.teamcode.modules.robot.GBSFlywheelModule
import org.firstinspires.ftc.teamcode.modules.robot.GBSIntakeModule
import org.firstinspires.ftc.teamcode.opmodes.GBSOpMode

@Suppress("unused")
@TeleOp(name = "GBSNewDecodeTeleOp", group = "TeleOp")
class GBSNewDecodeTeleOp : GBSOpMode() {
    override fun initialize(): Result<Unit> {
        val context = GBSModuleContext(this)

        this.registerModules(
            "base" to GBSBaseModule(context),
            "flywheel" to GBSFlywheelModule(context),
            "intake" to GBSIntakeModule(context),
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