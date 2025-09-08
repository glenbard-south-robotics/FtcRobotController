package org.firstinspires.ftc.teamcode.modules

abstract class GBSRobotModule(protected val context: GBSModuleContext) {
    open fun initialize(): Result<Unit> = Result.success(Unit)
    abstract fun run(): Result<Unit>
    open fun shutdown(): Result<Unit> = Result.success(Unit)
}