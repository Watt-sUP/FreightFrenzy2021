package org.firstinspires.ftc.teamcode.teleOp.testers;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@TeleOp(name = "DistanceTester")
public class DistanceTest extends LinearOpMode {
    DistanceSensor distance;

    @Override
    public void runOpMode() {
        distance = hardwareMap.get(DistanceSensor.class, "Distance");


        waitForStart();
        while (opModeIsActive()) {

            telemetry.addData("Distance: ",distance.getDistance(DistanceUnit.CM));
            telemetry.update();

        }
    }
}
