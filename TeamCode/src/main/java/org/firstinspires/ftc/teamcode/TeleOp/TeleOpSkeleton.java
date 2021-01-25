package org.firstinspires.ftc.teamcode.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.teamcode.BaseTeleOp;

import java.security.Policy;

// CHANGE NAMES LATER!!!
@TeleOp(name = "Main TeleOp", group = "Not Main")
public class TeleOpSkeleton extends BaseTeleOp {
    // presumably going to need multiple versions of teleop because two days comp

    private ElapsedTime elapsedTime;

    private boolean previouslyPressed = false;
    private boolean isPreviouslyPressed = false;
    private boolean handOpen = true;
    private boolean slowMode = false;

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

        if (isPreviouslyPressed) {
            if (!driver.b) {
                isPreviouslyPressed = false;
            }
        } else {
            if (driver.b) {
                // just to switch between the two states
                slowMode = !slowMode;
                isPreviouslyPressed = true;
            } else {
                isPreviouslyPressed = false;
            }
        }

        // this time the if is outside the state machine so that it will constantly change the state of the drive
        // motors
        if (slowMode) {
            x = x*0.5f;
            y = y*0.5f;
            z = z*0.5f;
        }

        telemetry.addData("Slow mode: ", slowMode);
        driveMotors(x, y, z);

        /***    -----------------------------------------------------------------------------------------------------------     */

        // toggle for driver.x
        if (previouslyPressed) {
            // if the button was previously pressed then check if its pressed NOW
            // if not then change previously pressed to false
            if (!driver.x) {
                previouslyPressed = false;
            }
        } else {
            // if the button was NOT previously pressed then check if it's pressed NOW
            // if it is than that means this was the point where it changed states.
            if (driver.x) {
                // just to switch between the two states
                // this is inside the state machine because it only needs to be set once
                if (handOpen) {
                    wobbleGoalHand.setPosition(wobbleHandClosed);
                } else {
                    wobbleGoalHand.setPosition(wobbleHandOpen);
                }
                handOpen = !handOpen;
                previouslyPressed = true;
            } else {
                previouslyPressed = false;
            }
        }

//        if (driver.b) {
//            wobbleGoalHand.setPosition(wobbleHandOpen);
//        } else if (driver.y) {
//            wobbleGoalHand.setPosition(wobbleHandClosed);
//        }

        // makes the limit switch work one way and not the other
        telemetry.addData("touch limit", touchLimitSwitch.hasHitLimit());

        telemetry.addData("magnetic limit", magneticLimitSwitch.hasHitLimit());

        if (touchLimitSwitch.hasHitLimit() || magneticLimitSwitch.hasHitLimit()) {
            if (touchLimitSwitch.hasHitLimit() && driver.a) {
                wobbleGoalArm.setPower(0.6);
            } else if (magneticLimitSwitch.hasHitLimit() && driver.y) {
                wobbleGoalArm.setPower(-0.6);
            } else {
                wobbleGoalArm.setPower(0.0);
            }
        } else {
            if (driver.y) {
                wobbleGoalArm.setPower(-0.6);
            } else if (driver.a) {
                wobbleGoalArm.setPower(0.6);
            } else {
                wobbleGoalArm.setPower(0.0);
            }
        }

        // the triggers are analog values
        if (driver.right_trigger > 0.1) {
            runShooter(driver.right_trigger);
        } else if (driver.right_bumper) {
            runShooter(0.75);
        } else {
            runShooter(0.0);
        }

        telemetry.addData("Flywheel Speed", driver.right_trigger);

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

        } else if (driver.dpad_left) {
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
