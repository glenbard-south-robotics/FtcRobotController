package org.firstinspires.ftc.teamcode.config.modules.robot

import org.firstinspires.ftc.teamcode.config.modules.IGBSRobotModuleConfiguration

@Suppress("PropertyName")
interface IGBSWebcamModuleConfiguration : IGBSRobotModuleConfiguration {
    /**
     * How decimated should the input video be, don't touch this if you don't know what you're doing
     */
    val DECIMATION: Float
}