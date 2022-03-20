package org.firstinspires.ftc.teamcode.autonom;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.drive.DriveConstants;
import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.hardware.Mugurel;
import org.firstinspires.ftc.teamcode.trajectorysequence.TrajectorySequence;

//@Disabled
@Autonomous(name = "Autonom cub rosu", group = "autonom")
public class AutonomCubRed extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {
        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);
        Mugurel robot = new Mugurel(hardwareMap);

        Pose2d startPose = new Pose2d(12, -63, Math.toRadians(90));

        drive.setPoseEstimate(startPose);

        /*
        Cubul de sus
        */

        Trajectory delivery = drive.trajectoryBuilder(startPose)
                .splineToLinearHeading(new Pose2d(-12, -48, Math.toRadians(12)), Math.toRadians(45))
                .build();


        Trajectory supply = drive.trajectoryBuilder(delivery.end())
                .addTemporalMarker(0.5, () -> robot.cupa.servo.setPosition(0.97))
                .addTemporalMarker(0.7, () -> robot.glisiere.setToPosition(0))
                .splineToSplineHeading(new Pose2d(25, -65, Math.toRadians(180)), Math.toRadians(0))
                .splineTo(new Vector2d(55, -65.5), Math.toRadians(0))
                .build();

        Trajectory returnToDelivery = drive.trajectoryBuilder(supply.end(), true)
                .splineToSplineHeading(new Pose2d(25, -65, Math.toRadians(180)), Math.toRadians(0))
                .splineToLinearHeading(new Pose2d(-12, -48, Math.toRadians(12)), Math.toRadians(45))
                .build();


        waitForStart();

        robot.cupa.toggleDeget();
        robot.glisiere.setToPosition(3);
        sleep(500);
        robot.cupa.servo.setPosition(0.5);
        drive.followTrajectory(delivery);
        robot.cupa.toggleDeget();
        sleep(300);
        robot.maturica.toggleCollect();
        drive.followTrajectory(supply);
        robot.cupa.toggleDeget();
        robot.glisiere.setToPosition(3);
        sleep(500);
        robot.cupa.servo.setPosition(0.5);
        drive.followTrajectory(returnToDelivery);
        drive.followTrajectory(delivery);
        robot.cupa.toggleDeget();
        sleep(300);
        robot.maturica.toggleCollect();
        drive.followTrajectory(supply);
        robot.cupa.toggleDeget();
        robot.glisiere.setToPosition(3);
        sleep(500);
        robot.cupa.servo.setPosition(0.5);
        drive.followTrajectory(returnToDelivery);
        drive.followTrajectory(delivery);
        robot.cupa.toggleDeget();
        sleep(300);
        robot.maturica.toggleCollect();
        drive.followTrajectory(supply);
        robot.cupa.toggleDeget();
        robot.glisiere.setToPosition(3);
        sleep(500);
        robot.cupa.servo.setPosition(0.5);
        drive.followTrajectory(returnToDelivery);
        drive.followTrajectory(delivery);
        robot.cupa.toggleDeget();
        sleep(300);
        robot.maturica.toggleCollect();
        drive.followTrajectory(supply);
        /**/
        /*
        Cub mijloc

        Trajectory deliveryFirst = drive.trajectoryBuilder(startPose)
                .lineToLinearHeading(new Pose2d(5, -40, Math.toRadians(125)))
                .build();

        Trajectory supplyFirst = drive.trajectoryBuilder(deliveryFirst.end())
                .addTemporalMarker(0.5, () -> robot.cupa.servo.setPosition(0.97))
                .addTemporalMarker(0.7, () -> robot.glisiere.setToPosition(0))
                .lineToLinearHeading(new Pose2d(12, -66, Math.toRadians(180)))
                .build();

        Trajectory delivery = drive.trajectoryBuilder(startPose)
                .lineToLinearHeading(new Pose2d(8, -45, Math.toRadians(125)))
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

        waitForStart();

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

        */

        /*
        Cupa de jos



        Trajectory deliveryFirst = drive.trajectoryBuilder(startPose)
                .lineToLinearHeading(new Pose2d(4, -35, Math.toRadians(145)))
                .build();

        Trajectory supplyFirst = drive.trajectoryBuilder(deliveryFirst.end())
                .addTemporalMarker(0.1, () -> robot.glisiere.setToPosition(2))
                .addTemporalMarker(0.4, () -> robot.cupa.servo.setPosition(0.97))
                .addTemporalMarker(0.8, () -> robot.glisiere.setToPosition(0))
                .lineToLinearHeading(new Pose2d(12, -64, Math.toRadians(180)))
                .build();

        Trajectory delivery = drive.trajectoryBuilder(startPose)
                .lineToLinearHeading(new Pose2d(8, -45, Math.toRadians(125)))
                .build();


        Trajectory supply = drive.trajectoryBuilder(delivery.end())
                .addTemporalMarker(0.5, () -> robot.cupa.servo.setPosition(0.97))
                .addTemporalMarker(0.7, () -> robot.glisiere.setToPosition(0))
                .lineToLinearHeading(new Pose2d(12, -64, Math.toRadians(180)))
                .build();

        Trajectory move = drive.trajectoryBuilder(supply.end())
                .back(35)
                .build();

        Trajectory returnToOrigin = drive.trajectoryBuilder(move.end())
                .addTemporalMarker(0.5, () -> robot.maturica.toggleEject())
                .forward(35)
                .build();

        waitForStart();


        robot.brat.goToPosition(1600);
        sleep(1000);
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

         */
    }
}