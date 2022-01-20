package org.firstinspires.ftc.teamcode.Colector;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;


import org.firstinspires.ftc.teamcode.Config.Config;

@TeleOp(name="Glisiere + Coduri Combinate", group = "Testing")
public class Glisiere extends LinearOpMode {

    /*
        CR bogdan: pare ca asta e combinarea tuturor opmode-urilor, astfel ar fi bine sa aibe un
                   nume mai sugestiv.

                   acest opmode este prea lung si ar trebui sa facem obiecte pentru fiecare
                   sistem (in loc de opmode uri). de exemplu: pentru rata, putem avea clasa
                   Rata.java (care nu este un opmode, este un tip de obiect, echivalent cu
                   struct in c++).
     */

    Servo servoTest;
    double servoPosition = 0.04;
    private int isHeld, state = 0;
    private String motorData = "Idle";
    private boolean faceIsHeld = false, faceChanged;
    private String facingData = "Forwards";
    CRServo servoCarusel;
    private int isHeldservo, stateservo = 0;

    @Override
    public void runOpMode() throws InterruptedException {

        servoCarusel = hardwareMap.crservo.get(Config.rate);
        com.qualcomm.robotcore.hardware.Servo servoTest = hardwareMap.servo.get(Config.cupa);
        DcMotor motorTest = hardwareMap.dcMotor.get(Config.gli);
        DcMotor motorMatura = hardwareMap.dcMotor.get(Config.matura);
        DcMotor frontLeftMotor = hardwareMap.dcMotor.get(Config.left_front);
        DcMotor backLeftMotor = hardwareMap.dcMotor.get(Config.left_back);
        DcMotor frontRightMotor = hardwareMap.dcMotor.get(Config.right_front);
        DcMotor backRightMotor = hardwareMap.dcMotor.get(Config.right_back);

        frontRightMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        backRightMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        motorTest.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        servoCarusel.resetDeviceConfigurationForOpMode();

        waitForStart();

        while (opModeIsActive()) {

            int currentTicks = motorTest.getCurrentPosition();

            double acceleration = gamepad1.left_stick_y;
            double strafe = gamepad1.left_stick_x * -1.1;
            double rotation = gamepad1.right_stick_x;

            double powerLimit = 1.0;
            if(gamepad1.left_trigger >= 0.3 ) {
                powerLimit = 0.2;
            }
            else if(gamepad1.right_trigger >= 0.3 ) {
                powerLimit = 0.5;
            }
            else if(gamepad1.right_trigger < 0.3 && gamepad2.right_trigger < 0.3) {
                powerLimit = 1.0;
            }


            if(gamepad1.y && !faceIsHeld) {
                faceIsHeld = true;
                faceChanged = !faceChanged;
            }
            else faceIsHeld = false;


            if(faceChanged == false) {

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


            if (gamepad2.b  && isHeld == 0){
                isHeld = 1;
                if(state == -1) {state = 0; motorData="Idle"; motorMatura.setPower(0.0);}
                else {state = -1; motorData="Ejecting"; motorMatura.setPower(-1.0);}
            }
            else if(gamepad2.a && isHeld == 0) {
                isHeld = 1;
                if (state==1) {state=0; motorData = "Idle"; motorMatura.setPower(0.0);}
                else {state=1; motorData = "Collecting"; motorMatura.setPower(1.0);}
            }
            else isHeld = 0;

                  if (gamepad2.x) {
                    motorTest.setTargetPosition(0);
                    motorTest.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    motorTest.setPower(0.8);
                }

                if (gamepad2.right_trigger > 0.3) {
                    motorTest.setTargetPosition(-500);
                    motorTest.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    motorTest.setPower(1);
                }
                if (gamepad2.left_bumper) {
                    motorTest.setTargetPosition(-1100);
                    motorTest.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    motorTest.setPower(1);
                }
                if (gamepad2.right_bumper) {
                    motorTest.setTargetPosition(-1700);
                    motorTest.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    motorTest.setPower(1);
                }
                if (gamepad1.a) {
                    motorTest.setTargetPosition(currentTicks + 10);
                    motorTest.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    motorTest.setPower(1);
                }
                if (gamepad2.y) {
                if (servoPosition == 0.04) servoPosition = 0.70;
                else servoPosition = 0.04;
                servoTest.setPosition(servoPosition);
            }
            if (gamepad1.x && isHeldservo == 0) {
                isHeldservo = 1;
                if(stateservo == -1) {stateservo = 0;  servoCarusel.setPower(0);}
                else {stateservo = -1;  servoCarusel.setPower(-1);}
            }
            else if(gamepad1.b && isHeldservo == 0) {
                isHeldservo = 1;
                if (stateservo==1) {stateservo=0; servoCarusel.setPower(0.0);}
                else {stateservo=1; servoCarusel.setPower(1.0);}
            }
            else isHeldservo = 0;

                idle();
            telemetry.addData("Current Ticks:", currentTicks);
            telemetry.addData("ServoPosition:", servoPosition);
            telemetry.addData("Maturica Status:", motorData);
            telemetry.addData("Power Limit:", powerLimit);
            telemetry.addData("Faceing:", facingData);
            telemetry.update();
        }
        motorTest.setPower(0.0);
    }
}

