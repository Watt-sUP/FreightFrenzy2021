package org.firstinspires.ftc.teamcode.hardware;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;

public class Runner {
    public DcMotor leftFront, leftBack, rightFront, rightBack;
    private BNO055IMU imu;
    private double MOTOR_TICK_COUNT;
    private double faceAngle;
    private Telemetry telemetry;
    private double wheelAngle = Math.PI / 4;
    private final double wheelDiameter = 96;
    private final double wheelCircumference = wheelDiameter * Math.PI;

    public enum AutonomousMoveType { FORWARD, BACKWARD, LEFT, RIGHT, ROTATE }

    public class MotorPowers {
        public double lf, lb, rf, rb;

        MotorPowers() {
            lf = lb = rf = rb = 0;
        }

        MotorPowers(double _lf, double _lb, double _rf, double _rb) {
            lf = _lf;
            lb = _lb;
            rf = _rf;
            rb = _rb;
        }

        public void normalize() {
            double mx = Math.max(Math.max(Math.abs(lf), Math.abs(lb)), Math.max(Math.abs(rf), Math.abs(rb)));
            if (mx > 1.0) {
                lf /= mx;
                lb /= mx;
                rf /= mx;
                rb /= mx;
            }
        }

        public void speed(double spd) {
            spd = Math.abs(spd);

            if (spd > 1.0) spd = 1.0;
            double mx = Math.max(Math.max(Math.abs(lf), Math.abs(lb)), Math.max(Math.abs(rf), Math.abs(rb)));
            if (spd <= 1.0 && mx < spd) {
                double coef = spd / mx;
                lf *= coef;
                lb *= coef;
                rf *= coef;
                rb *= coef;
            }
        }

        public void rap(double r) {
            lf *= r;
            lb *= r;
            rf *= r;
            rb *= r;
        }
    }


