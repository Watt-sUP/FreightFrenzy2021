package org.firstinspires.ftc.teamcode.teleOp;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.gamepad.Axis;
import org.firstinspires.ftc.teamcode.gamepad.Button;
import org.firstinspires.ftc.teamcode.gamepad.GamepadEx;
import org.firstinspires.ftc.teamcode.hardware.Mugurel;

@TeleOp(name = "OpMode", group = "Testing")
public class DriverControlled extends LinearOpMode {

    //Declaratii
    private boolean isCupaProcessing = false, isDown = true;
    private boolean first = false, isGlisieraProcessing = false;
    private boolean faza_1 = false, faza_2 = false, faza_3 = false, faza_4 = false;
    private Mugurel robot;
    private int stateRata = -1;
    private ElapsedTime timpCupa, timerGli, runTime, timerPro;

    @Override
    public void runOpMode() throws InterruptedException {
        GamepadEx andrei = new GamepadEx(gamepad1);
        GamepadEx cristi = new GamepadEx(gamepad2);
        robot = new Mugurel(hardwareMap);
        runTime = new ElapsedTime();
        timpCupa = new ElapsedTime();
        timerGli = new ElapsedTime();
        timerPro = new ElapsedTime();

        robot.wheels.setUp();
        waitForStart();
        timpCupa.reset();
        timerGli.reset();
        runTime.reset();
        timerPro.reset();
        while (opModeIsActive()) {

            andrei.update();
            cristi.update();

            move(andrei.left_x, andrei.left_y, andrei.right_x, andrei.right_trigger.toButton(0.3), andrei.left_trigger.toButton(0.3), andrei.dpad_left, andrei.dpad_left);
            ruleta(cristi.left_y, cristi.left_x, cristi.right_y.toButton(0.03));
            maturica(cristi.a);
            deget(cristi.b);
            rata(andrei.x);
            glisiere(cristi.x, cristi.right_trigger.toButton(0.3), cristi.left_trigger.toButton(0.3), cristi.right_bumper, cristi.left_bumper);
            cupa(cristi.y);
            senzor(robot.distance);

            idle();
            telemetry.addData("Elapsed time:",runTime.toString());
            telemetry.update();
        }
        robot.glisiere.motor.setPower(0);
    }

    private void move(Axis lx, Axis ly, Axis rx, Button smallPower, Button mediumPower, Button dl, Button dr) {
        double modifier = 1.0;
        if (smallPower != null && smallPower.raw) modifier = 0.23;
        if (mediumPower != null && mediumPower.raw)  modifier = 0.5;

        final double drive_y = robot.runner.scalePower(ly.raw);
        final double drive_x = robot.runner.scalePower(lx.raw);
        final double turn = -robot.runner.scalePower(rx.raw);

        if (dr != null && dr.raw) robot.runner.moveWithAngle(-1,0,0, modifier);
        else if (dl != null && dl.raw) robot.runner.moveWithAngle(1,0,0, modifier);
        else robot.runner.moveWithAngle(drive_x, drive_y, turn, modifier);
    }

    private void deget(Button deget) {
        if (deget.pressed())
            robot.cupa.toggleDeget();
    }

    private void maturica(Button maturica) {
        if (maturica.pressed())
            robot.maturica.toggleCollect();
    }

    private void ruleta(Axis ly, Axis lx, Button magnet) {
        robot.ruleta.move(ly.raw, lx.raw);
        if (magnet.pressed())
            robot.ruleta.toggleCupa();
    }

    private void rata(Button rata) {
        double startingPower = 0;
        long start_time = System.nanoTime();

        if (rata.pressed()) {
            startingPower = 0.7;
            if (stateRata == -1) {
                stateRata = 0;
                robot.rata.rotate(0.0);
            } else {
                stateRata = -1;
                robot.rata.rotate(startingPower);
            }
        }

        if(robot.rata.motor.isBusy() && stateRata == -1) {
            long end_time = System.nanoTime();
            double difference = (end_time - start_time) / 1e6;

            if(difference >= 500 && difference <= 1500 && (difference - 1000) > 0)
                robot.rata.rotate(startingPower + ((difference - 1000) / 30));
        }
    }

    private void glisiere(Button poz0, Button poz1, Button poz2, Button poz3, Button poz4) {
        if (poz0.pressed()) {
            first = false;
            robot.glisiere.setToPosition(0);
            isDown = true;
        }
        if (poz1.pressed()) {
            first = true;
            robot.glisiere.setToPosition(1);
            robot.maturica.toggleEject();
            isDown = false;
        } else if (poz2.pressed()) {
            first = false;
            robot.glisiere.setToPosition(2);
            robot.maturica.toggleEject();
            isDown = false;
        } else if (poz3.pressed()) {
            first = false;
            robot.glisiere.setToPosition(3);
            robot.maturica.toggleEject();
            isDown = false;
        } else if (poz4.pressed()) {
            first = false;
            robot.glisiere.setToPosition(4);
            robot.maturica.toggleEject();
            isDown = false;
        }
    }

    private void cupa(Button cupa) {
        if (cupa.pressed() && !faza_3) {
            if(!first) {
                robot.cupa.toggleCupa();
                isCupaProcessing = true;
                timerGli.reset();
            }
            else {
                robot.glisiere.setToPosition(2);
                timpCupa.reset();
                faza_1 = true;
            }
        }

        if(timerGli.milliseconds() >= 400 && isCupaProcessing) {
            robot.glisiere.setToPosition(0);
            isDown = true;
            robot.maturica.toggleCollect();
            isCupaProcessing = false;
        }

        if(timpCupa.milliseconds() >= 800 && faza_1) {
            robot.cupa.servo.setPosition(0.5);
            faza_1 = false;
            faza_2 = true;
        }

        if(timpCupa.milliseconds() >= 1600 && faza_2) {
            robot.glisiere.setToPosition(1);
            faza_2 = false;
            faza_3 = true;
        }

        if(cupa.pressed() && faza_3) {
            robot.glisiere.setToPosition(2);
            timpCupa.reset();
            faza_3 = false;
            faza_4 = true;
        }

        if(timpCupa.milliseconds() >= 800 && faza_4) {
            robot.cupa.servo.setPosition(0.97);
            faza_4 = false;
        }
    }

    private void senzor(DistanceSensor du) {
        if(du.getDistance(DistanceUnit.CM) <= 4.0 && isDown) {
            robot.maturica.toggleEject();
            robot.cupa.toggleDeget();
            robot.glisiere.setToPosition(3);
            timerPro.reset();
            isGlisieraProcessing = true;
            first = false;
            isDown = false;
        }

        if(timerPro.milliseconds() >= 1000 && isGlisieraProcessing) {
            robot.cupa.toggleCupa();
            isGlisieraProcessing = false;
        }
    }
}

