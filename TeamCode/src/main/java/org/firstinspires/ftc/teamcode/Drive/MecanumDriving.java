package org.firstinspires.ftc.teamcode.Drive;

import android.annotation.SuppressLint;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

@TeleOp(name = "Freight Frenzy Mecanum Driving", group = "Drive")
public class MecanumDriving extends LinearOpMode {
    private final ElapsedTime runtime = new ElapsedTime();
    private int facing = 1;
    private boolean isHeld = false;
    private String facingData = "Forwards";

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
            double acceleration = gamepad1.left_stick_y;
            double strafe = gamepad1.left_stick_x * 1.1;
            double rotation = -gamepad1.right_stick_x;

            double powerLimit = 1.0 - gamepad1.right_trigger;
            if(gamepad1.b && !isHeld) {
                isHeld = true;
                facing = facing * (-1);

                if(facing == 1) facingData = "Forwards";
                else facingData = "Backwards";
            }
            else isHeld = false;

            double denominator = Math.max(Math.abs(acceleration) + Math.abs(strafe) + Math.abs(rotation), 1);
            double frontLeftPower = (acceleration + strafe + rotation) / denominator;
            double backLeftPower = (acceleration - strafe + rotation) / denominator;
            double frontRightPower = (acceleration - strafe - rotation) / denominator;
            double backRightPower = (acceleration + strafe - rotation) / denominator;

            frontLeftMotor.setPower(Range.clip(frontLeftPower, -powerLimit, powerLimit) * facing);
            backLeftMotor.setPower(Range.clip(backLeftPower, -powerLimit, powerLimit) * facing);
            frontRightMotor.setPower(Range.clip(frontRightPower, -powerLimit, powerLimit) * facing);
            backRightMotor.setPower(Range.clip(backRightPower, -powerLimit, powerLimit) * facing);

            telemetry.addData("Elapsed time:", runtime.toString());
            telemetry.addData("Power limit:", String.format("%.01f", powerLimit));
            telemetry.addData("Facing:", facingData);

            telemetry.update();
        }
    }
}
