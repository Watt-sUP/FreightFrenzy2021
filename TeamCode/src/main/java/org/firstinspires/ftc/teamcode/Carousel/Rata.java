package org.firstinspires.ftc.teamcode.Carousel;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name = "Carusel Rata", group = "Carusel")
public class Rata extends LinearOpMode {
    private int powerrat = 0;
    private int isHeld, state = 0;
    CRServo servoCarusel;
    @Override
    public void runOpMode() throws InterruptedException {

        servoCarusel = hardwareMap.crservo.get("RATE");
        servoCarusel.resetDeviceConfigurationForOpMode();
        waitForStart();

        while(opModeIsActive()) {
            if (gamepad1.a && isHeld == 0) {
                isHeld = 1;
                if(state == -1) {state = 0;  servoCarusel.setPower(0);}
                else {state = -1;  servoCarusel.setPower(-1);}
            }
            else if(gamepad1.b && isHeld == 0) {
                isHeld = 1;
                if (state==1) {state=0; servoCarusel.setPower(0.0);}
                else {state=1; servoCarusel.setPower(1.0);}
            }
            else isHeld = 0;
        }
        }
    }

