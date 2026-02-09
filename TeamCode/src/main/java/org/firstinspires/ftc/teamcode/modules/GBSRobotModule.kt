package org.firstinspires.ftc.teamcode.modules

import org.firstinspires.ftc.teamcode.config.modules.IGBSRobotModuleConfiguration
import org.firstinspires.ftc.teamcode.exceptions.GBSHardwareMissingException
import org.firstinspires.ftc.teamcode.modules.actions.GBSAnalogAction
import org.firstinspires.ftc.teamcode.modules.actions.GBSModuleActions
import org.firstinspires.ftc.teamcode.modules.actions.read
import org.firstinspires.ftc.teamcode.modules.actions.wasPressed
import org.firstinspires.ftc.teamcode.modules.actions.wasReleased
import org.firstinspires.ftc.teamcode.modules.telemetry.GBSTelemetry
import org.firstinspires.ftc.teamcode.modules.telemetry.addDebug

abstract class GBSRobotModule(
    val opModeContext: GBSModuleOpModeContext, val config: IGBSRobotModuleConfiguration
) {
    /**
     * Enable debug telemetry for this module
     */
    val enableDebugTelemetry: Boolean = config.DEBUG_TELEMETRY

    open fun initialize(): Result<Unit> = Result.success(Unit)
    abstract fun run(): Result<Unit>
    open fun shutdown(): Result<Unit> = Result.success(Unit)

    /**
     * @param hardwareId The name of the hardware device as specified in the Driver Hub
     * @return hardware<T>
     */
    inline fun <reified T> tryGetHardware(hardwareId: String): Result<T> {
        try {
            val hardware = opModeContext.hardwareMap.tryGet(T::class.java, hardwareId)
                ?: throw GBSHardwareMissingException(hardwareId)
            return Result.success(hardware)
        } catch (e: GBSHardwareMissingException) {
            return Result.failure(e)
        }
    }

    /**
     * Emit debug telemetry if enabled
     */
    fun emitDebugTelemetry(moduleName: String) {
        if (!enableDebugTelemetry) return

        GBSTelemetry(
            telemetry = opModeContext.telemetry, obj = this, groupPrefix = moduleName
        ).addDebug()
    }

    fun readAnalog(action: GBSAnalogAction): Double {
        return action.read(opModeContext.inputManager, config).toDouble()
    }

    fun readBinaryPressed(action: GBSModuleActions): Boolean {
        return action.wasPressed(opModeContext.inputManager, config)
    }

    fun readBinary(action: GBSModuleActions): Boolean {
        return action.read(opModeContext.inputManager, config)
    }

    fun readBinaryReleased(action: GBSModuleActions): Boolean {
        return action.wasReleased(opModeContext.inputManager, config)
    }

}
