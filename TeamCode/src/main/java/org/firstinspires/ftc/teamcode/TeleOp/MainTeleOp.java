package org.firstinspires.ftc.teamcode.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.teamcode.BaseTeleOp;

// CHANGE NAMES LATER!!!
@TeleOp(name = "Main TeleOp", group = "Not Main")
public class MainTeleOp extends BaseTeleOp {

    private ElapsedTime elapsedTime;
    private ElapsedTime rpmTimer;

    private boolean driverxIsPreviouslyPressed = false;
    private boolean driverbIsPreviouslyPressed = false;
    private boolean driverTriggerIsPreviouslyFlicked = false;
    private boolean handOpen = true;
    private boolean slowMode = false;
    private boolean shooterOn = false;
    private int shooterEncoderPreviousPosition;

    @Override
    public void init() {
        elapsedTime = new ElapsedTime();
        rpmTimer = new ElapsedTime();
        elapsedTime.reset();
        rpmTimer.reset();
        super.init();
        shooterEncoderPreviousPosition = 0;
    }

    @Override
    public void start() {
        super.start();
        elapsedTime.startTime();
        rpmTimer.startTime();
        shooterEncoderPreviousPosition = shooterLeft.getCurrentPosition();
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

        if (driverbIsPreviouslyPressed) {
            if (!driver.b) {
                driverbIsPreviouslyPressed = false;
            }
        } else {
            if (driver.b) {
                // just to switch between the two states
                slowMode = !slowMode;
                driverbIsPreviouslyPressed = true;
            } else {
                driverbIsPreviouslyPressed = false;
            }
        }

        // this time the if is outside the state machine so that it will constantly change the state of the drive
        // motors
        if (slowMode) {
            x = x*0.33f;
            y = y*0.33f;
            z = z*0.33f;
        } else {
            x = x*0.5f;
            y = y*0.5f;
            z = z*0.5f;
        }

        telemetry.addData("Slow mode: ", slowMode);
        driveMotors(x, y, z);

        /***    -----------------------------------------------------------------------------------------------------------     */

        // toggle for driver.x
        if (driverxIsPreviouslyPressed) {
            // if the button was previously pressed then check if its pressed NOW
            // if not then change previously pressed to false
            if (!driver.x) {
                driverxIsPreviouslyPressed = false;
            }
        } else {
            // if the button was NOT previously pressed then check if it's pressed NOW
            // if it is than that means this was the point where it changed states.
            if (driver.x) {
                // just to switch between the two states
                // this is inside the state machine because it only needs to be set once
                if (!handOpen) {
                    wobbleGoalHand.setPosition(wobbleHandClosed);
                } else {
                    wobbleGoalHand.setPosition(wobbleHandOpen);
                }
                handOpen = !handOpen;
                driverxIsPreviouslyPressed = true;
            } else {
                driverxIsPreviouslyPressed = false;
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

        if (driverTriggerIsPreviouslyFlicked) {
            // the triggers are analog values
            if (!(driver.right_trigger > 0.3)) {
                driverTriggerIsPreviouslyFlicked = false;
            }
        } else {
            if (driver.right_trigger > 0.3) {
                // just to switch between the two states
                shooterOn = !shooterOn;
                driverTriggerIsPreviouslyFlicked = true;
            } else {
                driverTriggerIsPreviouslyFlicked = false;
            }
        }


        if (shooterOn) {
            // 85.5 is the magic number for shooting from the line
            runShooter(0.855);
        } else if (driver.right_bumper) {
            runShooter(0.83);
        } else {
            runShooter(0.0);
        }

        // the triggers are analog values

//        else if (driver.right_bumper) {
//
//            runShooter(0.83);
//        } else {
//            runShooter(0.0);
//        }

        telemetry.addData("Flywheel Speed", driver.right_trigger);

        if (rpmTimer.seconds() >= 1.0d) {
            int shooterEncoderCurrentPosition = shooterLeft.getCurrentPosition();
            telemetry.addData("Flywheel RPM", getShooterRPM(rpmTimer.seconds(), shooterEncoderPreviousPosition, shooterEncoderCurrentPosition));
            shooterEncoderPreviousPosition = shooterEncoderCurrentPosition;
            rpmTimer.reset();
        }

        // Make another control with one belt running
        if (driver.left_trigger > 0.5) {
            intake.setPower(0.7);
            beltLeft.setPower(-1.0);
            beltRight.setPower(0.5);

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
