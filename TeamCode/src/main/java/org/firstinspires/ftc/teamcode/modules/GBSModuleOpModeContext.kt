package org.firstinspires.ftc.teamcode.modules

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.hardware.HardwareMap
import org.firstinspires.ftc.robotcore.external.Telemetry
import org.firstinspires.ftc.teamcode.GBSGamepadPair
import org.firstinspires.ftc.teamcode.modules.actions.GBSInputManager

data class GBSModuleOpModeContext(
    val opMode: LinearOpMode,
    val hardwareMap: HardwareMap,
    val telemetry: Telemetry,
    val inputManager: GBSInputManager,
) {
    constructor(opMode: LinearOpMode) : this(
        opMode = opMode,
        hardwareMap = opMode.hardwareMap,
        telemetry = opMode.telemetry,
        GBSInputManager(GBSGamepadPair(opMode.gamepad1, opMode.gamepad2))
    )
}
