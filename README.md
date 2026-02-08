# FtcRobotController Onboarding

This guide is designed to help members understand the structure of our codebase, find important
files, and safely make changes.
## Codebase Overview

The `TeamCode` module contains all the logic:

```
TeamCode/src/main.java/org/firstinspires/ftc/teamcode/
├── config/ # Configuration for modules, OpModes, and auto 
├── modules/ # Robot modules like intake, flywheel, and the base
└── opmodes/ # Main TeleOp and Auto OpModes 
```

## Configuration Files

Most configuration is contained in **objects** under `config/`

| Module / Feature | Config Object                  | Purpose                                                      |
|------------------|--------------------------------|--------------------------------------------------------------|
| Base             | GBSBaseModuleConfiguration     | Stick thresholds, fine-adjust power, base motor coefficients |
| Intake           | GBSIntakeModuleConfiguration   | Power multipliers, forward/reverse/slow mode coefficients    |
| Flywheel         | GBSFlywheelModuleConfiguration | Camera decimation, debug telemetry                           |

> [!TIP]
> All configuration values are documentation in their respective interfaces (e.g,
`IGBSIntakeModuleConfiguration`). Hover over a property to see its documentation

## Telemetry and Debugging

All modules have optional **debug telemetry** that can be enabled via their config:
```kotlin
override val DEBUG_TELEMETRY = true
```

This will output live data such as:
- Motor power
- Current module state (IDLE, FORWARD, AUTO, etc.)
- Camera State
- Slow Mode

> [!WARNING]
Use telemetry to **verify** changes before competing!

## Recommended Workflow

1. Understand the module you're editing
	1. Check `modules/robot` to see classes like `GBSIntakeModule`
2. Check the config object *first*
	1. Look at `config/` to see what variables you can safely modify (power coefficients, PID constants, etc.)
3.  Make **small** changes
	1. Avoid changing multiple modules at once, it's easier to debug with isolated changes
4.  Test on the robot
	1. Use the `GBSTestAll` OpMode to verify your changes
5. Use version control (git)
	1. Always commit changes with meaningful messages like: `fix: reduce intake reverse power`, or `feat: add slow mode for base`
	2. Know *how* to use git properly, always open a pull request (PR) before merging to allow other team members to approve and comment on your changes.

## Safe Editing Guidelines
- Velocities: Avoid exceeding safe motor limits (consult build team)
- Power coefficients: Keep them between 0.0 and 1.0
- Auto distances: Always test small values first to avoid collisions
- Telemetry: Leave `DEBUG_TELEMETRY` enabled for new team members

## Development Environment (NixOS + Terminal)

I know it may be a little scary, but we **compile and upload code via the terminal**, *not* the Android Studio Run button. This avoids Gradle / Java toolchain breakage and ensures reproducible builds across team members.

### Nix Shell

We use a `flake.nix` development shell:
```bash
nix develop
```
This provides:
- `jdk17` as `JAVA_HOME`
- `jdk8` as `JAVA_HOME_8`
- Gradle (`gradle`) for building projects
- Android command line tools (`android-tools`) for deploying
- `just` for convenient task aliases
### Justfile Commands

We use [Just](https://just.systems/) to simplify build and deploy commands:

```bash
just build-debug # Build a debug APK
just deploy-debug # Install the debug APK to the connected device

just build-release # Build a release APK
just deploy-rlease # Install the release APK to the connected device
```

Alternatively, you can use the combined recipes like:

```bash
just debug # Build and install debug APK

just release # Build and install release APK
```

### Why We Use Nix + Just

- Reproducible builds: everyone uses the same Gradle and JDKs
- No Android Studio breakage: avoids frequent JDK / Gradle misconfigurations
- Faster iteration: small CLI commands instead of full IDE builds
- Safe defaults: debug/release builds and deploys are predefined
## Learning Resources

- [Kotlin Basics](https://kotlinlang.org/docs/home.html)
- [FTC SDK Javadoc](https://javadoc.io/doc/org.firstinspires.ftc)
- [gm0 Software]([https://gm0.org/en/latest/](https://gm0.org/en/latest/docs/software/index.html)) 