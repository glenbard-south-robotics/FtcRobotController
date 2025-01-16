package org.firstinspires.ftc.teamcode.teleop;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.GlobalConstants;
import org.firstinspires.ftc.teamcode.modules.robot.RobotArm;
import org.firstinspires.ftc.teamcode.modules.robot.RobotBase;
import org.firstinspires.ftc.teamcode.modules.robot.RobotLinearSlide;

@TeleOp(name = "RobotTeleop", group = "Teleop")
public class RobotTeleop extends LinearOpMode {
    @Override
    public void runOpMode() {
        RobotArm arm = new RobotArm(hardwareMap, gamepad2);
        RobotBase base = new RobotBase(hardwareMap, gamepad1, gamepad2);
        RobotLinearSlide slide = new RobotLinearSlide(hardwareMap, gamepad2);

        waitForStart();
        while (opModeIsActive()) {
            arm.run();
            base.run();
            slide.run();
            idle();
            sleep(GlobalConstants.CYCLE_MS);
        }
    }

}
