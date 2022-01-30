package org.firstinspires.ftc.teamcode.hardware;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Config.Config;

public class Runner {
    private DcMotor leftFront, leftBack, rightFront, rightBack;
    private Gyro imu;
    public Telemetry telemetry;
    private double  MOTOR_TICK_COUNT;
    private double circumference = 97 * Math.PI;

    public Runner(HardwareMap hm) {
        leftFront = hm.dcMotor.get(Config.left_front);
        leftBack = hm.dcMotor.get(Config.left_back);
        rightFront = hm.dcMotor.get(Config.right_front);
        rightBack = hm.dcMotor.get(Config.right_back);
        imu = new Gyro(hm.get(BNO055IMU.class, Config.imu));

        leftFront.setDirection(DcMotorSimple.Direction.FORWARD);
        leftBack.setDirection(DcMotorSimple.Direction.FORWARD);
        rightFront.setDirection(DcMotorSimple.Direction.REVERSE);
        rightBack.setDirection(DcMotorSimple.Direction.REVERSE);

        leftFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        leftBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        MOTOR_TICK_COUNT = leftFront.getMotorType().getTicksPerRev();

        setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }

    public void setMode(DcMotor.RunMode mode) {
        leftFront.setMode(mode);
        leftBack.setMode(mode);
        rightFront.setMode(mode);
        leftBack.setMode(mode);
    }

    public void setPower(double pw) {
        setPower(pw, pw, pw, pw);
    }

    public void setPower(double lf, double lb, double rf, double rb) {
        leftFront.setPower(lf);
        leftBack.setPower(lb);
        rightFront.setPower(rf);
        rightBack.setPower(rb);
    }

    public void setTargetPositions(int lf, int lb, int rf, int rb) {
        leftFront.setTargetPosition(lf);
        leftBack.setTargetPosition(lb);
        rightFront.setTargetPosition(rf);
        rightBack.setTargetPosition(rb);
    }

    public void setTargetPositions(int target) {
        setTargetPositions(target, target, target, target);
    }

    public void walk(int distance) {
        setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        int target =(int) (distance * MOTOR_TICK_COUNT / circumference);
        setTargetPositions(target);
        setMode(DcMotor.RunMode.RUN_TO_POSITION);
        setPower(1);
        while(leftFront.isBusy() && rightFront.isBusy()) {
            telemetry.addData("Status", "Walking using encoders");
            telemetry.update();
        }
    }

    public void walkSlow(int distance) {
        setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        int target =(int) (distance * MOTOR_TICK_COUNT / circumference);
        setTargetPositions(target);
        setMode(DcMotor.RunMode.RUN_TO_POSITION);
        setPower(0.25);
        while(leftFront.isBusy() && rightFront.isBusy()) {
            telemetry.addData("Status", "Walking using encoders");
            telemetry.update();
        }
    }

    public void turn(int angle, int power){
        imu.resetAngle();
        if (angle < 0) {
            setPower(power, power, -power, -power);
        } else if (angle > 0) {
            setPower(-power, -power, power, power);
        } else return;

        if (angle < 0) {
            while (imu.getAngle() == 0) {}
            while (imu.getAngle() > angle) {}
        } while (imu.getAngle() < angle) {}

        setPower(0);
        imu.resetAngle();
    }

    public void strafe(int distance)
    {
        setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        int target =(int) (distance * MOTOR_TICK_COUNT / circumference);
        setTargetPositions(target, -target, -target, target);
        setMode(DcMotor.RunMode.RUN_TO_POSITION);
        setPower(1);
        while(leftFront.isBusy() && rightFront.isBusy()) {
            telemetry.addData("Status", "Walking using encoders");
            telemetry.update();
        }
    }

}
