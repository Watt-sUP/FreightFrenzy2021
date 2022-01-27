package org.firstinspires.ftc.teamcode.Ruleta;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.Config.Config;

@TeleOp(name = "Ruleta", group = "Ruleta")
public class Ruleta extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {

        int stateServoX = 0, stateServoY = 0, stateServoEx = 0;
        boolean isXHeld = false, isYHeld = false, isExHeld = false;
        /*
            [X] CR-someday bogdan: nume mai sugestive pentru cele 3 servo-uri
         */
        CRServo servoRuletaX;
        CRServo servoRuletaY;
        CRServo servoRuletaEx;

        servoRuletaX = hardwareMap.crservo.get(Config.rul1);
        servoRuletaY = hardwareMap.crservo.get(Config.rul2);
        servoRuletaEx = hardwareMap.crservo.get(Config.rul3);

        servoRuletaX.resetDeviceConfigurationForOpMode();
        servoRuletaY.resetDeviceConfigurationForOpMode();
        servoRuletaEx.resetDeviceConfigurationForOpMode();

        waitForStart();

        while (opModeIsActive()) {
            /*
                CR bogdan: servo urile ar trebui sa se roteasca doar atunci cand axa nu este 0,
                           in plus, este folosita aceeasi variabila pentru a tine starea tuturor
                           celor 3 servo uri.
             */

            if((gamepad2.left_stick_x < -0.1 || gamepad2.left_stick_x > 0.1) && !isXHeld) {
                isXHeld = true;
                servoRuletaX.setPower(gamepad2.left_stick_x);
            }
            else if(gamepad2.left_stick_x < 0.1 && gamepad2.left_stick_x > -0.1) { isXHeld = false; servoRuletaX.setPower(0.0);}

            if((gamepad2.left_stick_y < -0.1 || gamepad2.left_stick_y > 0.1) && !isYHeld) {
                isYHeld = true;
                servoRuletaY.setPower(-gamepad2.left_stick_y);
            }
            else if(gamepad2.left_stick_y < 0.1 && gamepad2.left_stick_y > -0.1) { isYHeld = false; servoRuletaY.setPower(0.0); }

            if((gamepad2.right_stick_y < -0.1 || gamepad2.right_stick_y > 0.1) && !isExHeld) {
                isExHeld = true;
                servoRuletaEx.setPower(-gamepad2.right_stick_y);
            }
            else if(gamepad2.right_stick_y < 0.1 && gamepad2.right_stick_y > -0.1) { isExHeld = false; servoRuletaEx.setPower(0.0); }
        }
    }
}