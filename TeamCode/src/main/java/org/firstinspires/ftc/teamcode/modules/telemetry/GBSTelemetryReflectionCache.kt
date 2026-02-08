package org.firstinspires.ftc.teamcode.modules.telemetry

import java.lang.reflect.Field
import java.lang.reflect.Method

internal object GBSTelemetryReflectionCache {
    private val fieldCache = mutableMapOf<Class<*>, List<Field>>()

    private val methodCache = mutableMapOf<Class<*>, List<Method>>()

    fun debugFields(clazz: Class<*>): List<Field> = fieldCache.getOrPut(clazz) {
        clazz.declaredFields.filter {
            it.isAnnotationPresent(GBSTelemetryDebug::class.java) && !it.isSynthetic
        }.onEach { it.isAccessible = true }
    }

    fun debugMethods(clazz: Class<*>): List<Method> = methodCache.getOrPut(clazz) {
        clazz.declaredMethods.filter {
            it.isAnnotationPresent(GBSTelemetryDebug::class.java) && it.parameterCount == 0
        }.onEach { it.isAccessible = true }
    }
}