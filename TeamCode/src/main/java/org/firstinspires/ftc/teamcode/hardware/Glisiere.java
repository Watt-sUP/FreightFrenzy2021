package org.firstinspires.ftc.teamcode.hardware;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Glisiere {
    public DcMotor motor;
    private int[] positions = {0, 150, 1300, 2400, 3000, 500};

    public Glisiere(HardwareMap hardwareMap) {
        motor = hardwareMap.dcMotor.get(Config.glisiera);
        motor.setDirection(DcMotorSimple.Direction.FORWARD);
        motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motor.setTargetPosition(0);
        motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }

    public void setToPosition(int position) {
        motor.setTargetPosition(positions[position]);
        motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        if(position != 0) motor.setPower(1);
        else motor.setPower(0.8);
    }

    public void modifyPosition(int ticks){
        motor.setTargetPosition(motor.getCurrentPosition() + ticks);
        motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        motor.setPower(0.5);
    }

    public double getPosition() {
        return motor.getCurrentPosition();
    }

    public int getTicks() {
        return motor.getCurrentPosition();
    }
}
