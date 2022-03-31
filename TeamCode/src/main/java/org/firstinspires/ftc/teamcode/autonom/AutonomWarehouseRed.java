package org.firstinspires.ftc.teamcode.autonom;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.acmerobotics.roadrunner.trajectory.constraints.TrajectoryAccelerationConstraint;
import com.acmerobotics.roadrunner.trajectory.constraints.TrajectoryVelocityConstraint;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;
import org.firstinspires.ftc.teamcode.drive.DriveConstants;
import org.firstinspires.ftc.teamcode.drive.SampleMecanumDriveCancelable;
import org.firstinspires.ftc.teamcode.hardware.Config;
import org.firstinspires.ftc.teamcode.hardware.Mugurel;
import org.firstinspires.ftc.teamcode.trajectorysequence.TrajectorySequence;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

//@Disabled
@Autonomous(name = "Autonom Warehouse Red", group = "autonom")
public class AutonomWarehouseRed extends LinearOpMode {

    private static final String TFOD_MODEL_ASSET = "/sdcard/FIRST/tflitemodels/modelorange3.tflite";
    private static final String[] LABELS = {
            "Team Marker"
    };

    private static final String VUFORIA_KEY = Config.VuforiaKey;
    private VuforiaLocalizer vuforia;
    private TFObjectDetector tfod;
    private int level = 1;
    private double confidence;

    SampleMecanumDriveCancelable drive;
    Mugurel robot;
    ElapsedTime timer;
    boolean is_parked;

    public TrajectoryVelocityConstraint velocityConstraint(double v) {
        return SampleMecanumDriveCancelable.getVelocityConstraint(v, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH);
    }

    public TrajectoryAccelerationConstraint accelerationConstraint() {
        return SampleMecanumDriveCancelable.getAccelerationConstraint(DriveConstants.MAX_ACCEL);
    }

    public TrajectoryAccelerationConstraint accelerationConstraint(double a) {
        return SampleMecanumDriveCancelable.getAccelerationConstraint(a);
    }

    public Trajectory delivery3, delivery2, delivery1;
    public TrajectorySequence supply3, supply2, supply1;
    public TrajectorySequence supply, park;
    public AtomicBoolean sensor_on;
    public double vv = 45;

    public void update_supply_trajectory(int cycle_id) {
//        supply = drive.trajectorySequenceBuilder(delivery.end())
//                .addTemporalMarker(0.5, () -> robot.cupa.collect())
//                .addTemporalMarker(1, () -> robot.glisiere.setToPosition(0))
//                .addTemporalMarker(1.5, () -> robot.maturica.collect())
//                .splineToLinearHeading(new Pose2d(15, -64.5, Math.toRadians(176)), Math.toRadians(0), velocityConstraint(25), accelerationConstraint())
//                .addDisplacementMarker(() -> sensor_on.set(true))
//                .lineToConstantHeading(new Vector2d(40, -67), velocityConstraint(vv), accelerationConstraint())
//                .lineToConstantHeading(new Vector2d(60, -67), velocityConstraint(7), accelerationConstraint())
//                .build();

        if(cycle_id <= 1)   return;

        telemetry.addData("Supply", "Updated");
        telemetry.update();

        int[] posX_cyc = {0, 50, 55, 57, 60};

        supply = drive.trajectorySequenceBuilder(drive.getPoseEstimate())
                .addTemporalMarker(0.7, () -> robot.cupa.collect())
                .addTemporalMarker(1.2, () -> robot.glisiere.setToPosition(0))
//                .addTemporalMarker(2, () -> robot.maturica.collect())
                .splineToLinearHeading(new Pose2d(15, -64.5, Math.toRadians(176)), Math.toRadians(0), velocityConstraint(27), accelerationConstraint())
//                .splineToSplineHeading(new Pose2d(15, -64.5, Math.toRadians(176)), Math.toRadians(0), velocityConstraint(27), accelerationConstraint())
                .addDisplacementMarker(() -> sensor_on.set(true))
                .addDisplacementMarker(() -> robot.maturica.collect())
                .lineToConstantHeading(new Vector2d(posX_cyc[cycle_id], -67), velocityConstraint(vv), accelerationConstraint(33))
//                .lineToConstantHeading(new Vector2d(60, -67), velocityConstraint(vv), accelerationConstraint(15))
//                .splineToSplineHeading(new Pose2d(46 + (cycle_id - 1) * 5, -67, Math.toRadians(176)), Math.toRadians(0), velocityConstraint(vv), accelerationConstraint())
//                .lineToConstantHeading(new Vector2d(60, -67), velocityConstraint(7), accelerationConstraint())
                .build();

    }

