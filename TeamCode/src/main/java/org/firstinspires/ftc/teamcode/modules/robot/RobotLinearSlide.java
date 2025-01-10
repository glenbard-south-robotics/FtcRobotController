package org.firstinspires.ftc.teamcode.modules.robot;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.gamepad2;
import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.telemetry;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.GlobalConstants;
import org.firstinspires.ftc.teamcode.modules.CustomMathFunctions;

/**
 * @name RobotLinearSlide
 * @description This class controls the motors found on the robot using human input.
 */
public class RobotLinearSlide {
    private final DcMotorEx LINEAR_SLIDE_MOTOR;

    public RobotLinearSlide(@NonNull HardwareMap map) {
        this.LINEAR_SLIDE_MOTOR = map.get(DcMotorEx.class, "linear_slide");
        this.setMotorPolicies();
    }

    public void run() {
        telemetry.addLine("Linear Slide running!");
        this.setPosition(Math.abs(gamepad2.left_stick_y));
        telemetry.addData("Linear Slide Power", Math.abs(gamepad2.left_stick_y));
        telemetry.update();
    }

    /**
     * @name setPosition
     * @description Sets the power of the linear slide servo. <br/>
     * Throws a <b>RuntimeException</b> if any of the motors are null.
     */
    public void setPosition(float position) {
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
