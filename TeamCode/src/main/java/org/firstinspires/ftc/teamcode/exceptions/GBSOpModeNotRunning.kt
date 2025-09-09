package org.firstinspires.ftc.teamcode.exceptions

class GBSOpModeNotRunning(msg: String) : Exception("OpMode must be running before using: $msg!")
