package org.firstinspires.ftc.teamcode.tests;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.GlobalConstants;
import org.firstinspires.ftc.teamcode.modules.behaviors.ModularOpMode;
import org.firstinspires.ftc.teamcode.modules.robot.RobotArm;

@TeleOp(name = "RobotArmTest", group = "Tests")
public class RobotArmTest extends ModularOpMode {

    @Override
    public void runOpMode() {
        useModules(RobotArm.class);

        waitForStart();
        while (opModeIsActive()) {
            executeModules();
            idle();
            sleep(GlobalConstants.CYCLE_MS);
        }
    }
}