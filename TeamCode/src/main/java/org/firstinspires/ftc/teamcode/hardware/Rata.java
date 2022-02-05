package org.firstinspires.ftc.teamcode.hardware;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Rata {
    private Telemetry telemetry;
    public DcMotor motor;

    public Rata(HardwareMap hardwareMap, Telemetry telemetry) {
        motor = hardwareMap.dcMotor.get(Config.rate);
        //motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        //motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motor.setDirection(DcMotorSimple.Direction.FORWARD);

        this.telemetry = telemetry;
    }

    public void rotate(double power) {
        motor.setPower(power);
    }

    public void score(int position, double power) {
        motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        motor.setTargetPosition(position);
        motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        motor.setPower(power);
    }

    public void stop() {
        motor.setPower(0);
        motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }
}
