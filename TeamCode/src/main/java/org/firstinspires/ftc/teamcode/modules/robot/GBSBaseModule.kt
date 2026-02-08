package org.firstinspires.ftc.teamcode.modules.robot

import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorSimple
import com.qualcomm.robotcore.util.ElapsedTime
import org.firstinspires.ftc.teamcode.config.GBSBaseModuleConfiguration
import org.firstinspires.ftc.teamcode.exceptions.GBSInvalidStateException
import org.firstinspires.ftc.teamcode.modules.GBSModuleOpModeContext
import org.firstinspires.ftc.teamcode.modules.GBSRobotModule
import org.firstinspires.ftc.teamcode.modules.actions.GBSAnalogAction
import org.firstinspires.ftc.teamcode.modules.actions.GBSModuleActions
import org.firstinspires.ftc.teamcode.modules.actions.read
import org.firstinspires.ftc.teamcode.modules.actions.wasPressed
import org.firstinspires.ftc.teamcode.modules.telemetry.GBSTelemetryDebug
import kotlin.math.abs
import kotlin.math.roundToInt

private const val WHEELS_INCHES_TO_TICKS = 140.0 / Math.PI

enum class GBSBaseModuleState {
    IDLE, MANUAL, AUTO_DRIVE,
}

@Suppress("unused")
class GBSBaseModule(context: GBSModuleOpModeContext, hardware: String = "none") :
    GBSRobotModule(context, hardware) {

    override val enableDebugTelemetry: Boolean = GBSBaseModuleConfiguration.DEBUG_TELEMETRY

    private var state: GBSBaseModuleState = GBSBaseModuleState.IDLE
    private var fineAdjustMode: Boolean = false

    private lateinit var leftDrive: DcMotor
    private lateinit var rightDrive: DcMotor

    private var autoSpeed: Double = 0.0
    private var autoDriveTimer: ElapsedTime = ElapsedTime()
    private var autoTimeoutMs: Int = 0
    private val autoDriveCallbacks: MutableList<() -> Unit> = ArrayList()

    override fun initialize(): Result<Unit> {
        tryGetHardware<DcMotor>("leftDrive").fold({ leftDrive = it }, { return Result.failure(it) })
        tryGetHardware<DcMotor>("rightDrive").fold(
            { rightDrive = it },
            { return Result.failure(it) })

        leftDrive.direction = DcMotorSimple.Direction.REVERSE
        leftDrive.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
        rightDrive.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER

        return Result.success(Unit)
    }

    override fun run(): Result<Unit> {
        return when (state) {
            GBSBaseModuleState.IDLE -> handleIdleState()
            GBSBaseModuleState.MANUAL -> handleManualState()
            GBSBaseModuleState.AUTO_DRIVE -> handleAutoDriveState()
        }
    }

    override fun shutdown(): Result<Unit> {
        setMotorPowers(0.0, 0.0)
        return Result.success(Unit)
    }

    private fun getManualPowerCoefficient(): Double =
        if (fineAdjustMode) GBSBaseModuleConfiguration.FINE_ADJUST_POWER_COEFFICIENT
        else GBSBaseModuleConfiguration.BASE_POWER_COEFFICIENT

    /**
     * Read the configured analog value for the given action
     * This resolves the binding in the configuration, so remaps apply automatically
     */
    private fun readConfiguredAnalog(action: GBSAnalogAction): Double =
        action.read(opModeContext.inputManager, GBSBaseModuleConfiguration).toDouble()

    /**
     * Toggle fine adjust using the configured binary binding
     */
    private fun handleFineAdjustToggle() {
        if (GBSModuleActions.BASE_SLOW_TOGGLE.wasPressed(
                opModeContext.inputManager, GBSBaseModuleConfiguration
            )
        ) {
            fineAdjustMode = !fineAdjustMode

            opModeContext.inputManager.gamepadPair.gamepad1.rumble(250)
        }
    }

    private fun sticksOverThreshold(): Boolean {
        val leftY = readConfiguredAnalog(GBSAnalogAction.LEFT_STICK_Y)
        val rightY = readConfiguredAnalog(GBSAnalogAction.RIGHT_STICK_Y)
        return leftY != 0.0 || rightY != 0.0
    }


    private fun handleIdleState(): Result<Unit> {
        if (sticksOverThreshold()) {
            state = GBSBaseModuleState.MANUAL
        } else {
            setMotorPowers(0.0, 0.0)
        }

        handleFineAdjustToggle()
        return Result.success(Unit)
    }

    private fun handleManualState(): Result<Unit> {
        val leftY = readConfiguredAnalog(GBSAnalogAction.LEFT_STICK_Y)
        val rightY = readConfiguredAnalog(GBSAnalogAction.RIGHT_STICK_Y)

        handleFineAdjustToggle()

        if (leftY == 0.0 && rightY == 0.0) {
            state = GBSBaseModuleState.IDLE
            setMotorPowers(0.0, 0.0)
            return Result.success(Unit)
        }

        val coefficient = getManualPowerCoefficient()
        val leftPower = leftY * coefficient
        val rightPower = rightY * coefficient

        return setMotorPowers(leftPower, rightPower)
    }

    private fun handleAutoDriveState(): Result<Unit> {
        if ((!leftDrive.isBusy && !rightDrive.isBusy) || autoDriveTimer.milliseconds() >= autoTimeoutMs) {
            setMotorPowers(0.0, 0.0)
            leftDrive.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
            rightDrive.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
            state = GBSBaseModuleState.IDLE
            autoDriveCallbacks.forEach { it() }
        }
        return Result.success(Unit)
    }

    fun setMotorPowers(leftPower: Double, rightPower: Double): Result<Unit> = try {
        leftDrive.power = -leftPower
        rightDrive.power = -rightPower
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }

    fun autoDrive(
        speed: Double,
        leftDistanceInches: Int,
        rightDistanceInches: Int,
        timeoutMs: Int = 5000,
        vararg callbacks: () -> Unit
    ): Result<Boolean> {
        if (state != GBSBaseModuleState.IDLE) return Result.failure(
            GBSInvalidStateException("Cannot autoDrive while not idle")
        )

        return try {
            autoDriveTimer.reset()
            state = GBSBaseModuleState.AUTO_DRIVE
            autoSpeed = speed
            autoTimeoutMs = timeoutMs
            autoDriveCallbacks.clear()
            autoDriveCallbacks.addAll(callbacks)

            leftDrive.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
            rightDrive.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER

            leftDrive.targetPosition = -(leftDistanceInches * WHEELS_INCHES_TO_TICKS).roundToInt()
            rightDrive.targetPosition = -(rightDistanceInches * WHEELS_INCHES_TO_TICKS).roundToInt()

            leftDrive.mode = DcMotor.RunMode.RUN_TO_POSITION
            rightDrive.mode = DcMotor.RunMode.RUN_TO_POSITION

            leftDrive.power = abs(autoSpeed)
            rightDrive.power = abs(autoSpeed)
            Result.success(false)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun autoPower(speed: Double, leftPower: Double, rightPower: Double): Result<Boolean> {
        if (state != GBSBaseModuleState.IDLE) return Result.failure(
            GBSInvalidStateException("Cannot autoPower while not idle")
        )

        return try {
            autoDriveTimer.reset()
            state = GBSBaseModuleState.AUTO_DRIVE
            autoSpeed = speed

            leftDrive.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
            rightDrive.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER

            leftDrive.targetPosition = -(leftPower * WHEELS_INCHES_TO_TICKS).roundToInt()
            rightDrive.targetPosition = -(rightPower * WHEELS_INCHES_TO_TICKS).roundToInt()

            leftDrive.mode = DcMotor.RunMode.RUN_TO_POSITION
            rightDrive.mode = DcMotor.RunMode.RUN_TO_POSITION

            leftDrive.power = abs(autoSpeed)
            rightDrive.power = abs(autoSpeed)
            Result.success(false)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    //region Telemetry

    @GBSTelemetryDebug(group = "Base")
    fun currentState(): String = state.name

    @GBSTelemetryDebug(group = "Base")
    fun fineAdjustEnabled(): Boolean = fineAdjustMode

    @GBSTelemetryDebug(group = "Base/Motors")
    fun leftMotorPower(): Double = leftDrive.power

    @GBSTelemetryDebug(group = "Base/Motors")
    fun rightMotorPower(): Double = rightDrive.power

    @GBSTelemetryDebug(group = "Base/Motors")
    fun leftMotorPosition(): Int = leftDrive.currentPosition

    @GBSTelemetryDebug(group = "Base/Motors")
    fun rightMotorPosition(): Int = rightDrive.currentPosition

    @GBSTelemetryDebug(group = "Base/Motors")
    fun leftMotorTarget(): Int = leftDrive.targetPosition

    @GBSTelemetryDebug(group = "Base/Motors")
    fun rightMotorTarget(): Int = rightDrive.targetPosition

    @GBSTelemetryDebug(group = "Base/Auto")
    fun autoSpeed(): Double = autoSpeed

    @GBSTelemetryDebug(group = "Base/Auto")
    fun autoElapsedTime(): Double = autoDriveTimer.milliseconds()

    @GBSTelemetryDebug(group = "Base/Auto")
    fun autoTimeout(): Int = autoTimeoutMs

    //endregion
}
