package org.firstinspires.ftc.teamcode.modules

import org.firstinspires.ftc.teamcode.exceptions.GBSHardwareMissingException
import kotlin.jvm.java

abstract class GBSRobotModule(val opModeContext: GBSModuleOpModeContext, protected val hardware: String?) {
    open fun initialize(): Result<Unit> = Result.success(Unit)
    abstract fun run(): Result<Unit>
    open fun shutdown(): Result<Unit> = Result.success(Unit)


    /**
     * @param hardwareId The name of the hardware device as specified in the Driver Hub
     * @return hardware<T>
     */
    inline fun<reified T> tryGetHardware(hardwareId: String): Result<T> {
        try {
            val hardware = opModeContext.hardwareMap.tryGet(T::class.java, hardwareId) ?: throw GBSHardwareMissingException(hardwareId)
            return Result.success(hardware)
        } catch (e: GBSHardwareMissingException) {
            return Result.failure(e)
        }
    }
}