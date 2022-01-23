package org.firstinspires.ftc.teamcode.Autonom;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.teamcode.Config.Config;

@TeleOp(name="Tester Pozitii Encodere", group = "Testing")
public class TesterEncodere extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {

        telemetry.addData("OpMode:", "Initiated.");
        boolean isHeld = false;

        DcMotor leftFront = hardwareMap.dcMotor.get("LF");
        DcMotor leftBack = hardwareMap.dcMotor.get("LB");
        DcMotor rightFront = hardwareMap.dcMotor.get("RF");
        DcMotor rightBack = hardwareMap.dcMotor.get("RB");

        rightBack.setDirection(DcMotorSimple.Direction.REVERSE);
        rightFront.setDirection(DcMotorSimple.Direction.REVERSE);

        leftFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);


        waitForStart();
        while (opModeIsActive()) {
            int LFTicks = leftFront.getCurrentPosition();
            int LBTicks = leftBack.getCurrentPosition();
            int RFTicks = rightFront.getCurrentPosition();
            int RBTicks = rightBack.getCurrentPosition();

            telemetry.addData("LF Ticks:", LFTicks);
            telemetry.addData("LB Ticks:", LBTicks);
            telemetry.addData("RF Ticks:", RFTicks);
            telemetry.addData("RB Ticks:", RBTicks);

            if(gamepad1.a && !isHeld) {
                leftFront.setTargetPosition(LFTicks - 50);
                leftBack.setTargetPosition(LBTicks - 50);
                rightFront.setTargetPosition(RFTicks - 50);
                rightBack.setTargetPosition(RBTicks - 50);

                leftFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                leftBack.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                rightFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                rightBack.setMode(DcMotor.RunMode.RUN_TO_POSITION);

                leftFront.setPower(-0.5);
                leftBack.setPower(-0.5);
                rightFront.setPower(-0.5);
                rightBack.setPower(-0.5);

                isHeld = true;
            }

            if(gamepad1.b && !isHeld) {
                leftFront.setTargetPosition(LFTicks - 50);
                leftBack.setTargetPosition(LBTicks + 50);
                rightFront.setTargetPosition(RFTicks + 50);
                rightBack.setTargetPosition(RBTicks - 50);

                leftFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                leftBack.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                rightFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                rightBack.setMode(DcMotor.RunMode.RUN_TO_POSITION);

                leftFront.setPower(-0.5);
                leftBack.setPower(0.5);
                rightFront.setPower(0.5);
                rightBack.setPower(-0.5);

                isHeld = true;
            }

            if(gamepad1.x && !isHeld) {
                leftFront.setTargetPosition(LFTicks - 100);
                leftBack.setTargetPosition(LBTicks - 100);
                rightFront.setTargetPosition(RFTicks + 100);
                rightBack.setTargetPosition(RBTicks + 100);

                leftFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                leftBack.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                rightFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                rightBack.setMode(DcMotor.RunMode.RUN_TO_POSITION);

                leftFront.setPower(-0.5);
                leftBack.setPower(-0.5);
                rightFront.setPower(0.5);
                rightBack.setPower(0.5);

                isHeld = true;
            }

            if(gamepad1.y && !isHeld) {
                leftFront.setTargetPosition(LFTicks + 50);
                leftBack.setTargetPosition(LBTicks + 50);
                rightFront.setTargetPosition(RFTicks + 50);
                rightBack.setTargetPosition(RBTicks + 50);

                leftFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                leftBack.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                rightFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                rightBack.setMode(DcMotor.RunMode.RUN_TO_POSITION);

                leftFront.setPower(0.5);
                leftBack.setPower(0.5);
                rightFront.setPower(0.5);
                rightBack.setPower(0.5);

                isHeld = true;
            }

            else if(!gamepad1.a && !gamepad1.b && !gamepad1.y && !gamepad1.x) isHeld = false;
            telemetry.update();
        }
    }
}
