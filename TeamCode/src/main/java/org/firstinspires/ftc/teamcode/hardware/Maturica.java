package org.firstinspires.ftc.teamcode.hardware;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Maturica {
    private Telemetry telemetry;
    private DcMotor motor;
    public String maturaData = "Idle";

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

    public void setMotorPower(double power) {
        if(power == 0) maturaData = "Idle";
        else if(power > 0) maturaData = "Collecting";
        else if(power < 0) maturaData = "Ejecting";

        motor.setPower(power);
    }

    public String getMaturaData() {
        return state.toString();
    }

    public void stop() {
        motor.setPower(0);
        state = State.Idle;
    }

    public void collect() {

    }
    /*
        CR bogdan: este buna implementarea, dar cred ca e mai bine daca avem functiile
                   stop, collect, eject, in loc de setPower.

                   de implementat celelalte functii (pe langa stop), si inlocuit utilizarile
                   setMotorPower cu una din cele 3 noi functii, cum trebuie.

                   putem in plus sa avem toggleCollect (si toggleEject) care face acelasi
                   lucru ca if ul daca puterea e 0 sau nu. (mai jos implementare pt toggleCollect)

                   de asemenea, e bine sa facem enum uri pentru starile unui mecanism pentru
                   a fi clar ce valori poate lua. (string e un tip prea general) vezi mai sus
                   cum e folosit state in loc de maturaData. trebuie inlocuita folosirea lui
                   maturaData cu getMaturaData. in general am vrea sa nu folosim direct variabile
                   dintr-o clasa, ci cu o functie care returneaza valoarea (getter).
     */

    public void toggleCollect() {
        if(state == State.Collecting)   stop();
        else    collect();
    }
}
