package org.firstinspires.ftc.teamcode.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.teamcode.BaseTeleOp;

import java.security.Policy;

// CHANGE NAMES LATER!!!
@TeleOp(name = "Don't use this one", group = "Not Main")
public class TeleOpSkeleton extends BaseTeleOp {
    // presumably going to need multiple versions of teleop because two days comp

    private ElapsedTime elapsedTime;
    private boolean buttonPressed = false;
    private boolean buttonJustReleased = false;
    private boolean handOpen = true;

    @Override
    public void init() {
        elapsedTime = new ElapsedTime();
        elapsedTime.reset();
        super.init();

    }

    @Override
    public void start() {
        super.start();
        elapsedTime.startTime();

    }

    @Override
    public void loop() {
        telemetry.addData("Time Passed: ", elapsedTime.seconds());

        // add in controls, change as needed

        x = shapeInput(applyDeadZone(driver.left_stick_x));
        y = shapeInput(applyDeadZone(driver.left_stick_y));
        z = shapeInput(applyDeadZone(driver.right_stick_x));

        if (invertMotors) {
            x = -x;
            y = -y;
        }

        // hopefully this works the way I think it will
        if (someControl) {
            elapsedTime.startTime();
            if (elapsedTime.seconds() >= 0.1) {
                slowMode = !slowMode;
            }
        }

        if (driver.dpad_down) {
            x /= 10;
            y /= 10;
            x /= 10;
        }

        driveMotors(x, y, z);

        // if it was released previously, then check if its pressed NOW
        if (buttonJustReleased) {
            if (someControl) {
                buttonPressed = true;
            }
        }

        // if it was pressed now then see if its not anymore, if its not then change positions.
        // I think this works.
        // Basically works on release of the button
        if (buttonPressed) {
            if (!someControl) {
                buttonJustReleased = true;

                // just to switch between the two states
                if (handOpen) {
                    wobbleGoalHand.setPosition(wobbleHandClosed);
                } else {
                    wobbleGoalHand.setPosition(wobbleHandOpen);
                }
            }
        }

/***    -----------------------------------------------------------------------------------------------------------     */

        // edit the power to be better for all of these
        // Figure out how these controls are being used (ie. toggle, hold, whatever)
        if (someControl) {
            setWobbleGoalArmUp();
        } else if (someControl) {
            setWobbleGoalArmDown();
        } else {
            //wobbleGoalArm.setPower(0.0);
        }

        if (driver.b) {
            wobbleGoalHand.setPosition(wobbleHandOpen);
        } else if (driver.y) {
            wobbleGoalHand.setPosition(wobbleHandClosed);
        }

        telemetry.addData("touch limit", touchLimitSwitch.hasHitLimit());

        telemetry.addData("magnetic limit", magneticLimitSwitch.hasHitLimit());

        if (touchLimitSwitch.hasHitLimit() || magneticLimitSwitch.hasHitLimit()) {
            if (touchLimitSwitch.hasHitLimit() && driver.a) {
                wobbleGoalArm.setPower(0.3);
            } else if (magneticLimitSwitch.hasHitLimit() && driver.x) {
                wobbleGoalArm.setPower(-0.3);
            } else {
                wobbleGoalArm.setPower(0.0);
            }
        } else {
            if (driver.x) {
                wobbleGoalArm.setPower(-0.3);
            } else if (driver.a) {
                wobbleGoalArm.setPower(0.3);
            } else {
                wobbleGoalArm.setPower(0.0);
            }
        }

        if (driver.right_trigger > 0.5) {
            runShooter(1.0);
        } else if (driver.left_bumper) {
            runShooter(0.5);
        } else {
            runShooter(0.0);
        }

        // reverse
        if (someControl) {
            runShooter(-1.0);
        } else {
        //    runShooter(0.0);
        }

        // Make another control with one belt running
        if (driver.left_trigger > 0.5) {
            intake.setPower(0.7);
            beltLeft.setPower(-1.0);
            beltRight.setPower(1.0);
        } else if (driver.left_bumper) {
            intake.setPower(-0.7);
            beltLeft.setPower(1.0);
            beltRight.setPower(-1.0);
        } else if (gunner.a) {
            beltLeft.setPower(-1.0);
            beltRight.setPower(0.0);
        } else if (gunner.b) {
            intake.setPower(0.7);
        } else if (gunner.x) {
            intake.setPower(-0.7);
        } else {
            intake.setPower(0.0);
            beltLeft.setPower(0.0);
            beltRight.setPower(0.0);
        }



    }
}
