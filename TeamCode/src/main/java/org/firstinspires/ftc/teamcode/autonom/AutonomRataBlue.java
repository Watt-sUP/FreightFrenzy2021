package org.firstinspires.ftc.teamcode.autonom;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

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

        Trajectory cube = drive.trajectoryBuilder(new Pose2d(-35.5, 62, Math.toRadians(270)))
                .addTemporalMarker(1, () -> robot.cupa.toggleCupa())
                .lineToLinearHeading(new Pose2d(-28, 55, Math.toRadians(310)))
                .build();

        Trajectory duck = drive.trajectoryBuilder(cube.end())
                .lineToLinearHeading(new Pose2d(-55, 57, Math.toRadians(310)))
                .addDisplacementMarker(() -> robot.rata.rotate(0.7))
                .build();

        Trajectory finish = drive.trajectoryBuilder(duck.end())
                .lineTo(new Vector2d(-60, 37))
                .build();


        waitForStart();

        robot.cupa.toggleDeget();
        robot.glisiere.setToPosition(4);
        drive.followTrajectory(cube);
        sleep(500);
        robot.cupa.toggleDeget();
        sleep(500);
        robot.cupa.toggleCupa();
        sleep(500);
        robot.glisiere.setToPosition(0);
        sleep(1000);
        drive.followTrajectory(duck);
        robot.rata.motor.setPower(1);
        sleep(3000);
        drive.followTrajectory(finish);
    }
}