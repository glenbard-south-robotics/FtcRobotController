package org.firstinspires.ftc.teamcode.tests;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.GlobalConstants;
import org.firstinspires.ftc.teamcode.modules.behaviors.ModularOpMode;
import org.firstinspires.ftc.teamcode.modules.robot.RobotBase;

@TeleOp(name = "RobotBaseTest", group = "Tests")
public class RobotBaseTest extends ModularOpMode {

    @Override
    public void runOpMode() {
        useModules(RobotBase.class);

        waitForStart();
        while (opModeIsActive()) {
            executeModules();
            idle();
            sleep(GlobalConstants.CYCLE_MS);
        }
    }
}