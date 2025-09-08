package org.firstinspires.ftc.teamcode.modules.robot.deprecated;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.GlobalConstants;

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
        this.setPowers(gamepadTwo.left_stick_y, gamepadTwo.left_stick_x);
    }

    public void setPowers(float position1, float position2) {
        position1 = CustomMathFunctions.clamp(-1, position1 * GlobalConstants.LINEAR_SLIDE_SENSITIVITY, 1);
        position2 = CustomMathFunctions.clamp(-1, position2 * GlobalConstants.LINEAR_SLIDE_SENSITIVITY, 1);

        if (this.LINEAR_SLIDE_MOTOR != null && this.LINEAR_SLIDE_MOTOR_2 != null) {
            this.LINEAR_SLIDE_MOTOR.setPower(position1);
            this.LINEAR_SLIDE_MOTOR_2.setPower(position2);
        } else {
            throw new RuntimeException("A LINEAR_SLIDE_MOTOR is null!");
        }
    }

    private void setMotorPolicies() {
        this.LINEAR_SLIDE_MOTOR.setDirection(DcMotorEx.Direction.FORWARD);
        this.LINEAR_SLIDE_MOTOR.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        this.LINEAR_SLIDE_MOTOR_2.setDirection(DcMotorEx.Direction.FORWARD);
        this.LINEAR_SLIDE_MOTOR_2.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
    }
}
