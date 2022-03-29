package org.firstinspires.ftc.teamcode.hardware;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Cupa {
    public Servo servo;
    private double del34Position = 0.5, collectPosition = 0.97, del2Position = 0.55; //0.05
    private static double stransPos = 0.25, desfacutPos = 0.55, desfacutMorePos = 0.65;
    private State state;
    private StateDeget stateDeget;
    public Servo deget;

    private enum State {
        Collect,
        Delivery_3_4,
        Delivery_2,
    }

    private enum StateDeget {
        Strans,
        Desfacut
    }

    public Cupa(HardwareMap hardwareMap) {
        servo = hardwareMap.servo.get(Config.cupa);
        state = State.Collect;
        servo.setPosition(collectPosition);
        stateDeget = StateDeget.Desfacut;
        deget = hardwareMap.servo.get(Config.deget);
        deget.setPosition(desfacutPos);
    }

    public double getServoPosition() {
        return servo.getPosition();
    }

    public void collect() {
        servo.setPosition(collectPosition);
        state = State.Collect;
    }

    public void delivery34() {
        servo.setPosition(del34Position);
        state = State.Delivery_3_4;
    }

    public void delivery2() {
        servo.setPosition(del2Position);
        state = State.Delivery_2;
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

    public void toggleDegetMore() {
        if(stateDeget == StateDeget.Desfacut) strange();
        else desfaceMore();
    }

    public void toggleCupa() {
        if(state == State.Collect) delivery34();
        else collect();
    }

    public void desfaceMore() {
        deget.setPosition(desfacutMorePos);
        stateDeget = StateDeget.Desfacut;
    }

    public void toggleCupaMore() {
        if(state == State.Delivery_3_4 || state == State.Delivery_2) collect();
        else delivery2();
    }


}
