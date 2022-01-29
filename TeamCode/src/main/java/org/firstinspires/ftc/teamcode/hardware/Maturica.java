package org.firstinspires.ftc.teamcode.hardware;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Config.Config;

public class Maturica {
    private Telemetry telemetry;
    private DcMotor motor;
    public String maturaData = "Idle";

    public Maturica(HardwareMap hardwareMap, Telemetry telemetry) {
        motor = hardwareMap.dcMotor.get(Config.matura);
        motor.setDirection(DcMotorSimple.Direction.FORWARD);

        this.telemetry = telemetry;
    }

    public void setMotorPower(double power) {
        if(power == 0) maturaData = "Idle";
        else if(power > 0) maturaData = "Collecting";
        else if(power < 0) maturaData = "Ejecting";

        motor.setPower(power);
    }
}
