package org.firstinspires.ftc.teamcode.autonom.testers;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.hardware.Config;
import org.firstinspires.ftc.teamcode.hardware.Runner;

@Autonomous(name = "Rotate Tester", group = "Autonom")
public class RotateTester extends LinearOpMode {

    private Runner runner;

    @Override
    public void runOpMode() throws InterruptedException {

        Runner runner = new Runner(hardwareMap, telemetry);
        waitForStart();
        runner.rotate(-90);
        while (opModeIsActive()) {
            telemetry.addData("Heading:", runner.getHeading());
            telemetry.update();
        };
    }
}