package org.firstinspires.ftc.teamcode.hardware;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;


public class Brat {

    private StateCupa stare;

    public CRServo servo_orizontal;
    public DcMotor motor_vertical;
    public Servo servo_fata;

    private enum StateCupa {
        collect,
        eject
    }

    public Brat(HardwareMap hardwareMap)
    {

        servo_orizontal = hardwareMap.crservo.get(Config.brat_orizontal);
        motor_vertical = hardwareMap.dcMotor.get(Config.brat_vertical);
        servo_fata = hardwareMap.servo.get(Config.servo_cupa);

        servo_orizontal.resetDeviceConfigurationForOpMode();
        motor_vertical.resetDeviceConfigurationForOpMode();
        servo_fata.resetDeviceConfigurationForOpMode();

        motor_vertical.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motor_vertical.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        stare = StateCupa.eject;


    }

    public double getPosition() {
        return motor_vertical.getCurrentPosition();
    }

    public void move(double powerVertical, double powerOrizontal)
    {
        /*
                CR-someday bogdan: in clasa de gamepad am putea face axele sa returneze 0 daca
                                   nu sunt apasate suficient (echivalent cu if urile astea)
        */



        if(powerVertical < -0.03 || powerVertical > 0.03) {
//            motor_vertical.setPower(-powerVertical * 0.254);
            motor_vertical.setPower(-powerVertical);
        }

        else if(powerVertical < 0.03 && powerVertical > -0.03)
            motor_vertical.setPower(0.0);


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
        servo_fata.setPosition(0);
        stare = StateCupa.eject;
    }

    public void collect() {
        servo_fata.setPosition(0.5);
        stare = StateCupa.collect;
    }

    public void goToPosition(int pos)
    {
        motor_vertical.setTargetPosition(pos);
        motor_vertical.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        motor_vertical.setPower(1);
    }
}
