package org.firstinspires.ftc.teamcode.Colector;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.Config.Config;

@TeleOp(name="Matura Controlat", group="Colectare")
public class Maturica extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {
        DcMotor motorMatura = hardwareMap.dcMotor.get(Config.matura);
        String motorData = "Idle";
        int state = 0;
        boolean isHeld = false;
        waitForStart();

        while (opModeIsActive()) {
            if(gamepad2.b && !isHeld) {
                isHeld = true;
                if(state == -1) {state = 0; motorData="Idle"; motorMatura.setPower(0.0);}
                else {state = -1; motorData="Ejecting"; motorMatura.setPower(-1.0);}
            }
            else if(gamepad2.a && !isHeld) {
                isHeld = true;
                if (state==1) {state=0; motorData = "Idle"; motorMatura.setPower(0.0);}
                else {state=1; motorData = "Collecting"; motorMatura.setPower(1.0);}
            }
            else if(!gamepad2.a && !gamepad2.b) isHeld = false;
            telemetry.addData("Motor Status:", motorData);
            telemetry.update();
        }
    }
}
