package org.firstinspires.ftc.teamcode.modules.behaviors;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

public abstract class ModularOpMode extends LinearOpMode {

    private final DefaultModuleBehaviorCollector collector = new DefaultModuleBehaviorCollector();

    @Override
    abstract public void runOpMode() throws InterruptedException;

    /**
     * @name useModules
     * @description Collects modules to be executed later.
     * @see ModularOpMode#executeModules()
     */
    public void useModules(Class<?>... modules) {
        collector.collect(modules);
    }

    /**
     * @name executeModules()
     * @description Executes the collected modules.
     * @see ModularOpMode#useModules(Class[])
     */
    public void executeModules() throws OpModeNotRunningException {
        if (this.opModeIsActive()) {
            collector.executeAll();
        } else {
            throw new OpModeNotRunningException("OpMode needs to be running before executing modules!");
        }
    }
}
