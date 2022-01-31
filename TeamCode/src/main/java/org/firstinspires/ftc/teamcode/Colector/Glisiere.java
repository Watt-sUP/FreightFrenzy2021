package org.firstinspires.ftc.teamcode.Colector;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;


import org.firstinspires.ftc.teamcode.hardware.Config;
import org.firstinspires.ftc.teamcode.hardware.Cupa;
import org.firstinspires.ftc.teamcode.hardware.Maturica;
import org.firstinspires.ftc.teamcode.hardware.Rata;

@TeleOp(name = "Glisiere + Coduri Combinate", group = "Testing")
public class Glisiere extends LinearOpMode {

    /*
        CR bogdan: acest opmode este prea lung si ar trebui sa facem obiecte pentru fiecare
                   sistem (in loc de opmode uri). de exemplu: pentru rata, putem avea clasa
                   Rata.java (care nu este un opmode, este un tip de obiect, echivalent cu
                   struct in c++).

        CR Cosmin: un checklist pentru clasele separate:
                   [-] Glisiere
                   [X] Maturica
                   [X] Cupa
                   [X] Rata
           CR Edy: [x] Ruleta
                   [-] Drive
     */


    //Declaratii
    //CR-someday Cosmin: optimizare declarari, sunt prea multe intr-un loc si ar putea fi separate
    double cupaPosition = 0.04;
    private int stateMaturica = 0;
    private double pow = 0.1;
    boolean isHeldMaturica = false;
    private boolean faceIsHeld = false, faceChanged, isCupaHeld = false, isHeldGlisiere = false;
    private String facingData = "Forwards";
    Rata rata = new Rata(hardwareMap, telemetry);
    Maturica maturica = new Maturica(hardwareMap, telemetry);
    Cupa cupa = new Cupa(hardwareMap, telemetry);
    private ElapsedTime timp = new ElapsedTime();
    private int isHeldRata, stateRata = 0;

