package org.firstinspires.ftc.teamcode.Autonom;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.Config.Config;
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

        runner = new Runner(
                hardwareMap.dcMotor.get(Config.left_front),
                hardwareMap.dcMotor.get(Config.left_back),
                hardwareMap.dcMotor.get(Config.right_front),
                hardwareMap.dcMotor.get(Config.right_back)
        );
        motorGlisiere = hardwareMap.dcMotor.get(Config.glisiera);
        servoCupa = hardwareMap.servo.get(Config.cupa);
        motorMatura = hardwareMap.dcMotor.get(Config.matura);
        rate = hardwareMap.dcMotor.get(Config.rate);

        waitForStart();

        //runForTicks(0, 0, 500, 500);
        runner.walk(450);
        glisiera(3);
        runner.walk(-450);
        runner.turn(945);
        runner.strafe(-300);
        motorMatura.setPower(1.0);
        runner.walk(-1300);
        sleep(1000);
        runner.walkSlow(300);
        motorMatura.setPower(-1.0);
        sleep(1000);
        runner.walk(1150);
        runner.strafe(300);
        runner.turn(-945);
        runner.walk(320);
        glisiera(3);
        runner.walk(-300);
        runner.strafe(-800);
        runner.turn(1890);
        runner.strafe(800);
        runner.strafe(-200);
        runner.walkSlow(200);
        rate.setPower(0.65);
        sleep(3500);
        runner.strafe(-300);
        runner.turn(-945);
        runner.strafe(-300);
        runner.walk(-2260);
        while (opModeIsActive() && motorGlisiere.isBusy()) idle();


    }
}