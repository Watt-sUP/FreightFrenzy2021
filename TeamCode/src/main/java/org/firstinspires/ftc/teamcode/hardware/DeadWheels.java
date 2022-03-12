package org.firstinspires.ftc.teamcode.hardware;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class DeadWheels {
    private Servo roataStanga, roataMijloc, roataDreapta;
    private double downPosition = 0.09, upPosition = 0.35;

    public DeadWheels(HardwareMap hardwareMap) {
        roataStanga = hardwareMap.servo.get(Config.servoStanga);
        roataMijloc = hardwareMap.servo.get(Config.servoMijloc);
        roataDreapta = hardwareMap.servo.get(Config.servoDreapta);
    }

    public void setDown() {
        roataDreapta.setPosition(downPosition - 0.015);
        roataMijloc.setPosition(downPosition - 0.065);
        roataStanga.setPosition(downPosition + 0.09);
    }

    public void setUp() {
        roataDreapta.setPosition(upPosition + 0.05);
        roataMijloc.setPosition(upPosition);
        roataStanga.setPosition(upPosition + 0.155);
    }
}