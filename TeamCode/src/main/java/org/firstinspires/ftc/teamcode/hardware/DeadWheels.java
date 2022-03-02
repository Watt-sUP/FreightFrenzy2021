package org.firstinspires.ftc.teamcode.hardware;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class DeadWheels {
    private Telemetry telemetry;
    private Servo roataStanga, roataMijloc, roataDreapta;
    private double downPosition = 0.1, upPosition = 0.75;

    public DeadWheels(HardwareMap hardwareMap, Telemetry telemetry) {
        this.telemetry = telemetry;
        roataStanga = hardwareMap.servo.get(Config.servoStanga);
        roataMijloc = hardwareMap.servo.get(Config.servoMijloc);
        roataDreapta = hardwareMap.servo.get(Config.servoDreapta);
    }

    public void setDown() {
        roataDreapta.setPosition(downPosition);
        roataMijloc.setPosition(downPosition);
        roataStanga.setPosition(downPosition);
    }

    public void setUp() {
        roataStanga.setPosition(upPosition);
        roataMijloc.setPosition(upPosition);
        roataDreapta.setPosition(upPosition);
    }
}
