package org.firstinspires.ftc.teamcode.modules;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * @name OnTickCollector
 * @description Collects all methods with the OnTick annotation.
 * @see DefaultModuleBehavior
 */
public class DefaultModuleBehaviorCollector {
    private final List<Method> collectedMethods = new ArrayList<>();

    /**
     * @name collect
     * @description Collects all of the OnTick annotations.
     */
    public void collect(List<Class<?>> classes) {
        for (Class<?> clazz : classes) {
            for (Method method : clazz.getDeclaredMethods()) {
                if (method.isAnnotationPresent(DefaultModuleBehavior.class)) {
                    collectedMethods.add(method);
                }
            }
        }
    }

    /**
     * @name executeAll
     * @description Executes all collected methods.
     */
    public void executeAll() {
        for (Method method : collectedMethods) {
            try {
                // Dynamically instantiate the class containing the method
                Object instance = method.getDeclaringClass().getDeclaredConstructor().newInstance();
                method.setAccessible(true); // Ensure private methods can be invoked
                method.invoke(instance);
            } catch (Exception e) {
                System.err.println("Failed to execute method " + method.getName() + ": " + e.getMessage());
            }
        }
    }
}
