package org.firstinspires.ftc.teamcode.modules.robot.deprecated;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.GlobalConstants;

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
            boolean slowModeActive = gamepadTwo.y;
            float speed = gamepadTwo.right_stick_y * (slowModeActive ?
                    GlobalConstants.ARM_BASE_SENSITIVITY_SLOW :
                    GlobalConstants.ARM_BASE_SENSITIVITY
            );

            this.setBasePower(speed);
        }
    }

    //!FIXME Names are incorrect, openClaw() and closeClaw() are swapped as well.
    double CLAW_RIGHT_OPEN = 0.7; // closed
    double CLAW_RIGHT_CLOSED = 0.1; // opens
    double CLAW_LEFT_OPEN = 0; // closed
    double CLAW_LEFT_CLOSED = 0.6; // opens

    public void openClaw() {
        this.CLAW_RIGHT.setPosition(CLAW_RIGHT_OPEN);
        this.CLAW_LEFT.setPosition(CLAW_LEFT_OPEN);
    }

    public void closeClaw() {
        this.CLAW_RIGHT.setPosition(CLAW_RIGHT_CLOSED);
        this.CLAW_LEFT.setPosition(CLAW_LEFT_CLOSED);
    }

    public void setBasePower(float basePower) {
        basePower = CustomMathFunctions.clamp(-1, basePower, 1);

        // Ensure the motor exists.
        if (this.ARM_BASE != null) {
            this.ARM_BASE.setPower(basePower);
        } else {
            throw new RuntimeException("ARM_BASE is null!");
        }
    }

    private void setMotorPolicies() {
        this.ARM_BASE.setDirection(DcMotor.Direction.REVERSE);
        this.ARM_BASE.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }
}