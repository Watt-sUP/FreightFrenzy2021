package org.firstinspires.ftc.teamcode.Autonom;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.hardware.Config;

@Autonomous(name = "Autonom partea de sus Stabil", group = "Autonom")
public class AutonomSus extends LinearOpMode {

    private DcMotor leftFront, leftBack, rightFront, rightBack, motorGlisiere, motorMatura;
    private Servo servoCupa;
    private DcMotor rate;
    private double MOTOR_TICK_COUNT = 537.7;
    private double circumference = Math.PI * 101.6;

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

    public void walk(int distance) {
        leftFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        int target =(int) (distance * MOTOR_TICK_COUNT / circumference);
        leftFront.setTargetPosition(target);
        leftBack.setTargetPosition(target);
        rightFront.setTargetPosition(target);
        rightBack.setTargetPosition(target);
        leftFront.setPower(1);
        leftBack.setPower(1);
        rightFront.setPower(1);
        rightBack.setPower(1);
        leftBack.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        leftFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightBack.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        while(leftFront.isBusy()&&rightFront.isBusy()){
            telemetry.addData("Status", "Walking using encoders");
            telemetry.update();
        }
        leftFront.setPower(0);
        leftBack.setPower(0);
        rightFront.setPower(0);
        rightBack.setPower(0);
    }

    public void walkSlow(int distance) {
        leftFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        int target =(int) (distance * MOTOR_TICK_COUNT / circumference);
        leftFront.setTargetPosition(target);
        leftBack.setTargetPosition(target);
        rightFront.setTargetPosition(target);
        rightBack.setTargetPosition(target);
        leftFront.setPower(0.25);
        leftBack.setPower(0.25);
        rightFront.setPower(0.25);
        rightBack.setPower(0.25);
        leftBack.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        leftFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightBack.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        while(leftFront.isBusy()&&rightFront.isBusy()){
            telemetry.addData("Status", "Walking using encoders");
            telemetry.update();
        }
        leftFront.setPower(0);
        leftBack.setPower(0);
        rightFront.setPower(0);
        rightBack.setPower(0);
    }

    public void turn(int angle){
        leftFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        int  target = (int) (angle * circumference / 360);
        leftFront.setTargetPosition(target);
        leftBack.setTargetPosition(target);
        rightFront.setTargetPosition(-target);
        rightBack.setTargetPosition(-target);
        leftFront.setPower(1);
        leftBack.setPower(1);
        rightFront.setPower(1);
        rightBack.setPower(1);
        leftBack.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        leftFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightBack.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        while(leftFront.isBusy()&&rightFront.isBusy()) {
            telemetry.addData("Status", "Turning to left");
            telemetry.update();
        }
        leftFront.setPower(0);
        leftBack.setPower(0);
        rightFront.setPower(0);
        rightBack.setPower(0);
    }

    public void strafe(int distance)
    {
        leftFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        int target =(int) (distance * MOTOR_TICK_COUNT / circumference);
        leftFront.setTargetPosition(target);
        leftBack.setTargetPosition(-target);
        rightFront.setTargetPosition(-target);
        rightBack.setTargetPosition(target);
        leftBack.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        leftFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightBack.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        leftFront.setPower(1);
        leftBack.setPower(1);
        rightFront.setPower(1);
        rightBack.setPower(1);
        while(leftFront.isBusy()&&rightFront.isBusy()){
            telemetry.addData("Status", "Walking using encoders");
            telemetry.update();
        }
        leftFront.setPower(0);
        leftBack.setPower(0);
        rightFront.setPower(0);
        rightBack.setPower(0);
    }

    @Override
    public void runOpMode() throws InterruptedException {

        leftFront = hardwareMap.dcMotor.get(Config.left_front);
        leftBack = hardwareMap.dcMotor.get(Config.left_back);
        rightFront = hardwareMap.dcMotor.get(Config.right_front);
        rightBack = hardwareMap.dcMotor.get(Config.right_back);
        motorGlisiere = hardwareMap.dcMotor.get(Config.glisiera);
        servoCupa = hardwareMap.servo.get(Config.cupa);
        motorMatura = hardwareMap.dcMotor.get(Config.matura);
        rate = hardwareMap.dcMotor.get(Config.rate);
        rightFront.setDirection(DcMotorSimple.Direction.REVERSE);
        rightBack.setDirection(DcMotorSimple.Direction.REVERSE);

        waitForStart();

        //runForTicks(0, 0, 500, 500);
        walk(450);

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
        motorGlisiere.setTargetPosition(0);
        motorGlisiere.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        motorGlisiere.setPower(0.8);

        walk(-450);
        turn(945);
        strafe(-300);
        motorMatura.setPower(1.0);
        walk(-1300);
        sleep(1000);
        walkSlow(300);
        motorMatura.setPower(-1.0);
        sleep(1000);
        walk(1150);
        strafe(300);
        turn(-945);
        walk(320);
        motorGlisiere.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motorGlisiere.setTargetPosition(-1700);
        motorGlisiere.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        motorGlisiere.setPower(1);

        sleep(200);
        servoCupa.setPosition(0.70);
        sleep(2000);
        servoCupa.setPosition(0.04);

        motorGlisiere.setPower(0);

        sleep(1000);

        motorGlisiere.setTargetPosition(0);
        motorGlisiere.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        motorGlisiere.setPower(0.8);
        walk(-300);
        strafe(-800);
        turn(1890);
        strafe(800);
        strafe(-200);
        walkSlow(200);
        rate.setPower(0.65);
        sleep(3500);
        strafe(-300);
        turn(-945);
        strafe(-300);
        walk(-2260);
        while (opModeIsActive() && motorGlisiere.isBusy()) idle();
    }
}