package org.firstinspires.ftc.teamcode.Colector;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;


import org.firstinspires.ftc.teamcode.Config.Config;

@TeleOp(name = "Glisiere + Coduri Combinate", group = "Testing")
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
    private int state = 0;
    boolean isHeld = false;
    private String motorData = "Idle";
    private boolean faceIsHeld = false, faceChanged, isCupaHeld = false;
    private String facingData = "Forwards";
    DcMotor motorCarusel;
    private ElapsedTime timp = new ElapsedTime();
    private int isHeldservo, stateservo = 0;


    private boolean isPressed = false;

    @Override
    public void runOpMode() throws InterruptedException {

        motorCarusel = hardwareMap.dcMotor.get(Config.rate);
        com.qualcomm.robotcore.hardware.Servo servoGlisiera = hardwareMap.servo.get(Config.cupa);
        DcMotor motorTest = hardwareMap.dcMotor.get(Config.gli);
        DcMotor motorMatura = hardwareMap.dcMotor.get(Config.matura);
        DcMotor frontLeftMotor = hardwareMap.dcMotor.get(Config.left_front);
        DcMotor backLeftMotor = hardwareMap.dcMotor.get(Config.left_back);
        DcMotor frontRightMotor = hardwareMap.dcMotor.get(Config.right_front);
        DcMotor backRightMotor = hardwareMap.dcMotor.get(Config.right_back);
        int stateServo = 0;
        boolean isRuletaHeld = false;

        CRServo servoRuleta1;
        CRServo servoRuleta2;
        CRServo servoRuleta3;

        servoRuleta1 = hardwareMap.crservo.get(Config.rul1);
        servoRuleta2 = hardwareMap.crservo.get(Config.rul2);
        servoRuleta3 = hardwareMap.crservo.get(Config.rul3);

        servoRuleta1.resetDeviceConfigurationForOpMode();

        frontRightMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        backRightMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        motorTest.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motorCarusel.resetDeviceConfigurationForOpMode();

        waitForStart();
        timp.reset();

        while (opModeIsActive()) {

            int currentTicks = motorTest.getCurrentPosition();

            double acceleration = gamepad1.left_stick_y;
            double strafe = gamepad1.left_stick_x * -1.1;
            double rotation = gamepad1.right_stick_x;

            double powerLimit = 1.0;
            if (gamepad1.right_trigger >= 0.3) {
                powerLimit = 0.3;
            } else if (gamepad1.left_trigger >= 0.3) {
                powerLimit = 0.5;
            } else if (gamepad1.right_trigger < 0.3 && gamepad2.right_trigger < 0.3) {
                powerLimit = 1.0;
            }


            if (gamepad2.left_stick_y == 1 && !isRuletaHeld) {
                isRuletaHeld = true;
                if (stateServo == -1) {
                    stateServo = 0;
                    servoRuleta1.setPower(0);
                } else {
                    stateServo = -1;
                    servoRuleta1.setPower(-1);
                }
            } else if (gamepad2.left_stick_y == -1 && !isRuletaHeld) {
                isRuletaHeld = true;
                if (stateServo == 1) {
                    stateServo = 0;
                    servoRuleta1.setPower(0.0);
                } else {
                    stateServo = 1;
                    servoRuleta1.setPower(1.0);
                }
            } else if (gamepad2.left_stick_x == 1 && !isRuletaHeld) {
                isRuletaHeld = true;
                if (stateServo == -1) {
                    stateServo = 0;
                    servoRuleta2.setPower(0);
                } else {
                    stateServo = -1;
                    servoRuleta2.setPower(-1);
                }
            } else if (gamepad2.left_stick_x == -1 && !isRuletaHeld) {
                isRuletaHeld = true;
                if (stateServo == 1) {
                    stateServo = 0;
                    servoRuleta2.setPower(0.0);
                } else {
                    stateServo = 1;
                    servoRuleta2.setPower(1.0);
                }
            } else if (gamepad2.right_stick_y == 1 && !isRuletaHeld) {
                isRuletaHeld = true;
                if (stateServo == -1) {
                    stateServo = 0;
                    servoRuleta3.setPower(0);
                } else {
                    stateServo = -1;
                    servoRuleta3.setPower(-1);
                }
            } else if (gamepad2.right_stick_y == -1 && !isRuletaHeld) {
                isRuletaHeld = true;
                if (stateServo == 1) {
                    stateServo = 0;
                    servoRuleta3.setPower(0.0);
                } else {
                    stateServo = 1;
                    servoRuleta3.setPower(1.0);
                }
            } else if (gamepad2.left_stick_y == 0 && gamepad2.left_stick_x == 0 && gamepad2.right_stick_y == 0)
                isRuletaHeld = false;

            if (gamepad1.y && !faceIsHeld) {
                faceIsHeld = true;
                faceChanged = !faceChanged;
            } else if (!gamepad1.y) faceIsHeld = false;


            if (faceChanged == false) {

                frontRightMotor.setDirection(DcMotorSimple.Direction.REVERSE);
                backRightMotor.setDirection(DcMotorSimple.Direction.REVERSE);
                frontLeftMotor.setDirection(DcMotorSimple.Direction.FORWARD);
                backLeftMotor.setDirection(DcMotorSimple.Direction.FORWARD);

                rotation = rotation * (-1);
                facingData = "Forwards";
            } else {
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


            if (gamepad2.b && !isHeld) {
                isHeld = true;
                if (state == -1) {
                    state = 0;
                    motorData = "Idle";
                    motorMatura.setPower(0.0);
                } else {
                    state = -1;
                    motorData = "Ejecting";
                    motorMatura.setPower(-1.0);
                }
            } else if (gamepad2.a && !isHeld) {
                isHeld = true;
                if (state == 1) {
                    state = 0;
                    motorData = "Idle";
                    motorMatura.setPower(0.0);
                } else {
                    state = 1;
                    motorData = "Collecting";
                    motorMatura.setPower(1.0);
                }
            } else if (!gamepad2.a && !gamepad2.b) isHeld = false;

            if (gamepad2.x && !isPressed) {
                motorTest.setTargetPosition(0);
                motorTest.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                motorTest.setPower(0.8);
                isPressed = true;
            }
            if (gamepad2.right_trigger > 0.3 && !isPressed) {
                motorTest.setTargetPosition(-500);
                motorTest.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                motorTest.setPower(1);
                isPressed = true;
            } else if (gamepad2.left_bumper && !isPressed) {
                motorTest.setTargetPosition(-1100);
                motorTest.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                motorTest.setPower(1);
                isPressed = true;
            } else if (gamepad2.right_bumper && !isPressed) {
                motorTest.setTargetPosition(-1700);
                motorTest.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                motorTest.setPower(1);
                isPressed = true;
            }  else if (gamepad2.left_trigger < 0.3 && gamepad2.right_trigger < 0.3 && !gamepad2.left_bumper && !gamepad2.right_bumper && !gamepad2.x) isPressed = false;

            if (gamepad2.y && !isCupaHeld) {
                if (servoPosition == 0.04) servoPosition = 0.70;
                else servoPosition = 0.04;
                servoGlisiera.setPosition(servoPosition);
                isCupaHeld = true;
            } else if (!gamepad2.y) isCupaHeld = false;
            if (gamepad1.x && isHeldservo == 0) {
                isHeldservo = 1;
                if (stateservo == -1) {
                    stateservo = 0;
                    motorCarusel.setPower(0);
                } else {
                    stateservo = -1;
                    motorCarusel.setPower(-1);
                }
            } else if (gamepad1.b && isHeldservo == 0) {
                isHeldservo = 1;
                if (stateservo == 1) {
                    stateservo = 0;
                    motorCarusel.setPower(0.0);
                } else {
                    stateservo = 1;
                    motorCarusel.setPower(1.0);
                }
            } else if (!gamepad1.x && !gamepad1.b) isHeldservo = 0;

            idle();
            telemetry.addData("Current Ticks:", currentTicks);
            telemetry.addData("ServoPosition:", servoPosition);
            telemetry.addData("Maturica Status:", motorData);
            telemetry.addData("Power Limit:", powerLimit);
            telemetry.addData("Facing:", facingData);
            telemetry.addData("Elapsed time:",timp.toString());
            telemetry.update();
        }
        motorTest.setPower(0.0);
    }
}

