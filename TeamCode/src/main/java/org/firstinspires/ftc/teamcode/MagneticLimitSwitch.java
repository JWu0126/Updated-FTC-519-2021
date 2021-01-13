package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class MagneticLimitSwitch {

    private DigitalChannel magneticLimitSwitch;

    protected MagneticLimitSwitch(HardwareMap map) {
        magneticLimitSwitch = map.get(DigitalChannel.class, "magLimit");
        magneticLimitSwitch.setMode(DigitalChannel.Mode.INPUT);
    }

    public boolean hasHitLimit() {
        return !magneticLimitSwitch.getState();
    }
}
