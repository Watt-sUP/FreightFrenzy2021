package org.firstinspires.ftc.teamcode.Carousel;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;

import org.firstinspires.ftc.teamcode.Config.Config;

@TeleOp(name = "Carusel Rata", group = "Carusel")
public class Rata extends LinearOpMode {
    /*
        CR-someday bogdan: ar fi bine sa mutam variabilele in interiorul functiei runOpMode,
                           pentru ca par locale doar acolo.
                           variabilele int care sunt 0 sau 1 putem sa le facem boolean.
                           variabilele nefolosite pot fi sterse.
     */
    private int powerrat = 0;
    private int isHeldservo, stateservo = 0;
    CRServo servoCarusel;
    @Override
    public void runOpMode() throws InterruptedException {

        servoCarusel = hardwareMap.crservo.get(Config.rate);

        servoCarusel.resetDeviceConfigurationForOpMode();
        waitForStart();

        while(opModeIsActive()) {
            /*
                CR bogdan: aici cred ca nu va merge codul pentru ca isHeldservo se va face 0 la
                           urmatoarea trecere prin loop chiar daca butonul este inca apasat.
                           practic ce se va intampla e ca se va porni si opri de multe ori, parand
                           ca merge sacadat.

                           vezi diferenta dintre

                           if(gamepad1.a && !isHeldservo) ..
                           else isHeldservo = 0

                           si

                           if(gamepad1.a && !isHeldservo) ..
                           else if(!gamepad1.a) isHeldservo = false
             */
            if (gamepad1.a && isHeldservo == 0) {
                isHeldservo = 1;
                if(stateservo == -1) {stateservo = 0;  servoCarusel.setPower(0);}
                else {stateservo = -1;  servoCarusel.setPower(-1);}
            }
            else if(gamepad1.b && isHeldservo == 0) {
                isHeldservo = 1;
                if (stateservo==1) {stateservo=0; servoCarusel.setPower(0.0);}
                else {stateservo=1; servoCarusel.setPower(1.0);}
            }
            else isHeldservo = 0;
        }
        }
    }

