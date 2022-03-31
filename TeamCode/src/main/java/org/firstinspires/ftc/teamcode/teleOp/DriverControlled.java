package org.firstinspires.ftc.teamcode.teleOp;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.gamepad.Axis;
import org.firstinspires.ftc.teamcode.gamepad.Button;
import org.firstinspires.ftc.teamcode.gamepad.GamepadEx;
import org.firstinspires.ftc.teamcode.hardware.Config;
import org.firstinspires.ftc.teamcode.hardware.Mugurel;

import dalvik.system.DelegateLastClassLoader;

@TeleOp(name = "OpMode", group = "Testing")
public class DriverControlled extends LinearOpMode {

    //Declaratii
    private boolean faceChanged = false, isCupaProcessing = false, isDown = true, faceIsHeld = false;
    private boolean first = false, isGlisieraProcessing = false, cleaning = false, magnetic = false, deget =true;
    private boolean emergency = false, antiprost = false;
    private Mugurel robot;
    private int stateRata = -1;
    private ElapsedTime timpCupa, timerGli, runTime, timerPro, timerMag, timerMaturica, timerStop;
    private ElapsedTime timerCos;

    @Override
    public void runOpMode() throws InterruptedException {
        GamepadEx andrei = new GamepadEx(gamepad1);
        GamepadEx cristi = new GamepadEx(gamepad2);
        robot = new Mugurel(hardwareMap);
        runTime = new ElapsedTime();
        timpCupa = new ElapsedTime();
        timerGli = new ElapsedTime();
        timerPro = new ElapsedTime();
        timerMag = new ElapsedTime();
        timerMaturica = new ElapsedTime();
        timerStop = new ElapsedTime();
        timerCos = new ElapsedTime();

        DcMotor frontLeftMotor = hardwareMap.dcMotor.get(Config.right_back);
        DcMotor backLeftMotor = hardwareMap.dcMotor.get(Config.right_front);
        DcMotor frontRightMotor = hardwareMap.dcMotor.get(Config.left_back);
        DcMotor backRightMotor = hardwareMap.dcMotor.get(Config.left_front);


        frontRightMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        backRightMotor.setDirection(DcMotorSimple.Direction.REVERSE);


        robot.wheels.setUp();
        waitForStart();
        timpCupa.reset();
        timerGli.reset();
        runTime.reset();
        timerPro.reset();
        timerMag.reset();
        timerMaturica.reset();
        timerStop.reset();
        timerCos.reset();
        while (opModeIsActive()) {

            andrei.update();
            cristi.update();

            double acceleration = gamepad1.left_stick_y;
            double strafe = gamepad1.left_stick_x * -1.1;
            double rotation = gamepad1.right_stick_x;
            telemetry.addData("Brat position:", robot.brat.getPosition());

            double powerLimit = 1.0;
            if (gamepad1.left_trigger >= 0.3) {
                powerLimit = 0.4;
            } else if (gamepad1.right_trigger >= 0.3) {
                powerLimit = 0.3;
            } else {
                powerLimit = 1.0;
            }

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
            } else {
                frontRightMotor.setDirection(DcMotorSimple.Direction.FORWARD);
                backRightMotor.setDirection(DcMotorSimple.Direction.FORWARD);
                frontLeftMotor.setDirection(DcMotorSimple.Direction.REVERSE);
                backLeftMotor.setDirection(DcMotorSimple.Direction.REVERSE);
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


            brat(gamepad2.left_stick_y, gamepad2.right_stick_x, cristi.dpad_down);
            maturica(cristi.a);
//            deget(cristi.b);
            rata(andrei.x, andrei.y);
            glisiere(cristi.x, cristi.right_trigger.toButton(0.3), cristi.left_trigger.toButton(0.3), cristi.right_bumper, cristi.left_bumper, cristi.b);
            cupa(cristi.y);
            senzor(robot.distance);
//            emergency(andrei.a);

            idle();

            telemetry.addData("Elapsed time:",runTime.toString());
            telemetry.update();
        }
        robot.glisiere.motor.setPower(0);
    }

//    private void deget(Button deget) {
//        if (deget.pressed())
//            robot.cupa.toggleDeget();
//    }

    private void maturica(Button maturica) {
        if (maturica.pressed())
            robot.maturica.toggleCollect();
    }

    private void brat(double verticalMovement, double horizontalMovement, Button magnet) {
        robot.brat.move(horizontalMovement);
        robot.brat.changePosition(verticalMovement);
        if (magnet.pressed()) {
            robot.brat.toggleCupa();
            timerMag.reset();
            magnetic = true;
        }

        if(timerMag.milliseconds() >= 1000 && magnetic) {
            robot.brat.toggleCupa();
            magnetic = false;
        }


    }

