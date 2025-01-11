package org.firstinspires.ftc.teamcode.modules.robot;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

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
    private final DcMotor ARM_BASE;

    private final Gamepad gamepadTwo;

    public RobotArm(@NonNull HardwareMap map, @NonNull Gamepad gamepadTwo) {
        this.CLAW_LEFT = map.get(Servo.class, "claw_left");
        this.CLAW_RIGHT = map.get(Servo.class, "claw_right");
        this.ARM_BASE = map.get(DcMotor.class, "arm_base");

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

        // Manually adjust the two claws on their own.
        //if (gamepadTwo.left_trigger > GlobalConstants.ARM_ANALOG_THRESHOLD ||
        //       gamepadTwo.right_trigger > GlobalConstants.ARM_ANALOG_THRESHOLD
        //) {
        //   this.setClawsPosition(
        //           gamepadTwo.right_trigger * GlobalConstants.ARM_CLAW_SENSITIVITY,
        //           gamepadTwo.left_trigger * GlobalConstants.ARM_CLAW_SENSITIVITY
        //   );
        //}

        // Set the power of the base motor
        if (Math.abs(gamepadTwo.right_stick_y) >= GlobalConstants.ARM_ANALOG_BASE_THRESHOLD) {
            this.setBasePower(gamepadTwo.right_stick_y * GlobalConstants.ARM_BASE_SENSITIVITY);
        }
    }

    double CLAW_RIGHT_OPEN = 0.50;
    double CLAW_RIGHT_CLOSED = 0.1;
    double CLAW_LEFT_OPEN = 0;
    double CLAW_LEFT_CLOSED = 0.50;

    public void openClaw() {
        this.CLAW_RIGHT.setPosition(CLAW_RIGHT_OPEN);
        this.CLAW_LEFT.setPosition(CLAW_LEFT_OPEN);
    }

    public void closeClaw() {
        this.CLAW_RIGHT.setPosition(CLAW_RIGHT_CLOSED);
        this.CLAW_LEFT.setPosition(CLAW_LEFT_CLOSED);
    }

//    public void setClawsPosition(float clawRight, float clawLeft) {
//        clawRight = CustomMathFunctions.clamp(0, clawRight, 1);
//        clawLeft = CustomMathFunctions.clamp(0, clawLeft, 1);
//
//        // Ensure the motors exist before setting their position
//        if (this.CLAW_RIGHT != null) {
//            this.CLAW_RIGHT.setPosition(clawRight);
//        } else {
//            throw new RuntimeException("CLAW_RIGHT is null!");
//        }
//        if (this.CLAW_LEFT != null) {
//            this.CLAW_LEFT.setPosition(clawLeft);
//        } else {
//            throw new RuntimeException("CLAW_LEFT is null!");
//        }
//
//    }

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