package org.firstinspires.ftc.teamcode.teleOp.testers;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.hardware.Rata;

@TeleOp(name = "Carousel Rata", group = "Carusel")
@Disabled
public class RataOpmode extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        int stateMotor = 0;
        boolean isHeldMotor = false;
        double startingPower = 0;
        long start_time = System.nanoTime();
        int position = 1500;
        Rata rata = new Rata(hardwareMap, telemetry);
        waitForStart();

        while (opModeIsActive()) {
//            if (gamepad1.a && !isHeldMotor) {
//                isHeldMotor = true;
//                if (stateMotor == -1) {
//                    stateMotor = 0;
//                    rata.rotate(0);
//
//                } else {
//                    stateMotor = -1;
//                    rata.rotate(-0.75);
//                }
//            }
            if (gamepad1.b && !isHeldMotor) {
                    isHeldMotor = true;
                    startingPower = 0.7;
                    start_time = System.nanoTime();
                    if(stateMotor == 0){ rata.rotate(startingPower); stateMotor = 1;}
                    else { rata.rotate(0.0); stateMotor = 0; }
            }
            else if (!gamepad1.b) isHeldMotor = false;

            if(rata.motor.isBusy() && stateMotor == 1) {
                long end_time = System.nanoTime();
                double difference = (end_time - start_time) / 1e6;

                if(difference >= 500 && difference <= 1500) rata.rotate(startingPower + (difference - 1000) / 30);
                telemetry.addData("Power:", startingPower + (difference - 1000) / 30);
                telemetry.update();
            }
        }
    }
}
