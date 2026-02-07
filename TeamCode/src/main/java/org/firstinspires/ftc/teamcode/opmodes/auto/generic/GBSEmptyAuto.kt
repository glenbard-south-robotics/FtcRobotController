package org.firstinspires.ftc.teamcode.opmodes.auto.generic

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode

@Autonomous(name = "GBSEmptyAuto", group = "Generic")
class GBSEmptyAuto : LinearOpMode() {
    override fun runOpMode() {
        waitForStart()

        stop()
    }
}