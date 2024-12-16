package org.firstinspires.ftc.teamcode.modules.behaviors;

import android.util.Pair;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * @name DefaultModuleBehaviorCollector
 * @description Collects all methods with the DefaultModuleBehavior annotation.
 * @see DefaultModuleBehavior
 */
public class DefaultModuleBehaviorCollector {
    private final List<Method> collectedMethods = new ArrayList<>();
    private final List<Pair<Method, Object>> instances = new ArrayList<>();

    /**
     * @name collect
     * @description Collects all the DefaultModuleBehavior annotations.
     */
    public void collect(Class<?>... classes) {
        for (Class<?> clazz : classes) {
            for (Method method : clazz.getDeclaredMethods()) {
                if (method.isAnnotationPresent(DefaultModuleBehavior.class)) {
                    collectedMethods.add(method);
                }
            }
        }

        createInstances();
    }

    /**
     * @name createInstances
     * @description Creates classes for each robot component.
     * @see DefaultModuleBehaviorCollector#collect(Class[])
     */
    private void createInstances() {
        for (Method method : collectedMethods) {
            Object instance;
            try {
                instance = method.getDeclaringClass().getDeclaredConstructor().newInstance();
                this.instances.add(new Pair<>(method, instance));
            } catch (Exception e) {
                System.err.println("Failed to create a new class: " + e.getMessage());
            }
        }
    }

    /**
     * @name executeAll
     * @description Executes all collected methods.
     */
    public void executeAll() {

        for (Pair<Method, Object> instance : instances) {
            try {
                instance.first.setAccessible(true); // Ensure private methods can be invoked
                instance.first.invoke(instance);
                instances.add(instance);
            } catch (Exception e) {
                System.err.println("Failed to execute method " + instance.first.getName() + ": " + e.getMessage());
            }
        }
    }

    /**
     * @name getInstances()
     * @description Returns all collected instances.
     */
    public ArrayList<Pair<Method, Object>> getInstances() {
        return (ArrayList<Pair<Method, Object>>) this.instances;
    }
}
