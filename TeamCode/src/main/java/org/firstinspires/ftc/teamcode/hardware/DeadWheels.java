package org.firstinspires.ftc.teamcode.hardware;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ServoImplEx;

import org.firstinspires.ftc.robotcore.external.Telemetry;
@com.acmerobotics.dashboard.config.Config
public class  DeadWheels {
    private Servo roataStanga, roataDreapta;
    private ServoImplEx roataMijloc;
    private double upPosition = 0.35, downPosition = 0.1;
    private static double rightWheel = 0.06, leftWheel = 0.17, midWheel = 0.11;
    public DeadWheels(HardwareMap hardwareMap) {
        roataStanga = hardwareMap.get(Servo.class, Config.servoStanga);
        roataMijloc = hardwareMap.get(ServoImplEx.class, Config.servoMijloc);
        roataDreapta = hardwareMap.get(Servo.class, Config.servoDreapta);
    }

    public void setDown() {
        roataDreapta.setPosition(0.05);
//        roataMijloc.setPosition(0.12);
        roataMijloc.setPwmDisable();
        roataStanga.setPosition(0.21);
    }

    public void setUp() {
        roataDreapta.setPosition(upPosition + 0.05);
        roataMijloc.setPosition(upPosition);
        roataStanga.setPosition(upPosition + 0.155);
    }
}