package org.firstinspires.ftc.teamcode.Autonom;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.Config.Config;

@Autonomous(name = "Autonom partea de sus", group = "Autonom")
public class AutonomSus extends LinearOpMode {

    private DcMotor leftFront, leftBack, rightFront, rightBack, motorGlisiere;
    private Servo servoCupa;

    public void runForTicks(int LF, int LB, int RF, int RB) {
        leftFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        if(LF != 0) leftFront.setTargetPosition(LF);
        if(LB != 0) leftBack.setTargetPosition(LB);
        if(RF != 0) rightFront.setTargetPosition(RF);
        if(RB != 0) rightBack.setTargetPosition(RB);

        if(LF != 0) leftFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        if(LB != 0) leftBack.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        if(RF != 0) rightFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        if(RB != 0) rightBack.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        if(LF != 0) leftFront.setPower(1);
        if(LB != 0) leftBack.setPower(1);
        if(RF != 0) rightFront.setPower(1);
        if(RB != 0) rightBack.setPower(1);

        while (opModeIsActive() && (leftFront.isBusy() || rightFront.isBusy())) {

            telemetry.addData("Left Front encoder position:", leftFront.getCurrentPosition());
            telemetry.addData("Left Back encoder position:", leftFront.getCurrentPosition());
            telemetry.addData("Right Front encoder position:", leftFront.getCurrentPosition());
            telemetry.addData("Right Back encoder position:", leftFront.getCurrentPosition());

            telemetry.update();
            idle();
        }
    }

    @Override
    public void runOpMode() throws InterruptedException {

        leftFront = hardwareMap.dcMotor.get(Config.left_front);
        leftBack = hardwareMap.dcMotor.get(Config.left_back);
        rightFront = hardwareMap.dcMotor.get(Config.right_front);
        rightBack = hardwareMap.dcMotor.get(Config.right_back);
        motorGlisiere = hardwareMap.dcMotor.get(Config.gli);
        servoCupa = hardwareMap.servo.get(Config.cupa);

        rightFront.setDirection(DcMotorSimple.Direction.REVERSE);
        rightBack.setDirection(DcMotorSimple.Direction.REVERSE);

        waitForStart();

        runForTicks(0, 0, 500, 500);
        runForTicks(700, 700, 700, 700);

        motorGlisiere.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motorGlisiere.setTargetPosition(-1700);
        motorGlisiere.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        motorGlisiere.setPower(1);

        /*
            CR bogdan: trebuie o clasa separata pentru cupa, cu functii pentru diferitele pozitii ale cupei,
                       astfel, nu trebuie sa ne gandim de fiecare data care sunt constantele,
                       plus ca se intelege ce face codul mai bine daca am avea cupa.jos(),
                       cupa.mid(), cupa.sus(), etc..
         */
        sleep(200);
        servoCupa.setPosition(0.70);
        sleep(2000);
        servoCupa.setPosition(0.04);

        motorGlisiere.setPower(0);

        sleep(1000);
        /*
            CR bogdan: target position trebuie sa fie 0, (nu -currentPosition).
         */
        motorGlisiere.setTargetPosition(motorGlisiere.getCurrentPosition() * (-1));
        motorGlisiere.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        motorGlisiere.setPower(0.8);

        while (opModeIsActive() && motorGlisiere.isBusy()) idle();
    }
}