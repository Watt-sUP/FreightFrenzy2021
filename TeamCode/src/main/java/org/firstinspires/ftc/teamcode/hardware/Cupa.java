package org.firstinspires.ftc.teamcode.hardware;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Cupa {
    private Telemetry telemetry;
    public Servo servo;
    private double downPosition = 0.65, upPosition = 0.97, upMorePosition = 0.55, downMorePosition = 0.6; //0.05
    private static double stransPos = 0.5, desfacutPos = 0.8;
    private State state;
    private StateDeget stateDeget;
    public Servo deget;

    private enum State {
        Down,
        DownMore,
        Up,
        UpMore
    }

    private enum StateDeget {
        Strans,
        Desfacut
    }

    public Cupa(HardwareMap hardwareMap, Telemetry telemetry) {
        servo = hardwareMap.servo.get(Config.cupa);
        state = State.Up;
        servo.setPosition(upPosition);
        stateDeget = StateDeget.Desfacut;
        this.telemetry = telemetry;
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

    private void downMore() {
        servo.setPosition(downMorePosition);
        state = State.DownMore;
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

    public void toggleCupa(boolean more) {
        if(state == State.Down || state == State.DownMore) up();
        else if(more) downMore();
        else down();
    }

    public void toggleCupaMore() {
        if(state == State.UpMore || state == State.Up) down();
        else upMore();
    }


}
