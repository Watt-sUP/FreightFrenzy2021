package org.firstinspires.ftc.teamcode.autonom;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.drive.DriveConstants;
import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.hardware.Mugurel;

//@Disabled
@Autonomous(name = "Autonom test 2", group = "autonom")
public class AU2 extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {
        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);
        Mugurel robot = new Mugurel(hardwareMap);

        Pose2d startPose = new Pose2d(13, -63, Math.toRadians(90));

        drive.setPoseEstimate(startPose);


        Trajectory delivery = drive.trajectoryBuilder(startPose)
                .addDisplacementMarker(() -> {
                    robot.cupa.toggleDeget();
                    robot.glisiere.setToPosition(4);
                })
                .addTemporalMarker(0.5, () -> robot.cupa.servo.setPosition(0.5))
                .splineTo(new Vector2d(-9, -50), Math.toRadians(93), SampleMecanumDrive.getVelocityConstraint(25, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH), SampleMecanumDrive.getAccelerationConstraint(DriveConstants.MAX_ACCEL))
                .addDisplacementMarker(() -> robot.cupa.toggleDeget())
                .build();


        Trajectory supply1 = drive.trajectoryBuilder(delivery.end())
                .addTemporalMarker(0.5, () -> robot.cupa.servo.setPosition(0.97))
                .addTemporalMarker(1, () -> robot.glisiere.setToPosition(0))
                .addTemporalMarker(1.5, () -> robot.maturica.toggleCollect())
                .splineToSplineHeading(new Pose2d(0, -60.5, Math.toRadians(180)), Math.toRadians(120), SampleMecanumDrive.getVelocityConstraint(25, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH), SampleMecanumDrive.getAccelerationConstraint(DriveConstants.MAX_ACCEL))
                .splineToConstantHeading(new Vector2d(10, -66), Math.toRadians(0), SampleMecanumDrive.getVelocityConstraint(25, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH), SampleMecanumDrive.getAccelerationConstraint(DriveConstants.MAX_ACCEL))
                .splineToConstantHeading(new Vector2d(45, -66), Math.toRadians(0), SampleMecanumDrive.getVelocityConstraint(25, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH), SampleMecanumDrive.getAccelerationConstraint(DriveConstants.MAX_ACCEL))
                .build();

        Trajectory supply2 = drive.trajectoryBuilder(delivery.end())
                .addTemporalMarker(0.5, () -> robot.cupa.servo.setPosition(0.97))
                .addTemporalMarker(1, () -> robot.glisiere.setToPosition(0))
                .addTemporalMarker(1.5, () -> robot.maturica.toggleCollect())
                .splineToSplineHeading(new Pose2d(0, -60.5, Math.toRadians(180)), Math.toRadians(120), SampleMecanumDrive.getVelocityConstraint(25, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH), SampleMecanumDrive.getAccelerationConstraint(DriveConstants.MAX_ACCEL))
                .splineToConstantHeading(new Vector2d(10, -66), Math.toRadians(0), SampleMecanumDrive.getVelocityConstraint(25, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH), SampleMecanumDrive.getAccelerationConstraint(DriveConstants.MAX_ACCEL))
                .splineToConstantHeading(new Vector2d(50, -66), Math.toRadians(0), SampleMecanumDrive.getVelocityConstraint(25, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH), SampleMecanumDrive.getAccelerationConstraint(DriveConstants.MAX_ACCEL))
                .build();

        Trajectory supply3 = drive.trajectoryBuilder(delivery.end())
                .addTemporalMarker(0.5, () -> robot.cupa.servo.setPosition(0.97))
                .addTemporalMarker(1, () -> robot.glisiere.setToPosition(0))
                .addTemporalMarker(1.5, () -> robot.maturica.toggleCollect())
                .splineToSplineHeading(new Pose2d(0, -60.5, Math.toRadians(180)), Math.toRadians(120), SampleMecanumDrive.getVelocityConstraint(25, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH), SampleMecanumDrive.getAccelerationConstraint(DriveConstants.MAX_ACCEL))
                .splineToConstantHeading(new Vector2d(10, -66), Math.toRadians(0), SampleMecanumDrive.getVelocityConstraint(25, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH), SampleMecanumDrive.getAccelerationConstraint(DriveConstants.MAX_ACCEL))
                .splineToConstantHeading(new Vector2d(55, -66), Math.toRadians(0), SampleMecanumDrive.getVelocityConstraint(25, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH), SampleMecanumDrive.getAccelerationConstraint(DriveConstants.MAX_ACCEL))
                .build();

        Trajectory reverse1 = drive.trajectoryBuilder(supply1.end())
                .addDisplacementMarker(() -> {
                    robot.cupa.toggleDeget();
                    robot.glisiere.setToPosition(4);
                })
                .addTemporalMarker(0.15, () -> robot.maturica.toggleEject())
                .addTemporalMarker(0.5, () -> robot.cupa.servo.setPosition(0.5))
                .splineToConstantHeading(new Vector2d(20, -66), Math.toRadians(180), SampleMecanumDrive.getVelocityConstraint(25, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH), SampleMecanumDrive.getAccelerationConstraint(DriveConstants.MAX_ACCEL))
                .splineTo(new Vector2d(-6, -50), Math.toRadians(93), SampleMecanumDrive.getVelocityConstraint(25, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH), SampleMecanumDrive.getAccelerationConstraint(DriveConstants.MAX_ACCEL))
                .addDisplacementMarker(() -> robot.cupa.toggleDeget())
                .build();

        Trajectory reverse2 = drive.trajectoryBuilder(supply2.end())
                .addDisplacementMarker(() -> {
                    robot.cupa.toggleDeget();
                    robot.glisiere.setToPosition(4);
                })
                .addTemporalMarker(0.15, () -> robot.maturica.toggleEject())
                .addTemporalMarker(0.5, () -> robot.cupa.servo.setPosition(0.5))
                .splineToConstantHeading(new Vector2d(20, -66), Math.toRadians(180), SampleMecanumDrive.getVelocityConstraint(25, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH), SampleMecanumDrive.getAccelerationConstraint(DriveConstants.MAX_ACCEL))
                .splineTo(new Vector2d(-6, -50), Math.toRadians(93), SampleMecanumDrive.getVelocityConstraint(25, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH), SampleMecanumDrive.getAccelerationConstraint(DriveConstants.MAX_ACCEL))
                .addDisplacementMarker(() -> robot.cupa.toggleDeget())
                .build();

        Trajectory reverse3 = drive.trajectoryBuilder(supply3.end())
                .addDisplacementMarker(() -> {
                    robot.cupa.toggleDeget();
                    robot.glisiere.setToPosition(4);
                })
                .addTemporalMarker(0.15, () -> robot.maturica.toggleEject())
                .addTemporalMarker(0.5, () -> robot.cupa.servo.setPosition(0.5))
                .splineToConstantHeading(new Vector2d(20, -66), Math.toRadians(180), SampleMecanumDrive.getVelocityConstraint(25, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH), SampleMecanumDrive.getAccelerationConstraint(DriveConstants.MAX_ACCEL))
                .splineTo(new Vector2d(-6, -50), Math.toRadians(93), SampleMecanumDrive.getVelocityConstraint(25, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH), SampleMecanumDrive.getAccelerationConstraint(DriveConstants.MAX_ACCEL))
                .addDisplacementMarker(() -> robot.cupa.toggleDeget())
                .build();

        while(!isStarted()) {
            telemetry.addData("Ready", "for play");
            telemetry.update();
        }

        drive.followTrajectory(delivery);
        sleep(1000);
        drive.followTrajectory(supply1);
        sleep(1000);
        drive.followTrajectory(reverse1);
        sleep(1000);
        drive.followTrajectory(supply2);
        sleep(1000);
        drive.followTrajectory(reverse2);
        sleep(1000);
        drive.followTrajectory(supply3);
    }
}