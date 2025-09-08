package org.firstinspires.ftc.teamcode

interface GBSRobotModule {
    fun initialize(): Result<Unit> = Result.success(Unit)
    fun run(): Result<Unit>
    fun shutdown(): Result<Unit> = Result.success(Unit)
}
