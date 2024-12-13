# TeamCode 2024

## File Structure

```
teamcode/
├── auto/ -- Contains finalized auto OpModes for use in competition.
├── teleop/ -- Contains finalized TeleOp OpModes for use in competition.
├── tests/ -- Contains tests for each part of the robot.
└── modules/ -- Contains files shared between all the files.
```

## Refactor Changes

With the 2024 second semester code changes, the codebase has undergone significant refactoring. One
major change involves how different OpModes interact with motors and servos on the robot. Each *
*module** represents an independent part of the robot, designed to function independently of the
rest of the system.

### Key Features of Modules

- Each **module** implements [RobotModule](./modules/behaviors/RobotModule.java), which defines the
  `defaultModuleBehavior()` method.
- Example: The [RobotArm](./modules/robot/RobotArm.java) module binds methods to specific
  controllers by default, such as opening the claw with the `A` button.

### Using Modules

#### Step 1: Create a Module Collector and Component List

In an OpMode, start by creating an instance of
the [DefaultModuleBehaviorCollector](./modules/behaviors/DefaultModuleBehaviorCollector.java).

```java
@TeleOp(name = "RobotArmTeleop", group = "Teleop")
public class RobotArmTeleop extends LinearOpMode {

    private final DefaultModuleBehaviorCollector collector = new DefaultModuleBehaviorCollector();

}
```

#### Step 2: Override `runOpMode()` and Collect Modules

Pass the required modules to the `collector.collect()` statement and the collector will initialize them.

```java
@TeleOp(name = "RobotArmTeleop", group = "Teleop")
public class RobotArmTeleop extends LinearOpMode {

  @Override
  public void runOpMode() {
    collector.collect(RobotArm.class);
  }
}
```

#### Step 3: Implement the Main Loop

Define the behavior of the OpMode in the main loop.

```java
@TeleOp(name = "RobotArmTeleop", group = "Teleop")
public class RobotArmTeleop extends LinearOpMode {

    @Override
    public void runOpMode() {
        waitForStart();
        while (opModeIsActive()) {
            collector.executeAll();
            sleep(GlobalConstants.CYCLE_MS);
            idle();
        }
    }
}
```

#### Completed Example
```java
@TeleOp(name = "RobotArmTeleop", group = "Teleop")
public class RobotArmTeleop extends LinearOpMode {
    private DefaultModuleBehaviorCollector collector = new DefaultModuleBehaviorCollector();
    
    @Override
    public void runOpMode() {
        collector.collect(RobotArm.class);
        
        waitForStart();
        while (opModeIsActive()) {
            collector.executeAll();
            sleep(GlobalConstants.CYCLE_MS);
            idle();
        }
    }
}
```

### Overriding Base Modules

In some scenarios, you may need to customize the behavior of a module for specific conditions. This
can be achieved by overriding methods in the base module class.

#### Example: Extending the `RobotArm` Module

In this example, the `SpecialOpModeRobotArm` class adds a condition requiring the `Y` button to be
held in addition to the `A` button to open or close the claw.

```java
public class SpecialOpModeRobotArm extends RobotArm {
    public SpecialOpModeRobotArm() {
        super();
    }

    @Override
    public void defaultModuleBehavior() {
        if (gamepad2.a && gamepad2.y) {
            this.openCloseClaw();
        }
    }
}
```

### Global Constants

As of this revision all constants, such as `ARM_CLAW_SENSITIVITY` are stored in the [GlobalConstants](GlobalConstants.java) file