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
import org.firstinspires.ftc.teamcode.hardware.DeadWheels;
import org.firstinspires.ftc.teamcode.hardware.Glisiere;
import org.firstinspires.ftc.teamcode.hardware.Maturica;
import org.firstinspires.ftc.teamcode.hardware.Rata;
import org.firstinspires.ftc.teamcode.hardware.Ruleta;

@TeleOp(name = "OpMode", group = "Testing")
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
    private boolean faceIsHeld = false, faceChanged = false, isCupaHeld = false, isCupaProcessing = false, isDown = true;
    private boolean isHeldGlisiere = false, isHeldMaturica = false, boolTimer = false, isMagnet = false, isHeldDeget = false;
    private boolean isHeldRata = false, cupaMore = false, first = false, activat = false, dezactivat = false;
    private boolean faza_1 = false, faza_2 = false, faza_3 = false, faza_4 = false;
    private String facingData = "Forwards";
    private final ElapsedTime timp = new ElapsedTime();
    private final ElapsedTime timpCupa = new ElapsedTime();
    private int powerRata = -1, stateRata = -1;
    private double duckMotorPower = 0.60;
    private boolean isDpadUpPressed = false, isDpadDownPressed = false;

    @Override
    public void runOpMode() throws InterruptedException {

        Rata rata = new Rata(hardwareMap, telemetry);
        Maturica maturica = new Maturica(hardwareMap, telemetry);
        Cupa cupa = new Cupa(hardwareMap, telemetry);
        Glisiere glisiere = new Glisiere(hardwareMap, telemetry);
        Ruleta ruleta = new Ruleta(hardwareMap);
        DeadWheels wheels = new DeadWheels(hardwareMap);
        ElapsedTime timerGli = new ElapsedTime();
        ElapsedTime timerCol = new ElapsedTime();

        DcMotor frontLeftMotor = hardwareMap.dcMotor.get(Config.left_front);
        DcMotor backLeftMotor = hardwareMap.dcMotor.get(Config.left_back);
        DcMotor frontRightMotor = hardwareMap.dcMotor.get(Config.right_front);
        DcMotor backRightMotor = hardwareMap.dcMotor.get(Config.right_back);


        frontRightMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        backRightMotor.setDirection(DcMotorSimple.Direction.REVERSE);


        wheels.setUp();
        waitForStart();
        timp.reset();
        timpCupa.reset();
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

            if(gamepad2.dpad_up && !isDpadUpPressed){
                glisiere.modifyPosition(80);
                isDpadUpPressed = true;
            }else{
                isDpadUpPressed = false;
            }
            if(gamepad2.dpad_down && !isDpadDownPressed){
                glisiere.modifyPosition(-80 );
                isDpadDownPressed = true;
            }else {
                isDpadDownPressed = false;
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
            ruleta.move(gamepad2.left_stick_y,gamepad2.left_stick_x);

            if (gamepad2.right_stick_y > 0.03 && !isMagnet) {
                isMagnet = true;
                ruleta.toggleCupa();
            } else if(gamepad2.right_stick_y < 0.03) isMagnet = false;

            //Maturica
            if (gamepad2.a && !isHeldMaturica) {
                isHeldMaturica = true;
                maturica.toggleCollect();
            } else if (!gamepad2.a) isHeldMaturica = false;
            telemetry.addData("Maturica Status:", maturica.getMaturaData());

            if (gamepad2.b && !isHeldDeget) {
                isHeldDeget = true;
                cupa.toggleDeget();
            } else if (!gamepad2.b) isHeldDeget = false;

            //Glisiere
           if (gamepad2.x && !isHeldGlisiere) {
                cupaMore = false;
                first = false;
                glisiere.setToPosition(0);
                isDown = true;
                isHeldGlisiere = true;
            }
            if (gamepad2.right_trigger > 0.3 && !isHeldGlisiere) {
                cupaMore = false;
                first = true;
                glisiere.setToPosition(1);
                maturica.toggleEject();
                isDown = false;
                isHeldGlisiere = true;
            } else if (gamepad2.left_trigger > 0.3 && !isHeldGlisiere) {
                cupaMore = true;
                first = false;
                glisiere.setToPosition(2);
                maturica.toggleEject();
                isDown = false;
                isHeldGlisiere = true;
            } else if (gamepad2.right_bumper && !isHeldGlisiere) {
                cupaMore = false;
                first = false;
                glisiere.setToPosition(3);
                maturica.toggleEject();
                isDown = false;
                isHeldGlisiere = true;
            } else if (gamepad2.left_bumper && !isHeldGlisiere) {
                cupaMore = false;
                first = false;
                glisiere.setToPosition(4);
                maturica.toggleEject();
                isDown = false;
                isHeldGlisiere = true;
            } else if (gamepad2.left_trigger < 0.3 && gamepad2.right_trigger < 0.3 && !gamepad2.left_bumper && !gamepad2.right_bumper && !gamepad2.x) isHeldGlisiere = false;
            telemetry.addData("Glisiera Ticks:", glisiere.getTicks());

            //Cupa
            if (gamepad2.y && !isCupaHeld && !faza_3) {
                if(!first)
                    cupa.toggleCupa(cupaMore);
                else {
                    glisiere.setToPosition(2);
                    timpCupa.reset();
                    faza_1 = true;
                }
                isCupaHeld = true;
                boolTimer = true;
                isCupaProcessing = true;
            }
            else if (!gamepad2.y) isCupaHeld = false;

            if(timpCupa.milliseconds() >= 800 && faza_1) {
                cupa.servo.setPosition(0.5);
                faza_1 = false;
                faza_2 = true;
            }

            if(timpCupa.milliseconds() >= 1600 && faza_2) {
                glisiere.setToPosition(1);
                faza_2 = false;
                faza_3 = true;
            }

            if(gamepad2.y && !isCupaHeld && faza_3) {
                glisiere.setToPosition(2);
                timpCupa.reset();
                faza_3 = false;
                faza_4 = true;
            } else if(!gamepad2.y) isCupaHeld = false;

            if(timpCupa.milliseconds() >= 800 && faza_4) {
                cupa.servo.setPosition(0.97);
                faza_4 = false;
            }

            //Rata

            double startingPower = 0;
            long start_time = System.nanoTime();

            if (gamepad1.x && !isHeldRata) {
                isHeldRata = true;
                startingPower = 0.7;
                if (stateRata == -1) {
                    stateRata = 0;
                    rata.rotate(0.0);
                } else {
                    stateRata = -1;
                    rata.rotate(startingPower);
                }
            } else if (!gamepad1.x) isHeldRata = false;

            if(rata.motor.isBusy() && stateRata == -1) {
                long end_time = System.nanoTime();
                double difference = (end_time - start_time) / 1e6;

                if(difference >= 500 && difference <= 1500 && (difference - 1000) > 0) rata.rotate(startingPower + ((difference - 1000) / 30));
            }

            idle();
            telemetry.addData("Elapsed time:",timp.toString());
            telemetry.update();
        }
        glisiere.motor.setPower(0);
    }
}

