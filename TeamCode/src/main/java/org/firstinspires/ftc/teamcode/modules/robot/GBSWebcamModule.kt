package org.firstinspires.ftc.teamcode.modules.robot

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName
import org.firstinspires.ftc.robotcore.external.hardware.camera.controls.ExposureControl
import org.firstinspires.ftc.robotcore.external.hardware.camera.controls.GainControl
import org.firstinspires.ftc.teamcode.exceptions.GBSHardwareMissingException
import org.firstinspires.ftc.teamcode.modules.GBSModuleContext
import org.firstinspires.ftc.teamcode.modules.GBSRobotModule
import org.firstinspires.ftc.vision.VisionPortal
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor
import java.util.ArrayList
import java.util.concurrent.TimeUnit

class GBSWebcamModule(context: GBSModuleContext, hardware: String) : GBSRobotModule(context, hardware) {
    private lateinit var visionPortal: VisionPortal
    private lateinit var aprilTagProcessor: AprilTagProcessor
    var aprilTagDetections: ArrayList<AprilTagDetection> = ArrayList()

    var tag: AprilTagDetection? = null

    override fun initialize(): Result<Unit> {
        return try {
            aprilTagProcessor = AprilTagProcessor.Builder().build()
            aprilTagProcessor.setDecimation(2.0F)

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
            context.telemetry.addLine("[ERR]: An exception was raised in GBSHopperModule::init: ${e.message}")
            context.telemetry.update()
            Result.failure(e)
        }
    }

    fun setExposure(exposureMs: Int, gain: Int) {
        if (visionPortal.cameraState != VisionPortal.CameraState.STREAMING) {
            while (!context.opMode.isStopRequested && (visionPortal.cameraState != VisionPortal.CameraState.STREAMING)) {
                context.opMode.sleep(20)
            }
        }

        if (!context.opMode.isStopRequested) {
            val exposureControl = visionPortal.getCameraControl(ExposureControl::class.java)

            if (exposureControl.mode != ExposureControl.Mode.Manual) {
                exposureControl.setMode(ExposureControl.Mode.Manual)
                context.opMode.sleep(50)
            }

            exposureControl.setExposure(exposureMs.toLong(), TimeUnit.MILLISECONDS)
            context.opMode.sleep(20)
            val gainControl = visionPortal.getCameraControl(GainControl::class.java)
            gainControl.setGain(gain);
            context.opMode.sleep(20)
        }
    }

    override fun run(): Result<Unit> {
        val currentDetections = aprilTagProcessor.detections

        for (detection in currentDetections) {
            context.telemetry.addLine("detectionId=${detection.id}")
            context.telemetry.addLine("rawPoseX=${detection.rawPose.x}, rawPoseX=${detection.rawPose.y}, rawPoseX=${detection.rawPose.z}")
            context.telemetry.addLine("botPosePosition=${detection.robotPose.position}, botPoseOrientation=${detection.robotPose.orientation}")
        }

        aprilTagDetections = currentDetections

        return Result.success(Unit)
    }


}