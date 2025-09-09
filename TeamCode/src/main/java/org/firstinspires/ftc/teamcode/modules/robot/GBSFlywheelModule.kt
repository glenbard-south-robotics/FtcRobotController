package org.firstinspires.ftc.teamcode.modules.robot

import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorSimple
import org.firstinspires.ftc.teamcode.exceptions.GBSHardwareMissingException
import org.firstinspires.ftc.teamcode.modules.GBSModuleContext
import org.firstinspires.ftc.teamcode.modules.GBSRobotModule

class GBSFlywheelModule(context: GBSModuleContext) : GBSRobotModule(context) {
    private lateinit var flywheel: DcMotor

    override fun initialize(): Result<Unit> {
        return try {
            val flywheel = context.hardwareMap.tryGet(DcMotor::class.java, "flywheel")
                ?: throw GBSHardwareMissingException("flywheel")

            this.flywheel = flywheel

            flywheel.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
            flywheel.direction = DcMotorSimple.Direction.REVERSE

            Result.success(Unit)
        } catch (e: Exception) {
            context.telemetry.addLine("[ERR]: An exception was raised in GBSFlywheelModule: ${e.message}")
            context.telemetry.update()

            Result.failure(e)
        }
    }

    override fun run(): Result<Unit> {
        TODO("Not yet implemented")
    }

    override fun shutdown(): Result<Unit> {
        context.telemetry.addLine("[STDN]: GBSFlywheelModule shutdown.")
        context.telemetry.update()
        return super.shutdown()
    }
}