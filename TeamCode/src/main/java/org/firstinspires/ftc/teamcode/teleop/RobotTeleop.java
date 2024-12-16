package org.firstinspires.ftc.teamcode.teleop;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.GlobalConstants;
import org.firstinspires.ftc.teamcode.modules.behaviors.ModularOpMode;
import org.firstinspires.ftc.teamcode.modules.robot.RobotArm;
import org.firstinspires.ftc.teamcode.modules.robot.RobotBase;
import org.firstinspires.ftc.teamcode.modules.robot.RobotLinearSlide;

@TeleOp(name = "RobotTeleop", group = "Teleop")
public class RobotTeleop extends ModularOpMode {
    @Override
    public void runOpMode() {
        useModules(RobotArm.class, RobotBase.class, RobotLinearSlide.class);

        waitForStart();
        while (opModeIsActive()) {
            executeModules();
            idle();
            sleep(GlobalConstants.CYCLE_MS);
        }
    }

}
