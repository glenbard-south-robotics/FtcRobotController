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

- Each **module** implements [RobotModule](./modules/RobotModule.java), which defines the
  `defaultModuleBehavior()` method.
- Example: The [RobotArm](./modules/robotarm/RobotArm.java) module binds methods to specific
  controllers by default, such as opening the claw with the `A` button.

### Using Modules

#### Step 1: Create a Module Collector and Component List

In an OpMode, start by creating an instance of
the [DefaultModuleBehaviorCollector](./modules/DefaultModuleBehaviorCollector.java) and a list to
hold the components for the OpMode.

```java

@TeleOp(name = "RobotArmTeleop", group = "Teleop")
public class RobotTeleop extends LinearOpMode {

    private final DefaultModuleBehaviorCollector collector = new DefaultModuleBehaviorCollector();
    private final ArrayList<Class<?>> classes = new ArrayList<>();

}
```

#### Step 2: Define Module Variables

Define each module you want to use as an instance variable.

```java

@TeleOp(name = "RobotArmTeleop", group = "Teleop")
public class RobotTeleop extends LinearOpMode {

    private RobotArm arm;
    private RobotBase base;
}
```

#### Step 3: Override `runOpMode()` and Collect Modules

Add the required modules to the `classes` list and use the collector to initialize them.

```java

@TeleOp(name = "RobotArmTeleop", group = "Teleop")
public class RobotTeleop extends LinearOpMode {

    @Override
    public void runOpMode() {
        classes.add(RobotArm.class);
        classes.add(RobotBase.class);

        collector.collect(classes);
    }
}
```

#### Step 4: Map Hardware

Map the necessary motors and servos in `hardwareMap`.

```java

@TeleOp(name = "RobotArmTeleop", group = "Teleop")
public class RobotTeleop extends LinearOpMode {

    @Override
    public void runOpMode() {
        DcMotor leftFrontDrive = hardwareMap.get(DcMotor.class, "left_front_drive");
        DcMotor leftBackDrive = hardwareMap.get(DcMotor.class, "left_back_drive");
        DcMotor rightFrontDrive = hardwareMap.get(DcMotor.class, "right_front_drive");
        DcMotor rightBackDrive = hardwareMap.get(DcMotor.class, "right_back_drive");

        Servo armClawLeft = hardwareMap.get(Servo.class, "arm_hand_left");
        Servo armClawRight = hardwareMap.get(Servo.class, "arm_hand_right");
        CRServo armBase = hardwareMap.get(CRServo.class, "arm_base");
    }
}
```

#### Step 5: Initialize Modules

Create instances of the desired modules using the mapped hardware.

```java

@TeleOp(name = "RobotArmTeleop", group = "Teleop")
public class RobotTeleop extends LinearOpMode {

    @Override
    public void runOpMode() {
        base = new RobotBase(new RobotBaseMotors(leftFrontDrive, leftBackDrive, rightFrontDrive, rightBackDrive));
        arm = new RobotArm(new RobotArmMotors(armClawLeft, armClawRight, armBase));
    }
}
```

#### Step 6: Implement the Main Loop

Define the behavior of the OpMode in the main loop.

```java

@TeleOp(name = "RobotArmTeleop", group = "Teleop")
public class RobotTeleop extends LinearOpMode {

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

### Overriding Base Modules

In some scenarios, you may need to customize the behavior of a module for specific conditions. This
can be achieved by overriding methods in the base module class.

#### Example: Extending the `RobotArm` Module

In this example, the `SpecialOpModeRobotArm` class adds a condition requiring the `Y` button to be
held in addition to the `A` button to open or close the claw.

```java
public class SpecialOpModeRobotArm extends RobotArm {
    public SpecialOpModeRobotArm(RobotArmMotors motors) {
        super(motors);
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