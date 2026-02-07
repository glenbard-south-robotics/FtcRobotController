package org.firstinspires.ftc.teamcode.exceptions

class GBSAddModulesAfterInitializationException(msg: String) : Exception("Cannot initialize modules after OpMode is running: $msg!")