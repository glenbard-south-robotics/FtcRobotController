package org.firstinspires.ftc.teamcode.opmodes.auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.GlobalConstants;
import org.firstinspires.ftc.teamcode.modules.robot.deprecated.RobotBase;

@Autonomous(name = "ParkAutoClose", group = "Auto")
public class ParkAutoClose extends LinearOpMode {

    @Override
    public void runOpMode() {
        RobotBase base = new RobotBase(hardwareMap, gamepad1, gamepad2);

        waitForStart();
        if (opModeIsActive()) {
            base.setMotorPowersCoordinates(0, 0, 0);
            sleep(1000);
            base.setMotorPowersCoordinates(0, 0, 0);
            base.setMotorPowersCoordinates(0.5f, 0, 0);
            sleep(500);
            base.setMotorPowersCoordinates(0, 0, 0);
            base.setMotorPowersCoordinates(0, 1, 0);
            sleep(3000);
            base.setMotorPowersCoordinates(0, 0, 0);
            idle();
            sleep(GlobalConstants.CYCLE_MS);
        }
    }

}
