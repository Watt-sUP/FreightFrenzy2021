package org.firstinspires.ftc.teamcode.Colector;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.hardware.Maturica;

@TeleOp(name="Matura Controlat", group="Colectare")
public class MaturicaOpmode extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {
        Maturica maturica = new Maturica(hardwareMap, telemetry);
        boolean isHeld = false;
        waitForStart();

        while (opModeIsActive()) {
            if(gamepad2.b && !isHeld) {
                isHeld = true;
                maturica.toggleEject();
            }
            else if(gamepad2.a && !isHeld) {
                isHeld = true;
                maturica.toggleCollect();
            }
            else if(!gamepad2.a && !gamepad2.b) isHeld = false;
            telemetry.addData("Motor Status:", maturica.getMaturaData());
            telemetry.update();
        }
    }
}
