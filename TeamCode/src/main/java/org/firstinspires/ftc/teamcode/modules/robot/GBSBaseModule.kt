package org.firstinspires.ftc.teamcode.modules.robot

import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorSimple
import org.firstinspires.ftc.teamcode.modules.GBSModuleContext
import org.firstinspires.ftc.teamcode.modules.GBSRobotModule

class GBSBaseModule(context: GBSModuleContext) : GBSRobotModule(context) {
    private lateinit var leftDrive: DcMotor
    private lateinit var rightDrive: DcMotor

    override fun initialize(): Result<Unit> {
        return try {
            leftDrive = context.hardwareMap.get(DcMotor::class.java, "leftDrive")
            rightDrive = context.hardwareMap.get(DcMotor::class.java, "rightDrive")

            leftDrive.direction = DcMotorSimple.Direction.REVERSE

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
