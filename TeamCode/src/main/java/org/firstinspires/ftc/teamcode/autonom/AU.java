package org.firstinspires.ftc.teamcode.autonom;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.hardware.Mugurel;

//@Disabled
@Autonomous(name = "Autonom test", group = "autonom")
public class AU extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {
        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);
        Mugurel robot = new Mugurel(hardwareMap);

        Pose2d startPose = new Pose2d(12, -63, Math.toRadians(90));

        drive.setPoseEstimate(startPose);


        Trajectory delivery = drive.trajectoryBuilder(startPose)
                .splineToSplineHeading(new Pose2d(0, -48, Math.toRadians(120)), Math.toRadians(120))
                .build();


        Trajectory supply = drive.trajectoryBuilder(delivery.end())
                .splineToSplineHeading(new Pose2d(12, -65, Math.toRadians(180)), Math.toRadians(0))
                .splineToConstantHeading(new Vector2d(40, -65), Math.toRadians(0))
                .build();

        Trajectory reverse = drive.trajectoryBuilder(supply.end())
                .splineToConstantHeading(new Vector2d(20, -65), Math.toRadians(180))
                .splineToSplineHeading(new Pose2d(0, -48, Math.toRadians(120)), Math.toRadians(120))
                .build();


        waitForStart();

        drive.followTrajectory(delivery);
        sleep(1000);
        drive.followTrajectory(supply);
        sleep(1000);
        drive.followTrajectory(reverse);
    }
}