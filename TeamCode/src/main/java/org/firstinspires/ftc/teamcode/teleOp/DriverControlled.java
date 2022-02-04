package org.firstinspires.ftc.teamcode.teleOp;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;


import org.firstinspires.ftc.teamcode.hardware.Config;
import org.firstinspires.ftc.teamcode.hardware.Cupa;
import org.firstinspires.ftc.teamcode.hardware.Glisiere;
import org.firstinspires.ftc.teamcode.hardware.Maturica;
import org.firstinspires.ftc.teamcode.hardware.Rata;
import org.firstinspires.ftc.teamcode.hardware.Ruleta;

@TeleOp(name = "OpMode Principal", group = "Testing")
public class DriverControlled extends LinearOpMode {

    /*
        CR bogdan: acest opmode este prea lung si ar trebui sa facem obiecte pentru fiecare
                   sistem (in loc de opmode uri). de exemplu: pentru rata, putem avea clasa
                   Rata.java (care nu este un opmode, este un tip de obiect, echivalent cu
                   struct in c++).

        CR Cosmin: un checklist pentru clasele separate:
                   [X] Glisiere
                   [X] Maturica
                   [X] Cupa
                   [X] Rata
           CR Edy: [X] Ruleta
                   [-] Drive
     */


    //Declaratii
    //CR-someday Cosmin: optimizare declarari, sunt prea multe intr-un loc si ar putea fi separate
    private boolean faceIsHeld = false, faceChanged = false, isCupaHeld = false;
    private boolean isHeldGlisiere = false, isHeldMaturica = false, boolTimer = false, isTransit = false, isHeldRata = false;
    private String facingData = "Forwards";
    private final ElapsedTime timp = new ElapsedTime();
    private int powerRata = -1, stateRata = -1;
    private double duckMotorPower = 0.60;

    @Override
    public void runOpMode() throws InterruptedException {

        Rata rata = new Rata(hardwareMap, telemetry);
        Maturica maturica = new Maturica(hardwareMap, telemetry);
        Cupa cupa = new Cupa(hardwareMap, telemetry);
        Glisiere glisiere = new Glisiere(hardwareMap, telemetry);
        Ruleta ruleta = new Ruleta(hardwareMap, telemetry);
        ElapsedTime timerGli = new ElapsedTime();
        ElapsedTime timerCol = new ElapsedTime();

        DcMotor frontLeftMotor = hardwareMap.dcMotor.get(Config.left_front);
        DcMotor backLeftMotor = hardwareMap.dcMotor.get(Config.left_back);
        DcMotor frontRightMotor = hardwareMap.dcMotor.get(Config.right_front);
        DcMotor backRightMotor = hardwareMap.dcMotor.get(Config.right_back);


        frontRightMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        backRightMotor.setDirection(DcMotorSimple.Direction.REVERSE);

        waitForStart();
        timp.reset();
        timerGli.reset();
        timerCol.reset();
        while (opModeIsActive()) {



            //Drive
            double acceleration = gamepad1.left_stick_y;
            double strafe = gamepad1.left_stick_x * -1.1;
            double rotation = gamepad1.right_stick_x;


            double powerLimit = 1.0;
            if (gamepad1.right_trigger >= 0.3) {
                powerLimit = 0.3;
            } else if (gamepad1.left_trigger >= 0.3) {
                powerLimit = 0.5;
            } else if (gamepad1.right_trigger < 0.3 && gamepad1.left_trigger < 0.3) {
                powerLimit = 1.0;
            }
            telemetry.addData("Power Limit:", powerLimit);


            if (gamepad1.y && !faceIsHeld) {
                faceIsHeld = true;
                faceChanged = !faceChanged;
            } else if (!gamepad1.y) faceIsHeld = false;

            if (!faceChanged) {

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
            telemetry.addData("Facing:", facingData);

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
            ruleta.move(gamepad2.left_stick_y,gamepad2.left_stick_x,gamepad2.right_stick_y);



            //Maturica
            if (gamepad2.b && !isHeldMaturica) {
                isHeldMaturica = true;
                maturica.toggleEject();
            } else if (gamepad2.a && !isHeldMaturica) {
                isHeldMaturica = true;
                maturica.toggleCollect();
            } else if (!gamepad2.a && !gamepad2.b) isHeldMaturica = false;
            telemetry.addData("Maturica Status:", maturica.getMaturaData());



            //Glisiere
           if (gamepad2.x && !isHeldGlisiere) {
                glisiere.setToPosition(0);
                isHeldGlisiere = true;
            }
            if (gamepad2.right_trigger > 0.3 && !isHeldGlisiere) {
                glisiere.setToPosition(1);
                maturica.toggleEject();
                isHeldGlisiere = true;
            } else if (gamepad2.left_trigger > 0.3 && !isHeldGlisiere) {
                glisiere.setToPosition(2);
                maturica.toggleEject();
                isHeldGlisiere = true;
            } else if (gamepad2.right_bumper && !isHeldGlisiere) {
                glisiere.setToPosition(3);
                maturica.toggleEject();
                isHeldGlisiere = true;
            } else if (gamepad2.left_bumper && !isHeldGlisiere) {
                glisiere.setToPosition(4);
                maturica.toggleEject();
                isHeldGlisiere = true;
            } else if (gamepad2.left_trigger < 0.3 && gamepad2.right_trigger < 0.3 && !gamepad2.left_bumper && !gamepad2.right_bumper && !gamepad2.x) isHeldGlisiere = false;
            telemetry.addData("Glisiera Ticks:", glisiere.getTicks());

            //Cupa
            if (gamepad2.y && !isCupaHeld) {
                cupa.toggleCupa();
                timerGli.reset();
                isCupaHeld = true;
                boolTimer = true;
            }
            else if (!gamepad2.y) isCupaHeld = false;
            if(timerGli.milliseconds() >= 1000 &&  boolTimer) {
                cupa.toggleCupa();
                boolTimer = false;
                isTransit = true;
            }
            if(timerGli.milliseconds() >= 2022 && isTransit) {
                glisiere.setToPosition(0);
                maturica.toggleCollect();
                isTransit = false;
                isHeldGlisiere = true;
            }


            //Rata
            if (gamepad1.x && !isHeldRata) {
                powerRata *= -1;

                if(powerRata > 0){
                    rata.rotate(0.75);
                }
                else
                    rata.rotate(0.0);

//            if(rata.motor.isBusy()) {
//                if(rata.motor.getCurrentPosition() % 50 <= 10 && rata.motor.getPower() < 1.0) {
//                    rata.rotate(Math.abs(rata.motor.getPower() + 0.07) * -1.0);
//               }
            } else isHeldRata = false;

            if (gamepad2.x && !isHeldRata) {
                powerRata *= -1;
                isHeldRata = true;
                if(powerRata > 0){
                    rata.rotate(duckMotorPower);
                }
                else
                    rata.rotate(0.0);
            } else isHeldRata = false;
            if(rata.motor.isBusy()){
                if(duckMotorPower < 1) {
                    duckMotorPower += 0.05;
                    rata.rotate(duckMotorPower);
                }
            }

            idle();
            telemetry.addData("Elapsed time:",timp.toString());
            telemetry.addData("Putere rata:", rata.motor.getPower());
            telemetry.update();
        }
        glisiere.motor.setPower(0);
    }
}

