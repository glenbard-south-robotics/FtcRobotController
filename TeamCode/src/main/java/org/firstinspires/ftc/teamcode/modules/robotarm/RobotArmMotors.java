package org.firstinspires.ftc.teamcode.modules.robotarm;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * @name RobotArmMotors
 * @description Interface for the motors of the RobotArm class.
 * @see RobotArm
 */
public class RobotArmMotors {
    DcMotorEx ARM_BASE;
    Servo CLAW_LEFT;
    Servo CLAW_RIGHT;

    public RobotArmMotors(Servo clawLeft, Servo clawRight, DcMotorEx armBase) {
        this.CLAW_LEFT = clawLeft;
        this.CLAW_RIGHT = clawRight;
        this.ARM_BASE = armBase;
    }
}
