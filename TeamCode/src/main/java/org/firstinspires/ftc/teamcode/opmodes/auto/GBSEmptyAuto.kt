package org.firstinspires.ftc.teamcode.opmodes.auto

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import org.firstinspires.ftc.teamcode.CYCLE_MS

@Autonomous(name = "GBSEmptyAuto")
class GBSEmptyAuto : LinearOpMode() {
    override fun runOpMode() {
        waitForStart()

        // Do nothing!
        while (opModeIsActive()) {
            idle()
            sleep(CYCLE_MS)
        }
    }
}