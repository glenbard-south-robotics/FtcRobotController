package org.firstinspires.ftc.teamcode.modules.robotlinearslide;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.gamepad2;

import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.modules.CustomMathFunctions;
import org.firstinspires.ftc.teamcode.modules.DefaultModuleBehavior;
import org.firstinspires.ftc.teamcode.modules.RobotModule;

/**
 * @name RobotLinearSlide
 * @description Takes in motors, and controller inputs and powers the respective slide motor.
 * @see RobotLinearSlideMotors
 */
public class RobotLinearSlide implements RobotModule {
    private final RobotLinearSlideMotors motors;

    public RobotLinearSlide(RobotLinearSlideMotors motors) {
        this.motors = motors;
        this.setMotorPolicies();
    }

    @DefaultModuleBehavior
    public void defaultModuleBehavior() {
        this.setPosition(Math.abs(gamepad2.left_stick_y));
    }

    /**
     * @name setPosition
     * @description Sets the power of the linear slide servo. <br/>
     * Throws a <b>RuntimeException</b> if any of the motors are null.
     */
    void setPosition(float position) {
        position = CustomMathFunctions.clamp(0, position, 1);
        if (this.motors.LINEAR_SLIDE_MOTOR != null) {
            this.motors.LINEAR_SLIDE_MOTOR.setPower(position);
        } else {
            throw new RuntimeException("LINEAR_SLIDE_MOTOR is null!");
        }
    }

    /**
     * @name setMotorPolicies()
     * @description Changes the default policies of each motor.
     */
    private void setMotorPolicies() {
        this.motors.LINEAR_SLIDE_MOTOR.setDirection(DcMotor.Direction.FORWARD);
        this.motors.LINEAR_SLIDE_MOTOR.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }
}
