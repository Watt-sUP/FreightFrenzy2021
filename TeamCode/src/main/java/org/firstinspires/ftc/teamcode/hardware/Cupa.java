package org.firstinspires.ftc.teamcode.hardware;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Cupa {
    public Servo servo;
    private double downPosition = 0.6, upPosition = 0.97, upMorePosition = 0.55; //0.05
    private static double stransPos = 0.25, desfacutPos = 0.55;
    private State state;
    private StateDeget stateDeget;
    public Servo deget;

    private enum State {
        Down,
        Up,
        UpMore
    }

    private enum StateDeget {
        Strans,
        Desfacut
    }

    public Cupa(HardwareMap hardwareMap) {
        servo = hardwareMap.servo.get(Config.cupa);
        state = State.Up;
        servo.setPosition(upPosition);
        stateDeget = StateDeget.Desfacut;
        deget = hardwareMap.servo.get(Config.deget);
        deget.setPosition(desfacutPos);
    }

    public double getServoPosition() {
        return servo.getPosition();
    }

    public void down() {
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
        if(state == State.Down) up();
        else down();
    }

    public void toggleCupaMore() {
        if(state == State.UpMore || state == State.Up) down();
        else upMore();
    }


}
