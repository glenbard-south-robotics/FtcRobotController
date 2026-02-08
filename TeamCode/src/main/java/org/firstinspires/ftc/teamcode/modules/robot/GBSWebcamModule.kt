package org.firstinspires.ftc.teamcode.modules.robot

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName
import org.firstinspires.ftc.robotcore.external.hardware.camera.controls.ExposureControl
import org.firstinspires.ftc.robotcore.external.hardware.camera.controls.GainControl
import org.firstinspires.ftc.teamcode.config.GBSWebcamModuleConfig
import org.firstinspires.ftc.teamcode.exceptions.GBSInvalidStateException
import org.firstinspires.ftc.teamcode.modules.GBSModuleOpModeContext
import org.firstinspires.ftc.teamcode.modules.GBSRobotModule
import org.firstinspires.ftc.vision.VisionPortal
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor
import java.util.concurrent.TimeUnit

class GBSWebcamModule(context: GBSModuleOpModeContext, hardware: String) :
    GBSRobotModule(context, hardware) {
    private lateinit var visionPortal: VisionPortal
    private lateinit var aprilTagProcessor: AprilTagProcessor
    var aprilTagDetections: ArrayList<AprilTagDetection> = ArrayList()

    override fun initialize(): Result<Unit> {
        return try {
            if (hardware == null) {
                return Result.failure(GBSInvalidStateException("Cannot initialize a GBSWebcamModule with null hardware!"))
            }

            aprilTagProcessor = AprilTagProcessor.Builder().build()
            aprilTagProcessor.setDecimation(GBSWebcamModuleConfig.DECIMATION)

            this.tryGetHardware<WebcamName>(hardware).fold({
                visionPortal =
                    VisionPortal.Builder()
                        .setCamera(it)
                        .setLiveViewContainerId(0)
                        .addProcessor(aprilTagProcessor)
                        .build()

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
            gainControl.gain = gain;
            opModeContext.opMode.sleep(20)
        }
    }

    override fun run(): Result<Unit> {
        val currentDetections = aprilTagProcessor.detections

        if (GBSWebcamModuleConfig.DEBUG_TELEMETRY) {
            for (detection in currentDetections) {
                opModeContext.telemetry.addLine("detectionId=${detection.id}")
                opModeContext.telemetry.addLine("rawPoseX=${detection.rawPose.x}, rawPoseX=${detection.rawPose.y}, rawPoseX=${detection.rawPose.z}")
                opModeContext.telemetry.addLine("botPosePosition=${detection.robotPose.position}, botPoseOrientation=${detection.robotPose.orientation}")
            }
        }

        aprilTagDetections = currentDetections

        return Result.success(Unit)
    }
}