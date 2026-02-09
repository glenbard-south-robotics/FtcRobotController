package org.firstinspires.ftc.teamcode.opmodes.auto.blue

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import org.firstinspires.ftc.teamcode.config.GBS6ArtifactCloseBlueConfiguration
import org.firstinspires.ftc.teamcode.modules.GBSModuleOpModeContext
import org.firstinspires.ftc.teamcode.modules.robot.GBSBaseModule
import org.firstinspires.ftc.teamcode.modules.robot.GBSFlywheelModule
import org.firstinspires.ftc.teamcode.modules.robot.GBSIntakeModule
import org.firstinspires.ftc.teamcode.modules.robot.GBSWebcamModule
import org.firstinspires.ftc.teamcode.opmodes.GBSOpMode
import kotlin.math.abs

@Suppress("unused")
@Autonomous(name = "GBS6ArtifactCloseBlue", group = "Blue")
class GBS6ArtifactCloseBlue() : GBSOpMode() {
    override fun initialize(): Result<Unit> {
        val context = GBSModuleOpModeContext(this)

        this.registerModules(
            "base" to GBSBaseModule(context),
            "flywheel" to GBSFlywheelModule(context),
            "intake" to GBSIntakeModule(context),
            "webcam2" to GBSWebcamModule(context, "webcam2")
        )

        return Result.success(Unit)
    }

    fun fireThree() {
        val intake = getModule<GBSIntakeModule>("intake")

        intake.autoIntakeForward()
        sleep(5000)
        intake.autoIntakeStop()
    }

    fun lineUpAprilTag(timeoutMs: Long = 1500) {
        val base = getModule<GBSBaseModule>("base")
        val webcam = getModule<GBSWebcamModule>("webcam2")

        val startTime = System.currentTimeMillis()

        while (opModeIsActive() && System.currentTimeMillis() - startTime < timeoutMs) {
            if (webcam.aprilTagDetections.isEmpty()) {
                base.autoPower(0.0, 0.0, 0.0)
                sleep(10)
                continue
            }

            val tag = webcam.aprilTagDetections.first()
            val errorYaw =
                GBS6ArtifactCloseBlueConfiguration.DESIRED_TAG_ORIENTATION.yaw - tag.robotPose.orientation.yaw

            if (abs(errorYaw) <= GBS6ArtifactCloseBlueConfiguration.ERROR_EPSILON) {
                break
            }

            val turn = (errorYaw * GBS6ArtifactCloseBlueConfiguration.PID_K_P).coerceIn(-0.3, 0.3)

            base.autoPower(
                GBS6ArtifactCloseBlueConfiguration.ERROR_CORRECTION_SPEED, -turn, turn
            )

            sleep(10)
        }

        base.autoPower(0.0, 0.0, 0.0)
    }

    override fun runLinear(): Result<Unit> {
        val base = getModule<GBSBaseModule>("base")
        val flywheel = getModule<GBSFlywheelModule>("flywheel")
        val intake = getModule<GBSIntakeModule>("intake")

        flywheel.autoFlywheelOn()
        sleep(GBS6ArtifactCloseBlueConfiguration.SPINUP_MS)

        base.autoDrive(
            GBS6ArtifactCloseBlueConfiguration.BASE_POWER,
            GBS6ArtifactCloseBlueConfiguration.MOTOR_DISTANCES.first,
            GBS6ArtifactCloseBlueConfiguration.MOTOR_DISTANCES.second,
            4000,
            {
                base.autoDrive(
                    GBS6ArtifactCloseBlueConfiguration.BASE_POWER, 12, -12, 2000, {
                        lineUpAprilTag()
                        fireThree()

                        base.autoDrive(
                            GBS6ArtifactCloseBlueConfiguration.BASE_POWER,
                            GBS6ArtifactCloseBlueConfiguration.SPIKE_MOTOR_DISTANCES.first,
                            GBS6ArtifactCloseBlueConfiguration.SPIKE_MOTOR_DISTANCES.second,
                            3000,
                            {
                                intake.autoIntakeForward()
                                base.autoDrive(
                                    GBS6ArtifactCloseBlueConfiguration.SPIKE_FORWARD_SPEED,
                                    GBS6ArtifactCloseBlueConfiguration.SPIKE_FORWARD_DISTANCE,
                                    GBS6ArtifactCloseBlueConfiguration.SPIKE_FORWARD_DISTANCE,
                                    2500,
                                    {
                                        sleep(400)
                                        intake.autoIntakeStop()

                                        base.autoDrive(
                                            GBS6ArtifactCloseBlueConfiguration.BASE_POWER,
                                            -GBS6ArtifactCloseBlueConfiguration.SPIKE_FORWARD_DISTANCE,
                                            -GBS6ArtifactCloseBlueConfiguration.SPIKE_FORWARD_DISTANCE,
                                            2500,
                                            {
                                                base.autoDrive(
                                                    GBS6ArtifactCloseBlueConfiguration.BASE_POWER,
                                                    -GBS6ArtifactCloseBlueConfiguration.SPIKE_MOTOR_DISTANCES.first,
                                                    -GBS6ArtifactCloseBlueConfiguration.SPIKE_MOTOR_DISTANCES.second,
                                                    3000,
                                                    {
                                                        lineUpAprilTag()
                                                        fireThree()
                                                    })
                                            })
                                    })
                            })
                    })
            })

        return Result.success(Unit)
    }

    override fun runLoop(): Result<Unit> {

        return Result.success(Unit)
    }

    override fun shutdown(): Result<Unit> {
        return Result.success(Unit)
    }
}
