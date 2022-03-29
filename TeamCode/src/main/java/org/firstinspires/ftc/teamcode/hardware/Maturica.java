package org.firstinspires.ftc.teamcode.hardware;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Maturica {
    public DcMotor motor;

    public enum State {
        Idle,
        Collecting,
        Ejecting
    };

    private State state;

    public Maturica(HardwareMap hardwareMap) {
        motor = hardwareMap.dcMotor.get(Config.matura);
        motor.setDirection(DcMotorSimple.Direction.FORWARD);

        state = State.Idle;
    }

    public String getMaturaData() {
        return state.toString();
    }

    public void stop() {
        motor.setPower(0);
        state = State.Idle;
    }

    public void collect() {
        state = State.Collecting;
        motor.setPower(-1.0);
    }

    public void eject() {
        state = State.Ejecting;
        motor.setPower(1.0);
    }

    public void toggleCollect() {
        if(state == State.Collecting) stop();
        else collect();
    }

    public void toggleEject() {
        if(state == State.Ejecting) stop();
        else eject();
    }
}
