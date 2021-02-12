package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.teamcode.BaseAutonomous;
import org.firstinspires.ftc.teamcode.Recording.BlackBox;
import org.firstinspires.ftc.teamcode.Recording.PersistentFileInputStream;

@Autonomous(name = "Encoder Red Left Side Setup ", group = "test")
public class LeftRedWobbleGoalSetUpEncoder extends BaseAutonomous {
    LocationFinder locationFinder;

    LocationFinder.location location;
//
//    AutonomousPlayback.RedLeftA redLeftA;
//    AutonomousPlayback.RedLeftB redLeftB;
//    AutonomousPlayback.RedLeftC redLeftC;

    private PersistentFileInputStream inputStream;
    private BlackBox.Player player;
    private String recordingName;
    private ElapsedTime elapsedTime;

    @Override
    public void init() {
        super.init();
        backLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        locationFinder = new LocationFinder(hardwareMap) {
            @Override
            public void loop() {}
        };
    }

    @Override
    public void start() {
        stepCounter.next();
    }

    @Override
    public void loop() {
        switch (stepCounter.getStep()) {
            case 1:

                location = locationFinder.find();
                telemetry.addData("The Wobble Goal Location is at ", location);
                stepCounter.next();
                break;

            case 2:
                // it seems that the init function causes issues
                // after commenting out the super.init line it returned to the same issue that it had before.
                // my guess is that hardwareMap is the cause of these issues. Investigate this
                telemetry.addLine("in case 2");
                switch (location) {
                    case CLOSE:
                        telemetry.addData("location chosen: ", location);
//                        try {
//                            redLeftA = new AutonomousPlayback.RedLeftA();
//                            redLeftA.init();
//                        } catch (Exception e) {
//                            telemetry.addLine("Things didn't work");
//                        }
                        recordingName = "EncoderRedLeftAShoot";
                        break;
                    case MED:
                        telemetry.addData("location chosen: ", location);
//                        try {
//                            redLeftB = new AutonomousPlayback.RedLeftB();
//                            redLeftB.init();
//                        } catch (Exception e) {
//                            telemetry.addLine("things didn't work");
//                        }
                        recordingName = "EncoderRedLeftBShoot";
                        break;
                    case FAR:
                        telemetry.addData("location chosen: ", location);
//                        try {
//                            redLeftC = new AutonomousPlayback.RedLeftC();
//                            redLeftC.init();
//                        } catch (Exception e) {
//                            telemetry.addLine("Things didn't work");
//                        }
                        recordingName = "EncoderRedLeftCShoot";
                        break;
                }

                stepCounter.next();
                break;

            case 3:
                // It never enters the loop function
                // I guessed that it was because it never went through its init phase so I did that in the previous case
                // This caused it to give an error that said there was a null pointer reference with a dc motor
                telemetry.addData("Going to ", location);

                if (player == null) {
                    try {
                        inputStream = new PersistentFileInputStream(this.recordingName);
                        player = new BlackBox.Player(inputStream.get(), hardwareMap);
                        elapsedTime = new ElapsedTime();
                        elapsedTime.reset();
                    } catch (Exception e) {
                        this.telemetry.addData("Can't create player", "!");
                    }
                } else {
                    try {
                        telemetry.addLine("Trying to loop");
                        boolean hasMoreEvents = player.playback(elapsedTime.time());
                        if (!hasMoreEvents) {
                            stepCounter.next();
                        }
                    } catch (Exception e) {
                        this.telemetry.addData("Can't play back auto", "!");
                    }
                }

                break;

            case 4:
                telemetry.addLine("Finished playback");
                stepCounter.next();
                break;

            case 5:
                break;
        }
    }
}
