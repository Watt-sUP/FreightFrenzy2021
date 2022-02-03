package org.firstinspires.ftc.teamcode.teleOp.testers;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.hardware.Cupa;

@TeleOp(name = "Cupa", group = "Colectare")
@Disabled
public class CupaOpmode extends LinearOpMode {
    Cupa cupa = new Cupa(hardwareMap, telemetry);
    boolean isHeld = false;

    @Override
    public void runOpMode() throws  InterruptedException {
        waitForStart();

        while (opModeIsActive()) {
            if (gamepad1.a && !isHeld) {
                cupa.toggleCupa();
                isHeld = true;
            }
            else if(!gamepad1.a) isHeld = false;
            telemetry.addData("Servo position:", cupa.getServoPosition());
        }

    }
}