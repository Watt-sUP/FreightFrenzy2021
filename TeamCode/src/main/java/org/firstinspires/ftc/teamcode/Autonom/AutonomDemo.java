package org.firstinspires.ftc.teamcode.Autonom;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Config.Config;

//@Disabled-
@Autonomous(name = "Autonom Demo", group = "Autonom")
public class AutonomDemo extends LinearOpMode {

    private ElapsedTime runtime = new ElapsedTime();

    @Override
    public void runOpMode() {

        DcMotor leftFront = hardwareMap.dcMotor.get(Config.left_front);
        DcMotor leftBack = hardwareMap.dcMotor.get(Config.left_back);
        DcMotor rightFront = hardwareMap.dcMotor.get(Config.right_front);
        DcMotor rightBack = hardwareMap.dcMotor.get(Config.right_back);
        DcMotor motorRata = hardwareMap.dcMotor.get(Config.rate);

        rightBack.setDirection(DcMotorSimple.Direction.REVERSE);
        rightFront.setDirection(DcMotorSimple.Direction.REVERSE);

        waitForStart();

        leftFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        leftFront.setTargetPosition(-300);
        leftBack.setTargetPosition(-300);
        rightFront.setTargetPosition(-300);
        rightBack.setTargetPosition(-300);

        leftFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        leftBack.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightBack.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        /*
            CR bogdan: cand esti in RUN_TO_POSITION, puterea se poate da mereu pozitiva, ca
                       motorul se duce automat in directia care trebuie
         */
        leftFront.setPower(-1);
        leftBack.setPower(-1);
        rightFront.setPower(-1);
        rightBack.setPower(-1);

        while (opModeIsActive() && leftFront.isBusy()) {

            telemetry.addData("Left Front encoder position:", leftFront.getCurrentPosition());
            telemetry.addData("Left Back encoder position:", leftFront.getCurrentPosition());
            telemetry.addData("Right Front encoder position:", leftFront.getCurrentPosition());
            telemetry.addData("Right Back encoder position:", leftFront.getCurrentPosition());
            telemetry.addData("Elapsed time:",runtime.toString());

            telemetry.update();
            idle();
        }


        leftFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        leftFront.setTargetPosition(970);
        leftBack.setTargetPosition(-970);
        rightFront.setTargetPosition(-970);
        rightBack.setTargetPosition(970);

        leftFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        leftBack.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightBack.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        leftFront.setPower(0.3);
        leftBack.setPower(-0.3);
        rightFront.setPower(-0.3);
        rightBack.setPower(0.3);

        while (opModeIsActive() && leftFront.isBusy()) {
            telemetry.addData("Left Front encoder position:", leftFront.getCurrentPosition());
            telemetry.addData("Left Back encoder position:", leftFront.getCurrentPosition());
            telemetry.addData("Right Front encoder position:", leftFront.getCurrentPosition());
            telemetry.addData("Right Back encoder position:", leftFront.getCurrentPosition());
            telemetry.update();
            idle();
        }

        leftFront.setPower(0);
        leftBack.setPower(0);
        rightFront.setPower(0);
        rightBack.setPower(0);

        motorRata.setPower(-1.0);
        sleep(4000);
        motorRata.setPower(0);

        leftFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        leftFront.setTargetPosition(-1200);
        leftBack.setTargetPosition(-1200);


        leftFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        leftBack.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightBack.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        leftFront.setPower(1);
        leftBack.setPower(1);


        while (opModeIsActive() && leftFront.isBusy()) {
            telemetry.addData("Left Front encoder position:", leftFront.getCurrentPosition());
            telemetry.addData("Left Back encoder position:", leftFront.getCurrentPosition());
            telemetry.addData("Right Front encoder position:", leftFront.getCurrentPosition());
            telemetry.addData("Right Back encoder position:", leftFront.getCurrentPosition());
            telemetry.update();
            idle();
        }
        leftFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        leftFront.setTargetPosition(-1700);
        leftBack.setTargetPosition(1700);
        rightFront.setTargetPosition(1700);
        rightBack.setTargetPosition(-1700);

        leftFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        leftBack.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightBack.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        leftFront.setPower(0.8);
        leftBack.setPower(0.8);
        rightFront.setPower(0.8);
        rightBack.setPower(0.8);

        while (opModeIsActive() && leftFront.isBusy()) {
            telemetry.addData("Left Front encoder position:", leftFront.getCurrentPosition());
            telemetry.addData("Left Back encoder position:", leftFront.getCurrentPosition());
            telemetry.addData("Right Front encoder position:", leftFront.getCurrentPosition());
            telemetry.addData("Right Back encoder position:", leftFront.getCurrentPosition());
            telemetry.addData("Elapsed time:",runtime.toString());

            telemetry.update();
            idle();
        }

        leftFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        leftFront.setTargetPosition(-3425);
        leftBack.setTargetPosition(-3425);
        rightFront.setTargetPosition(-3425);
        rightBack.setTargetPosition(-3425);

        leftFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        leftBack.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightBack.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        leftFront.setPower(1);
        leftBack.setPower(1);
        rightFront.setPower(0.9);
        rightBack.setPower(1);

        while (opModeIsActive() && leftFront.isBusy()) {
            telemetry.addData("Left Front encoder position:", leftFront.getCurrentPosition());
            telemetry.addData("Left Back encoder position:", leftFront.getCurrentPosition());
            telemetry.addData("Right Front encoder position:", leftFront.getCurrentPosition());
            telemetry.addData("Right Back encoder position:", leftFront.getCurrentPosition());
            telemetry.addData("Elapsed time:",runtime.toString());

            telemetry.update();
            idle();
        }
    }
}
