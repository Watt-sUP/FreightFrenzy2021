package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp(name="Servo Position Tester Glisiere", group = "Testing")
public class PositionTester extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {
        DcMotor motorTest = hardwareMap.dcMotor.get("GLI");
        motorTest.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        waitForStart();
        while (opModeIsActive()) {
            int currentTicks = motorTest.getCurrentPosition();
            if(gamepad1.a) {
                motorTest.setTargetPosition(currentTicks + 50);
                motorTest.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                motorTest.setPower(0.3);
            }

            if(gamepad1.a) {
                motorTest.setTargetPosition(currentTicks + 500);
                motorTest.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                motorTest.setPower(0.3);
            }
            idle();
            telemetry.addData("Current Ticks:", currentTicks);
            telemetry.update();
        }
        motorTest.setPower(0.0);
        motorTest.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }
}
