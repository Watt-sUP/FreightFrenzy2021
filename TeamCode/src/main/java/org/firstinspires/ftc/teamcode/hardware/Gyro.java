package org.firstinspires.ftc.teamcode.hardware;

import com.qualcomm.hardware.bosch.BNO055IMU;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

public class Gyro {
    private BNO055IMU imu;
    public Telemetry telemetry;
    private double globalAngle;
    private Orientation lastAngles = new Orientation();

    public Gyro(BNO055IMU imu) {
        this.imu = imu;

        BNO055IMU.Parameters param = new BNO055IMU.Parameters();

        param.mode = BNO055IMU.SensorMode.IMU;
        param.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        param.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        param.loggingEnabled = false;

        imu.initialize(param);
    }

    /*
    public double checkDirection()
    {
        double correction, angle, gain = .10;
        angle = getAngle();
        if (angle == 0)
            correction = 0;
        else
            correction = -angle;
        correction = correction * gain;
        return correction;
    }
    */

    public double getAngle() {
        //TODO: De verificat ce axa da unghiul
        Orientation angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        double deltaAngle = angles.firstAngle - lastAngles.firstAngle;
        if (deltaAngle < -180)
            deltaAngle += 360;
        else if (deltaAngle > 180)
            deltaAngle -= 360;
        globalAngle += deltaAngle;
        lastAngles = angles;
        return globalAngle;
    }

    public void resetAngle() {
        lastAngles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        globalAngle = 0;
    }

}
