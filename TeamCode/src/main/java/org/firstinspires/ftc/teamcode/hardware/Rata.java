package org.firstinspires.ftc.teamcode.hardware;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Rata {
    public DcMotorEx motor;
    private double currentPower;

    public Rata(HardwareMap hardwareMap) {
        motor = hardwareMap.get(DcMotorEx.class, Config.rate);
        motor.setDirection(DcMotorSimple.Direction.FORWARD);
    }

    public void rotate(double power) {
        motor.setPower(power);
    }

    public void score(int position, double power) {
        motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        motor.setTargetPosition(position);
        motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        motor.setPower(power);
    }

    public void stop() {
        motor.setPower(0);
        motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public void setRed() {
        motor.setDirection(DcMotorSimple.Direction.FORWARD);
    }

    public void setBlue() {
        motor.setDirection(DcMotorSimple.Direction.REVERSE);
    }

    private double inchToM(double inch) {
        return inch * 0.0254;
    }

    public void score_rata_experimental() {
        motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        final double carousel_radius = inchToM(7.5);  // meters
        final double carousel_circumference = 2.0 * Math.PI * carousel_radius;

        final double wheel_radius = inchToM(1.4173);   // meters
        final double wheel_circumference = 2.0 * Math.PI * wheel_radius;

        final double needed_length = carousel_circumference + inchToM(2.0);

        final double needed_revolutions = needed_length / wheel_circumference;
        final double TICKS_PER_REVOLUTION = motor.getMotorType().getTicksPerRev();
        final double MAX_VELOCITY = 3000;

        final double needed_ticks = needed_revolutions * TICKS_PER_REVOLUTION;

        final double duck_position = inchToM(3.0);   // meters

        final double miu = 0.05;     // coef frecare
        final double ff = miu * 9.81;   // forta frecare
        final double ff2 = ff * ff;

        final double vel_rap = wheel_radius / carousel_radius;
        final double motor_to_vel = 1.0 / TICKS_PER_REVOLUTION * vel_rap * duck_position;

        ElapsedTime timer = new ElapsedTime();
        timer.reset();

        double dt = 0.0001;
        double last_s = timer.seconds();

        while(motor.getCurrentPosition() < needed_ticks) {

            double now_velocity = motor.getVelocity();
            double now_time = timer.seconds();

            double l = now_velocity, r = MAX_VELOCITY;
            for(int i = 0; i < 14; i++) {
                double m = (l + r) / 2.0;
                double tm = timer.seconds();
                double v = m;

                double last_v = now_velocity * motor_to_vel;
                double now_v = v * motor_to_vel;
//                double acc = (now_v - last_v) / (tm - now_time);
                double acc = (now_v - last_v) / dt;

                double inert = acc;
                double cntrf = now_v * now_v / duck_position;

                double force = inert * inert + cntrf * cntrf;

                if(force < ff2)
                    l = m;
                else
                    r = m;
            }

            motor.setVelocity(l);
//            motor.setVelocity(MAX_VELOCITY);
            double now_s = timer.seconds();
            dt = now_s - last_s;
            last_s = now_s;
        }
        motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }
}
