package org.firstinspires.ftc.teamcode.autonom;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcontroller.external.samples.HardwarePushbot;
import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;
import org.firstinspires.ftc.teamcode.hardware.Config;
import org.firstinspires.ftc.teamcode.hardware.Cupa;
import org.firstinspires.ftc.teamcode.hardware.Glisiere;

import java.util.List;

@Autonomous(name="Un autonom frumos sus", group="Autonom")
public class AutonomSus extends LinearOpMode {

    /* Declare OpMode members. */
    HardwarePushbot robot   = new HardwarePushbot();   // Use a Pushbot's hardware

    static final double     COUNTS_PER_MOTOR_REV    = 1500 ;    // eg: TETRIX Motor Encoder
    static final double     DRIVE_GEAR_REDUCTION    = 1.0 ;     // This is < 1.0 if geared UP
    static final double     WHEEL_DIAMETER_CM   = 96.0 ;     // For figuring circumference
    static final double     COUNTS_PER_CM         = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) /
            (WHEEL_DIAMETER_CM * 3.1415);

    static final double     DRIVE_SPEED             = 0.1;     // Nominal speed for better accuracy.
    static final double     TURN_SPEED              = 0.45;     // Nominal half speed for better accuracy.

    static final double     HEADING_THRESHOLD       = 1 ;      // As tight as we can make it with an integer gyro
    static final double     P_TURN_COEFF            = 0.03;     // Larger is more responsive, but also less stable
    static final double     P_DRIVE_COEFF           = 0.15;     // Larger is more responsive, but also less stable

    private DcMotor leftFront, leftBack, rightFront, rightBack;
    private DcMotor motorMatura;
    private BNO055IMU gyro;
    private double MOTOR_TICK_COUNT;
    private DcMotor rate;
    private double circumference = 96.0 * Math.PI;

    private static final String TFOD_MODEL_ASSET = "FreightFrenzy_DM.tflite";
    private static final String[] LABELS = {
            "Duck",
            "Team Marker"
    };

    private static final String VUFORIA_KEY = Config.VuforiaKey;
    private VuforiaLocalizer vuforia;
    private TFObjectDetector tfod;
    private enum Locations {Top, Middle, Bottom};
    private Locations teamMarkerLocation = null;

    @Override
    public void runOpMode() {

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

        motorMatura = hardwareMap.get(DcMotorEx.class, Config.matura);
        rate = hardwareMap.dcMotor.get(Config.rate);

        initVuforia();
        initTfod();

        if (tfod != null) {
            tfod.activate();
            tfod.setZoom(1.5, 16.0/9.0);
        }

        MOTOR_TICK_COUNT = leftFront.getMotorType().getTicksPerRev();

        Glisiere glisiere = new Glisiere(hardwareMap, telemetry);
        Cupa cupa = new Cupa(hardwareMap, telemetry);


        gyro = hardwareMap.get(BNO055IMU.class, Config.imu);

        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();

        parameters.mode = BNO055IMU.SensorMode.IMU;
        parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.loggingEnabled = false;

        ElapsedTime timer = new ElapsedTime();

        timer.reset();

        gyro.initialize(parameters);
        while (!gyro.isGyroCalibrated() && timer.milliseconds() < 1000) {
            telemetry.addData("Gyro", "Calibrating...");
            telemetry.update();
            sleep(50);
            idle();
        }

        leftFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);


        telemetry.addData(">", "Robot Ready.");
        telemetry.update();

        leftFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        leftBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        int pos = 0;
        while (!isStarted()) {

            if (tfod != null) {
                List<Recognition> updatedRecognitions = tfod.getUpdatedRecognitions();
                if (updatedRecognitions != null) {
                    telemetry.addData("# Object Detected", updatedRecognitions.size());

                    int i = 0;
                    teamMarkerLocation = Locations.Bottom;
                    for (Recognition recognition : updatedRecognitions) {
                        if (recognition.getLabel() == "Duck") {
                            telemetry.addData("Team Marker:", "Detected");
                            if (((recognition.getRight() - recognition.getLeft()) / 2 + recognition.getLeft()) < (recognition.getImageWidth() / 2))
                                teamMarkerLocation = Locations.Middle;
                            else if (((recognition.getRight() - recognition.getLeft()) / 2 + recognition.getLeft()) >= (recognition.getImageWidth() / 2))
                                teamMarkerLocation = Locations.Top;
                            break;
                        }
                        if (recognition.getLabel() != "Duck")
                            telemetry.addData("Team Marker:", "Not detected");
                        i++;
                    }
                    telemetry.addData("Marker Location:", teamMarkerLocation.toString());
                    telemetry.update();
                }
            }
        }

        if(teamMarkerLocation == Locations.Bottom) pos = 1;
        else if(teamMarkerLocation == Locations.Middle) pos = 2;
        else pos = 3;

        strafe(380);
        if(pos == 1)
            walk(550);
        else if( pos == 2)
            walk(500);
        else
            walk(475);

        glisiere.setToPosition(pos);

        sleep(1000);
        cupa.toggleCupa();
        sleep(800);
        cupa.toggleCupa();
        sleep(600);
        glisiere.setToPosition(0);
        sleep(500);
        reset();
        if(pos == 1)
            walk(-100);
        else if( pos == 2)
            walk(-75);
        else
            walk(-50);
        reset();
        if(pos == 1)
            gyroTurn(TURN_SPEED, 112.0);
        else
            gyroTurn(TURN_SPEED, 109.0);
        motorMatura.setPower(-0.5);
        walkToDuck(-700);
        sleep(100);
        reset();
        gyroTurn(TURN_SPEED, 65.0);
        walkSlow(255);

        glisiere.setToPosition(3);

        sleep(600);
        cupa.toggleCupa();
        sleep(800);
        cupa.toggleCupa();
        sleep(500);

