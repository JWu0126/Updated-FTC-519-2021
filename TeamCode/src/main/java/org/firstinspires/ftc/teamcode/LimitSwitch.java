package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class LimitSwitch {

    private DigitalChannel limitSwitch;

    protected LimitSwitch(HardwareMap map, String deviceName) {
        limitSwitch = map.get(DigitalChannel.class, deviceName);
        limitSwitch.setMode(DigitalChannel.Mode.INPUT);
    }

    public boolean hasHitLimit() {
        return !limitSwitch.getState();
    }
}
