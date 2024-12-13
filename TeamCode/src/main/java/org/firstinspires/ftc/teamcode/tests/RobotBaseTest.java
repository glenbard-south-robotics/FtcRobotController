package org.firstinspires.ftc.teamcode.tests;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.GlobalConstants;import org.firstinspires.ftc.teamcode.modules.behaviors.DefaultModuleBehaviorCollector;
import org.firstinspires.ftc.teamcode.modules.robot.RobotBase;

@TeleOp(name = "RobotBaseTest", group = "Tests")
public class RobotBaseTest extends LinearOpMode {
    private final DefaultModuleBehaviorCollector collector = new DefaultModuleBehaviorCollector();

    @Override
    public void runOpMode() {
        // Tell the DefaultModuleBehaviorCollector to instantiate all methods
        collector.collect(RobotBase.class);

        // Run the main loop
        waitForStart();
        while (opModeIsActive()) {
            collector.executeAll();
            sleep(GlobalConstants.CYCLE_MS);
            idle();
        }
    }
}