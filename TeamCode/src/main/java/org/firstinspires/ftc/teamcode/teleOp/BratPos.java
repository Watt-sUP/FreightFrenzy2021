package org.firstinspires.ftc.teamcode.teleOp;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.gamepad.Button;
import org.firstinspires.ftc.teamcode.gamepad.GamepadEx;
import org.firstinspires.ftc.teamcode.hardware.Brat;
import org.firstinspires.ftc.teamcode.hardware.Config;
import org.firstinspires.ftc.teamcode.hardware.Cupa;
import org.firstinspires.ftc.teamcode.hardware.Mugurel;

@TeleOp(name = "Pozitii brat", group = "Testing")
public class BratPos extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {
        Brat brat = new Brat(hardwareMap);
        Cupa cupa = new Cupa(hardwareMap);
        boolean isHeld = false;
        waitForStart();
        double pos = 0.5;
        cupa.deget.setPosition(0);
        brat.motor_vertical.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        while (opModeIsActive()) {
            brat.motor_vertical.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            brat.motor_vertical.setPower(gamepad1.left_stick_y * 0.4);
            if(gamepad1.y && !isHeld) {
                pos += 0.05;
                cupa.deget.setPosition(pos);
                isHeld = true;
            } else if(gamepad1.x && !isHeld) {
                pos -= 0.05;
                cupa.deget.setPosition(pos);
                isHeld = true;
            } else if(!gamepad1.y && !gamepad1.x) isHeld = false;
            telemetry.addData("Pozitie brat:", brat.motor_vertical.getCurrentPosition());
            telemetry.addData("Pozitie servo:", pos);
            telemetry.update();
        }
    }
}

