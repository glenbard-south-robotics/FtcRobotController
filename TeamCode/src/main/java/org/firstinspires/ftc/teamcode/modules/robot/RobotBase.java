package org.firstinspires.ftc.teamcode.modules.robot;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
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

    private final DcMotor LANDING_GEAR;

    private final Gamepad gamepadOne, gamepadTwo;

    public RobotBase(@NonNull HardwareMap map, @NonNull Gamepad gamepadOne, @NonNull Gamepad gamepadTwo) {
        this.LEFT_FRONT_DRIVE = map.get(DcMotor.class, "left_front_drive");
        this.LEFT_BACK_DRIVE = map.get(DcMotor.class, "left_back_drive");
        this.RIGHT_FRONT_DRIVE = map.get(DcMotor.class, "right_front_drive");
        this.RIGHT_BACK_DRIVE = map.get(DcMotor.class, "right_back_drive");

        this.LANDING_GEAR = map.get(DcMotor.class, "landing_gear");

        this.gamepadOne = gamepadOne;
        this.gamepadTwo = gamepadTwo;
        this.setMotorPolicies();
    }

    public void run() {
        float axial = -gamepadOne.left_stick_y;
        float lateral = gamepadOne.left_stick_x;
        float yaw = gamepadOne.right_stick_x;

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

        if (gamepadTwo.left_bumper) {
            this.setLandingGear(-1);
        }

        if (gamepadTwo.right_bumper) {
            this.setLandingGear(1);
        }
    }

    /**
     * @name setMotorPolicies()
     * @description Changes the default policies of each motor.
     */
    private void setMotorPolicies() {
        this.LEFT_FRONT_DRIVE.setDirection(DcMotor.Direction.FORWARD);
        this.LEFT_BACK_DRIVE.setDirection(DcMotor.Direction.FORWARD);
        this.RIGHT_FRONT_DRIVE.setDirection(DcMotor.Direction.REVERSE);
        this.RIGHT_BACK_DRIVE.setDirection(DcMotor.Direction.FORWARD);

        this.LEFT_FRONT_DRIVE.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        this.LEFT_BACK_DRIVE.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        this.RIGHT_FRONT_DRIVE.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        this.RIGHT_BACK_DRIVE.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        this.LANDING_GEAR.setDirection(DcMotorSimple.Direction.FORWARD);
        this.LANDING_GEAR.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    /**
     * @name setMotorPowers()
     * @description Sets the power of all base motors. <br/>
     * Throws a <b>RuntimeException</b> if any of the motors are null.
     */
    public void setMotorPowers(float leftFrontPower, float leftBackPower, float rightFrontPower, float rightBackPower) {
        boolean slowModeActive = gamepadOne.y;
        float speed = (slowModeActive ? GlobalConstants.BASE_SENSITIVITY_SLOW : GlobalConstants.BASE_SENSITIVITY);

        leftFrontPower = CustomMathFunctions.clamp(-1, leftFrontPower * speed, 1);
        leftBackPower = CustomMathFunctions.clamp(-1, leftBackPower * speed, 1);
        rightFrontPower = CustomMathFunctions.clamp(-1, rightFrontPower * speed, 1);
        rightBackPower = CustomMathFunctions.clamp(-1, rightBackPower * speed, 1);

        this.LEFT_FRONT_DRIVE.setPower(leftFrontPower);
        this.LEFT_BACK_DRIVE.setPower(leftBackPower);
        this.RIGHT_FRONT_DRIVE.setPower(rightFrontPower);
        this.RIGHT_BACK_DRIVE.setPower(rightBackPower);
    }

    public void setMotorPowersCoordinates(float axial, float lateral, float yaw) {
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

    public void setLandingGear(float power) {
        this.LANDING_GEAR.setPower(power);
    }
}
