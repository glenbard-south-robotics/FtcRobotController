package org.firstinspires.ftc.teamcode.modules

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.hardware.HardwareMap
import org.firstinspires.ftc.robotcore.external.Telemetry
import org.firstinspires.ftc.teamcode.GBSGamepadPair

data class GBSModuleContext(
    val opMode: LinearOpMode,
    val hardwareMap: HardwareMap,
    val telemetry: Telemetry,
    val gamepads: GBSGamepadPair
) {
    constructor(opMode: LinearOpMode) : this(
        opMode = opMode,
        hardwareMap = opMode.hardwareMap,
        telemetry = opMode.telemetry,
        gamepads = GBSGamepadPair(opMode.gamepad1, opMode.gamepad2)
    )
}
