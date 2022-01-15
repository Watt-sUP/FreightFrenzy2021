package org.firstinspires.ftc.teamcode.Colector;

package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorSimple;


@TeleOp
public class Servo extends LinearOpMode {

    @Override
    public void runOpMode()
    {
        CRServo servo = hardwareMap.crservo.get("back_servo");
        servo.setDirection(DcMotorSimple.Direction.FORWARD);

        waitForStart();

        while(opModeIsActive())
        {
            servo.setPower(1.0);

            telemetry.update();
        }
    }

}

