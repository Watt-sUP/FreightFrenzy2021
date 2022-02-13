package org.firstinspires.ftc.teamcode.hardware;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Cupa {
    private Telemetry telemetry;
    public Servo servo;
    private double downPosition = 0.01, upPosition = 0.5, upMorePosition = 0.7;
    private State state;

    private enum State {
        Down,
        Up,
        UpMore
    }

    public Cupa(HardwareMap hardwareMap, Telemetry telemetry) {
        servo = hardwareMap.servo.get(Config.cupa);
        state = State.Down;
        this.telemetry = telemetry;
    }

    public double getServoPosition() {
        return servo.getPosition();
    }

    private void down() {
        servo.setPosition(downPosition);
        state = State.Down;
    }

    private void up() {
        servo.setPosition(upPosition);
        state = State.Up;
    }

    private void upMore() {
        servo.setPosition(upMorePosition);
        state = State.UpMore;
    }


    public void toggleCupa() {
        if(state == State.Up || state == State.UpMore) down();
        else up();
    }

    public void toggleCupaMore() {
        if(state == State.UpMore || state == State.Up) down();
        else upMore();
    }

}
