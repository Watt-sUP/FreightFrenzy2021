package org.firstinspires.ftc.teamcode.hardware;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Cupa {
    private Telemetry telemetry;
    private Servo servo;
    private double downPosition = 0.05, upPosition = 0.7;
    private double midPosition = 0.1; //Placeholder
    private State state;

    private enum State {
        Down,
        Middle,
        Up
    }

    public Cupa(HardwareMap hardwareMap, Telemetry telemetry) {
        servo = hardwareMap.servo.get(Config.cupa);
        state = State.Down;
        this.telemetry = telemetry;
    }

    public double getServoPosition() {
        return servo.getPosition();
    }

    public void down() {
        servo.setPosition(downPosition);
        state = State.Down;
    }

    public void up() {
        servo.setPosition(upPosition);
        state = State.Up;
    }

    public void mid() {
        servo.setPosition(midPosition);
        state = State.Middle;
    }

    public void toggleCupa() {
        if(state == State.Up) down();
        else up();
    }

}
