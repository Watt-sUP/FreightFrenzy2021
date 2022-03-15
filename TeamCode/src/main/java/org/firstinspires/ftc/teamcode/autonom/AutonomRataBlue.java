package org.firstinspires.ftc.teamcode.autonom;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.hardware.Mugurel;

@Autonomous(name = "Autonom rata albastru", group = "autonom")
public class AutonomRataBlue extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {
        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);
        Mugurel robot = new Mugurel(hardwareMap);

        Trajectory duck = drive.trajectoryBuilder(new Pose2d(-35.5, 62, Math.toRadians(270)))
                .addTemporalMarker(1, () -> robot.cupa.toggleCupa())
                .splineToLinearHeading(new Pose2d(-19, 39), Math.toRadians(295))
                .addDisplacementMarker(() -> robot.cupa.toggleDeget())
                .addDisplacementMarker(() -> {
                    robot.cupa.toggleCupa();
                })
                .addTemporalMarker(2, () -> robot.glisiere.setToPosition(0))
                .splineToLinearHeading(new Pose2d(-50, 60), Math.toRadians(90))
                .addDisplacementMarker(() -> robot.rata.rotate(0.7))
                .build();

        Trajectory finish = drive.trajectoryBuilder(duck.end())
                .splineTo(new Vector2d(-60, 37),Math.toRadians(0))
                .build();


        waitForStart();

        while (opModeIsActive() && !isStopRequested()) {
            robot.cupa.toggleDeget();
            robot.glisiere.setToPosition(3);
            drive.followTrajectory(duck);
            sleep(1000);
            drive.followTrajectory(finish);
        }
    }
}