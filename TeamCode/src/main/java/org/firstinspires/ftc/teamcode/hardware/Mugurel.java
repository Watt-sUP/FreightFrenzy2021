package org.firstinspires.ftc.teamcode.hardware;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Mugurel {
    public Rata rata;
    public Maturica maturica;
    public Cupa cupa;
    public Glisiere glisiere;
    public Ruleta ruleta;
    public DeadWheels wheels;
    public DistanceSensor distance;
    public Runner runner;

    public Mugurel(HardwareMap hm) {
        rata = new Rata(hm);
        maturica = new Maturica(hm);
        cupa = new Cupa(hm);
        glisiere = new Glisiere(hm);
        ruleta = new Ruleta(hm);
        wheels = new DeadWheels(hm);
        runner = new Runner(
                hm.get(DcMotor.class, Config.left_front),
                hm.get(DcMotor.class, Config.right_front),
                hm.get(DcMotor.class, Config.left_back),
                hm.get(DcMotor.class, Config.right_back)
        );
        distance = hm.get(DistanceSensor.class, Config.distance);
    }
}
