package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

public abstract class BaseOpMode extends OpMode{

    public static final int SECONDS_PER_MINUTE = 60;
    // declare motors and servos
    protected DcMotor frontLeft;
    protected DcMotor frontRight;
    protected DcMotor backLeft;
    protected DcMotor backRight;

    protected DcMotor wobbleGoalArm;
    protected Servo wobbleGoalHand;

    protected DcMotor intake;
    protected CRServo beltRight;
    protected CRServo beltLeft;

    protected DcMotor shooterLeft;
    protected DcMotor shooterRight;

    protected LimitSwitch magneticLimitSwitch;
    protected LimitSwitch touchLimitSwitch;

    // Uncomment if used
//    DcMotor feeder;

    protected final int armDown = 1000;
    protected final int armUp = 0;
    protected final int acceptableRange = 30;

    protected final double wobbleHandOpen = 0.3;
    protected final double wobbleHandClosed = 0.6;

    @Override
    public void init() {
        // find drive train motors config in app?
        frontLeft = hardwareMap.dcMotor.get("front_left");
        frontRight = hardwareMap.dcMotor.get("front_right");
        backLeft = hardwareMap.dcMotor.get("back_left");
        backRight = hardwareMap.dcMotor.get("back_right");

        frontLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        frontRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        backLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        backRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        // set all drive train motors to Brake
        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        // flip the left side
        frontLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        backLeft.setDirection(DcMotorSimple.Direction.REVERSE);

        // find the wobble goal motors and servos in app
        wobbleGoalArm = hardwareMap.dcMotor.get("wobble_goal_arm");
        wobbleGoalHand = hardwareMap.servo.get("wobble_goal_hand");

        wobbleGoalArm.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        //wobbleGoalArm.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        //wobbleGoalArm.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        magneticLimitSwitch = new LimitSwitch(hardwareMap, "magLimit");
        touchLimitSwitch = new LimitSwitch(hardwareMap, "touchLimit");

        // find the ring manipulator motors in app
        intake = hardwareMap.dcMotor.get("intake");
        beltLeft = hardwareMap.crservo.get("belt_left");
        beltRight = hardwareMap.crservo.get("belt_right");

        shooterLeft = hardwareMap.dcMotor.get("shooter_left");
        shooterLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        shooterLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        shooterRight = hardwareMap.dcMotor.get("shooter_right");
        shooterRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        shooterRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        // set shooter and intake to "float" at zero to maintain rotation
        shooterLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        shooterRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        intake.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
    }

    @Override
    public void start() {
        // what does this actually do?
        super.start();
    }

    protected void runShooter(double power) {
        shooterLeft.setPower(-power);
        shooterRight.setPower(power);
    }

    protected static double getShooterRPM(double elapsedTimeInSeconds, int previousPosition, int currentPosition) {
        return ((currentPosition - previousPosition) * SECONDS_PER_MINUTE)/elapsedTimeInSeconds;
    }

    protected void setWobbleGoalArmDown() {
        boolean closeEnough = false;

        while (!closeEnough) {
            if (wobbleGoalArm.getCurrentPosition() > armDown) {
                wobbleGoalArm.setPower(0.02);
            } else if (wobbleGoalArm.getCurrentPosition() < armDown) {
                wobbleGoalArm.setPower(-0.02);
            }

            if (wobbleGoalArm.getCurrentPosition() < armDown - acceptableRange && wobbleGoalArm.getCurrentPosition() > armDown + acceptableRange) {
                closeEnough = true;
            }

        }
    }

    protected void setWobbleGoalArmUp() {
        boolean closeEnough = false;

        while (!closeEnough) {
            if (wobbleGoalArm.getCurrentPosition() > armUp) {
                wobbleGoalArm.setPower(0.02);
            } else if (wobbleGoalArm.getCurrentPosition() < armUp) {
                wobbleGoalArm.setPower(-0.02);
            }

            if (wobbleGoalArm.getCurrentPosition() < armUp - acceptableRange && wobbleGoalArm.getCurrentPosition() > armUp + acceptableRange) {
                closeEnough = true;
            }

        }
    }
}
