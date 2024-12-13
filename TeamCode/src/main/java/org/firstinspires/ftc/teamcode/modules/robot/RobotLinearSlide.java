package org.firstinspires.ftc.teamcode.modules.robot;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.gamepad2;
import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.hardwareMap;

import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.teamcode.GlobalConstants;
import org.firstinspires.ftc.teamcode.modules.CustomMathFunctions;
import org.firstinspires.ftc.teamcode.modules.behaviors.DefaultModuleBehavior;
import org.firstinspires.ftc.teamcode.modules.behaviors.RobotModule;

/**
 * @name RobotLinearSlide
 * @description Takes in motors, and controller inputs and powers the respective slide motor.
 */
public class RobotLinearSlide implements RobotModule {
    private final DcMotorEx LINEAR_SLIDE_MOTOR = hardwareMap.get(DcMotorEx.class, "linear_slide");

    public RobotLinearSlide() {
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
        position = CustomMathFunctions.clamp(0, position * GlobalConstants.LINEAR_SLIDE_SENSITIVITY, 1);
        if (this.LINEAR_SLIDE_MOTOR != null) {
            this.LINEAR_SLIDE_MOTOR.setPower(position);
        } else {
            throw new RuntimeException("LINEAR_SLIDE_MOTOR is null!");
        }
    }

    /**
     * @name setMotorPolicies()
     * @description Changes the default policies of each motor.
     */
    private void setMotorPolicies() {
        this.LINEAR_SLIDE_MOTOR.setDirection(DcMotorEx.Direction.FORWARD);
        this.LINEAR_SLIDE_MOTOR.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
    }
}
