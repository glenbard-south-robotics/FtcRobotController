package org.firstinspires.ftc.teamcode.modules;

import org.firstinspires.ftc.teamcode.modules.defaultmodulebehavior.DefaultModuleBehavior;

public interface RobotModule {
    @DefaultModuleBehavior
    default void defaultModuleBehavior() {}
}
