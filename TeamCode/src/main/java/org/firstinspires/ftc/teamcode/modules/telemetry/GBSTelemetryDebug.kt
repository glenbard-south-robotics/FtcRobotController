package org.firstinspires.ftc.teamcode.modules.telemetry

@Target(
    AnnotationTarget.FIELD, AnnotationTarget.FUNCTION
)
@Retention(AnnotationRetention.RUNTIME)
annotation class GBSTelemetryDebug(
    val group: String = "default"
)