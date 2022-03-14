package org.firstinspires.ftc.teamcode.hardware;

import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Mugurel {
    public Rata rata;
    public Maturica maturica;
    public Cupa cupa;
    public Glisiere glisiere;
    public Brat brat;
    public DeadWheels wheels;
    public DistanceSensor distance;
    public Runner runner;

    public Mugurel(HardwareMap hm) {
        rata = new Rata(hm);
        maturica = new Maturica(hm);
        cupa = new Cupa(hm);
        glisiere = new Glisiere(hm);
        brat = new Brat(hm);
        wheels = new DeadWheels(hm);
        distance = hm.get(DistanceSensor.class, Config.distance);
    }
}
