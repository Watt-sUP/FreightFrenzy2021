package org.firstinspires.ftc.teamcode.autonom;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

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
@Autonomous(name = "Autonom rata rosu", group = "autonom")
public class AutonomRataRed extends LinearOpMode {

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

        Pose2d startPosition = new Pose2d(-35.5, -62, Math.toRadians(90));

        drive.setPoseEstimate(startPosition);

        initVuforia();
        initTfod();

        if (tfod != null) {
            tfod.activate();
            tfod.setZoom(1.0, 16.0 / 9.0);
        }

        int pos = 0;
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
            Trajectory cube = drive.trajectoryBuilder(new Pose2d(-35.5, -62, Math.toRadians(90)))
                    .lineToLinearHeading(new Pose2d(-30, -40, Math.toRadians(40)))
                    .build();

            Trajectory duck = drive.trajectoryBuilder(cube.end())
                    .addTemporalMarker(0.5, () -> robot.cupa.toggleCupa())
                    .addTemporalMarker(0.7, () -> robot.glisiere.setToPosition(0))
                    .lineToLinearHeading(new Pose2d(-65, -55, Math.toRadians(270)))
                    .build();

            Trajectory finish = drive.trajectoryBuilder(duck.end())
                    .lineTo(new Vector2d(-60, -34))
                    .build();


            robot.brat.goToPosition(1600);
            sleep(1000);
            robot.cupa.toggleDeget();
            robot.glisiere.setToPosition(3);
            sleep(1000);
            robot.cupa.toggleCupa();
            drive.followTrajectory(cube);
            sleep(500);
            robot.cupa.toggleDeget();
            sleep(1000);
            drive.followTrajectory(duck);
            robot.rata.motor.setPower(0.6);
            sleep(1000);
            robot.rata.motor.setPower(0.6);
            sleep(5000);
            drive.followTrajectory(finish);
        } else if(teamMarkerLocation == Locations.Middle) {

            Trajectory cube = drive.trajectoryBuilder(new Pose2d(-35.5, -62, Math.toRadians(90)))
                    .lineToLinearHeading(new Pose2d(-26, -37, Math.toRadians(35)))
                    .build();

            Trajectory duck = drive.trajectoryBuilder(cube.end())
                    .addTemporalMarker(0.3, () -> robot.cupa.toggleCupa())
                    .addTemporalMarker(0.7, () -> robot.glisiere.setToPosition(0))
                    .lineToLinearHeading(new Pose2d(-65, -55, Math.toRadians(270)))
                    .build();

            Trajectory finish = drive.trajectoryBuilder(duck.end())
                    .lineTo(new Vector2d(-60, -34))
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
            robot.rata.motor.setPower(0.6);
            sleep(1000);
            robot.rata.motor.setPower(0.6);
            sleep(5000);
            drive.followTrajectory(finish);
        } else {

            Trajectory cube = drive.trajectoryBuilder(new Pose2d(-35.5, -62, Math.toRadians(90)))
                    .lineToLinearHeading(new Pose2d(-24, -36, Math.toRadians(37)))
                    .build();

            Trajectory duck = drive.trajectoryBuilder(cube.end())
                    .addTemporalMarker(0.3, () -> robot.glisiere.setToPosition(2))
                    .addTemporalMarker(0.7, () -> robot.cupa.servo.setPosition(0.97))
                    .addTemporalMarker(1.1, () -> robot.glisiere.setToPosition(0))
                    .lineToLinearHeading(new Pose2d(-65, -55, Math.toRadians(270)), SampleMecanumDrive.getVelocityConstraint(15, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH), SampleMecanumDrive.getAccelerationConstraint(DriveConstants.MAX_ACCEL))
                    .build();

            Trajectory finish = drive.trajectoryBuilder(duck.end())
                    .lineTo(new Vector2d(-60, -34))
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
            robot.cupa.toggleDeget();
            sleep(1000);
            drive.followTrajectory(duck);
            robot.rata.motor.setPower(0.6);
            sleep(1000);
            robot.rata.motor.setPower(0.6);
            sleep(5000);
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
}