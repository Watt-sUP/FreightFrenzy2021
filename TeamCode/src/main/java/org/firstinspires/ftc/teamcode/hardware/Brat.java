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
    private int[] poz = {0, 400, 1975, 2130, 2275};
    public boolean isBrat = false;
    public int pozition = 0;

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

    public void move(double powerOrizontal)
    {
        if(powerOrizontal < -0.03 || powerOrizontal > 0.03) {
            servo_orizontal.setPower(-powerOrizontal * 0.5);
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
        servo_fata.setPosition(1);
        stare = StateCupa.collect;
    }

    public void changePosition(double change) {
        if(change >= 0.1 && !isBrat) {
            if(pozition < 4)
                pozition++;
            isBrat = true;
            goToPosition(poz[pozition]);
        } else if(change <= -0.1 && !isBrat) {
            if(pozition > 0)
                pozition--;
            isBrat = true;
            goToPosition(poz[pozition]);
        }
        else if(change == 0) isBrat = false;
    }

    public void goToPosition(int pos)
    {
        motor_vertical.setTargetPosition(pos);
        motor_vertical.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        motor_vertical.setPower(1);
    }
}
