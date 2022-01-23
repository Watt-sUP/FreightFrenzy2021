package org.firstinspires.ftc.teamcode.Colector;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Config.Config;

@TeleOp(name = "Cupa" )
public class CUPA extends LinearOpMode {
    Servo servoCupa;
    private ElapsedTime runtime = new ElapsedTime();
    double servoPosition = 0.0;
    boolean isHeld = false;

    @Override
    public void runOpMode() throws  InterruptedException {
        servoCupa = hardwareMap.servo.get(Config.cupa);

        waitForStart();

        while (opModeIsActive()) {
            /*
                CR-someday bogdan: de obicei e bine sa evitam comparatia de egalitate pe float/double
                                   astfel, vom avea o variabila care tine starea/pozitia in care este servo-ul
             */
            /*
                CR bogdan:  codul functioneaza sacadat (se trece de mai multe ori prin loop la
                            o singura apasare de buton). trebuie schimbat astfel incat schimbarea
                            pozitie sa se efectueze doar o singura data la o apasare de buton
             */
            if (gamepad1.a && !isHeld) {
                if (servoPosition <= 0.02) servoPosition = 0.5;
                else servoPosition = 0.0;
                servoCupa.setPosition(servoPosition);
                isHeld = true;
            }
            else if(!gamepad1.a) isHeld = false;
            telemetry.addData("Elapsed Time:", runtime.toString());
            telemetry.addData("Servo position:", servoPosition);
        }

    }
}