    private void rata(Button rata, Button change) {
        double startingPower = 0;
        double multiplier = 1;
        long start_time = System.nanoTime();

        if(change.pressed())
            multiplier = -multiplier;

        if (rata.pressed()) {
            startingPower = 0.7;
            if (stateRata == -1) {
                stateRata = 0;
                robot.rata.rotate(0.0);
            } else {
                stateRata = -1;
                robot.rata.rotate(startingPower * multiplier);
            }
        }

        if(robot.rata.motor.isBusy() && stateRata == -1) {
            long end_time = System.nanoTime();
            double difference = (end_time - start_time) / 1e6;

            if(difference >= 500 && difference <= 1500 && (difference - 1000) > 0)
                robot.rata.rotate((startingPower + ((difference - 1000) / 30)) * multiplier);
        }
    }

    private void glisiere(Button poz0, Button poz1, Button poz2, Button poz3, Button poz4, Button b) {
        if (poz0.pressed()) {
            first = false;
            robot.glisiere.setToPosition(0);
            isDown = true;
        }
        if (poz1.pressed()) {
            first = true;
            robot.glisiere.setToPosition(5);
            isDown = false;
        } else if (poz2.pressed()) {
            first = false;
            robot.glisiere.setToPosition(2);
            isDown = false;
        } else if (poz3.pressed()) {
            first = false;
            robot.glisiere.setToPosition(3);
            isDown = false;
        } else if (poz4.pressed()) {
            first = false;
            robot.glisiere.setToPosition(4);
            isDown = false;
        }
/*
        if(isDown) {
            if(b.pressed()) {
                timerMaturica.reset();
                cleaning = true;
                robot.cupa.toggleDeget();
 //               robot.glisiere.setToPosition(3);
                timerCos.reset();
                timerPro.reset();
                isGlisieraProcessing = true;
                first = false;
                isDown = false;
                deget = false;
            }
        } else */
       // {
            if(b.pressed())
                robot.cupa.toggleDeget();
       // }
        if (timerCos.milliseconds() >= 500 && !deget)
        {
            robot.glisiere.setToPosition(3);
            deget= true;
        }
        if(timerPro.milliseconds() >= 1000 && isGlisieraProcessing) {
            robot.cupa.toggleCupa();
            isGlisieraProcessing = false;
        }

        if(timerMaturica.milliseconds() >= 200 && cleaning) {
            robot.maturica.toggleEject();
            cleaning = false;
        }
    }

    private void cupa(Button cupa) {
        if (cupa.pressed() && !antiprost) {
            if(!first) {
                robot.cupa.toggleCupa();
                isCupaProcessing = true;
                timerGli.reset();
            }
            else {
                robot.glisiere.setToPosition(2);
                first = false;
            }
            antiprost = true;
        }

        if(timerGli.milliseconds() >= 400 && isCupaProcessing) {
            robot.glisiere.setToPosition(0);
            isDown = true;
            robot.maturica.toggleCollect();
            isCupaProcessing = false;
            antiprost = false;
        }
    }

    public void senzor(DistanceSensor du) {
        double distance = du.getDistance(DistanceUnit.CM);
        telemetry.addData("Distanta senzor:", distance);
        if(distance <= 5 && isDown) {
            timerMaturica.reset();
            cleaning = true;
            timerCos.reset();
            robot.cupa.toggleDeget();
            timerPro.reset();
            deget = false;
            isGlisieraProcessing = true;
            first = false;
            isDown = false;
        }
        if (timerCos.milliseconds() >= 700 && !deget)
        {
            robot.glisiere.setToPosition(3);
            deget= true;
        }

        if(timerPro.milliseconds() >= 1000 && isGlisieraProcessing) {
            robot.cupa.toggleCupa();
            isGlisieraProcessing = false;
        }

        if(timerMaturica.milliseconds() >= 200 && cleaning) {
            robot.maturica.toggleEject();
            cleaning = false;
        }
  }

    public void emergency(Button stop) {
        if(stop.pressed()) {
            emergency = true;
            timerStop.reset();
            robot.glisiere.motor.setPower(0);
            robot.cupa.toggleDeget();
            robot.cupa.toggleCupa();
        }

        if(timerStop.milliseconds() >= 400 && emergency) {
            robot.glisiere.setToPosition(0);
            emergency = false;
        }
    }
}

