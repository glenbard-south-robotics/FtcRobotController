package org.firstinspires.ftc.teamcode.modules.robot;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.gamepad1;
import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.hardwareMap;

import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.GlobalConstants;
import org.firstinspires.ftc.teamcode.modules.CustomMathFunctions;
import org.firstinspires.ftc.teamcode.modules.behaviors.DefaultModuleBehavior;
import org.firstinspires.ftc.teamcode.modules.behaviors.RobotModule;

/**
 * @name RobotBase
 * @description This class controls the motors found on the robot using human input.
 * @implNote This is automatically called by DefaultModuleBehaviorCollector.
 * @see org.firstinspires.ftc.teamcode.modules.behaviors.DefaultModuleBehaviorCollector
 */
public class RobotBase implements RobotModule {
    private final DcMotor LEFT_FRONT_DRIVE = hardwareMap.get(DcMotor.class, "left_front_drive");
    private final DcMotor LEFT_BACK_DRIVE = hardwareMap.get(DcMotor.class, "left_back_drive");
    private final DcMotor RIGHT_FRONT_DRIVE = hardwareMap.get(DcMotor.class, "_front_drive");
    private final DcMotor RIGHT_BACK_DRIVE = hardwareMap.get(DcMotor.class, "right_back_drive");

    public RobotBase() {
        this.setMotorPolicies();
    }

    @DefaultModuleBehavior
    public void defaultModuleBehavior() {
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