    public void build_trajectories() {
        Pose2d startPose = new Pose2d(12, -63, Math.toRadians(90));
        drive.setPoseEstimate(startPose);

        sensor_on = new AtomicBoolean(false);


        delivery3 = drive.trajectoryBuilder(startPose)
                .addTemporalMarker(0.5, () -> robot.cupa.delivery34())
                .splineToLinearHeading(new Pose2d(3.5, -51, Math.toRadians(120)), Math.toRadians(120), velocityConstraint(vv), accelerationConstraint())
                .addDisplacementMarker(() -> robot.cupa.desface())
                .build();

        supply3 = drive.trajectorySequenceBuilder(delivery3.end())
                .addTemporalMarker(0.5, () -> robot.cupa.collect())
                .addTemporalMarker(1, () -> robot.glisiere.setToPosition(0))
//                .addTemporalMarker(2, () -> robot.maturica.collect())
                .splineToLinearHeading(new Pose2d(15, -64.5, Math.toRadians(176)), Math.toRadians(0), velocityConstraint(27), accelerationConstraint())
                .addDisplacementMarker(() -> sensor_on.set(true))
                .addDisplacementMarker(() -> robot.maturica.collect())
                .lineToConstantHeading(new Vector2d(50, -67), velocityConstraint(vv), accelerationConstraint(33))
                .build();

        delivery2 = drive.trajectoryBuilder(startPose)
                .addTemporalMarker(0.5, () -> robot.cupa.delivery2())
                .splineToLinearHeading(new Pose2d(0, -41, Math.toRadians(120)), Math.toRadians(120), velocityConstraint(20), accelerationConstraint())
                .addDisplacementMarker(() -> robot.cupa.toggleDeget())
                .build();

        supply2 = drive.trajectorySequenceBuilder(delivery2.end())
                .addTemporalMarker(1, () -> robot.cupa.collect())
                .addTemporalMarker(1.5, () -> robot.glisiere.setToPosition(0))
//                .addTemporalMarker(2, () -> robot.maturica.collect())
                .lineToLinearHeading(new Pose2d(14, -64.5, Math.toRadians(176)), velocityConstraint(26), accelerationConstraint())
                .addDisplacementMarker(() -> sensor_on.set(true))
                .addDisplacementMarker(() -> robot.maturica.collect())
                .lineToConstantHeading(new Vector2d(50, -67), velocityConstraint(vv), accelerationConstraint(33))
                .build();

        delivery1 = drive.trajectoryBuilder(startPose)
                .addTemporalMarker(0.7, () -> robot.cupa.servo.setPosition(0.6))
                .addTemporalMarker(1.3, () -> robot.glisiere.setToPosition(1))
                .lineToLinearHeading(new Pose2d(-12, -42.5, Math.toRadians(90)), velocityConstraint(25), accelerationConstraint())
//                .addDisplacementMarker(() -> robot.cupa.delivery2())
                .build();

        supply1 = drive.trajectorySequenceBuilder(delivery1.end())
                .addTemporalMarker(0.8, () -> robot.glisiere.setToPosition(2))
                .addTemporalMarker(1.2, () -> robot.cupa.collect())
                .addTemporalMarker(1.6, () -> robot.glisiere.setToPosition(0))
//                .addTemporalMarker(2, () -> robot.maturica.collect())
                .lineToLinearHeading(new Pose2d(14, -65, Math.toRadians(176)), velocityConstraint(26), accelerationConstraint())
                .addDisplacementMarker(() -> sensor_on.set(true))
                .addDisplacementMarker(() -> robot.maturica.collect())
                .waitSeconds(0.4)
                .lineToConstantHeading(new Vector2d(50, -67), velocityConstraint(vv), accelerationConstraint(33))
                .build();

//        update_supply_trajectory(0);
    }

    public void delivery_x() {
        robot.cupa.strange();

        if(level == 1)  {
            supply = supply1;
            robot.glisiere.setToPosition(2);
            drive.followTrajectory(delivery1);
            robot.cupa.servo.setPosition(0.5);
            sleep(300);
            robot.cupa.deget.setPosition(0.5);
        } else if(level == 2) {
            supply = supply2;
            robot.glisiere.setToPosition(2);
            sleep(500);
            drive.followTrajectory(delivery2);
        }

        if(level == 3)  {
            supply = supply3;
            robot.glisiere.setToPosition(4);
            sleep(500);
            drive.followTrajectory(delivery3);
        }
    }

