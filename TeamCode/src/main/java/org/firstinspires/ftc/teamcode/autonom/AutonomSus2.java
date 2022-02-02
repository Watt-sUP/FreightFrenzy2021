package org.firstinspires.ftc.teamcode.autonom;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.teamcode.hardware.Config;
import org.firstinspires.ftc.teamcode.hardware.Cupa;
import org.firstinspires.ftc.teamcode.hardware.Glisiere;
import org.firstinspires.ftc.teamcode.hardware.Runner;

@Autonomous(name = "Autonom partea de sus Experimental", group = "Autonom")
public class AutonomSus2 extends LinearOpMode {

    private DcMotorEx leftFront, leftBack, rightFront, rightBack, motorMatura;
    private DcMotor rate;
    private double MOTOR_TICK_COUNT = 1500;
    private double circumference = Math.PI * 101.6;

    public void walk(int distance) {
        leftFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        int target =(int) (distance * MOTOR_TICK_COUNT / (circumference * 3));
        double rate = 1 / target;
        leftFront.setTargetPosition(target);
        leftBack.setTargetPosition(target);
        rightFront.setTargetPosition(target);
        rightBack.setTargetPosition(target);
        leftFront.setPower(0.35);
        leftBack.setPower(0.35);
        rightFront.setPower(0.35);
        rightBack.setPower(0.35);
        leftBack.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        leftFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightBack.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        while(leftFront.isBusy()&&rightFront.isBusy()){
            telemetry.addData("Status", "Walking using encoders");
            telemetry.update();
            if(leftFront.getCurrentPosition() % 50 == 0 || leftFront.getCurrentPosition() % -50 == 0) {
                leftFront.setPower(Math.abs(leftFront.getPower()) + 0.25);
                leftBack.setPower(Math.abs(leftBack.getPower()) + 0.25);
                rightFront.setPower(Math.abs(rightFront.getPower()) + 0.25);
                rightBack.setPower(Math.abs(rightBack.getPower()) + 0.25);
            }
            if(Math.abs(leftFront.getCurrentPosition()) % - Math.abs(target) <= 20) {
                leftFront.setPower(0.4);
                leftBack.setPower(0.4);
                rightFront.setPower(0.4);
                rightBack.setPower(0.4);
            }
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
        int target =(int) (distance * MOTOR_TICK_COUNT / (circumference * 3));
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
        int target =(int) (distance * MOTOR_TICK_COUNT / (circumference * 3));
        leftFront.setTargetPosition(target);
        leftBack.setTargetPosition(-target);
        rightFront.setTargetPosition(-target);
        rightBack.setTargetPosition(target);
        leftBack.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        leftFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightBack.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        leftFront.setPower(0.5);
        leftBack.setPower(0.5);
        rightFront.setPower(0.5);
        rightBack.setPower(0.5);
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

        Glisiere glisiere = new Glisiere(hardwareMap, telemetry);
        Cupa cupa = new Cupa(hardwareMap, telemetry);

        leftFront = hardwareMap.get(DcMotorEx.class, Config.left_front);
        leftBack = hardwareMap.get(DcMotorEx.class, Config.left_back);
        rightFront = hardwareMap.get(DcMotorEx.class, Config.right_front);
        rightBack = hardwareMap.get(DcMotorEx.class, Config.right_back);
        motorMatura = hardwareMap.get(DcMotorEx.class, Config.matura);
        rate = hardwareMap.dcMotor.get(Config.rate);
        rightFront.setDirection(DcMotorSimple.Direction.REVERSE);
        rightBack.setDirection(DcMotorSimple.Direction.REVERSE);

        telemetry.addData("Ticks pe Rev:", leftFront.getMotorType().getTicksPerRev());
        telemetry.update();

        waitForStart();

        //runForTicks(0, 0, 500, 500);
        walk(450);

        glisiere.setToPosition(4);

        sleep(200);
        cupa.toggleCupa();
        sleep(2000);
        cupa.toggleCupa();

        glisiere.motor.setPower(0);

        sleep(1000);
        glisiere.setToPosition(0);

        walk(-450);
        turn(945);
        strafe(-300);
        motorMatura.setPower(-1.0);
        walk(-1370);
        sleep(1000);
        walk(1450);
        strafe(300);
        turn(-945);
        walk(320);
        glisiere.setToPosition(3);

        sleep(200);
        cupa.toggleCupa();
        sleep(2000);
        cupa.toggleCupa();

        glisiere.motor.setPower(0);

        sleep(1000);

        glisiere.setToPosition(0);

        walk(-300);
        strafe(-800);
        turn(1890);
        strafe(800);
        strafe(-200);
        walkSlow(200);

        rate.setPower(0.55);

        sleep(3500);
        strafe(-300);
        turn(-945);
        strafe(-300);
        walk(-2260);
        Runner runner = new Runner(hardwareMap);
        runner.moveLeftRight(20, Runner.AutonomousMoveType.LEFT);
    }
}