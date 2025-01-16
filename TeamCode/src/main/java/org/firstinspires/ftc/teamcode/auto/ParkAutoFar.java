package org.firstinspires.ftc.teamcode.auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.GlobalConstants;
import org.firstinspires.ftc.teamcode.modules.robot.RobotBase;

@Autonomous(name = "ParkAutoFar", group = "Auto")
public class ParkAutoFar extends LinearOpMode {

    @Override
    public void runOpMode() {
        RobotBase base = new RobotBase(hardwareMap, gamepad1, gamepad2);

        waitForStart();
        if (opModeIsActive()) {
            base.setMotorPowersCoordinates(0, 0, 0);
            base.setMotorPowersCoordinates(0, 0, 0);
            sleep(20000);
            base.setMotorPowersCoordinates(0.5f, 0, 0);
            base.setMotorPowersCoordinates(0, 0, 0);
            sleep(500);
            base.setMotorPowersCoordinates(0, 1, 0);
            base.setMotorPowersCoordinates(0, 0, 0);
            sleep(4250);
            idle();
            sleep(GlobalConstants.CYCLE_MS);
        }
    }

}
