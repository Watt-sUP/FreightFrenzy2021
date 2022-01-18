<<<<<<< HEAD
package org.firstinspires.ftc.teamcode.Colector;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Config.Config;

@TeleOp(name = "Cupa" )
public class CUPA extends LinearOpMode {
    Servo servotest;
    private ElapsedTime runtime = new ElapsedTime();
    double servoPosition = 0.0;

    @Override
    public void runOpMode() throws  InterruptedException {
        servotest = (Servo) hardwareMap.servo.get(Config.cupa);

        waitForStart();

        while (opModeIsActive()) {
            if (gamepad1.a) {
                if (servoPosition == 0.0) servoPosition = 0.5;
                else servoPosition = 0.0;
                servotest.setPosition(servoPosition);
            }
            telemetry.addData("Elapsed Time:", runtime.toString());
            telemetry.addData("Servo position:", servoPosition);
        }

    }
}
=======
package org.firstinspires.ftc.teamcode.Colector;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp(name = "Cupa" )
public class CUPA extends LinearOpMode {
    Servo servotest;
    private ElapsedTime runtime = new ElapsedTime();
    double servoPosition = 0.0;

    @Override
    public void runOpMode() throws  InterruptedException {
        servotest = (Servo) hardwareMap.servo.get("CUPA");

        waitForStart();

        while (opModeIsActive()) {
            if (gamepad1.a) {
                if (servoPosition == 0.0) servoPosition = 0.5;
                else servoPosition = 0.0;
                servotest.setPosition(servoPosition);
            }
            telemetry.addData("Elapsed Time:", runtime.toString());
            telemetry.addData("Servo position:", servoPosition);
        }

    }
}
>>>>>>> ecad45d32c989cb8f905146baef09cfcea2ca48a
