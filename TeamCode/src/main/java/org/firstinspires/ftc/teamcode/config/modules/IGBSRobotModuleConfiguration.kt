package org.firstinspires.ftc.teamcode.config.modules

import org.firstinspires.ftc.teamcode.modules.actions.GBSAnalogAction
import org.firstinspires.ftc.teamcode.modules.actions.GBSAnalogBinding
import org.firstinspires.ftc.teamcode.modules.actions.GBSBinaryBinding
import org.firstinspires.ftc.teamcode.modules.actions.GBSModuleActions

@Suppress("PropertyName")
interface IGBSRobotModuleConfiguration {
    /**
     * Should we enable debug telemetry such as motor power?
     */
    val DEBUG_TELEMETRY: Boolean

    /**
     * Binary bindings, such as buttons
     */
    val BINARY_BINDINGS: Map<GBSModuleActions, GBSBinaryBinding>

    /**
     * Analog bindings, such as sticks
     */
    val ANALOG_BINDINGS: Map<GBSAnalogAction, GBSAnalogBinding>
}