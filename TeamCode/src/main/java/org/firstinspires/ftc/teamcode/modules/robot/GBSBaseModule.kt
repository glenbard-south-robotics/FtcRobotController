package org.firstinspires.ftc.teamcode.modules.robot

import com.qualcomm.robotcore.hardware.DcMotor
import org.firstinspires.ftc.teamcode.modules.GBSModuleContext
import org.firstinspires.ftc.teamcode.modules.GBSRobotModule

class GBSBaseModule(context: GBSModuleContext) : GBSRobotModule(context) {
    private lateinit var leftDrive: DcMotor

    override fun initialize(): Result<Unit> {
        return try {
            leftDrive = context.hardwareMap.get(DcMotor::class.java, "leftDrive")
            Result.success(Unit)
        } catch (e: Exception) {
            context.telemetry.addLine("An exception was raised in GBSBaseModule: ${e.message}")
            context.telemetry.update()
            Result.failure(e)
        }
    }

    override fun run(): Result<Unit> {
        return Result.success(Unit)
    }

    override fun shutdown(): Result<Unit> {
        return Result.success(Unit)
    }
}
