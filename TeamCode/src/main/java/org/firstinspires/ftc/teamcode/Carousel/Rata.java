package org.firstinspires.ftc.teamcode.Carousel;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.Servo;
<<<<<<< HEAD
<<<<<<< HEAD

import org.firstinspires.ftc.teamcode.Config.Config;
=======
>>>>>>> ecad45d32c989cb8f905146baef09cfcea2ca48a
=======
>>>>>>> ecad45d32c989cb8f905146baef09cfcea2ca48a

@TeleOp(name = "Carusel Rata", group = "Carusel")
public class Rata extends LinearOpMode {
    private int powerrat = 0;
    private int isHeldservo, stateservo = 0;
    CRServo servoCarusel;
    @Override
    public void runOpMode() throws InterruptedException {

<<<<<<< HEAD
<<<<<<< HEAD
        servoCarusel = hardwareMap.crservo.get(Config.rate);
=======
        servoCarusel = hardwareMap.crservo.get("RATE");
>>>>>>> ecad45d32c989cb8f905146baef09cfcea2ca48a
=======
        servoCarusel = hardwareMap.crservo.get("RATE");
>>>>>>> ecad45d32c989cb8f905146baef09cfcea2ca48a
        servoCarusel.resetDeviceConfigurationForOpMode();
        waitForStart();

        while(opModeIsActive()) {
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

