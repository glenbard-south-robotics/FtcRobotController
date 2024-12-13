package org.firstinspires.ftc.teamcode.modules.robotbase;

import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * @name RobotBaseMotors
 * @description Interface for the motors of the RobotArm class.
 * @see RobotBase
 */
public class RobotBaseMotors {
    DcMotor LEFT_FRONT_DRIVE;
    DcMotor LEFT_BACK_DRIVE;
    DcMotor RIGHT_FRONT_DRIVE;
    DcMotor RIGHT_BACK_DRIVE;

    public RobotBaseMotors(DcMotor leftFrontDrive, DcMotor leftBackDrive, DcMotor rightFrontDrive, DcMotor rightBackDrive) {
        this.LEFT_FRONT_DRIVE = leftFrontDrive;
        this.LEFT_BACK_DRIVE = leftBackDrive;
        this.RIGHT_FRONT_DRIVE = rightFrontDrive;
        this.RIGHT_BACK_DRIVE = rightBackDrive;
    }
}
