package org.firstinspires.ftc.teamcode.autonom;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;
import org.firstinspires.ftc.teamcode.drive.DriveConstants;
import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.hardware.Config;
import org.firstinspires.ftc.teamcode.hardware.Mugurel;

import java.util.List;

//@Disabled
@Autonomous(name = "Autonom rata albastru", group = "autonom")
public class AutonomRataBlue extends LinearOpMode {

    private static final String TFOD_MODEL_ASSET = "/sdcard/FIRST/tflitemodels/modelorange.tflite";
    private static final String[] LABELS = {
            "Team Marker"
    };

    private static final String VUFORIA_KEY = Config.VuforiaKey;
    private VuforiaLocalizer vuforia;
    private TFObjectDetector tfod;
    enum Locations {Top, Middle, Bottom};
    private Locations teamMarkerLocation = null;
    private double confidence;
    private Mugurel robot;

    @Override
    public void runOpMode() throws InterruptedException {
        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);
        robot = new Mugurel(hardwareMap);

        Pose2d startPosition = new Pose2d(-35.5, 62, Math.toRadians(270));

        drive.setPoseEstimate(startPosition);

        initVuforia();
        initTfod();

        if (tfod != null) {
            tfod.activate();
            tfod.setZoom(1.0, 16.0/9.0);
        }


