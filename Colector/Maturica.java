package org.firstinspires.ftc.teamcode.Colector;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp(name="Matura Controlat", group="Colectare")
public class Maturica extends LinearOpMode {
    private String motorData = "Idle";
    private int isHeld, state = 0;

    @Override
    public void runOpMode() throws InterruptedException {
        DcMotor motorMatura = hardwareMap.dcMotor.get("MAT");
        waitForStart();

        while (opModeIsActive()) {
            if(gamepad2.b && isHeld == 0) {
                isHeld = 1;
                if(state == -1) {state = 0; motorData="Idle"; motorMatura.setPower(0.0);}
                else {state = -1; motorData="Ejecting"; motorMatura.setPower(-1.0);}
            }
            else if(gamepad2.a && isHeld == 0) {
                isHeld = 1;
                if (state==1) {state=0; motorData = "Idle"; motorMatura.setPower(0.0);}
                else {state=1; motorData = "Collecting"; motorMatura.setPower(1.0);}
            }
            else isHeld = 0;
            telemetry.addData("Motor Status:", motorData);
            telemetry.update();
        }
    }
}
