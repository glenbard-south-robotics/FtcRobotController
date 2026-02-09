package org.firstinspires.ftc.teamcode.modules.robot

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName
import org.firstinspires.ftc.robotcore.external.hardware.camera.controls.ExposureControl
import org.firstinspires.ftc.robotcore.external.hardware.camera.controls.GainControl
import org.firstinspires.ftc.teamcode.config.GBSWebcamModuleConfig
import org.firstinspires.ftc.teamcode.modules.GBSModuleOpModeContext
import org.firstinspires.ftc.teamcode.modules.GBSRobotModule
import org.firstinspires.ftc.teamcode.modules.telemetry.GBSTelemetryDebug
import org.firstinspires.ftc.vision.VisionPortal
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor
import java.util.concurrent.TimeUnit

@Suppress("unused")
class GBSWebcamModule(context: GBSModuleOpModeContext, val hardware: String) :
    GBSRobotModule(context, GBSWebcamModuleConfig) {

    private lateinit var visionPortal: VisionPortal
    private lateinit var aprilTagProcessor: AprilTagProcessor
    var aprilTagDetections: ArrayList<AprilTagDetection> = ArrayList()

    val decimation = GBSWebcamModuleConfig.DECIMATION

    override fun initialize(): Result<Unit> {
        return try {
            aprilTagProcessor = AprilTagProcessor.Builder().build()
            aprilTagProcessor.setDecimation(decimation)

            this.tryGetHardware<WebcamName>(hardware).fold({
                visionPortal = VisionPortal.Builder().setCamera(it).setLiveViewContainerId(0)
                    .addProcessor(aprilTagProcessor).build()

            }, {
                throw it
            })

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun setExposure(exposureMs: Int, gain: Int) {
        if (visionPortal.cameraState != VisionPortal.CameraState.STREAMING) {
            while (!opModeContext.opMode.isStopRequested && (visionPortal.cameraState != VisionPortal.CameraState.STREAMING)) {
                opModeContext.opMode.sleep(20)
            }
        }

        if (!opModeContext.opMode.isStopRequested) {
            val exposureControl = visionPortal.getCameraControl(ExposureControl::class.java)

            if (exposureControl.mode != ExposureControl.Mode.Manual) {
                exposureControl.mode = ExposureControl.Mode.Manual
                opModeContext.opMode.sleep(50)
            }

            exposureControl.setExposure(exposureMs.toLong(), TimeUnit.MILLISECONDS)
            opModeContext.opMode.sleep(20)
            val gainControl = visionPortal.getCameraControl(GainControl::class.java)
            gainControl.gain = gain
            opModeContext.opMode.sleep(20)
        }
    }

    override fun run(): Result<Unit> {
        aprilTagDetections = aprilTagProcessor.detections

        return Result.success(Unit)
    }

    //region Telemetry

    private fun Double.format(digits: Int) = "%.${digits}f".format(this)

    @GBSTelemetryDebug(group = "Vision")
    fun cameraState(): VisionPortal.CameraState = visionPortal.cameraState

    @GBSTelemetryDebug(group = "Vision")
    fun tagCount(): Int = aprilTagDetections.size

    @GBSTelemetryDebug(group = "Vision")
    fun detectionIds(): String = aprilTagDetections.joinToString(", ") { it.id.toString() }

    @GBSTelemetryDebug(group = "Vision")
    fun detectionPoses(): String = aprilTagDetections.joinToString("\n") { det ->
        val p = det.rawPose
        "ID:${det.id} x=${p.x.format(2)} y=${p.y.format(2)} z=${p.z.format(2)}"
    }

    @GBSTelemetryDebug(group = "Vision")
    fun robotRelativePoses(): String = aprilTagDetections.joinToString("\n") { det ->
        val pos = det.robotPose.position
        val ori = det.robotPose.orientation
        "ID:${det.id} pos=(${pos.x.format(2)}, ${pos.y.format(2)}, ${pos.z.format(2)}) " + "ori=(yaw:${
            ori.yaw.format(
                1
            )
        }, pitch:${ori.pitch.format(1)}, roll:${
            ori.roll.format(
                1
            )
        })"
    }

    //endregion
}