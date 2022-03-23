package org.firstinspires.ftc.teamcode.autonom;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
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
                    robot.glisiere.setToPosition(3);
                })
                .addTemporalMarker(0.5, () -> robot.cupa.servo.setPosition(0.5))
                .splineToSplineHeading(new Pose2d(-10, -49.5, Math.toRadians(95)), Math.toRadians(120))
                .addDisplacementMarker(() -> robot.cupa.toggleDeget())
                .build();


        Trajectory supply = drive.trajectoryBuilder(delivery.end())
                .addTemporalMarker(0.5, () -> robot.glisiere.setToPosition(0))
                .addTemporalMarker(1, () -> robot.maturica.toggleCollect())
                .splineToSplineHeading(new Pose2d(0, -60.5, Math.toRadians(180)), Math.toRadians(120))
                .splineToConstantHeading(new Vector2d(10, -65.5), Math.toRadians(0))
                .splineToConstantHeading(new Vector2d(45, -65.5), Math.toRadians(0))
                .build();

        Trajectory reverse = drive.trajectoryBuilder(supply.end())
                .splineToConstantHeading(new Vector2d(20, -65.5), Math.toRadians(180))
                .splineTo(new Vector2d(-10, -48), Math.toRadians(90))
                .build();
        waitForStart();

        drive.followTrajectory(delivery);
        sleep(1000);
        robot.cupa.servo.setPosition(0.97);
        drive.followTrajectory(supply);
        sleep(1000);
        drive.followTrajectory(reverse);
        sleep(1000);
    }
}