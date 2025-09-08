package org.firstinspires.ftc.teamcode.modules

import org.firstinspires.ftc.teamcode.GBSRobotModule

class BaseModule : GBSRobotModule {
    override fun run(): Result<Unit> {
        return Result.success(Unit)
    }

    override fun initialize(): Result<Unit> {
        return super.initialize()
    }
}