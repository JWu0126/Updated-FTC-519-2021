package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.Gamepad;

// Last year's wasn't abstract but I don't understand the need for a loop
public abstract class BaseTeleOp extends BaseOpMode{

    protected Gamepad driver;
    protected Gamepad gunner;
    protected boolean invertMotors = false;
    protected boolean slowMode = false;

    // place holder for controls that we put in later
    protected final boolean someControl = false;

    // x is left and right, y is forwards and backwards, z is turning
    protected float x;
    protected float y;
    protected float z;

    @Override
    public void init() {
        super.init();
        driver = gamepad1;
        gunner = gamepad2;
    }

    @Override
    public void start() {
        super.start();

        // reset wobbleGoalHand to the "zeroed" position
        wobbleGoalHand.setPosition(wobbleHandClosed);
    }

    // last years was different
    // I don't understand the need to check for non-zero values
    protected static float shapeInput(float input) {
        if (input > 0.0) {
            return input * input;
        } else {
            return input * -input;
        }
    }

    protected void driveMotors(double x, double y, double z) {
        // for whatever reason this doesn't need to be flipped. IDK, maybe something is backwards
        y = -y;
        backLeft.setPower((-x+y+z)*0.9);
        backRight.setPower((y+x-z)*0.9);
        frontLeft.setPower((y+x+z)*1.1);
        frontRight.setPower((y-x-z)*1.1);
    }

    protected float applyDeadZone(float joystickValue) {
        float value = joystickValue;

        if (value < 0.05 && value > -0.05) {
            value = 0.0f;
        }

        return value;
    }

}
