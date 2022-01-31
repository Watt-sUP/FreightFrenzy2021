package org.firstinspires.ftc.teamcode.Autonom;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.hardware.Config;
import org.firstinspires.ftc.teamcode.hardware.Runner;

@Autonomous(name = "Autonom partea de sus", group = "Autonom")
public class AutonomSus extends LinearOpMode {

    private DcMotor motorGlisiere, motorMatura;
    private Servo servoCupa;
    private DcMotor rate;
    private Runner runner;
    private final int[] positions = new int[]{0, -500, -1100, -1700, -1900};

    public void glisiera(int position) {
        motorGlisiere.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motorGlisiere.setTargetPosition(positions[position]);
        motorGlisiere.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        motorGlisiere.setPower(1);
        sleep(200);
        servoCupa.setPosition(0.70);
        sleep(2000);
        servoCupa.setPosition(0.04);
        motorGlisiere.setPower(0);
        sleep(1000);
        motorGlisiere.setTargetPosition(positions[0]);
        motorGlisiere.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        motorGlisiere.setPower(0.8);
    }

    @Override
    public void runOpMode() throws InterruptedException {

        runner = new Runner(hardwareMap);
        motorGlisiere = hardwareMap.dcMotor.get(Config.glisiera);
        servoCupa = hardwareMap.servo.get(Config.cupa);
        motorMatura = hardwareMap.dcMotor.get(Config.matura);
        rate = hardwareMap.dcMotor.get(Config.rate);

        waitForStart();

        runner.moveForwardBackward(450, Runner.AutonomousMoveType.FORWARD);
        glisiera(3);
        runner.moveForwardBackward(450, Runner.AutonomousMoveType.FORWARD);
        runner.rotate(90);
        runner.moveLeftRight(300, Runner.AutonomousMoveType.LEFT);
        motorMatura.setPower(1.0);
        runner.moveForwardBackward(1300, Runner.AutonomousMoveType.BACKWARD);
        sleep(1000);
        runner.walkSlow(300);
        motorMatura.setPower(-1.0);
        sleep(1000);
        runner.moveForwardBackward(1150, Runner.AutonomousMoveType.FORWARD);
        runner.moveLeftRight(300, Runner.AutonomousMoveType.RIGHT);
        runner.rotate(-90);
        runner.moveForwardBackward(320, Runner.AutonomousMoveType.FORWARD);
        glisiera(3);
        runner.moveForwardBackward(300, Runner.AutonomousMoveType.BACKWARD);
        runner.moveLeftRight(800, Runner.AutonomousMoveType.LEFT);
        runner.rotate(180);
        runner.moveLeftRight(800, Runner.AutonomousMoveType.RIGHT);
        runner.moveLeftRight(200, Runner.AutonomousMoveType.LEFT);
        runner.walkSlow(200);
        rate.setPower(0.65);
        sleep(3500);
        runner.moveLeftRight(300, Runner.AutonomousMoveType.LEFT);
        runner.rotate(-90);
        runner.moveLeftRight(300, Runner.AutonomousMoveType.LEFT);
        runner.walk(-2260);
        while (opModeIsActive() && motorGlisiere.isBusy()) idle();


    }
}