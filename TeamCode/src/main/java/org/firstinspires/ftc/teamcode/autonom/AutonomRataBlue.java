package org.firstinspires.ftc.teamcode.autonom;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.drive.DriveConstants;
import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.hardware.Mugurel;

//@Disabled
@Autonomous(name = "Autonom rata albastru", group = "autonom")
public class AutonomRataBlue extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {
        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);
        Mugurel robot = new Mugurel(hardwareMap);

        Pose2d startPosition = new Pose2d(-35.5, 62, Math.toRadians(270));

        drive.setPoseEstimate(startPosition);

        /*
        Cubul de sus

        Trajectory cube = drive.trajectoryBuilder(startPosition)
                .lineToLinearHeading(new Pose2d(-30, 40, Math.toRadians(320)))
                .build();

         Trajectory duck = drive.trajectoryBuilder(cube.end())
                .addTemporalMarker(0.5, () -> robot.cupa.toggleCupa())
                .addTemporalMarker(0.7, () -> robot.glisiere.setToPosition(0))
                .lineToLinearHeading(new Pose2d(-65, 55, Math.toRadians(90)))
                .build();

        Trajectory finish = drive.trajectoryBuilder(duck.end())
                .lineTo(new Vector2d(-60, 34))
                .build();


        waitForStart();

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
        */
        /*
        Cubul de mijloc


            Trajectory cube = drive.trajectoryBuilder(startPosition)
                    .lineToLinearHeading(new Pose2d(-26, 37, Math.toRadians(325)))
                    .build();

            Trajectory duck = drive.trajectoryBuilder(cube.end())
                    .addTemporalMarker(0.3, () -> robot.cupa.toggleCupa())
                    .addTemporalMarker(0.7, () -> robot.glisiere.setToPosition(0))
                    .lineToLinearHeading(new Pose2d(-65, 55, Math.toRadians(90)))
                    .build();

            Trajectory finish = drive.trajectoryBuilder(duck.end())
                    .lineTo(new Vector2d(-60, 34))
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
            */

            /*
            Cubul de jos
             */
        Trajectory cube = drive.trajectoryBuilder(startPosition)
                .lineToLinearHeading(new Pose2d(-24, 36, Math.toRadians(323)))
                .build();

        Trajectory duck = drive.trajectoryBuilder(cube.end())
                .addTemporalMarker(0.3, () -> robot.glisiere.setToPosition(2))
                .addTemporalMarker(0.7, () -> robot.cupa.servo.setPosition(0.97))
                .addTemporalMarker(1.1, () -> robot.glisiere.setToPosition(0))
                .lineToLinearHeading(new Pose2d(-65, 55, Math.toRadians(90)), SampleMecanumDrive.getVelocityConstraint(15, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH), SampleMecanumDrive.getAccelerationConstraint(DriveConstants.MAX_ACCEL))
                .build();

        Trajectory finish = drive.trajectoryBuilder(duck.end())
                .lineTo(new Vector2d(-60, 34))
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