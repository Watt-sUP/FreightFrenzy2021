package org.firstinspires.ftc.teamcode.hardware;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Glisiere {
    private Telemetry telemetry;
    public DcMotor motor;
    private int[] positions = {0, 800, 1650, 2150, 2700};

    public Glisiere(HardwareMap hardwareMap, Telemetry telemetry) {
        motor = hardwareMap.dcMotor.get(Config.glisiera);
        motor.setDirection(DcMotorSimple.Direction.FORWARD);
        motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        this.telemetry = telemetry;
    }

    public void setToPosition(int position) {
        motor.setTargetPosition(positions[position]);
        motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        if(position != 0) motor.setPower(1);
        else motor.setPower(0.8);
    }

    public int getTicks() {
        return motor.getCurrentPosition();
    }
}
