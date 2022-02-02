package org.firstinspires.ftc.teamcode.teleOp.testers;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.hardware.Config;
import org.firstinspires.ftc.teamcode.hardware.Cupa;

@TeleOp(name="Servo Position Tester Glisiere", group = "Testing")
@Disabled
public class Tester_pozitii extends LinearOpMode {

    private boolean isHeld = false;
    @Override
    public void runOpMode() throws InterruptedException {
        Cupa cupa = new Cupa(hardwareMap, telemetry);
        DcMotor motorTest = hardwareMap.dcMotor.get(Config.glisiera);
        motorTest.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        waitForStart();
        while (opModeIsActive()) {
            int currentTicks = motorTest.getCurrentPosition();
            if(gamepad2.right_bumper) {
                motorTest.setTargetPosition(currentTicks - 125);
                motorTest.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                motorTest.setPower(0.15);
            }

            if(gamepad2.left_bumper) {
                motorTest.setTargetPosition(currentTicks + 125);
                motorTest.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                motorTest.setPower(0.15);
            }

            if(gamepad1.y && !isHeld) {
                cupa.toggleCupa();
                isHeld = true;
            }
            else if(!gamepad1.y) isHeld = false;
            idle();
            telemetry.addData("Current Ticks:", currentTicks);
            telemetry.addData("ServoPosition:", cupa.getServoPosition());
            telemetry.update();
        }
        motorTest.setPower(0.0);
        motorTest.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }
}

