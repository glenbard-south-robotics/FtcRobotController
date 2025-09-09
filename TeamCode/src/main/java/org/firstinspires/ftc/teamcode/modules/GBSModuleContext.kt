package org.firstinspires.ftc.teamcode.modules

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.hardware.HardwareMap
import org.firstinspires.ftc.robotcore.external.Telemetry

data class GBSModuleContext(
    val opMode: LinearOpMode,
    val hardwareMap: HardwareMap,
    val telemetry: Telemetry
)