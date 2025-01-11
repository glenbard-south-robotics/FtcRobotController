package org.firstinspires.ftc.teamcode.modules.robot;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.FFlags;
import org.firstinspires.ftc.teamcode.GlobalConstants;
import org.firstinspires.ftc.teamcode.modules.CustomMathFunctions;

/**
 * @name RobotArm
 * @description This class controls the motors found on the robot using human input.
 */
public class RobotArm {
    // Motors
    private final Servo CLAW_LEFT;
    private final Servo CLAW_RIGHT;
    private final DcMotorEx ARM_BASE;

    private final Gamepad gamepadTwo;

    public RobotArm(@NonNull HardwareMap map, @NonNull Gamepad gamepadTwo) {
        this.CLAW_LEFT = map.get(Servo.class, "claw_left");
        this.CLAW_RIGHT = map.get(Servo.class, "claw_right");
        this.ARM_BASE = map.get(DcMotorEx.class, "arm_base");

        this.gamepadTwo = gamepadTwo;

        this.setMotorPolicies();
    }

    public void run() {
        if (gamepadTwo.a) {
            openClaw();
        }
        if (gamepadTwo.b) {
            closeClaw();
        }


        // Set the power of the base motor
        if (Math.abs(gamepadTwo.right_stick_y) >= GlobalConstants.ARM_ANALOG_BASE_THRESHOLD) {
            if (!FFlags.ARM_GRAVITY_COMPENSATION) {
                boolean slowModeActive = gamepadTwo.y;
                float speed = gamepadTwo.right_stick_y * (slowModeActive ? GlobalConstants.ARM_BASE_SENSITIVITY_SLOW : GlobalConstants.ARM_BASE_SENSITIVITY);

                this.setBasePower(speed);
            } else {
                double velocity = this.ARM_BASE.getVelocity(AngleUnit.DEGREES);

                // Desired speed in degrees per second.
                float desiredVelocity = 18f;
                float velocityDifference = (float) (desiredVelocity - velocity);

                // The velocity difference being less than 0 means that the arm is too fast.
                if (velocityDifference < 0) {
                    float brakingCoefficient = CustomMathFunctions.clamp(-1, 0.5f * velocityDifference, 1);


                    float speed = gamepadTwo.right_stick_y * GlobalConstants.ARM_BASE_SENSITIVITY;

                    this.setBasePower(speed);
                }
            }

            this.ARM_BASE.getVelocity();
        }
    }

    //!FIXME Names are incorrect.
    double CLAW_RIGHT_OPEN = 0.7; // closed
    double CLAW_RIGHT_CLOSED = 0.1; // opens
    double CLAW_LEFT_OPEN = 0; // closed
    double CLAW_LEFT_CLOSED = 0.6; // opens

    // A - closes
    public void openClaw() {
        this.CLAW_RIGHT.setPosition(CLAW_RIGHT_OPEN);
        this.CLAW_LEFT.setPosition(CLAW_LEFT_OPEN);
    }

    // B - opens
    public void closeClaw() {
        this.CLAW_RIGHT.setPosition(CLAW_RIGHT_CLOSED);
        this.CLAW_LEFT.setPosition(CLAW_LEFT_CLOSED);
    }

    /**
     * @name setBasePower()
     * @description Set the power of the base motor. <br/>
     * Throws a <b>RuntimeException</b> if any of the motors are null.
     */
    public void setBasePower(float basePower) {
        basePower = CustomMathFunctions.clamp(-1, basePower, 1);

        // Ensure the motor exists.
        if (this.ARM_BASE != null) {
            this.ARM_BASE.setPower(basePower);
        } else {
            throw new RuntimeException("ARM_BASE is null!");
        }
    }

    /**
     * @name setMotorPolicies()
     * @description Changes the default policies of each motor.
     */
    private void setMotorPolicies() {
        this.ARM_BASE.setDirection(DcMotor.Direction.REVERSE);
        this.ARM_BASE.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }
}