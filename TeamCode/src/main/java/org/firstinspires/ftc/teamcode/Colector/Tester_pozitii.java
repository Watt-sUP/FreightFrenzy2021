package org.firstinspires.ftc.teamcode.Colector;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.Config.Config;

@TeleOp(name="Servo Position Tester Glisiere", group = "Testing")
public class Tester_pozitii extends LinearOpMode {
    Servo servoTest;

    double servoPosition = 0.04;
    @Override
    public void runOpMode() throws InterruptedException {
        Servo servoTest = hardwareMap.servo.get(Config.cupa);
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

            if(gamepad1.y) {
                if (servoPosition == 0.04) servoPosition = 0.70;
                else servoPosition = 0.04;
                servoTest.setPosition(servoPosition);
            }
            idle();
            telemetry.addData("Current Ticks:", currentTicks);
            telemetry.addData("ServoPosition:", servoPosition);
            telemetry.update();
        }
        motorTest.setPower(0.0);
        motorTest.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }
}

