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
    private boolean faceIsHeld = false, triggerIsHeld = false, faceChanged;
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

            double powerLimit;
            if(gamepad1.left_trigger > 0.3 && !triggerIsHeld) {
                triggerIsHeld = true;
                powerLimit = 0.2;
            }
            else if(gamepad1.right_trigger > 0.3 && !triggerIsHeld) {
                triggerIsHeld = true;
                powerLimit = 0.5;
            }
            else {
                powerLimit = 1.0;
                triggerIsHeld = false;
            }


            if(gamepad1.y && !faceIsHeld) {
                faceIsHeld = true;
                faceChanged = !faceChanged;
            }
            else faceIsHeld = false;

            double denominator = Math.max(Math.abs(acceleration) + Math.abs(strafe) + Math.abs(rotation), 1);
            double frontLeftPower = (acceleration + strafe + rotation) / denominator;
            double backLeftPower = (acceleration - strafe + rotation) / denominator;
            double frontRightPower = (acceleration - strafe - rotation) / denominator;
            double backRightPower = (acceleration + strafe - rotation) / denominator;


            if(faceChanged == false) {
                frontLeftMotor.setPower(Range.clip(frontLeftPower, -powerLimit, powerLimit));
                backLeftMotor.setPower(Range.clip(backLeftPower, -powerLimit, powerLimit));
                frontRightMotor.setPower(Range.clip(frontRightPower, -powerLimit, powerLimit));
                backRightMotor.setPower(Range.clip(backRightPower, -powerLimit, powerLimit));

                facingData = "Forwards";
            }
            else {
                frontLeftMotor.setPower(Range.clip(backRightPower, -powerLimit, powerLimit));
                backLeftMotor.setPower(Range.clip(frontRightPower, -powerLimit, powerLimit));
                frontRightMotor.setPower(Range.clip(backLeftPower, -powerLimit, powerLimit));
                backRightMotor.setPower(Range.clip(frontLeftPower, -powerLimit, powerLimit));
            }

            telemetry.addData("Elapsed time:", runtime.toString());
            telemetry.addData("Power limit:", String.format("%.01f", powerLimit));
            telemetry.addData("Facing:", facingData);

            telemetry.update();
        }
    }
}