    public Runner(HardwareMap hardwareMap, Telemetry telemetry) {
        leftFront = hardwareMap.dcMotor.get(Config.left_front);
        leftBack = hardwareMap.dcMotor.get(Config.left_back);
        rightFront = hardwareMap.dcMotor.get(Config.right_front);
        rightBack = hardwareMap.dcMotor.get(Config.right_back);

        leftFront.setDirection(DcMotorSimple.Direction.FORWARD);
        leftBack.setDirection(DcMotorSimple.Direction.FORWARD);
        rightFront.setDirection(DcMotorSimple.Direction.REVERSE);
        rightBack.setDirection(DcMotorSimple.Direction.REVERSE);

        leftFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        leftBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        MOTOR_TICK_COUNT = leftFront.getMotorType().getTicksPerRev();

        imu = hardwareMap.get(BNO055IMU.class, Config.imu);

        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();

        parameters.mode = BNO055IMU.SensorMode.IMU;
        parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.loggingEnabled = false;

        ElapsedTime timer = new ElapsedTime();

        this.telemetry = telemetry;
        timer.reset();
        imu.initialize(parameters);
        while (!imu.isGyroCalibrated() && timer.milliseconds() < 1000) {
            telemetry.addData("Gyro", "Calibrating...");
            telemetry.update();
        }

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

    public void setPower(MotorPowers pw) {
        setPower(pw.lf, pw.lb, pw.rf, pw.rb);
    }

    public void setFace(double angle) {
        faceAngle = angle;
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

    public void stop() {
        setPower(0);
        setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }

    public void walk(int distance) {
        setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        int target =(int) (distance * MOTOR_TICK_COUNT / wheelCircumference);
        setTargetPositions(target);
        setMode(DcMotor.RunMode.RUN_TO_POSITION);
        setPower(0.3);
        while(leftBack.isBusy()) {
            if(leftBack.getCurrentPosition() % 20 == 0)
            {
                setPower(leftBack.getPower() + 0.1);
            }
        }
    }

    public void walkSlow(int distance) {
        setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        int target =(int) (distance * MOTOR_TICK_COUNT / wheelCircumference);
        setTargetPositions(target);
        setMode(DcMotor.RunMode.RUN_TO_POSITION);
        setPower(0.25);
        while(leftBack.isBusy()) {
            if(leftBack.getCurrentPosition() % 20 == 0)
            {
                setPower(leftBack.getPower() + 0.1);
            }
        }
    }

    public double getHeading() {
        return imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZXY, AngleUnit.DEGREES).firstAngle;
    }

    public double getAngleDistance(double start, double fin) {
        start = AngleUnit.normalizeDegrees(start);
        fin = AngleUnit.normalizeDegrees(fin);
        double dist = fin - start;
        dist = AngleUnit.normalizeDegrees(dist);
        return dist;
    }

    public void rotate(double degrees){
        degrees = AngleUnit.normalizeDegrees(degrees);
        double dist = getAngleDistance(getHeading(), degrees);
        rotateP(dist);
    }
    /*
    public void reset(DcMotor.RunMode mode) {
        setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        setMode(mode);
    }
    */
    public void rotateP(double degrees) {
        setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        double accepted = 0.5;
        double needAngle = AngleUnit.normalizeDegrees(getHeading() + degrees);
        double lastPower = 0.0;
        double maxDifference = 0.1;
        double angleDecrease = 40.0;
        while (true) {
            double myAngle = getHeading();
            double distance = getAngleDistance(myAngle, needAngle);
            if (Math.abs(distance) < accepted) break;

            double power = 0.0;

            if (Math.abs(distance) < angleDecrease)
                power = Math.abs(distance) / angleDecrease;
            else
                power = 1.0;

            power = Math.min(power, lastPower + maxDifference);
            power = Math.max(power, lastPower - maxDifference);
            lastPower = power;
            power = -power;
            if (distance < 0) power = -power;

            setPower(power, power, -power, -power);
        }
        stop();
    }

    public MotorPowers angleDrive(double speed, double angle, double rot) {
        angle += faceAngle;
        angle += wheelAngle;

        double lf = speed * Math.sin(-angle) - rot; // -power
        double rf = speed * Math.cos(-angle) + rot; // 1+power
        double lb = speed * Math.cos(-angle) - rot; // 1-power
        double rb = speed * Math.sin(-angle) + rot; // power
        lf = -lf; // power
        rf = -rf; //-1
        lb = -lb; //power-1
        rb = -rb; //-power

        MotorPowers mpw = new MotorPowers(lf, lb, rf, rb);

        mpw.normalize();
        mpw.speed(speed);
        return mpw;
    }

    public void angleMove(double speed, double angle, double rot) {
        angleMove(speed, angle, rot, 1.0);
    }

    public void angleMove(double speed, double angle, double rot, double rap) {
        MotorPowers pw = angleDrive(speed, angle, rot);
        pw.rap(rap);
        setPower(pw);
    }

    public void strafe(int distance)
    {
        setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        int target =(int) (distance * MOTOR_TICK_COUNT / wheelCircumference);
        setTargetPositions(target, -target, -target, target);
        setMode(DcMotor.RunMode.RUN_TO_POSITION);
        setPower(1);
    }

    public int distanceToTicks(double dist) {
        double ans = (dist * MOTOR_TICK_COUNT) / wheelCircumference;
        return (int) ans;
    }

    public int distanceToTicksLeftRight(double dist) {
        double ans = (double) distanceToTicks(dist) * 1.00;
        return (int) ans;
    }


    public void moveForwardBackward(double distance, AutonomousMoveType type)
    {
        if(type != AutonomousMoveType.FORWARD && type != AutonomousMoveType.BACKWARD)   return;
        int ticks = distanceToTicks(distance);
        if(type == AutonomousMoveType.BACKWARD)    ticks = -ticks;
        setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        setTargetPositions(ticks, ticks, ticks, ticks);
        setMode(DcMotor.RunMode.RUN_TO_POSITION);
        move();
    }

    public void moveLeftRight(double distance, AutonomousMoveType type)
    {
        if(type != AutonomousMoveType.LEFT && type != AutonomousMoveType.RIGHT) return;
        int ticks = distanceToTicksLeftRight(distance);
        if(type == AutonomousMoveType.LEFT) ticks = -ticks;
        setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        setTargetPositions(ticks, -ticks, -ticks, ticks);
        setMode(DcMotor.RunMode.RUN_TO_POSITION);
        move();
    }

    public void move()
    {
        double lastPower = 0.0;
        double maxDifference = 0.1;
        int ticksDecrease = (int) (1.0 * MOTOR_TICK_COUNT);
        while (isBusy()) {
            double power = 0.0;

            int ticksDistance = getTicksDistance();

            if (ticksDistance < ticksDecrease)
                power = (double) ticksDistance / (double) ticksDecrease;
            else
                power = 1.0;

            power = Math.min(power, lastPower + maxDifference);
            power = Math.max(power, lastPower - maxDifference);
            power = Math.max(power, 0.3);
            lastPower = power;

            setPower(power);
        }
        stop();
    }

    public boolean isBusy() {

        final int LIMIT = 10;
        int rem = getTicksDistance();
        if(rem <= LIMIT)    return false;
        return true;
    }

    public int getTicksDistance() {
        int sum = 0;
        sum += Math.abs(leftFront.getCurrentPosition() - leftFront.getTargetPosition());
        sum += Math.abs(leftBack.getCurrentPosition() - leftBack.getTargetPosition());
        sum += Math.abs(rightFront.getCurrentPosition() - rightFront.getTargetPosition());
        sum += Math.abs(rightBack.getCurrentPosition() - rightBack.getTargetPosition());
        return sum / 4;
    }



}
