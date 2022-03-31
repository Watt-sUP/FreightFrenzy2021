package org.firstinspires.ftc.teamcode.autonom;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;
import org.firstinspires.ftc.teamcode.drive.DriveConstants;
import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.hardware.Config;
import org.firstinspires.ftc.teamcode.hardware.Mugurel;
import org.firstinspires.ftc.teamcode.trajectorysequence.TrajectorySequence;

import java.util.List;

@Disabled
@Autonomous(name = "Autonom cub rosu", group = "autonom")
public class AutonomCubRed extends LinearOpMode {
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

    @Override
    public void runOpMode() throws InterruptedException {
        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);
        Mugurel robot = new Mugurel(hardwareMap);

        Pose2d startPose = new Pose2d(12, -63, Math.toRadians(90));

        drive.setPoseEstimate(startPose);

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
                    teamMarkerLocation = Locations.Bottom;
                    for (Recognition recognition : updatedRecognitions) {
                        if (recognition.getLabel() == "Team Marker") {
                            telemetry.addData("Team Marker:", "Detected");
                            if(((recognition.getBottom() - recognition.getTop())) < (recognition.getImageHeight() * 0.8)) {
                                if (((recognition.getRight() - recognition.getLeft()) / 2) + recognition.getLeft() < (recognition.getImageWidth() / 3) && recognition.getConfidence() > confidence) {
                                    teamMarkerLocation = Locations.Bottom;
                                    confidence = recognition.getConfidence();
                                }
                                else if (((recognition.getRight() - recognition.getLeft()) / 2) + recognition.getLeft() < (recognition.getImageWidth() * 2 / 3) && recognition.getConfidence() > confidence) {
                                    teamMarkerLocation = Locations.Middle;
                                    confidence = recognition.getConfidence();
                                }
                                else if (((recognition.getRight() - recognition.getLeft()) / 2) + recognition.getLeft() >= (recognition.getImageWidth() * 2 / 3) && recognition.getConfidence() > confidence) {
                                    teamMarkerLocation = Locations.Top;
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
                    if(teamMarkerLocation != Locations.Bottom) { confidence = confidence * 100; telemetry.addData("Localization Confidence:", (int) confidence); }
                    telemetry.update();
                }
            }
        }

        if(teamMarkerLocation == Locations.Top) {

            //Pose2d exit = new Pose2d(12, -66, Math.toRadians(180));
            Trajectory deliveryFirst = drive.trajectoryBuilder(startPose)
                    .addDisplacementMarker(() -> {
                        robot.cupa.toggleDeget();
                        robot.glisiere.setToPosition(4);
                    })
                    .addTemporalMarker(0.5, () -> robot.cupa.servo.setPosition(0.5))
                    .lineToLinearHeading(new Pose2d(8, -45, Math.toRadians(125)))
                    .addDisplacementMarker(() -> robot.cupa.toggleDeget())
                    .build();


            Trajectory supply = drive.trajectoryBuilder(deliveryFirst.end())
                    .addTemporalMarker(0.5, () -> robot.cupa.servo.setPosition(0.97))
                    .addTemporalMarker(1, () -> robot.glisiere.setToPosition(0))
                    .lineToLinearHeading(new Pose2d(12, -66, Math.toRadians(180)))
                    .build();

            Trajectory move = drive.trajectoryBuilder(supply.end())
                    .addDisplacementMarker(() -> robot.maturica.toggleCollect())
                    .back(35)
                    .build();


            Trajectory delivery = drive.trajectoryBuilder(move.end())
                    .addDisplacementMarker(() -> {
                        robot.cupa.toggleDeget();
                        robot.glisiere.setToPosition(4);
                    })
                    .addTemporalMarker(0.3, () -> robot.maturica.toggleEject())
                    .addTemporalMarker(0.5, () -> robot.cupa.servo.setPosition(0.5))
                    .forward(35)
                    .splineTo(new Vector2d(8, -45), Math.toRadians(125))
                    .addDisplacementMarker(() -> robot.cupa.toggleDeget())
                    .build();


            drive.followTrajectory(deliveryFirst);
            sleep(1000);
            drive.followTrajectory(supply);
            sleep(1000);
            drive.followTrajectory(delivery);
            sleep(1000);
            drive.followTrajectory(supply);
            sleep(1000);
            drive.followTrajectory(delivery);
            sleep(1000);
            drive.followTrajectory(supply);
            sleep(1000);
            drive.followTrajectory(delivery);
            sleep(1000);
            drive.followTrajectory(supply);
        } else if(teamMarkerLocation == Locations.Middle) {
            Trajectory deliveryFirst = drive.trajectoryBuilder(startPose)
                    .lineToLinearHeading(new Pose2d(5, -40, Math.toRadians(130)))
                    .build();

            Trajectory supplyFirst = drive.trajectoryBuilder(deliveryFirst.end())
                    .addTemporalMarker(0.5, () -> robot.cupa.servo.setPosition(0.97))
                    .addTemporalMarker(1, () -> robot.glisiere.setToPosition(0))
                    .lineToLinearHeading(new Pose2d(12, -66, Math.toRadians(180)))
                    .build();

            Trajectory delivery = drive.trajectoryBuilder(supplyFirst.end())
                    .lineToLinearHeading(new Pose2d(8, -50, Math.toRadians(125)))
                    .build();


            Trajectory supply = drive.trajectoryBuilder(delivery.end())
                    .addTemporalMarker(0.5, () -> robot.cupa.servo.setPosition(0.97))
                    .addTemporalMarker(1, () -> robot.glisiere.setToPosition(0))
                    .lineToLinearHeading(new Pose2d(12, -66, Math.toRadians(180)))
                    .build();

            Trajectory move = drive.trajectoryBuilder(supply.end())
                    .back(35)
                    .build();

            Trajectory returnToOrigin = drive.trajectoryBuilder(move.end())
                    .addTemporalMarker(0.5, () -> robot.maturica.toggleEject())
                    .forward(35)
                    .build();


            robot.cupa.toggleDeget();
            robot.glisiere.setToPosition(2);
            sleep(500);
            robot.cupa.servo.setPosition(0.6);
            drive.followTrajectory(deliveryFirst);
            robot.cupa.toggleDeget();
            sleep(300);
            robot.maturica.toggleCollect();
            drive.followTrajectory(supplyFirst);
            drive.followTrajectory(move);
            robot.cupa.toggleDeget();
            robot.glisiere.setToPosition(4);
            sleep(500);
            robot.cupa.servo.setPosition(0.5);
            drive.followTrajectory(returnToOrigin);
            drive.followTrajectory(delivery);
            robot.cupa.toggleDeget();
            sleep(300);
            robot.maturica.toggleCollect();
            drive.followTrajectory(supply);
            drive.followTrajectory(move);
            robot.cupa.toggleDeget();
            robot.glisiere.setToPosition(4);
            sleep(500);
            robot.cupa.servo.setPosition(0.5);
            drive.followTrajectory(returnToOrigin);
            drive.followTrajectory(delivery);
            robot.cupa.toggleDeget();
            sleep(300);
            robot.maturica.toggleCollect();
            drive.followTrajectory(supply);
            drive.followTrajectory(move);
            robot.cupa.toggleDeget();
            robot.glisiere.setToPosition(4);
            sleep(500);
            robot.cupa.servo.setPosition(0.5);
            drive.followTrajectory(returnToOrigin);
            drive.followTrajectory(delivery);
            robot.cupa.toggleDeget();
            sleep(300);
            robot.maturica.toggleCollect();
            drive.followTrajectory(supply);
            drive.followTrajectory(move);

        } else {

            Trajectory deliveryFirst = drive.trajectoryBuilder(startPose)
                    .lineToLinearHeading(new Pose2d(4, -35, Math.toRadians(145)))
                    .build();

            Trajectory supplyFirst = drive.trajectoryBuilder(deliveryFirst.end())
                    .addTemporalMarker(0.1, () -> robot.glisiere.setToPosition(2))
                    .addTemporalMarker(0.4, () -> robot.cupa.servo.setPosition(0.97))
                    .addTemporalMarker(0.8, () -> robot.glisiere.setToPosition(0))
                    .lineToLinearHeading(new Pose2d(12, -66, Math.toRadians(180)))
                    .build();

            Trajectory delivery = drive.trajectoryBuilder(startPose)
                    .lineToLinearHeading(new Pose2d(8, -45, Math.toRadians(130)))
                    .build();


            Trajectory supply = drive.trajectoryBuilder(delivery.end())
                    .addTemporalMarker(0.5, () -> robot.cupa.servo.setPosition(0.97))
                    .addTemporalMarker(0.7, () -> robot.glisiere.setToPosition(0))
                    .lineToLinearHeading(new Pose2d(12, -66, Math.toRadians(180)))
                    .build();

            Trajectory move = drive.trajectoryBuilder(supply.end())
                    .back(35)
                    .build();

            Trajectory returnToOrigin = drive.trajectoryBuilder(move.end())
                    .addTemporalMarker(0.5, () -> robot.maturica.toggleEject())
                    .forward(35)
                    .build();

            /*
            robot.brat.goToPosition(1600);
            sleep(1000);

             */
            robot.cupa.toggleDeget();
            robot.glisiere.setToPosition(2);
            sleep(500);
            robot.cupa.servo.setPosition(0.5);
            sleep(300);
            robot.glisiere.setToPosition(1);
            drive.followTrajectory(deliveryFirst);
            robot.cupa.toggleDeget();
            sleep(300);
            robot.maturica.toggleCollect();
            drive.followTrajectory(supplyFirst);
            drive.followTrajectory(move);
            robot.cupa.toggleDeget();
            robot.glisiere.setToPosition(3);
            sleep(500);
            robot.cupa.servo.setPosition(0.5);
            drive.followTrajectory(returnToOrigin);
            drive.followTrajectory(delivery);
            robot.cupa.toggleDeget();
            sleep(300);
            robot.maturica.toggleCollect();
            drive.followTrajectory(supply);
            drive.followTrajectory(move);
            robot.cupa.toggleDeget();
            robot.glisiere.setToPosition(3);
            sleep(500);
            robot.cupa.servo.setPosition(0.5);
            drive.followTrajectory(returnToOrigin);
            drive.followTrajectory(delivery);
            robot.cupa.toggleDeget();
            sleep(300);
            robot.maturica.toggleCollect();
            drive.followTrajectory(supply);
            drive.followTrajectory(move);
            robot.cupa.toggleDeget();
            robot.glisiere.setToPosition(3);
            sleep(500);
            robot.cupa.servo.setPosition(0.5);
            drive.followTrajectory(returnToOrigin);
            drive.followTrajectory(delivery);
            robot.cupa.toggleDeget();
            sleep(300);
            robot.maturica.toggleCollect();
            drive.followTrajectory(supply);
            drive.followTrajectory(move);
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
        tfodParameters.minResultConfidence = 0.9f;
        tfodParameters.isModelTensorFlow2 = true;
        tfodParameters.inputSize = 320;
        tfodParameters.useObjectTracker = false;
        tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia);
        tfod.loadModelFromFile(TFOD_MODEL_ASSET, LABELS);
    }
}