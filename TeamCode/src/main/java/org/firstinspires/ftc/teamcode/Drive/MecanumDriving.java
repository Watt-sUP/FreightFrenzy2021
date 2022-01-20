package org.firstinspires.ftc.teamcode.Drive;

import android.annotation.SuppressLint;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.Config.Config;

@TeleOp(name = "Controlat Mecanum", group = "Drive")
public class MecanumDriving extends LinearOpMode {



    @SuppressLint("DefaultLocale")
    @Override
    public void runOpMode() throws InterruptedException {
        DcMotor frontLeftMotor = hardwareMap.dcMotor.get(Config.left_front);
        DcMotor backLeftMotor = hardwareMap.dcMotor.get(Config.left_back);
        DcMotor frontRightMotor = hardwareMap.dcMotor.get(Config.right_front);
        DcMotor backRightMotor = hardwareMap.dcMotor.get(Config.right_back);

        String facingData = "Forwards";
        final ElapsedTime runtime = new ElapsedTime();
        boolean faceIsHeld = false, faceChanged = false;

        waitForStart();
        runtime.reset();

        while (opModeIsActive()) {
            double acceleration = gamepad1.left_stick_y;
            double strafe = gamepad1.left_stick_x * -1.1;
            double rotation = gamepad1.right_stick_x;

            double powerLimit = 1.0;
            /*
               [X] CR bogdan: aici nu cred ca e nevoie de variabila triggerIsHeld
             */
            if(gamepad1.left_trigger >= 0.3) powerLimit = 0.2;
            else if(gamepad1.right_trigger >= 0.3) powerLimit = 0.5;
            else if(gamepad1.left_trigger < 0.3 && gamepad1.right_trigger < 0.3) powerLimit = 1.0;

            /*
                CR-someday bogdan: pare complicat modul in care este facuta schimbarea de fete
                                   se poate face mult mai usor prin schimbarea motoarelor ca obiecte
                                   adica frontRightMotor = initial_frontRightMotor (in caz de face = forward)
                                      si frontRightMotor = initial_backLeftMotor (in caz de face = backward)
             */
            if(gamepad1.y && !faceIsHeld) {
                faceIsHeld = true;
                faceChanged = !faceChanged;
            }
            else if(!gamepad1.y) faceIsHeld = false;

            if(!faceChanged) {

                frontRightMotor.setDirection(DcMotorSimple.Direction.REVERSE);
                backRightMotor.setDirection(DcMotorSimple.Direction.REVERSE);
                frontLeftMotor.setDirection(DcMotorSimple.Direction.FORWARD);
                backLeftMotor.setDirection(DcMotorSimple.Direction.FORWARD);

                rotation = rotation * (-1);
                facingData = "Forwards";
            }
            else {
                frontRightMotor.setDirection(DcMotorSimple.Direction.FORWARD);
                backRightMotor.setDirection(DcMotorSimple.Direction.FORWARD);
                frontLeftMotor.setDirection(DcMotorSimple.Direction.REVERSE);
                backLeftMotor.setDirection(DcMotorSimple.Direction.REVERSE);

                facingData = "Backwards";
            }

            double denominator = Math.max(Math.abs(acceleration) + Math.abs(strafe) + Math.abs(rotation), 1);
            double frontLeftPower = (acceleration + strafe + rotation) / denominator;
            double backLeftPower = (acceleration - strafe + rotation) / denominator;
            double frontRightPower = (acceleration - strafe - rotation) / denominator;
            double backRightPower = (acceleration + strafe - rotation) / denominator;

            frontLeftMotor.setPower(Range.clip(frontLeftPower, -powerLimit, powerLimit));
            backLeftMotor.setPower(Range.clip(backLeftPower, -powerLimit, powerLimit));
            frontRightMotor.setPower(Range.clip(frontRightPower, -powerLimit, powerLimit));
            backRightMotor.setPower(Range.clip(backRightPower, -powerLimit, powerLimit));

            telemetry.addData("Elapsed time:", runtime.toString());
            telemetry.addData("Power limit:", String.format("%.01f", powerLimit));
            telemetry.addData("Facing:", facingData);

            telemetry.update();
        }
    }
}
