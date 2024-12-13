package org.firstinspires.ftc.teamcode.teleop;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.modules.DefaultModuleBehaviorCollector;
import org.firstinspires.ftc.teamcode.modules.robotarm.RobotArm;
import org.firstinspires.ftc.teamcode.modules.robotarm.RobotArmMotors;
import org.firstinspires.ftc.teamcode.modules.robotbase.RobotBase;
import org.firstinspires.ftc.teamcode.modules.robotbase.RobotBaseMotors;
import org.firstinspires.ftc.teamcode.modules.robotlinearslide.RobotLinearSlide;
import org.firstinspires.ftc.teamcode.modules.robotlinearslide.RobotLinearSlideMotors;

import java.util.ArrayList;

@TeleOp(name = "RobotTeleop", group = "Teleop")
public class RobotTeleop extends LinearOpMode {

    private final DefaultModuleBehaviorCollector defaultModuleBehaviorCollector = new DefaultModuleBehaviorCollector();
    private final ArrayList<Class<?>> classes = new ArrayList<>();

    private RobotBase base = null;
    private RobotLinearSlide slide = null;
    private RobotArm arm = null;

    @Override
    public void runOpMode() {
        // Add all classes that implement OnTick to the classes list.
        classes.add(RobotArm.class);
        classes.add(RobotBase.class);
        classes.add(RobotLinearSlide.class);

        // Add all methods of each class to the list.
        defaultModuleBehaviorCollector.collect(classes);

        DcMotor leftFrontDrive = hardwareMap.get(DcMotor.class, "left_front_drive");
        DcMotor leftBackDrive = hardwareMap.get(DcMotor.class, "left_back_drive");
        DcMotor rightFrontDrive = hardwareMap.get(DcMotor.class, "right_front_drive");
        DcMotor rightBackDrive = hardwareMap.get(DcMotor.class, "right_back_drive");

        Servo armClawLeft = hardwareMap.get(Servo.class, "arm_hand_left");
        Servo armClawRight = hardwareMap.get(Servo.class, "arm_hand_right");
        DcMotorEx armBase = hardwareMap.get(DcMotorEx.class, "arm_base");

        DcMotorEx linearSlide = hardwareMap.get(DcMotorEx.class, "linear_slide");

        base = new RobotBase(new RobotBaseMotors(leftFrontDrive, leftBackDrive, rightFrontDrive, rightBackDrive));
        arm = new RobotArm(new RobotArmMotors(armClawLeft, armClawRight, armBase));
        slide = new RobotLinearSlide(new RobotLinearSlideMotors(linearSlide));

        waitForStart();
        while (opModeIsActive()) {
            defaultModuleBehaviorCollector.executeAll();

            sleep(50);
            idle();
        }
    }
}
