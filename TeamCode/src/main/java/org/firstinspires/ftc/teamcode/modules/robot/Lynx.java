package org.firstinspires.ftc.teamcode.modules.robot;

import androidx.annotation.ColorInt;

import com.qualcomm.hardware.lynx.LynxModule;

/**
 * @name Led
 * @description Changes the color of the onboard lights to a specified color.
 * @noinspection unused
 */
public class Lynx {
    private LynxModule MASTER = null;

    public Lynx(LynxModule[] hubs) {
        for (LynxModule hub : hubs) {
            if (hub.isParent()) {
                MASTER = hub;
                break;
            }
        }
    }

    /**
     * @name setColor
     * @description Change the color of the onboard led. <br />
     * Color is given in AARRGGBB format as a packed int.  <br />
     * Throws a <b>RuntimeException</b> if the LynxModule is null.
     * @see LynxModule
     */
    public void setColor(@ColorInt int color) {
        if (this.MASTER != null) {
            this.MASTER.setConstant(color);
        } else {
            throw new RuntimeException("MASTER is null!");
        }
    }
}
