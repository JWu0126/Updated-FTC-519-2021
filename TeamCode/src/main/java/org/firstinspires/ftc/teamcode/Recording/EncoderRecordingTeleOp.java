package org.firstinspires.ftc.teamcode.Recording;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.teamcode.TeleOp.EncoderDriveTrainTeleOp;
import org.firstinspires.ftc.teamcode.TeleOp.MainTeleOp;

public class EncoderRecordingTeleOp extends EncoderDriveTrainTeleOp {

    private String recordingName;
    private PersistentFileOutputStream outputStream;
    private BlackBox.Recorder recorder;
    private boolean isRecordingMode = false;
    private ElapsedTime recordTimer;
    private RecordingThread recordingThread;

    public EncoderRecordingTeleOp(String name) {
        this.recordingName = name;
    }

    @Override
    public void init() {
        super.init();

        try {
            outputStream = new PersistentFileOutputStream(this.recordingName);

            recorder = new BlackBox.Recorder(this.hardwareMap, outputStream.get());
        } catch (Exception e) {
            this.telemetry.addData("Failed", e.getMessage());
            //this.requestOpModeStop();

            return;
        }
    }

    @Override
    public void start() {
        super.start();

        this.recordingThread = new RecordingThread(this.recorder, this.hardwareMap);
    }

    @Override
    public void loop() {
        super.loop();

        if (driver.dpad_up && (isRecordingMode == false)) {
            isRecordingMode = true;
            if (isRecordingMode) {
                if (recordTimer == null) {
                    recordTimer = new ElapsedTime();
                    //this.log("Starting recording!", "-");
                    recordTimer.reset();
                } else {
                    // TODO: pause timer
                }
            }
        }

        // Record the values
        if (this.isRecordingMode) {
            this.telemetry.addData("Currently Recording Values!!!!", "");

            this.recordingThread.run();
            // new RecordingThread(recorder, recordTimer, hardwareMap)
            /*try {
                this.recorder.recordAllDevices(recordTimer.time());
            } catch (Exception e) {
                e.printStackTrace();
                this.requestOpModeStop();
            }*/
        }
    }

    @Override
    public void stop() {
        super.stop();

        try {
            this.outputStream.get().close();
            this.telemetry.addData("Closed Output Stream", ":)");
        } catch (Exception e) {
            e.printStackTrace();
            this.telemetry.addData("FAILED to end recording", "!");
        }
    }


    @TeleOp(name = "EncoderRedLeftCShootRecord", group = "Recording")
    public static class EncoderRedLeftCShootRecord extends EncoderRecordingTeleOp {
        public EncoderRedLeftCShootRecord() {
            super("EncoderRedLeftCShoot");
        }
    }

    @TeleOp(name = "EncoderRedLeftBShootRecord", group = "Recording")
    public static class EncoderRedLeftBShootRecord extends EncoderRecordingTeleOp {
        public EncoderRedLeftBShootRecord() {
            super("EncoderRedLeftBShoot");
        }
    }

    @TeleOp(name = "EncoderRedLeftAShootRecord", group = "Recording")
    public static class EncoderRedLeftAShootRecord extends EncoderRecordingTeleOp {
        public EncoderRedLeftAShootRecord() {
            super("EncoderRedLeftAShoot");
        }
    }

}