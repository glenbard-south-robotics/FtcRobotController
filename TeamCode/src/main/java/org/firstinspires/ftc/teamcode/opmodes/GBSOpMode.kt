package org.firstinspires.ftc.teamcode.opmodes

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import org.firstinspires.ftc.teamcode.modules.GBSRobotModule

abstract class GBSOpMode : LinearOpMode() {
    private val moduleRegistry: HashMap<String, GBSRobotModule> = HashMap()
    private var initialized = false

    /**
     * Behaviors to run **before** the start button is pressed
     * @see registerModules
     */
    abstract fun initialize(): Result<Unit>

    /**
     * Behaviors to run **during** while the opmode is active
     */
    abstract fun run(): Result<Unit>

    /**
     * Behaviors to run **after** the stop button is pressed
     */
    abstract fun shutdown(): Result<Unit>

    /**
     * Register all given GBSRobotModules to the GBSOpMode
     * @param modules Pairs of module name -> module instance
     */
    fun registerModules(vararg modules: Pair<String, GBSRobotModule>) {
        check(!initialized) { "Cannot register modules after initialization: $modules" }
        moduleRegistry.putAll(modules)
    }

    /**
     * Get a module by name
     * @param name the name of the module
     * @return the module, or null if not registered
     */
    fun tryGetModule(name: String): GBSRobotModule? = moduleRegistry[name]

    /**
     * Get the module
     * @throws IllegalStateException if the given module is not found
     */
    inline fun <reified T : GBSRobotModule> getModule(name: String): T =
        tryGetModule(name)?.let { it as T } ?: error("Module '$name' not found")

    /**
     * Initialize and assert that all modules started correctly
     * @throws IllegalStateException when initialization fails
     */
    fun initializeRegistryModules() {
        for ((_, module) in moduleRegistry) {
            module.initialize().getOrElse { throw it }
        }
    }

    /**
     * Run and assert that all modules ran correctly
     * @throws IllegalStateException when the module failed to run
     */
    fun runRegistryModules() {
        for ((name, module) in moduleRegistry) {
            module.run().getOrElse { throw it }
            module.emitDebugTelemetry(name)
        }
    }

    /**
     * Shutdown all modules in the module registry
     */
    fun shutdownRegistryModules() {
        for ((name, module) in moduleRegistry) {
            module.shutdown().onFailure {
                telemetry.addLine("[WARN] Module '$name' shutdown failed: ${it.message}")
            }
        }
        telemetry.update()
    }

    override fun runOpMode() {
        initialize().getOrElse { throw it }

        initialized = true
        initializeRegistryModules()

        try {
            // Wait until the start button is pressed
            waitForStart()

            while (opModeIsActive()) {
                runRegistryModules()
                run().getOrElse { throw it }

                this.telemetry.update()
                idle()
            }
        } catch (e: Exception) {
            telemetry.addLine("[FATAL] ${e.message}")
            telemetry.update()
            requestOpModeStop()
            throw e
        } finally {
            shutdown().onFailure {
                telemetry.addLine("[WARN] OpMode shutdown failed: ${it.message}")
                telemetry.update()
            }
            shutdownRegistryModules()
        }
    }
}
