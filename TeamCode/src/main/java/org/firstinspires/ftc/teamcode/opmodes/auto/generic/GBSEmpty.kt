package org.firstinspires.ftc.teamcode.opmodes.auto.generic

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode

@Suppress("unused")
@Autonomous(name = "GBSEmpty", group = "Generic")
class GBSEmpty : LinearOpMode() {
    override fun runOpMode() {
        waitForStart()

        stop()
    }
}