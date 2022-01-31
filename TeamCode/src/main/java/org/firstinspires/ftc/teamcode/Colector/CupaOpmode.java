package org.firstinspires.ftc.teamcode.Colector;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.hardware.Config;
import org.firstinspires.ftc.teamcode.hardware.Cupa;

@TeleOp(name = "Cupa", group = "Colectare")
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