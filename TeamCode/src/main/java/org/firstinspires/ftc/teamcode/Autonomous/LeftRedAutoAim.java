package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import org.firstinspires.ftc.teamcode.BaseAutonomous;

@Autonomous(group = "test", name = "Left Red Auto-Aim")
public class LeftRedAutoAim extends BaseAutonomous {

    ShooterGoalTracker shooterGoalTracker = new ShooterGoalTracker(hardwareMap) {
        @Override
        public void loop() {

        }
    };

    @Override
    public void init() {
        super.init();
    }

    @Override
    public void start() {
        super.start();
    }

    @Override
    public void loop() {

    }
}
