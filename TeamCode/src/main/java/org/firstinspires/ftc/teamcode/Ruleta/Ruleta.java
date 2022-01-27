package org.firstinspires.ftc.teamcode.Ruleta;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Config.Config;

@TeleOp(name = "Ruleta", group = "Ruleta")
public class Ruleta extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {

        /*
            [X] CR-someday bogdan: nume mai sugestive pentru cele 3 servo-uri
         */
        CRServo servoRuletaX;
        CRServo servoRuletaY;
        CRServo servoRuletaEx;

        servoRuletaX = hardwareMap.crservo.get(Config.rul_x);
        servoRuletaY = hardwareMap.crservo.get(Config.rul_y);
        servoRuletaEx = hardwareMap.crservo.get(Config.rul_fata);

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

            if(gamepad2.left_stick_x < -0.1 || gamepad2.left_stick_x > 0.1) {
                servoRuletaX.setPower(-gamepad2.left_stick_x);
            }
            else if(gamepad2.left_stick_x < 0.1 && gamepad2.left_stick_x > -0.1)
                servoRuletaX.setPower(0.0);

            if(gamepad2.left_stick_y < -0.1 || gamepad2.left_stick_y > 0.1) {
                servoRuletaY.setPower(gamepad2.left_stick_y);
            }
            else if(gamepad2.left_stick_y < 0.1 && gamepad2.left_stick_y > -0.1)
                servoRuletaY.setPower(0.0);

            if(gamepad2.right_stick_y < -0.1 || gamepad2.right_stick_y > 0.1) {
                servoRuletaEx.setPower(-gamepad2.right_stick_y);
            }
            else if(gamepad2.right_stick_y < 0.1 && gamepad2.right_stick_y > -0.1)
                servoRuletaEx.setPower(0.0);

            telemetry.addData("Pozitie x", gamepad2.left_stick_x);
            telemetry.addData("Pozitie y", gamepad2.left_stick_y);
            telemetry.addData("Pozitie fata", gamepad2.right_stick_y);
            telemetry.update();
        }

    }

}
