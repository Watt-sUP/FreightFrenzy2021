package org.firstinspires.ftc.teamcode.FreightFrenzy;

import android.annotation.SuppressLint;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

@TeleOp(name = "Freight Frenzy Mecanum Driving")
public class MecanumDriving extends LinearOpMode {
    private final ElapsedTime runtime = new ElapsedTime();

    @SuppressLint("DefaultLocale")
    @Override
    public void runOpMode() throws InterruptedException {
        DcMotor frontLeftMotor = hardwareMap.dcMotor.get("lf");
        DcMotor backLeftMotor = hardwareMap.dcMotor.get("lb");
        DcMotor frontRightMotor = hardwareMap.dcMotor.get("rf");
        DcMotor backRightMotor = hardwareMap.dcMotor.get("rb");

        frontRightMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        backRightMotor.setDirection(DcMotorSimple.Direction.REVERSE);

        waitForStart();
        runtime.reset();

        while (opModeIsActive()) {
            double y = gamepad1.left_stick_y;
            double x = gamepad1.left_stick_x * 1.1;
            double rx = -gamepad1.right_stick_x;

            double limit = 1.0 - gamepad1.right_trigger;

            double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);
            double frontLeftPower = (y + x + rx) / denominator;
            double backLeftPower = (y - x + rx) / denominator;
            double frontRightPower = (y - x - rx) / denominator;
            double backRightPower = (y + x - rx) / denominator;

            frontLeftMotor.setPower(Range.clip(frontLeftPower, -limit, limit));
            backLeftMotor.setPower(Range.clip(backLeftPower, -limit, limit));
            frontRightMotor.setPower(Range.clip(frontRightPower, -limit, limit));
            backRightMotor.setPower(Range.clip(backRightPower, -limit, limit));

            telemetry.addData("Elapsed time:", runtime.toString());
            telemetry.addData("Power limit:", String.format("%.01f", limit));

            telemetry.update();
        }
    }
}