        while (!isStarted()) {

            if (tfod != null) {
                List<Recognition> updatedRecognitions = tfod.getUpdatedRecognitions();
                if (updatedRecognitions != null) {
                    telemetry.addData("# Object(s) Detected", updatedRecognitions.size());

                    confidence = 0.0;
                    int i = 0;
                    teamMarkerLocation = Locations.Top;
                    for (Recognition recognition : updatedRecognitions) {
                        if (recognition.getLabel() == "Team Marker") {
                            telemetry.addData("Team Marker:", "Detected");
                            if(((recognition.getBottom() - recognition.getTop())) < (recognition.getImageHeight() * 0.8)) {
                                if (((recognition.getRight() - recognition.getLeft()) / 2) + recognition.getLeft() < (recognition.getImageWidth() / 3) && recognition.getConfidence() > confidence) {
                                    teamMarkerLocation = Locations.Top;
                                    confidence = recognition.getConfidence();
                                }
                                else if (((recognition.getRight() - recognition.getLeft()) / 2) + recognition.getLeft() < (recognition.getImageWidth() * 2 / 3) && recognition.getConfidence() > confidence) {
                                    teamMarkerLocation = Locations.Middle;
                                    confidence = recognition.getConfidence();
                                }
                                else if (((recognition.getRight() - recognition.getLeft()) / 2) + recognition.getLeft() >= (recognition.getImageWidth() * 2 / 3) && recognition.getConfidence() > confidence) {
                                    teamMarkerLocation = Locations.Bottom;
                                    confidence = recognition.getConfidence();
                                }
                            }
                            break;
                        }
                        if (recognition.getLabel() != "Team Marker")
                            telemetry.addData("Team Marker:", "Not detected");
                        i++;
                    }
                    telemetry.addData("Marker Location:", teamMarkerLocation.toString());
                    if(teamMarkerLocation != Locations.Top) { confidence = confidence * 100; telemetry.addData("Localization Confidence:", (int) confidence); }
                    telemetry.update();
                }
            }
        }
        if(teamMarkerLocation == Locations.Top) {
            Trajectory cube = drive.trajectoryBuilder(new Pose2d(-35.5, 62, Math.toRadians(270)))
                    .lineToLinearHeading(new Pose2d(-30, 40, Math.toRadians(320)))
                    .build();

            Trajectory duck = drive.trajectoryBuilder(cube.end())
                    .addTemporalMarker(0.5, () -> robot.cupa.toggleCupa())
                    .addTemporalMarker(1, () -> robot.glisiere.setToPosition(0))
                    .splineToSplineHeading(new Pose2d(-57, 55, Math.toRadians(100)), Math.toRadians(100), SampleMecanumDrive.getVelocityConstraint(15, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH), SampleMecanumDrive.getAccelerationConstraint(DriveConstants.MAX_ACCEL))
                    .build();

            Trajectory finish = drive.trajectoryBuilder(duck.end())
                    .splineToLinearHeading(new Pose2d(-60, 35, Math.toRadians(90)), Math.toRadians(90), SampleMecanumDrive.getVelocityConstraint(25, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH), SampleMecanumDrive.getAccelerationConstraint(DriveConstants.MAX_ACCEL))
                    .build();


            robot.brat.goToPosition(1600);
            sleep(1000);
            robot.cupa.toggleDeget();
            robot.glisiere.setToPosition(4);
            sleep(1000);
            robot.cupa.toggleCupa();
            drive.followTrajectory(cube);
            sleep(500);
            robot.cupa.toggleDeget();
            sleep(1000);
            drive.followTrajectory(duck);
            robot.rata.motor.setPower(-0.6);
            sleep(3000);
            drive.followTrajectory(finish);
        } else if(teamMarkerLocation == Locations.Middle) {

            Trajectory cube = drive.trajectoryBuilder(new Pose2d(-35.5, 62, Math.toRadians(270)))
                    .lineToLinearHeading(new Pose2d(-26, 37, Math.toRadians(325)))
                    .build();

            Trajectory duck = drive.trajectoryBuilder(cube.end())
                    .addTemporalMarker(0.5, () -> robot.cupa.toggleCupa())
                    .addTemporalMarker(1, () -> robot.glisiere.setToPosition(0))
                    .splineToConstantHeading(new Vector2d(-30, 37), Math.toRadians(100), SampleMecanumDrive.getVelocityConstraint(30, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH), SampleMecanumDrive.getAccelerationConstraint(DriveConstants.MAX_ACCEL))
                    .splineToSplineHeading(new Pose2d(-57, 55, Math.toRadians(100)), Math.toRadians(100), SampleMecanumDrive.getVelocityConstraint(15, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH), SampleMecanumDrive.getAccelerationConstraint(DriveConstants.MAX_ACCEL))
                    .build();

            Trajectory finish = drive.trajectoryBuilder(duck.end())
                    .splineToLinearHeading(new Pose2d(-60, 35, Math.toRadians(90)), Math.toRadians(90), SampleMecanumDrive.getVelocityConstraint(25, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH), SampleMecanumDrive.getAccelerationConstraint(DriveConstants.MAX_ACCEL))
                    .build();


            waitForStart();

            robot.brat.goToPosition(1600);
            sleep(1000);
            robot.cupa.toggleDeget();
            robot.glisiere.setToPosition(2);
            sleep(1000);
            robot.cupa.toggleCupa();
            drive.followTrajectory(cube);
            sleep(500);
            robot.cupa.toggleDeget();
            sleep(1000);
            drive.followTrajectory(duck);
            robot.rata.motor.setPower(-0.6);
            sleep(3000);
            drive.followTrajectory(finish);
        } else {

            Trajectory cube = drive.trajectoryBuilder(new Pose2d(-35.5, 62, Math.toRadians(270)))
                    .lineToLinearHeading(new Pose2d(-24, 36, Math.toRadians(323)), SampleMecanumDrive.getVelocityConstraint(25, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH), SampleMecanumDrive.getAccelerationConstraint(DriveConstants.MAX_ACCEL))
                    .build();

            Trajectory duck = drive.trajectoryBuilder(cube.end())
                    .addTemporalMarker(0.3, () -> robot.glisiere.setToPosition(2))
                    .addTemporalMarker(0.7, () -> robot.cupa.servo.setPosition(0.97))
                    .addTemporalMarker(1.1, () -> robot.glisiere.setToPosition(0))
                    .splineToConstantHeading(new Vector2d(-30, 37), Math.toRadians(100), SampleMecanumDrive.getVelocityConstraint(30, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH), SampleMecanumDrive.getAccelerationConstraint(DriveConstants.MAX_ACCEL))
                    .splineToSplineHeading(new Pose2d(-57, 55, Math.toRadians(100)), Math.toRadians(100), SampleMecanumDrive.getVelocityConstraint(15, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH), SampleMecanumDrive.getAccelerationConstraint(DriveConstants.MAX_ACCEL))
                    .build();

            Trajectory finish = drive.trajectoryBuilder(duck.end())
                    .splineToLinearHeading(new Pose2d(-60, 35, Math.toRadians(90)), Math.toRadians(90), SampleMecanumDrive.getVelocityConstraint(25, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH), SampleMecanumDrive.getAccelerationConstraint(DriveConstants.MAX_ACCEL))
                    .build();


            robot.brat.goToPosition(1600);
            sleep(1000);
            robot.cupa.toggleDeget();
            robot.glisiere.setToPosition(2);
            sleep(1000);
            robot.cupa.servo.setPosition(0.5);
            sleep(1000);
            robot.glisiere.setToPosition(1);
            sleep(1000);
            drive.followTrajectory(cube);
            sleep(500);
            robot.cupa.toggleDegetMore();
            sleep(1000);
            drive.followTrajectory(duck);
            robot.rata.motor.setPower(-0.6);
            sleep(3000);
            drive.followTrajectory(finish);
        }
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
        tfodParameters.minResultConfidence = 0.5f;
        tfodParameters.isModelTensorFlow2 = true;
        tfodParameters.inputSize = 320;
        tfodParameters.useObjectTracker = false;
        tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia);
        tfod.loadModelFromFile(TFOD_MODEL_ASSET, LABELS);
    }

    private double inchToM(double inch) {
        return inch * 0.0254;
    }

    public void score_rata() {
        robot.rata.motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.rata.motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        final double carousel_radius = inchToM(7.5);  // meters
        final double carousel_circumference = 2.0 * Math.PI * carousel_radius;

        final double wheel_radius = inchToM(1.4173);   // meters
        final double wheel_circumference = 2.0 * Math.PI * wheel_radius;

        final double needed_length = carousel_circumference + inchToM(2.0);

        final double needed_revolutions = needed_length / wheel_circumference;
        final double TICKS_PER_REVOLUTION = robot.rata.motor.getMotorType().getTicksPerRev();
        final double MAX_VELOCITY = 3000;

        final double needed_ticks = needed_revolutions * TICKS_PER_REVOLUTION;

        final double duck_position = inchToM(3.0);   // meters

        final double miu = 0.38;     // coef frecare
        final double ff = miu * 9.81;   // forta frecare
        final double ff2 = ff * ff;

        final double vel_rap = wheel_radius / carousel_radius;
        final double motor_to_vel = 1.0 / TICKS_PER_REVOLUTION * vel_rap * duck_position;

        ElapsedTime timer = new ElapsedTime();
        timer.reset();

        double dt = 0.0001;
        double last_s = timer.seconds();

        while(robot.rata.motor.getCurrentPosition() < needed_ticks) {

            double now_velocity = robot.rata.motor.getVelocity();
            double now_time = timer.seconds();

            telemetry.addData("now_velocity", now_velocity);
            telemetry.addData("ticks", robot.rata.motor.getCurrentPosition());
            telemetry.addData("MAX_VELOCITY", MAX_VELOCITY);
            telemetry.addData("TICKS_PER_REVOLUTION", TICKS_PER_REVOLUTION);
            telemetry.addData("needed_revs", needed_revolutions);
            telemetry.update();

            double l = now_velocity, r = MAX_VELOCITY;
            for(int i = 0; i < 20; i++) {
                double m = (l + r) / 2.0;
                double tm = timer.seconds();
                double v = m;

                double last_v = now_velocity * motor_to_vel;
                double now_v = v * motor_to_vel;
//                double acc = (now_v - last_v) / (tm - now_time);
                double acc = (now_v - last_v) / dt;

                telemetry.addData("now_velocity", now_velocity);
                telemetry.addData("ticks", robot.rata.motor.getCurrentPosition());
                telemetry.addData("MAX_VELOCITY", MAX_VELOCITY);
                telemetry.addData("TICKS_PER_REVOLUTION", TICKS_PER_REVOLUTION);
                telemetry.addData("needed_revs", needed_revolutions);
                telemetry.addData("acc", acc);
                telemetry.addData("dt", dt);
                telemetry.update();

                double inert = acc;
                double cntrf = now_v * now_v / duck_position;

                double force = inert * inert + cntrf * cntrf;

                if(force < ff2)
                    l = m;
                else
                    r = m;
            }

            robot.rata.motor.setVelocity(l);
//            motor.setVelocity(MAX_VELOCITY);
            double now_s = timer.seconds();
            dt = now_s - last_s;
            last_s = now_s;
        }
        robot.rata.motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }
}