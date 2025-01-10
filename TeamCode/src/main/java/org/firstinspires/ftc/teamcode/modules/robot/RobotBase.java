package org.firstinspires.ftc.teamcode.modules.robot;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.gamepad1;
import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.hardwareMap;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.GlobalConstants;
import org.firstinspires.ftc.teamcode.modules.CustomMathFunctions;

/**
 * @name RobotBase
 * @description This class controls the motors found on the robot using human input.
 */
public class RobotBase {
    private final DcMotor LEFT_FRONT_DRIVE;
    private final DcMotor LEFT_BACK_DRIVE;
    private final DcMotor RIGHT_FRONT_DRIVE;
    private final DcMotor RIGHT_BACK_DRIVE;

    public RobotBase(@NonNull HardwareMap map) {
        this.LEFT_FRONT_DRIVE = map.get(DcMotor.class, "left_front_drive");
        this.LEFT_BACK_DRIVE = map.get(DcMotor.class, "left_back_drive");
        this.RIGHT_FRONT_DRIVE = map.get(DcMotor.class, "right_front_drive");
        this.RIGHT_BACK_DRIVE = hardwareMap.get(DcMotor.class, "right_back_drive");
        this.setMotorPolicies();
    }

    public void run() {
        float axial = -gamepad1.left_stick_y;
        float lateral = gamepad1.left_stick_x;
        float yaw = gamepad1.right_stick_x;

        float leftFrontPower = axial + lateral + yaw;
        float leftBackPower = axial - lateral + yaw;
        float rightFrontPower = axial - lateral - yaw;
        float rightBackPower = axial + lateral - yaw;

        setMotorPowers(
                leftFrontPower,
                leftBackPower,
                rightFrontPower,
                rightBackPower
        );
    }

    /**
     * @name setMotorPolicies()
     * @description Changes the default policies of each motor.
     */
    private void setMotorPolicies() {
        this.LEFT_FRONT_DRIVE.setDirection(DcMotor.Direction.REVERSE);
        this.LEFT_BACK_DRIVE.setDirection(DcMotor.Direction.FORWARD);
        this.RIGHT_FRONT_DRIVE.setDirection(DcMotor.Direction.REVERSE);
        this.RIGHT_BACK_DRIVE.setDirection(DcMotor.Direction.FORWARD);

        this.LEFT_FRONT_DRIVE.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        this.LEFT_BACK_DRIVE.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        this.RIGHT_FRONT_DRIVE.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        this.RIGHT_BACK_DRIVE.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    /**
     * @name setMotorPowers()
     * @description Sets the power of all base motors. <br/>
     * Throws a <b>RuntimeException</b> if any of the motors are null.
     */
    public void setMotorPowers(float leftFrontPower, float leftBackPower, float rightFrontPower, float rightBackPower) {
        leftFrontPower = CustomMathFunctions.clamp(0, leftFrontPower * GlobalConstants.BASE_SENSITIVITY, 1);
        leftBackPower = CustomMathFunctions.clamp(0, leftBackPower * GlobalConstants.BASE_SENSITIVITY, 1);
        rightFrontPower = CustomMathFunctions.clamp(0, rightFrontPower * GlobalConstants.BASE_SENSITIVITY, 1);
        rightBackPower = CustomMathFunctions.clamp(0, rightBackPower * GlobalConstants.BASE_SENSITIVITY, 1);

        this.LEFT_FRONT_DRIVE.setPower(leftFrontPower);
        this.LEFT_BACK_DRIVE.setPower(leftBackPower);
        this.RIGHT_FRONT_DRIVE.setPower(rightFrontPower);
        this.RIGHT_BACK_DRIVE.setPower(rightBackPower);
    }
}
