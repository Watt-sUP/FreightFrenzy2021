package org.firstinspires.ftc.teamcode.hardware;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Ruleta {

    private Telemetry telemetry;
    Gamepad gamepad2;

    CRServo servo_orizontal;
    CRServo servo_vertical;
    CRServo servo_fata;

    public Ruleta(HardwareMap hardwareMap, Telemetry telemetry)
    {

        servo_orizontal = hardwareMap.crservo.get(Config.rul_orizontal);
        servo_vertical = hardwareMap.crservo.get(Config.rul_vertical);
        servo_fata = hardwareMap.crservo.get(Config.rul_fata);

        servo_orizontal.resetDeviceConfigurationForOpMode();
        servo_vertical.resetDeviceConfigurationForOpMode();
        servo_fata.resetDeviceConfigurationForOpMode();

        this.telemetry = telemetry;

    }

    public void move(double powerVertical, double powerOrizontal, double powerFata)
    {
        /*
                CR-someday bogdan: in clasa de gamepad am putea face axele sa returneze 0 daca
                                   nu sunt apasate suficient (echivalent cu if urile astea)
        */

        if(gamepad2.left_stick_x < -0.05 || gamepad2.left_stick_x > 0.05) {
            servo_orizontal.setPower(powerOrizontal);
        }
        else if(gamepad2.left_stick_x < 0.05 && gamepad2.left_stick_x > -0.05)
            servo_orizontal.setPower(0.0);

        if(gamepad2.left_stick_y < -0.05 || gamepad2.left_stick_y > 0.05) {
            servo_vertical.setPower(powerVertical);
        }
        else if(gamepad2.left_stick_y < 0.05 && gamepad2.left_stick_y > -0.05)
            servo_vertical.setPower(0.0);

        if(gamepad2.right_stick_y < -0.05 || gamepad2.right_stick_y > 0.05) {
            servo_fata.setPower(powerFata);
        }
        else if(gamepad2.right_stick_y < 0.05 && gamepad2.right_stick_y > -0.05)
            servo_fata.setPower(0.0);
    }
}