    @Override
    public void runOpMode() throws InterruptedException {

        DcMotor motorGlisiera = hardwareMap.dcMotor.get(Config.glisiera);
        DcMotor frontLeftMotor = hardwareMap.dcMotor.get(Config.left_front);
        DcMotor backLeftMotor = hardwareMap.dcMotor.get(Config.left_back);
        DcMotor frontRightMotor = hardwareMap.dcMotor.get(Config.right_front);
        DcMotor backRightMotor = hardwareMap.dcMotor.get(Config.right_back);

        CRServo servoRuletaX;
        CRServo servoRuletaY;
        CRServo servoRuletaEx;

        servoRuletaX = hardwareMap.crservo.get(Config.rul_orizontal);
        servoRuletaY = hardwareMap.crservo.get(Config.rul_vertical);
        servoRuletaEx = hardwareMap.crservo.get(Config.rul_fata);

        servoRuletaX.resetDeviceConfigurationForOpMode();
        servoRuletaY.resetDeviceConfigurationForOpMode();
        servoRuletaEx.resetDeviceConfigurationForOpMode();

        frontRightMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        backRightMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        motorGlisiera.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        waitForStart();
        timp.reset();

        while (opModeIsActive()) {

            int currentTicks = motorGlisiera.getCurrentPosition();


            //Drive
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

            //Ruleta
            if(gamepad2.left_stick_x < -0.05 || gamepad2.left_stick_x > 0.05) {
                servoRuletaX.setPower(-gamepad2.left_stick_x / 2);
            }
            else if(gamepad2.left_stick_x < 0.05 && gamepad2.left_stick_x > -0.05)
                servoRuletaX.setPower(0.0);

            if(gamepad2.left_stick_y < -0.05 || gamepad2.left_stick_y > 0.05) {
                servoRuletaY.setPower(-gamepad2.left_stick_y);
            }
            else if(gamepad2.left_stick_y < 0.05 && gamepad2.left_stick_y > -0.05)
                servoRuletaY.setPower(0.0);

            if(gamepad2.right_stick_y < -0.05 || gamepad2.right_stick_y > 0.05) {
                servoRuletaEx.setPower(gamepad2.right_stick_y / 2);
            }
            else if(gamepad2.right_stick_y < 0.05 && gamepad2.right_stick_y > -0.05)
                servoRuletaEx.setPower(0.0);

            //Maturica
            if (gamepad2.b && !isHeldMaturica) {
                isHeldMaturica = true;
                if (stateMaturica == -1) {
                    stateMaturica = 0;
                    maturica.setMotorPower(0.0);
                } else {
                    stateMaturica = -1;
                    maturica.setMotorPower(-1.0);
                }
            } else if (gamepad2.a && !isHeldMaturica) {
                isHeldMaturica = true;
                if (stateMaturica == 1) {
                    stateMaturica = 0;
                    maturica.setMotorPower(0.0);
                } else {
                    stateMaturica = 1;
                    maturica.setMotorPower(1.0);
                }
            } else if (!gamepad2.a && !gamepad2.b) isHeldMaturica = false;

            //Glisiere
            if (gamepad2.x && !isHeldGlisiere) {
                motorGlisiera.setTargetPosition(0);
                motorGlisiera.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                motorGlisiera.setPower(0.8);
                isHeldGlisiere = true;
            }
            if (gamepad2.right_trigger > 0.3 && !isHeldGlisiere) {
                motorGlisiera.setTargetPosition(-500);
                motorGlisiera.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                motorGlisiera.setPower(1);
                isHeldGlisiere = true;
            } else if (gamepad2.left_trigger > 0.3 && !isHeldGlisiere) {
                motorGlisiera.setTargetPosition(-1100);
                motorGlisiera.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                motorGlisiera.setPower(1);
                isHeldGlisiere = true;
            } else if (gamepad2.right_bumper && !isHeldGlisiere) {
                motorGlisiera.setTargetPosition(-1700);
                motorGlisiera.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                motorGlisiera.setPower(1);
                isHeldGlisiere = true;
            } else if (gamepad2.left_bumper && !isHeldGlisiere) {
                motorGlisiera.setTargetPosition(-1900);
                motorGlisiera.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                motorGlisiera.setPower(1);
                isHeldGlisiere = true;
            } else if (gamepad2.left_trigger < 0.3 && gamepad2.right_trigger < 0.3 && !gamepad2.left_bumper && !gamepad2.right_bumper && !gamepad2.x) isHeldGlisiere = false;

            //Cupa
            if (gamepad2.y && !isCupaHeld) {
                if (cupaPosition == 0.05) cupaPosition = 0.70;
                else cupaPosition = 0.05;
                cupa.setServoPosition(cupaPosition);
                isCupaHeld = true;
            } else if (!gamepad2.y) isCupaHeld = false;

            //Rata
            if (gamepad1.b && isHeldRata == 0) {
                isHeldRata = 1;
                if (stateRata == -1) {
                    stateRata = 0;
                    pow = 0.1;
                    rata.rotate(0.0);
                } else {
                    stateRata = -1;
                    rata.rotate(pow * -1.0);
                }
            } else if (gamepad1.x && isHeldRata == 0) {
                isHeldRata = 1;
                if (stateRata == 1) {
                    stateRata = 0;
                    pow = 0.1;
                    rata.rotate(0.0);
                } else {
                    stateRata = 1;
                    rata.rotate(pow);
                }
            } else if (!gamepad1.x && !gamepad1.b) isHeldRata = 0;

            if(rata.motor.isBusy()) {
                if(rata.motor.getCurrentPosition() % 10 == 0) {
                    pow += 0.05;
                    rata.rotate(pow);
                }
            }

            idle();

            //Telemetry
            /* Si aici liniile ar putea fi separate si puse in sectiunea mecanismului
             care apartin, lasand doar telemetry.update la final */
            telemetry.addData("Glisiera Ticks:", currentTicks);
            telemetry.addData("Cupa Position:", cupa.servo.getPosition());
            telemetry.addData("Maturica Status:", maturica.maturaData);
            telemetry.addData("Power Limit:", powerLimit);
            telemetry.addData("Facing:", facingData);
            telemetry.addData("Elapsed time:",timp.toString());
            telemetry.update();
        }
        motorGlisiera.setPower(0.0);
    }
}

