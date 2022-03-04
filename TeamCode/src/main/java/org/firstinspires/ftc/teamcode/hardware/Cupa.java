package org.firstinspires.ftc.teamcode.hardware;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Cupa {
    private Telemetry telemetry;
    public Servo servo;
    private double downPosition = 0, upPosition = 0.8, upMorePosition = 0.7;
    private static double stransPos = 0.2, desfacutPos = 0.7;
    private State state;
    private StateDeget stateDeget;
    private Servo deget;

    private enum State {
        Down,
        Up,
        UpMore
    }

    private enum StateDeget {
        Strans,
        Desfacut
    }

    public Cupa(HardwareMap hardwareMap, Telemetry telemetry) {
        servo = hardwareMap.servo.get(Config.cupa);
        state = State.Down;
        stateDeget = StateDeget.Desfacut;
        this.telemetry = telemetry;
        deget = hardwareMap.servo.get(Config.deget);
        deget.setPosition(desfacutPos);
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

    public void strange() {
        deget.setPosition(stransPos);
        stateDeget = StateDeget.Strans;
    }

    public void desface() {
        deget.setPosition(desfacutPos);
        stateDeget = StateDeget.Desfacut;
    }

    public void toggleDeget() {
        if(stateDeget == StateDeget.Desfacut) strange();
        else desface();
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
