package org.firstinspires.ftc.teamcode.Carousel;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;

@TeleOp(name = "Carusel Rata", group = "Carusel")
public class Rata extends LinearOpMode {
    private boolean isHeld = false;


    @Override
    public void runOpMode() throws InterruptedException {
        CRServo servoCarusel = hardwareMap.crservo.get("RATE");
        servoCarusel.resetDeviceConfigurationForOpMode();
        waitForStart();

        while(opModeIsActive()) {
            if(gamepad2.y && !isHeld) {
                servoCarusel.setPower(1.0);
                isHeld = true;
            }
            else { isHeld = false; servoCarusel.setPower(0.0); }
        }
    }
}
