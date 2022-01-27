package org.firstinspires.ftc.teamcode.Carousel;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.Config.Config;

@TeleOp(name = "Carusel Rata", group = "Carusel")
public class Rata extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        int stateMotor = 0;
        boolean isHeldMotor = false;
        DcMotor motorCarousel;
        motorCarousel = hardwareMap.dcMotor.get(Config.rate);

        motorCarousel.resetDeviceConfigurationForOpMode();
        waitForStart();

        while (opModeIsActive()) {
            if (gamepad1.a && !isHeldMotor) {
                isHeldMotor = true;
                if (stateMotor == -1) {
                    stateMotor = 0;
                    motorCarousel.setPower(0);
                } else {
                    stateMotor = -1;
                    motorCarousel.setPower(-0.7);
                }
            } else if (gamepad1.b && !isHeldMotor) {
                isHeldMotor = true;
                if (stateMotor == 1) {
                    stateMotor = 0;
                    motorCarousel.setPower(0.0);
                } else {
                    stateMotor = 1;
                    motorCarousel.setPower(0.7);
                }
            } else if (!gamepad1.a && !gamepad1.b) isHeldMotor = false;
        }
    }
}

