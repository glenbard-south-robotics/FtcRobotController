package org.firstinspires.ftc.teamcode.modules

import org.firstinspires.ftc.teamcode.GBSModuleContext
import org.firstinspires.ftc.teamcode.GBSRobotModule

class GBSBaseModule(context: GBSModuleContext) : GBSRobotModule(context) {
    override fun initialize(): Result<Unit> {
        return Result.success(Unit)
    }

    override fun run(): Result<Unit> {
        return Result.success(Unit)
    }

    override fun shutdown(): Result<Unit> {
        return Result.success(Unit)
    }
}