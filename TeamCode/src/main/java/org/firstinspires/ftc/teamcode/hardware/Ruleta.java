package org.firstinspires.ftc.teamcode.hardware;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;


public class Ruleta {

    private StateCupa stare;

    CRServo servo_orizontal;
    CRServo servo_vertical;
    Servo servo_fata;

    private enum StateCupa {
        collect,
        eject
    }

    public Ruleta(HardwareMap hardwareMap)
    {

        servo_orizontal = hardwareMap.crservo.get(Config.brat_orizontal);
        servo_vertical = hardwareMap.crservo.get(Config.brat_vertical);
        servo_fata = hardwareMap.servo.get(Config.servo_cupa);

        servo_orizontal.resetDeviceConfigurationForOpMode();
        servo_vertical.resetDeviceConfigurationForOpMode();
        servo_fata.resetDeviceConfigurationForOpMode();

        stare = StateCupa.eject;


    }

    public void move(double powerVertical, double powerOrizontal)
    {
        /*
                CR-someday bogdan: in clasa de gamepad am putea face axele sa returneze 0 daca
                                   nu sunt apasate suficient (echivalent cu if urile astea)
        */



        if(powerVertical < -0.03 || powerVertical > 0.03) {
            servo_vertical.setPower(-powerVertical);
        }
        else if(powerVertical < 0.03 && powerVertical > -0.03)
            servo_vertical.setPower(0.0);

        if(powerOrizontal < -0.03 || powerOrizontal > 0.03) {
            servo_orizontal.setPower(-powerOrizontal);
        }
        else if(powerOrizontal < 0.03 && powerOrizontal > -0.03)
            servo_orizontal.setPower(0.0);
    }

    public void toggleCupa() {
        if(stare == StateCupa.collect) eject();
        else collect();
    }

    public void eject() {
        servo_fata.setPosition(0.5);
        stare = StateCupa.eject;
    }

    public void collect() {
        servo_fata.setPosition(1.0);
        stare = StateCupa.collect;
    }
}
