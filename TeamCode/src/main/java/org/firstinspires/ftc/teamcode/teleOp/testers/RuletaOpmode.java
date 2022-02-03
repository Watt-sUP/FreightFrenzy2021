package org.firstinspires.ftc.teamcode.teleOp.testers;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.hardware.Ruleta;

@TeleOp(name = "Ruleta", group = "Ruleta")
@Disabled
public class RuletaOpmode extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {

        waitForStart();

        while (opModeIsActive()) {

            Ruleta ruleta = new Ruleta(hardwareMap, telemetry);
            ruleta.move(-gamepad2.left_stick_y,-gamepad2.left_stick_x / 2,gamepad2.right_stick_y / 2);

            telemetry.addData("Pozitie x", gamepad2.left_stick_x);
            telemetry.addData("Pozitie y", gamepad2.left_stick_y);
            telemetry.addData("Pozitie fata", gamepad2.right_stick_y);
            telemetry.update();
        }

    }

}