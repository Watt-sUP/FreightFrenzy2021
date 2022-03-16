package org.firstinspires.ftc.teamcode.autonom;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.hardware.Mugurel;

@Disabled
@Autonomous(name = "Autonom cub albastru", group = "autonom")
public class AutonomCubBlue extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {
        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);
        Mugurel robot = new Mugurel(hardwareMap);

        Trajectory delivery = drive.trajectoryBuilder(new Pose2d(12, 62, Math.toRadians(0)))
                .addTemporalMarker(1, () -> robot.cupa.toggleCupa())
                .splineToLinearHeading(new Pose2d(-6, 39), Math.toRadians(245))
                .addDisplacementMarker(() -> robot.cupa.toggleDeget())
                .build();

        Trajectory supply = drive.trajectoryBuilder(delivery.end())
                .addTemporalMarker(1, () -> robot.glisiere.setToPosition(0))
                .splineToLinearHeading(new Pose2d(12, 62), Math.toRadians(0))
                .addDisplacementMarker( () -> robot.maturica.toggleCollect())
                .splineTo(new Vector2d(57, 62),Math.toRadians(0))
                .build();

        Trajectory returnToOrigin = drive.trajectoryBuilder(supply.end())
                .addTemporalMarker(0.3, () -> robot.maturica.toggleEject())
                .splineTo(new Vector2d(12, 62),Math.toRadians(0))
                .addDisplacementMarker(() -> robot.cupa.toggleDeget())
                .build();



        waitForStart();

        while (opModeIsActive() && !isStopRequested()) {
            robot.cupa.toggleDeget();
            robot.glisiere.setToPosition(3);
            drive.followTrajectory(delivery);
            robot.cupa.toggleCupa();
            drive.followTrajectory(supply);
            sleep(300);
            drive.followTrajectory(returnToOrigin);
            robot.glisiere.setToPosition(3);
            drive.followTrajectory(delivery);
            robot.cupa.toggleCupa();
            drive.followTrajectory(supply);
        }
    }
}