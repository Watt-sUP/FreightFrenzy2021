package org.firstinspires.ftc.teamcode.teleOp.testers;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.teamcode.hardware.DeadWheels;

@TeleOp(name = "TesterDead")

public class DeadOpmode extends LinearOpMode{

    boolean isHeld = false;


    @Override
    public void runOpMode() throws  InterruptedException {
        DeadWheels wheels = new DeadWheels(hardwareMap);
        waitForStart();

        while (opModeIsActive()) {
            if (gamepad1.a && !isHeld) {
                wheels.setDown();
                isHeld = true;
            } else if (!gamepad1.a)
                isHeld = false;

            if(gamepad1.b && !isHeld) {
                wheels.setUp();
                isHeld = true;
            } else if (!gamepad1.b)
                isHeld = false;
        }
    }
}