//        glisiere.motor.setPower(0);

        glisiere.setToPosition(0);
        reset();
        walkSlow(-50);
        reset();
        gyroTurn(TURN_SPEED, 90.0);
        strafe(-1100);
        motorMatura.setPower(-1.0);
        run(-1100);
        sleep(300);
        glisiere.setToPosition(1);
        motorMatura.setPower(1.0);
        glisiere.setToPosition(3);
        walk(1500);
        strafe(300);
        reset();
        gyroTurn(TURN_SPEED, 0.0);
        //turn(-945);
        walk(350);


        sleep(600);
        cupa.toggleCupa();
        sleep(800);
        cupa.toggleCupa();
        sleep(500);
        glisiere.setToPosition(0);
        run(-320);
        reset();
        gyroTurn(TURN_SPEED, 90.0);
        strafeFast(-300);
        run(-1420);
        telemetry.addData("Path", "Complete");
        telemetry.update();
    }

    public double getHeading() {
        return gyro.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES).firstAngle;
    }

    private void initVuforia() {
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();

        parameters.vuforiaLicenseKey = VUFORIA_KEY;
        parameters.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;

        vuforia = ClassFactory.getInstance().createVuforia(parameters);
    }

    private void initTfod() {
        int tfodMonitorViewId = hardwareMap.appContext.getResources().getIdentifier(
                "tfodMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(tfodMonitorViewId);
        tfodParameters.minResultConfidence = 0.3f;
        tfodParameters.isModelTensorFlow2 = true;
        tfodParameters.inputSize = 320;
        tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia);
        tfod.loadModelFromAsset(TFOD_MODEL_ASSET, LABELS);
    }

    private void reset() {
        leftFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        leftBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public void walk(int distance) {
        leftFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        int target =(int) (distance * MOTOR_TICK_COUNT / (circumference * 3));
        leftFront.setTargetPosition(target);
        leftBack.setTargetPosition(target);
        rightFront.setTargetPosition(target);
        rightBack.setTargetPosition(target);
        leftFront.setPower(0.55);
        leftBack.setPower(0.55);
        rightFront.setPower(0.55);
        rightBack.setPower(0.55);
        leftBack.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        leftFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightBack.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        while(leftFront.isBusy()&&rightFront.isBusy()){
            telemetry.addData("Status", "Walking using encoders");
            telemetry.update();
            if(leftFront.getCurrentPosition() % 25 == 0 || leftFront.getCurrentPosition() % -25 == 0) {
                leftFront.setPower(Math.abs(leftFront.getPower()) + 0.25);
                leftBack.setPower(Math.abs(leftBack.getPower()) + 0.25);
                rightFront.setPower(Math.abs(rightFront.getPower()) + 0.25);
                rightBack.setPower(Math.abs(rightBack.getPower()) + 0.25);
            }
            if(Math.abs(leftFront.getCurrentPosition()) % - Math.abs(target) <= 20) {
                leftFront.setPower(0.4);
                leftBack.setPower(0.4);
                rightFront.setPower(0.4);
                rightBack.setPower(0.4);
            }
        }
        leftFront.setPower(0);
        leftBack.setPower(0);
        rightFront.setPower(0);
        rightBack.setPower(0);
    }

    public void walkToDuck(int distance) {
        leftFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        int target =(int) (distance * MOTOR_TICK_COUNT / (circumference * 3));
        leftFront.setTargetPosition(target);
        leftBack.setTargetPosition(target);
        rightFront.setTargetPosition(target);
        rightBack.setTargetPosition(target);
        leftFront.setPower(0.2);
        leftBack.setPower(0.2);
        rightFront.setPower(0.2);
        rightBack.setPower(0.2);
        leftBack.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        leftFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightBack.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        while(leftFront.isBusy()&&rightFront.isBusy()){
            telemetry.addData("Status", "Walking using encoders");
            telemetry.update();
            if(leftFront.getCurrentPosition() % 25 == 0 || leftFront.getCurrentPosition() % -25 == 0) {
                leftFront.setPower(Math.abs(leftFront.getPower()) + 0.15);
                leftBack.setPower(Math.abs(leftBack.getPower()) + 0.15);
                rightFront.setPower(Math.abs(rightFront.getPower()) + 0.15);
                rightBack.setPower(Math.abs(rightBack.getPower()) + 0.15);
            }
            if(Math.abs(leftFront.getCurrentPosition()) % - Math.abs(target) <= 20) {
                leftFront.setPower(0.4);
                leftBack.setPower(0.4);
                rightFront.setPower(0.4);
                rightBack.setPower(0.4);
            }
        }
        leftFront.setPower(0);
        leftBack.setPower(0);
        rightFront.setPower(0);
        rightBack.setPower(0);
    }

    public void walkSlow(int distance) {
        leftFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        int target =(int) (distance * MOTOR_TICK_COUNT / (circumference * 3));
        leftFront.setTargetPosition(target);
        leftBack.setTargetPosition(target);
        rightFront.setTargetPosition(target);
        rightBack.setTargetPosition(target);
        leftFront.setPower(0.25);
        leftBack.setPower(0.25);
        rightFront.setPower(0.25);
        rightBack.setPower(0.25);
        leftBack.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        leftFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightBack.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        while(leftFront.isBusy()&&rightFront.isBusy()){
            telemetry.addData("Status", "Walking using encoders");
            telemetry.update();
        }
        leftFront.setPower(0);
        leftBack.setPower(0);
        rightFront.setPower(0);
        rightBack.setPower(0);
    }

    public void turn(int angle){
        leftFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        int  target = (int) (angle * 392 * Math.PI / 360);
        leftFront.setTargetPosition(target);
        leftBack.setTargetPosition(target);
        rightFront.setTargetPosition(-target);
        rightBack.setTargetPosition(-target);
        leftFront.setPower(1);
        leftBack.setPower(1);
        rightFront.setPower(1);
        rightBack.setPower(1);
        leftBack.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        leftFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightBack.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        while(leftFront.isBusy()&&rightFront.isBusy()) {
            telemetry.addData("Status", "Turning to left");
            telemetry.update();
        }
        leftFront.setPower(0);
        leftBack.setPower(0);
        rightFront.setPower(0);
        rightBack.setPower(0);
    }

    public void strafe(int distance)
    {
        leftFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        int target =(int) (distance * MOTOR_TICK_COUNT / (circumference * 3));
        leftFront.setTargetPosition(target);
        leftBack.setTargetPosition(-target);
        rightFront.setTargetPosition(-target);
        rightBack.setTargetPosition(target);
        leftBack.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        leftFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightBack.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        leftFront.setPower(0.8);
        leftBack.setPower(0.8);
        rightFront.setPower(0.8);
        rightBack.setPower(0.8);
        while(leftFront.isBusy()&&rightFront.isBusy()){
            telemetry.addData("Status", "Walking using encoders");
            telemetry.update();
        }
        leftFront.setPower(0);
        leftBack.setPower(0);
        rightFront.setPower(0);
        rightBack.setPower(0);
    }

    public void strafeFast(int distance)
    {
        leftFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        int target =(int) (distance * MOTOR_TICK_COUNT / (circumference * 3));
        leftFront.setTargetPosition(target);
        leftBack.setTargetPosition(-target);
        rightFront.setTargetPosition(-target);
        rightBack.setTargetPosition(target);
        leftBack.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        leftFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightBack.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        leftFront.setPower(1);
        leftBack.setPower(1);
        rightFront.setPower(1);
        rightBack.setPower(1);
        while(leftFront.isBusy()&&rightFront.isBusy()){
            telemetry.addData("Status", "Walking using encoders");
            telemetry.update();
        }
        leftFront.setPower(0);
        leftBack.setPower(0);
        rightFront.setPower(0);
        rightBack.setPower(0);
    }

    public void run(int distance) {
        leftFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        int target =(int) (distance * MOTOR_TICK_COUNT / (circumference * 3));
        leftFront.setTargetPosition(target);
        leftBack.setTargetPosition(target);
        rightFront.setTargetPosition(target);
        rightBack.setTargetPosition(target);
        leftFront.setPower(1);
        leftBack.setPower(1);
        rightFront.setPower(1);
        rightBack.setPower(1);
        leftBack.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        leftFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightBack.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        while(leftFront.isBusy()&&rightFront.isBusy()){
            telemetry.addData("Status", "Walking using encoders");
            telemetry.update();
        }
        leftFront.setPower(0);
        leftBack.setPower(0);
        rightFront.setPower(0);
        rightBack.setPower(0);
    }

    /**
     *  Method to drive on a fixed compass bearing (angle), based on encoder counts.
     *  Move will stop if either of these conditions occur:
     *  1) Move gets to the desired position
     *  2) Driver stops the opmode running.
     *
     * @param speed      Target speed for forward motion.  Should allow for _/- variance for adjusting heading
     * @param distance   Distance (in inches) to move from current position.  Negative distance means move backwards.
     * @param angle      Absolute Angle (in Degrees) relative to last gyro reset.
     *                   0 = fwd. +ve is CCW from fwd. -ve is CW from forward.
     *                   If a relative angle is required, add/subtract from current heading.
     */
    public void gyroDrive ( double speed,
                            double distance,
                            double angle) {

        int     newLeftTarget;
        int     newRightTarget;
        int     moveCounts;
        double  max;
        double  error;
        double  steer;
        double  leftSpeed;
        double  rightSpeed;

        // Ensure that the opmode is still active
        if (opModeIsActive()) {

            // Determine new target position, and pass to motor controller
            moveCounts = (int)(distance * MOTOR_TICK_COUNT);
            newLeftTarget = leftFront.getCurrentPosition() + moveCounts;
            newRightTarget = rightFront.getCurrentPosition() + moveCounts;

            // Set Target and Turn On RUN_TO_POSITION
            leftFront.setTargetPosition(newLeftTarget);
            leftBack.setTargetPosition(newLeftTarget);
            rightFront.setTargetPosition(newRightTarget);
            rightBack.setTargetPosition(newRightTarget);

            leftFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            leftBack.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            rightFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            rightBack.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            // start motion.
            speed = Range.clip(Math.abs(speed), 0.0, 1.0);
            leftFront.setPower(speed);
            leftBack.setPower(speed);
            rightFront.setPower(speed);
            rightBack.setPower(speed);

            // keep looping while we are still active, and BOTH motors are running.
            while (opModeIsActive() &&
                    (leftFront.isBusy() && rightFront.isBusy())) {

                // adjust relative speed based on heading error.
                error = getError(angle);
                steer = getSteer(error, P_DRIVE_COEFF);

                // if driving in reverse, the motor correction also needs to be reversed
                if (distance < 0)
                    steer *= -1.0;

                leftSpeed = speed - steer;
                rightSpeed = speed + steer;

                // Normalize speeds if either one exceeds +/- 1.0;
                max = Math.max(Math.abs(leftSpeed), Math.abs(rightSpeed));
                if (max > 1.0)
                {
                    leftSpeed /= max;
                    rightSpeed /= max;
                }

                leftFront.setPower(leftSpeed);
                leftBack.setPower(leftSpeed);
                rightFront.setPower(rightSpeed);
                rightBack.setPower(rightSpeed);

                // Display drive status for the driver.
                telemetry.addData("Err/St",  "%5.1f/%5.1f",  error, steer);
                telemetry.addData("Target",  "%7d:%7d",      newLeftTarget,  newRightTarget);
                telemetry.addData("Actual",  "%7d:%7d",      leftFront.getCurrentPosition(),
                        rightFront.getCurrentPosition());
                telemetry.addData("Speed",   "%5.2f:%5.2f",  leftSpeed, rightSpeed);
                telemetry.update();
            }

            // Stop all motion;
            leftFront.setPower(0);
            leftBack.setPower(0);
            rightFront.setPower(0);
            rightBack.setPower(0);

            // Turn off RUN_TO_POSITION
            leftFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            leftBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            rightFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            rightBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }
    }

    /**
     *  Method to spin on central axis to point in a new direction.
     *  Move will stop if either of these conditions occur:
     *  1) Move gets to the heading (angle)
     *  2) Driver stops the opmode running.
     *
     * @param speed Desired speed of turn.
     * @param angle      Absolute Angle (in Degrees) relative to last gyro reset.
     *                   0 = fwd. +ve is CCW from fwd. -ve is CW from forward.
     *                   If a relative angle is required, add/subtract from current heading.
     */
    public void gyroTurn (  double speed, double angle) {

        // keep looping while we are still active, and not on heading.
        while (opModeIsActive() && !onHeading(speed, angle, P_TURN_COEFF)) {
            // Update telemetry & Allow time for other processes to run.
            telemetry.update();
        }
    }

    /**
     *  Method to obtain & hold a heading for a finite amount of time
     *  Move will stop once the requested time has elapsed
     *
     * @param speed      Desired speed of turn.
     * @param angle      Absolute Angle (in Degrees) relative to last gyro reset.
     *                   0 = fwd. +ve is CCW from fwd. -ve is CW from forward.
     *                   If a relative angle is required, add/subtract from current heading.
     * @param holdTime   Length of time (in seconds) to hold the specified heading.
     */
    public void gyroHold( double speed, double angle, double holdTime) {

        ElapsedTime holdTimer = new ElapsedTime();

        // keep looping while we have time remaining.
        holdTimer.reset();
        while (opModeIsActive() && (holdTimer.time() < holdTime)) {
            // Update telemetry & Allow time for other processes to run.
            onHeading(speed, angle, P_TURN_COEFF);
            telemetry.update();
        }

        // Stop all motion;
        leftFront.setPower(0);
        leftBack.setPower(0);
        rightFront.setPower(0);
        rightBack.setPower(0);
    }

    /**
     * Perform one cycle of closed loop heading control.
     *
     * @param speed     Desired speed of turn.
     * @param angle     Absolute Angle (in Degrees) relative to last gyro reset.
     *                  0 = fwd. +ve is CCW from fwd. -ve is CW from forward.
     *                  If a relative angle is required, add/subtract from current heading.
     * @param PCoeff    Proportional Gain coefficient
     * @return
     */
    boolean onHeading(double speed, double angle, double PCoeff) {
        double   error ;
        double   steer ;
        boolean  onTarget = false ;
        double leftSpeed;
        double rightSpeed;

        // determine turn power based on +/- error
        error = AngleUnit.normalizeDegrees(angle - getHeading());

        if (Math.abs(error) <= HEADING_THRESHOLD) {
            steer = 0.0;
            leftSpeed  = 0.0;
            rightSpeed = 0.0;
            onTarget = true;
        }
        else {
            steer = getSteer(error, PCoeff);
            rightSpeed  = speed * steer;
            leftSpeed   = -rightSpeed;
        }

        // Send desired speeds to motors.
        leftFront.setPower(-leftSpeed);
        leftBack.setPower(-leftSpeed);
        rightFront.setPower(-rightSpeed);
        rightBack.setPower(-rightSpeed);

        // Display it for the driver.
        telemetry.addData("Heading", getHeading());
        telemetry.addData("Target", "%5.2f", angle);
        telemetry.addData("Err/St", "%5.2f/%5.2f", error, steer);
        telemetry.addData("Speed.", "%5.2f:%5.2f", leftSpeed, rightSpeed);

        return onTarget;
    }

    /**
     * getError determines the error between the target angle and the robot's current heading
     * @param   targetAngle  Desired angle (relative to global reference established at last Gyro Reset).
     * @return  error angle: Degrees in the range +/- 180. Centered on the robot's frame of reference
     *          +ve error means the robot should turn LEFT (CCW) to reduce error.
     */
    public double getError(double targetAngle) {

        double robotError;

        // calculate error in -179 to +180 range  (
        robotError = targetAngle - getHeading();
        while (robotError > 180)  robotError -= 360;
        while (robotError <= -180) robotError += 360;
        return robotError;
    }

    /**
     * returns desired steering force.  +/- 1 range.  +ve = steer left
     * @param error   Error angle in robot relative degrees
     * @param PCoeff  Proportional Gain Coefficient
     * @return
     */
    public double getSteer(double error, double PCoeff) {
        return Range.clip(error * PCoeff, -1, 1);
    }

}