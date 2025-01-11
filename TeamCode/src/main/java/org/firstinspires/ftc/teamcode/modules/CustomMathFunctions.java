package org.firstinspires.ftc.teamcode.modules;

public class CustomMathFunctions {
    /**
     * @name clamp
     * @description Ensures a value remains in the provided bounds.
     */
    public static float clamp(float small, float value, float big) {
        return Math.max(small, Math.min(big, value));
    }

}
