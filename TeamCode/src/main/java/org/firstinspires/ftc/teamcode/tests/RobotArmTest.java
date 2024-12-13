package org.firstinspires.ftc.teamcode.tests;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.GlobalConstants;import org.firstinspires.ftc.teamcode.modules.behaviors.DefaultModuleBehaviorCollector;
import org.firstinspires.ftc.teamcode.modules.robot.RobotArm;

@TeleOp(name = "RobotArmTest", group = "Tests")
public class RobotArmTest extends LinearOpMode {
    private final DefaultModuleBehaviorCollector collector = new DefaultModuleBehaviorCollector();

    @Override
    public void runOpMode() {
        // Tell the DefaultModuleBehaviorCollector to instantiate all methods
        collector.collect(RobotArm.class);

        // Run the main loop
        waitForStart();
        while (opModeIsActive()) {
            collector.executeAll();
            sleep(GlobalConstants.CYCLE_MS);
            idle();
        }
    }
}