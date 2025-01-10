package org.firstinspires.ftc.teamcode.modules.robot;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.telemetry;

import androidx.annotation.NonNull;

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

    // State machine variables.
    private boolean CLAW_OPEN = false;
    // Motors
    private final Servo CLAW_LEFT;
    private final Servo CLAW_RIGHT;
//    private final DcMotorEx ARM_BASE;

    private final Gamepad gamepadTwo;

    public RobotArm(@NonNull HardwareMap map, @NonNull Gamepad gamepadTwo) {
        this.CLAW_LEFT = map.get(Servo.class, "claw_left");
        this.CLAW_RIGHT = map.get(Servo.class, "claw_right");
//        this.ARM_BASE = map.get(DcMotorEx.class, "arm_base");

        this.gamepadTwo = gamepadTwo;

//        this.setMotorPolicies();
    }

    public void run() {
        if (gamepadTwo.a) {
            this.openCloseClaw();
        }

        // Manually adjust the two claws on their own.
        if (gamepadTwo.left_trigger > GlobalConstants.ARM_ANALOG_THRESHOLD ||
                gamepadTwo.right_trigger > GlobalConstants.ARM_ANALOG_THRESHOLD
        ) {
            this.setClawsPosition(
                    gamepadTwo.left_trigger * GlobalConstants.ARM_CLAW_SENSITIVITY,
                    gamepadTwo.right_trigger * GlobalConstants.ARM_CLAW_SENSITIVITY
            );
        }

        // Set the power of the base motor
        if (gamepadTwo.right_stick_y >= GlobalConstants.ARM_ANALOG_BASE_THRESHOLD) {
//            this.setBasePower(gamepadTwo.right_stick_y * GlobalConstants.ARM_BASE_SENSITIVITY);
        } /*else {
            this.setBasePower(GlobalConstants.ARM_GRAVITY_DIFFERENTIAL);
        }*/

//        telemetry.addData("Arm Power", gamepadTwo.right_stick_y * GlobalConstants.ARM_BASE_SENSITIVITY);
    }

    /**
     * @name openCloseClaw()
     * @description Opens and closes the claws fully by setting the position of the claw servos to their minimum. <br/>
     * Throws a <b>RuntimeException</b> if any of the motors are null.
     */
    public void openCloseClaw() {
        // Ensure the motors exist before setting their position
        if (this.CLAW_RIGHT != null) {
            this.CLAW_RIGHT.setPosition(this.CLAW_OPEN ? 0 : 0.48);
            CLAW_OPEN = !CLAW_OPEN;
        } else {
            throw new RuntimeException("CLAW_RIGHT is null!");
        }
        if (this.CLAW_LEFT != null) {
            this.CLAW_LEFT.setPosition(this.CLAW_OPEN ? 0 : 0.5);
            CLAW_OPEN = !CLAW_OPEN;
        } else {
            throw new RuntimeException("CLAW_LEFT is null!");
        }

    }

    /**
     * @name setClawsPosition()
     * @description Set the position of each claw servo. <br/>
     * Throws a <b>RuntimeException</b> if any of the motors are null.
     */
    public void setClawsPosition(float clawRight, float clawLeft) {
        clawRight = CustomMathFunctions.clamp(0, clawRight, 1);
        clawLeft = CustomMathFunctions.clamp(0, clawLeft, 1);

        // Ensure the motors exist before setting their position
        if (this.CLAW_RIGHT != null) {
            this.CLAW_RIGHT.setPosition(clawRight);
            CLAW_OPEN = !CLAW_OPEN;
        } else {
            throw new RuntimeException("CLAW_RIGHT is null!");
        }
        if (this.CLAW_LEFT != null) {
            this.CLAW_LEFT.setPosition(clawLeft);
            CLAW_OPEN = !CLAW_OPEN;
        } else {
            throw new RuntimeException("CLAW_LEFT is null!");
        }

    }

    /**
     * @name setBasePower()
     * @description Set the power of the base motor. <br/>
     * Throws a <b>RuntimeException</b> if any of the motors are null.
     */
//    public void setBasePower(float basePower) {
//        basePower = CustomMathFunctions.clamp(0, basePower, 1);
//
//        // Ensure the motor exists.
//        if (this.ARM_BASE != null) {
//            this.ARM_BASE.setPower(basePower);
//        } else {
//            throw new RuntimeException("ARM_BASE is null!");
//        }
//    }

    /**
     * @name setMotorPolicies()
     * @description Changes the default policies of each motor.
     */
//    private void setMotorPolicies() {
//        this.ARM_BASE.setDirection(DcMotor.Direction.REVERSE);
//        this.ARM_BASE.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
//    }
}