package org.firstinspires.ftc.teamcode.modules.robot;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.GlobalConstants;
import org.firstinspires.ftc.teamcode.modules.CustomMathFunctions;

/**
 * @name RobotLinearSlide
 * @description This class controls the motors found on the robot using human input.
 */
public class RobotLinearSlide {
    private final DcMotorEx LINEAR_SLIDE_MOTOR;
    private final DcMotorEx LINEAR_SLIDE_MOTOR_2;
    private final Gamepad gamepadTwo;

    public RobotLinearSlide(@NonNull HardwareMap map, @NonNull Gamepad gamepadTwo) {
        this.LINEAR_SLIDE_MOTOR = map.get(DcMotorEx.class, "linear_slide");
        this.LINEAR_SLIDE_MOTOR_2 = map.get(DcMotorEx.class, "linear_slide_2");
        this.gamepadTwo = gamepadTwo;
        this.setMotorPolicies();
    }

    public void run() {
        this.setPower(gamepadTwo.left_stick_y);
        this.setPowerAuxiliary(gamepadTwo.left_stick_x);
    }

    public void setPower(float position) {
        position = CustomMathFunctions.clamp(-1, position * GlobalConstants.LINEAR_SLIDE_SENSITIVITY, 1);
        if (this.LINEAR_SLIDE_MOTOR != null) {
            this.LINEAR_SLIDE_MOTOR.setPower(position);
        } else {
            throw new RuntimeException("LINEAR_SLIDE_MOTOR is null!");
        }
    }

    public void setPowerAuxiliary(float position) {
        position = CustomMathFunctions.clamp(-1, position * GlobalConstants.LINEAR_SLIDE_SENSITIVITY, 1);
        if (this.LINEAR_SLIDE_MOTOR_2 != null) {
            this.LINEAR_SLIDE_MOTOR_2.setPower(position);
        } else {
            throw new RuntimeException("LINEAR_SLIDE_MOTOR_2 is null!");
        }
    }

    /**
     * @name setMotorPolicies()
     * @description Changes the default policies of each motor.
     */
    private void setMotorPolicies() {
        this.LINEAR_SLIDE_MOTOR.setDirection(DcMotorEx.Direction.FORWARD);
        this.LINEAR_SLIDE_MOTOR.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        this.LINEAR_SLIDE_MOTOR_2.setDirection(DcMotorEx.Direction.FORWARD);
        this.LINEAR_SLIDE_MOTOR_2.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
    }
}
