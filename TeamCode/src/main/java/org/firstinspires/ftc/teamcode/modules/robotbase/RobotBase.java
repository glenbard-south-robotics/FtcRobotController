package org.firstinspires.ftc.teamcode.modules.robotbase;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.gamepad1;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.teamcode.GlobalConstants;
import org.firstinspires.ftc.teamcode.modules.CustomMathFunctions;
import org.firstinspires.ftc.teamcode.modules.DefaultModuleBehavior;
import org.firstinspires.ftc.teamcode.modules.RobotModule;

/**
 * @name RobotBase
 * @description Takes in motors, and controller inputs and powers the respective motors.
 * @see RobotBaseMotors
 */
public class RobotBase implements RobotModule {
    private RobotBaseMotors motors = null;

    public RobotBase(RobotBaseMotors motors) {
        this.motors = motors;
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
        this.motors.LEFT_FRONT_DRIVE.setDirection(DcMotor.Direction.REVERSE);
        this.motors.LEFT_BACK_DRIVE.setDirection(DcMotor.Direction.FORWARD);
        this.motors.RIGHT_FRONT_DRIVE.setDirection(DcMotor.Direction.REVERSE);
        this.motors.RIGHT_BACK_DRIVE.setDirection(DcMotor.Direction.FORWARD);

        this.motors.LEFT_FRONT_DRIVE.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        this.motors.LEFT_BACK_DRIVE.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        this.motors.RIGHT_FRONT_DRIVE.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        this.motors.RIGHT_BACK_DRIVE.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
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

        this.motors.LEFT_FRONT_DRIVE.setPower(leftFrontPower);
        this.motors.LEFT_BACK_DRIVE.setPower(leftBackPower);
        this.motors.RIGHT_FRONT_DRIVE.setPower(rightFrontPower);
        this.motors.RIGHT_BACK_DRIVE.setPower(rightBackPower);
    }

}
