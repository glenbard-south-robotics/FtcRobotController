package org.firstinspires.ftc.teamcode.modules.telemetry

import org.firstinspires.ftc.robotcore.external.Telemetry
import kotlin.collections.iterator

data class GBSTelemetry(
    val telemetry: Telemetry,
    val obj: Any,
    val groupPrefix: String? = null
)

fun GBSTelemetry.addDebug() {
    val groups = mutableMapOf<String, MutableList<Pair<String, Any?>>>()
    val clazz = obj.javaClass

    for (field in GBSTelemetryReflectionCache.debugFields(clazz)) {
        val debug = field.getAnnotation(GBSTelemetryDebug::class.java) ?: continue
        field.isAccessible = true

        val value = runCatching { field.get(obj) }.getOrNull()
        val group = groupPrefix?.let { "$it/${debug.group}" } ?: debug.group

        groups
            .getOrPut(group) { mutableListOf() }
            .add(field.name to value)
    }

    for (method in GBSTelemetryReflectionCache.debugMethods(clazz)) {
        val debug = method.getAnnotation(GBSTelemetryDebug::class.java) ?: continue
        if (method.parameterCount != 0) continue

        method.isAccessible = true
        val value = runCatching { method.invoke(obj) }.getOrNull()
        val group = groupPrefix?.let { "$it/${debug.group}" } ?: debug.group

        groups
            .getOrPut(group) { mutableListOf() }
            .add(method.name to value)
    }

    for ((group, entries) in groups) {
        telemetry.addLine("[$group]")
        for ((name, value) in entries) {
            telemetry.addData("  $name", value)
        }
    }
}
