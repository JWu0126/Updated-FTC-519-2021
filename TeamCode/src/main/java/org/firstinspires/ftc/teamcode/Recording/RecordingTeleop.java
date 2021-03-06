package org.firstinspires.ftc.teamcode.Recording;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.teamcode.TeleOp.MainTeleOp;

public class RecordingTeleop extends MainTeleOp {

    private String recordingName;
    private PersistentFileOutputStream outputStream;
    private BlackBox.Recorder recorder;
    private boolean isRecordingMode = false;
    private ElapsedTime recordTimer;
    private RecordingThread recordingThread;

    public RecordingTeleop(String name) {
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

   /*@TeleOp(name = "RecordRed", group = "Recording")
    public static class RecordRed extends RecordingTeleop {
        public RecordRed() { super("RedNoStone"); }
    }
    @TeleOp(name = "RecordBlue", group = "Recording")
    public static class RecordBlue extends RecordingTeleop {
        public RecordBlue() { super("BlueNoStone"); }
    }*/

    // the recordings
    @TeleOp(name = "Record Test Recording", group = "Recording")
    public static class RecordTestRecording extends RecordingTeleop {
        public RecordTestRecording() { super("TestRecording"); }
    }

    @TeleOp(name = "RedLeftARecord", group = "Recording")
    public static class RedLeftARecord extends RecordingTeleop {
        public RedLeftARecord() { super("RedLeftA"); }
    }

    @TeleOp(name = "RedLeftBRecord", group = "Recording")
    public static class RedLeftBRecord extends RecordingTeleop {
        public RedLeftBRecord() { super("RedLeftB"); }
    }

    @TeleOp(name = "RedLeftCRecord", group = "Recording")
    public static class RedLeftCRecord extends RecordingTeleop {
        public RedLeftCRecord() { super("RedLeftC"); }
    }

    @TeleOp(name = "RedLeftAShootRecord", group = "Recording")
    public static class RedLeftAShootRecord extends RecordingTeleop {
        public RedLeftAShootRecord() { super("RedLeftAShoot"); }
    }


    @TeleOp(name = "RedLeftBShootRecord", group = "Recording")
    public static class RedLeftBShootRecord extends RecordingTeleop {
        public RedLeftBShootRecord() {
            super("RedLeftBShoot");
        }
    }

    @TeleOp(name = "RedLeftCShootRecord", group = "Recording")
    public static class RedLeftCShootRecord extends RecordingTeleop {
        public RedLeftCShootRecord() {
            super("RedLeftCShoot");
        }
    }

    @TeleOp(name = "RecordBlueBlockFoundationPark", group = "Recording")
    public static class BlueBlockFoundationPark extends RecordingTeleop {
        public BlueBlockFoundationPark() {
            super("BlueBlockFoundationPark");
        }
    }

}
