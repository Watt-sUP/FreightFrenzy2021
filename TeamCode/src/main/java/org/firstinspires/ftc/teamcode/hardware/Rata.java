package org.firstinspires.ftc.teamcode.hardware;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Config.Config;

public class Rata {
    private Telemetry telemetry;
    private DcMotor motor;

    public Rata(HardwareMap hardwareMap, Telemetry telemetry) {
        motor = hardwareMap.dcMotor.get(Config.rate);
        motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motor.setDirection(DcMotorSimple.Direction.FORWARD);

        this.telemetry = telemetry;
    }

    public void rotate(double power) {
        motor.setPower(power);
    }
}
