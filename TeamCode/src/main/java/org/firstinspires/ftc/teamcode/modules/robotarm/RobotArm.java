package org.firstinspires.ftc.teamcode.modules.robotarm;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.gamepad2;

import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.GlobalConstants;
import org.firstinspires.ftc.teamcode.modules.CustomMathFunctions;
import org.firstinspires.ftc.teamcode.modules.DefaultModuleBehavior;
import org.firstinspires.ftc.teamcode.modules.RobotModule;

/**
 * @name RobotArm
 * @description Takes in motors, and controller inputs and powers the respective motors.
 * @see RobotArmMotors
 */
public class RobotArm implements RobotModule {
    private final RobotArmMotors motors;

    // State machine variables.
    private boolean CLAW_OPEN = false;

    public RobotArm(RobotArmMotors motors) {
        this.motors = motors;
        this.setMotorPolicies();
    }

    @DefaultModuleBehavior
    public void defaultModuleBehavior() {
        if (gamepad2.a) {
            this.openCloseClaw();
        }

        // Manually adjust the two claws on their own.
        if (gamepad2.left_trigger > GlobalConstants.ARM_ANALOG_THRESHOLD ||
                gamepad2.right_trigger > GlobalConstants.ARM_ANALOG_THRESHOLD
        ) {
            this.setClawsPosition(
                    gamepad2.left_trigger * GlobalConstants.ARM_CLAW_SENSITIVITY,
                    gamepad2.right_trigger * GlobalConstants.ARM_CLAW_SENSITIVITY
            );
        }

        // Set the power of the base motor
        if (gamepad2.right_stick_y >= GlobalConstants.ARM_ANALOG_BASE_THRESHOLD) {
            this.setBasePower(gamepad2.right_stick_y * GlobalConstants.ARM_BASE_SENSITIVITY);
        } else {
            this.setBasePower(GlobalConstants.ARM_GRAVITY_DIFFERENTIAL);
        }
    }

    /**
     * @name openCloseClaw()
     * @description Opens and closes the claws fully by setting the position of the claw servos to their minimum. <br/>
     * Throws a <b>RuntimeException</b> if any of the motors are null.
     */
    public void openCloseClaw() {
        // Ensure the motors exist before setting their position
        if (this.motors.CLAW_RIGHT != null) {
            this.motors.CLAW_RIGHT.setPosition(this.CLAW_OPEN ? 0 : 0.48);
            CLAW_OPEN = !CLAW_OPEN;
        } else {
            throw new RuntimeException("CLAW_RIGHT is null!");
        }
        if (this.motors.CLAW_LEFT != null) {
            this.motors.CLAW_LEFT.setPosition(this.CLAW_OPEN ? 0 : 0.5);
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
        if (this.motors.CLAW_RIGHT != null) {
            this.motors.CLAW_RIGHT.setPosition(clawRight);
            CLAW_OPEN = !CLAW_OPEN;
        } else {
            throw new RuntimeException("CLAW_RIGHT is null!");
        }
        if (this.motors.CLAW_LEFT != null) {
            this.motors.CLAW_LEFT.setPosition(clawLeft);
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
    public void setBasePower(float basePower) {
        basePower = CustomMathFunctions.clamp(0, basePower, 1);

        // Ensure the motor exists.
        if (this.motors.ARM_BASE != null) {
            this.motors.ARM_BASE.setPower(basePower);
        } else {
            throw new RuntimeException("ARM_BASE is null!");
        }
    }

    /**
     * @name setMotorPolicies()
     * @description Changes the default policies of each motor.
     */
    private void setMotorPolicies() {
        this.motors.ARM_BASE.setDirection(DcMotor.Direction.REVERSE);
        this.motors.ARM_BASE.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }
}