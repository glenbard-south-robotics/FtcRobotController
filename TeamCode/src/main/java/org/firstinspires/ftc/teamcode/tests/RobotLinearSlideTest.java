package org.firstinspires.ftc.teamcode.tests;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.GlobalConstants;
import org.firstinspires.ftc.teamcode.modules.behaviors.ModularOpMode;
import org.firstinspires.ftc.teamcode.modules.robot.RobotLinearSlide;

@TeleOp(name = "RobotLinearSlideTest", group = "Tests")
public class RobotLinearSlideTest extends ModularOpMode {

    @Override
    public void runOpMode() {
        useModules(RobotLinearSlide.class);

        waitForStart();
        while (opModeIsActive()) {
            executeModules();
            idle();
            sleep(GlobalConstants.CYCLE_MS);
        }
    }
}