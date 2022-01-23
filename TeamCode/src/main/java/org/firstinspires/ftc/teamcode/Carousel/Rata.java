package org.firstinspires.ftc.teamcode.Carousel;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;

import org.firstinspires.ftc.teamcode.Config.Config;

@TeleOp(name = "Carusel Rata", group = "Carusel")
public class Rata extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        int stateServo = 0;
        boolean isHeldServo = false;
        CRServo servoCarousel;
        servoCarousel = hardwareMap.crservo.get(Config.rate);

        servoCarousel.resetDeviceConfigurationForOpMode();
        waitForStart();

        while (opModeIsActive()) {
            if (gamepad1.a && !isHeldServo) {
                isHeldServo = true;
                if (stateServo == -1) {
                    stateServo = 0;
                    servoCarousel.setPower(0);
                } else {
                    stateServo = -1;
                    servoCarousel.setPower(-1);
                }
            } else if (gamepad1.b && !isHeldServo) {
                isHeldServo = true;
                if (stateServo == 1) {
                    stateServo = 0;
                    servoCarousel.setPower(0.0);
                } else {
                    stateServo = 1;
                    servoCarousel.setPower(1.0);
                }
            } else if (!gamepad1.a && !gamepad1.b) isHeldServo = false;
        }
    }
}

