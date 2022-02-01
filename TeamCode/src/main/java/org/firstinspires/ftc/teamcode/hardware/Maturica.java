package org.firstinspires.ftc.teamcode.hardware;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Maturica {
    private Telemetry telemetry;
    public DcMotor motor;

    public enum State {
        Idle,
        Collecting,
        Ejecting
    };

    private State state;

    public Maturica(HardwareMap hardwareMap, Telemetry telemetry) {
        motor = hardwareMap.dcMotor.get(Config.matura);
        motor.setDirection(DcMotorSimple.Direction.FORWARD);

        this.telemetry = telemetry;
        state = State.Idle;
    }

    public String getMaturaData() {
        return state.toString();
    }

    private void stop() {
        motor.setPower(0);
        state = State.Idle;
    }

    private void collect() {
        state = State.Collecting;
        motor.setPower(-1.0);
    }

    private void eject() {
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
