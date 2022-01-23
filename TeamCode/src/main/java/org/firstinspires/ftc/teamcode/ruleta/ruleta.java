package org.firstinspires.ftc.teamcode.ruleta;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;

import org.firstinspires.ftc.teamcode.Config.Config;

@TeleOp(name = "Ruleta", group = "Ruleta")
public class ruleta extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {
        int stateServo = 0;
        boolean isRuletaHeld = false;

        /*
            CR-someday bogdan: nume mai sugestive pentru cele 3 servo-uri
         */
        CRServo servoRuleta1;
        CRServo servoRuleta2;
        CRServo servoRuleta3;

        servoRuleta1 = hardwareMap.crservo.get(Config.rul1);
        servoRuleta2 = hardwareMap.crservo.get(Config.rul2);
        servoRuleta3 = hardwareMap.crservo.get(Config.rul3);

        servoRuleta1.resetDeviceConfigurationForOpMode();
        waitForStart();


        while (opModeIsActive()) {
            /*
                CR bogdan: servo urile ar trebui sa se roteasca doar atunci cand axa nu este 0,
                           in plus, este folosita aceeasi variabila pentru a tine starea tuturor
                           celor 3 servo uri.
             */

            if (gamepad2.left_stick_y == 1 && !isRuletaHeld) {
                isRuletaHeld = true;
                if (stateServo == -1) {
                    stateServo = 0;
                    servoRuleta1.setPower(0);
                } else {
                    stateServo = -1;
                    servoRuleta1.setPower(-1);
                }
            } else if (gamepad2.left_stick_y == -1 && !isRuletaHeld) {
                isRuletaHeld = true;
                if (stateServo == 1) {
                    stateServo = 0;
                    servoRuleta1.setPower(0.0);
                } else {
                    stateServo = 1;
                    servoRuleta1.setPower(1.0);
                }
            } else if (gamepad2.left_stick_x == 1 && !isRuletaHeld) {
                isRuletaHeld = true;
                if (stateServo == -1) {
                    stateServo = 0;
                    servoRuleta2.setPower(0);
                } else {
                    stateServo = -1;
                    servoRuleta2.setPower(-1);
                }
            } else if (gamepad2.left_stick_x == -1 && !isRuletaHeld) {
                isRuletaHeld = true;
                if (stateServo == 1) {
                    stateServo = 0;
                    servoRuleta2.setPower(0.0);
                } else {
                    stateServo = 1;
                    servoRuleta2.setPower(1.0);
                }
            } else if (gamepad2.right_stick_y == 1 && !isRuletaHeld) {
                isRuletaHeld = true;
                if (stateServo == -1) {
                    stateServo = 0;
                    servoRuleta3.setPower(0);
                } else {
                    stateServo = -1;
                    servoRuleta3.setPower(-1);
                }
            } else if (gamepad2.right_stick_y == -1 && !isRuletaHeld) {
                isRuletaHeld = true;
                if (stateServo == 1) {
                    stateServo = 0;
                    servoRuleta3.setPower(0.0);
                } else {
                    stateServo = 1;
                    servoRuleta3.setPower(1.0);
                }
            } else if (gamepad2.left_stick_y == 0 && gamepad2.left_stick_x == 0 && gamepad2.right_stick_y == 0)
                isRuletaHeld = false;
        }
    }
}