    public void cycle(int id) {
        telemetry.addData("Cycle", id);
        telemetry.update();

        update_supply_trajectory(id);

        drive.followTrajectorySequenceAsync(supply);
        while(drive.isBusy()) {
            drive.update();

            if(robot.distance.getDistance(DistanceUnit.CM) <= 5 && sensor_on.get()) {
                drive.breakFollowing();
                telemetry.addData("Break", "true");
                telemetry.update();
                break;
            }
        }
        sensor_on.set(false);

        if(robot.distance.getDistance(DistanceUnit.CM) > 5) {
            drive.turn(Math.toRadians(15));
            drive.turn(Math.toRadians(-15));
//            drive.turn(Math.toRadians(15));
//            drive.turn(Math.toRadians(-15));
        }
        robot.cupa.strange();

        if(timer.seconds() > 24)    {
            is_parked = true;
            return;
        }

        sleep(200);
        robot.glisiere.setToPosition(4);

        double deg_distance = Math.toDegrees(Math.toRadians(180) - drive.getPoseEstimate().getHeading());
        if( Math.abs(deg_distance) > 7 )
            drive.turn(Math.toRadians(deg_distance));

        Trajectory delivery_brk = drive.trajectoryBuilder(drive.getPoseEstimate())
//                .lineToLinearHeading(new Pose2d(drive.getPoseEstimate().getX(), drive.getPoseEstimate().getY(), Math.toRadians(180)))
                .addTemporalMarker(0.3, () -> robot.maturica.eject())
                .addTemporalMarker(0.7, () -> robot.cupa.delivery34())
                .addTemporalMarker(1.75, () -> robot.maturica.stop())
//                .splineTo(new Vector2d(15, -65), 0, velocityConstraint(vv), accelerationConstraint())
//                .splineToLinearHeading(new Pose2d(3.5, -51, Math.toRadians(120 - 2.5 * id)), Math.toRadians(120), velocityConstraint(vv), accelerationConstraint())
                .splineToSplineHeading(new Pose2d(15, -66, Math.toRadians(184)), Math.toRadians(180), velocityConstraint(vv), accelerationConstraint())
                .splineToSplineHeading(new Pose2d(3.5, -51, Math.toRadians(120 - 2.5 * id)), Math.toRadians(120), velocityConstraint(vv), accelerationConstraint())
                .build();

        drive.followTrajectory(delivery_brk);
        robot.cupa.desface();
        sleep(300);
    }

    public void park_robot() {
        if(is_parked)   return;
        park = drive.trajectorySequenceBuilder(drive.getPoseEstimate())
                .addTemporalMarker(0.5, () -> robot.cupa.collect())
                .addTemporalMarker(1, () -> robot.glisiere.setToPosition(0))
                .splineToLinearHeading(new Pose2d(15, -63.5, Math.toRadians(176)), Math.toRadians(0), velocityConstraint(25), accelerationConstraint())
//                .addDisplacementMarker(() -> robot.maturica.collect())
                .addDisplacementMarker(() -> sensor_on.set(true))
                .lineTo(new Vector2d(48, -67))
//                .lineTo(new Vector2d(60, -67), velocityConstraint(7), accelerationConstraint())
                .build();

        drive.followTrajectorySequence(park);
    }

    @Override
    public void runOpMode() throws InterruptedException {
        is_parked = false;
        timer = new ElapsedTime();
        drive = new SampleMecanumDriveCancelable(hardwareMap);
        robot = new Mugurel(hardwareMap);
        robot.brat.autonomousInitPosition();

        build_trajectories();

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
                    level = 3;
                    for (Recognition recognition : updatedRecognitions) {
                        if (recognition.getLabel() == "Team Marker") {
                            telemetry.addData("Team Marker:", "Detected");
                            if(((recognition.getBottom() - recognition.getTop())) < (recognition.getImageHeight() * 0.8)) {
                                if (((recognition.getRight() - recognition.getLeft()) / 2) + recognition.getLeft() < (recognition.getImageWidth() / 3) && recognition.getConfidence() > confidence) {
                                    level = 1;
                                    confidence = recognition.getConfidence();
                                }
                                else if (((recognition.getRight() - recognition.getLeft()) / 2) + recognition.getLeft() < (recognition.getImageWidth() * 2 / 3) && recognition.getConfidence() > confidence) {
                                    level = 2;
                                    confidence = recognition.getConfidence();
                                }
                                else if (((recognition.getRight() - recognition.getLeft()) / 2) + recognition.getLeft() >= (recognition.getImageWidth() * 2 / 3) && recognition.getConfidence() > confidence) {
                                    level = 3;
                                    confidence = recognition.getConfidence();
                                }
                            }
                            break;
                        }
                        if (recognition.getLabel() != "Team Marker")
                            telemetry.addData("Team Marker:", "Not detected");
                        i++;
                    }
                    telemetry.addData("Marker Location:", level);
                    if(level != 1) { confidence = confidence * 100; telemetry.addData("Localization Confidence:", (int) confidence); }
                    telemetry.update();
                }
            }
        }

        timer.reset();

        delivery_x();
        sleep(200);

        cycle(1);
        cycle(2);
        cycle(3);

        park_robot();
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
        tfodParameters.minResultConfidence = 0.9f;
        tfodParameters.isModelTensorFlow2 = true;
        tfodParameters.inputSize = 320;
        tfodParameters.useObjectTracker = false;
        tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia);
        tfod.loadModelFromFile(TFOD_MODEL_ASSET, LABELS);
    }
}