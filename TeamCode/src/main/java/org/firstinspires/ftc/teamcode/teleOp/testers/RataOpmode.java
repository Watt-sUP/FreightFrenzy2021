package org.firstinspires.ftc.teamcode.teleOp.testers;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.hardware.Rata;

@TeleOp(name = "Carousel Rata", group = "Carusel")
@Disabled
public class RataOpmode extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        int stateMotor = 0;
        boolean isHeldMotor = false;
        int position = 1500;
        Rata rata = new Rata(hardwareMap, telemetry);
        waitForStart();

        while (opModeIsActive()) {
            if (gamepad1.a && !isHeldMotor) {
                isHeldMotor = true;
                if (stateMotor == -1) {
                    stateMotor = 0;
                    rata.rotate(0);

                } else {
                    stateMotor = -1;
                    rata.rotate(-0.75);
                }
            } else if (gamepad1.b && !isHeldMotor) {
                isHeldMotor = true;
                if (stateMotor == 1) {
                    stateMotor = 0;
                    rata.rotate(0.0);
                } else {
                    stateMotor = 1;
                    rata.score(position, (rata.motor.getCurrentPosition() * 100 / position) / 200 + 0.5);
                    while (rata.motor.isBusy()) {
                        telemetry.addData("Current power:", rata.motor.getPower());
                        telemetry.update();
                    }
//                    rata.rotate(0.75);
                }
            } else if (!gamepad1.a && !gamepad1.b) isHeldMotor = false;
            telemetry.update();
        }
    }
}