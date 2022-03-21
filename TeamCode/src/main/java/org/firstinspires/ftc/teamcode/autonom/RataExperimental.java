package org.firstinspires.ftc.teamcode.autonom;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.hardware.Config;
import org.firstinspires.ftc.teamcode.hardware.Mugurel;

import java.util.concurrent.TimeUnit;

//@Disabled
@Autonomous(name = "RataExp", group = "autonom")
public class RataExperimental extends LinearOpMode {

    DcMotorEx motor;

    @Override
    public void runOpMode() throws InterruptedException {

        motor = hardwareMap.get(DcMotorEx.class, Config.rate);
        motor.setDirection(DcMotorSimple.Direction.FORWARD);
        motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        waitForStart();

        while(opModeIsActive()) {
            if(gamepad1.a) {
                score_rata();
            }
        }
    }

    private double inchToM(double inch) {
        return inch * 0.0254;
    }

    public void score_rata() {
        motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        final double carousel_radius = inchToM(7.5);  // meters
        final double carousel_circumference = 2.0 * Math.PI * carousel_radius;

        final double wheel_radius = inchToM(1.4173);   // meters
        final double wheel_circumference = 2.0 * Math.PI * wheel_radius;

        final double needed_length = carousel_circumference + inchToM(2.0);

        final double needed_revolutions = needed_length / wheel_circumference;
        final double TICKS_PER_REVOLUTION = motor.getMotorType().getTicksPerRev();
        final double MAX_VELOCITY = motor.getMotorType().getAchieveableMaxTicksPerSecond();

        final double needed_ticks = needed_revolutions * TICKS_PER_REVOLUTION;

        final double duck_position = inchToM(3.0);   // meters

        final double miu = 0.3;     // coef frecare
        final double ff = miu * 9.81;   // forta frecare
        final double ff2 = ff * ff;

        final double vel_rap = wheel_radius / carousel_radius;
        final double motor_to_vel = 1.0 / TICKS_PER_REVOLUTION * vel_rap * duck_position;

        ElapsedTime timer = new ElapsedTime();
        timer.reset();

        while(motor.getCurrentPosition() < needed_ticks) {

            double now_velocity = motor.getVelocity();
            double now_time = timer.time(TimeUnit.SECONDS);

            telemetry.addData("now_velocity", now_velocity);
            telemetry.addData("ticks", motor.getCurrentPosition());
            telemetry.update();

            double l = now_velocity, r = MAX_VELOCITY;
            for(int i = 0; i < 20; i++) {
                double m = (l + r) / 2.0;
                double tm = timer.time(TimeUnit.SECONDS);
                double v = m;

                double last_v = now_velocity * motor_to_vel;
                double now_v = v * motor_to_vel;
                double acc = (now_v - last_v) / (tm - now_time);

                double inert = acc;
                double cntrf = now_v * now_v / duck_position;

                double force = inert * inert + cntrf * cntrf;

                if(force < ff2)
                    l = m;
                else
                    r = m;
            }

            motor.setPower(l);
        }
    }
}