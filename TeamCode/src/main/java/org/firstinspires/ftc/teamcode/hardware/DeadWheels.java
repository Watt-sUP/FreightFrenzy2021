package org.firstinspires.ftc.teamcode.hardware;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;
@com.acmerobotics.dashboard.config.Config
public class DeadWheels {
    private Servo roataStanga, roataMijloc, roataDreapta;
    private double upPosition = 0.35, downPosition = 0.1;
    private static double rightWheel = 0.06, leftWheel = 0.17, midWheel = 0.11;
    public DeadWheels(HardwareMap hardwareMap) {
        roataStanga = hardwareMap.servo.get(Config.servoStanga);
        roataMijloc = hardwareMap.servo.get(Config.servoMijloc);
        roataDreapta = hardwareMap.servo.get(Config.servoDreapta);
    }

    public void setDown() {
        roataDreapta.setPosition(0.03);
        roataMijloc.setPosition(0.12);
        roataStanga.setPosition(downPosition + 0.09);
    }

    public void setUp() {
        roataDreapta.setPosition(upPosition + 0.05);
        roataMijloc.setPosition(upPosition);
        roataStanga.setPosition(upPosition + 0.155);
    }
}