package org.firstinspires.ftc.teamcode.autonom;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.acmerobotics.roadrunner.trajectory.constraints.TrajectoryAccelerationConstraint;
import com.acmerobotics.roadrunner.trajectory.constraints.TrajectoryVelocityConstraint;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.drive.DriveConstants;
import org.firstinspires.ftc.teamcode.drive.SampleMecanumDriveCancelable;
import org.firstinspires.ftc.teamcode.hardware.Mugurel;
import org.firstinspires.ftc.teamcode.trajectorysequence.TrajectorySequence;

import java.util.concurrent.atomic.AtomicBoolean;

//@Disabled
@Autonomous(name = "Autonom test", group = "autonom")
public class AU extends LinearOpMode {

    SampleMecanumDriveCancelable drive;
    Mugurel robot;

    public TrajectoryVelocityConstraint velocityConstraint(double v) {
        return SampleMecanumDriveCancelable.getVelocityConstraint(v, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH);
    }

    public TrajectoryAccelerationConstraint accelerationConstraint() {
        return SampleMecanumDriveCancelable.getAccelerationConstraint(DriveConstants.MAX_ACCEL);
    }

    public Trajectory delivery3, delivery2, delivery1;
    public TrajectorySequence supply, park;
    public AtomicBoolean sensor_on;
    public double vv = 45;

    public void update_supply_trajectory(int cycle_id) {
//        supply = drive.trajectorySequenceBuilder(delivery.end())
//                .addTemporalMarker(0.5, () -> robot.cupa.collect())
//                .addTemporalMarker(1, () -> robot.glisiere.setToPosition(0))
//                .addTemporalMarker(1.5, () -> robot.maturica.collect())
//                .splineToLinearHeading(new Pose2d(15, -64.5, Math.toRadians(176)), Math.toRadians(0), velocityConstraint(25), accelerationConstraint())
//                .addDisplacementMarker(() -> sensor_on.set(true))
//                .lineToConstantHeading(new Vector2d(40, -67), velocityConstraint(vv), accelerationConstraint())
//                .lineToConstantHeading(new Vector2d(60, -67), velocityConstraint(7), accelerationConstraint())
//                .build();

        supply = drive.trajectorySequenceBuilder(drive.getPoseEstimate())
                .addTemporalMarker(0.75, () -> robot.cupa.collect())
                .addTemporalMarker(1.25, () -> robot.glisiere.setToPosition(0))
                .addTemporalMarker(2, () -> robot.maturica.collect())
                .splineToLinearHeading(new Pose2d(15, -64.5, Math.toRadians(176)), Math.toRadians(0), velocityConstraint(27), accelerationConstraint())
//                .splineToSplineHeading(new Pose2d(15, -64.5, Math.toRadians(176)), Math.toRadians(0), velocityConstraint(27), accelerationConstraint())
                .addDisplacementMarker(() -> sensor_on.set(true))
                .lineToConstantHeading(new Vector2d(46 + (cycle_id - 1) * 5, -67), velocityConstraint(vv), accelerationConstraint())
//                .splineToSplineHeading(new Pose2d(46 + (cycle_id - 1) * 5, -67, Math.toRadians(176)), Math.toRadians(0), velocityConstraint(vv), accelerationConstraint())
//                .lineToConstantHeading(new Vector2d(60, -67), velocityConstraint(7), accelerationConstraint())
                .build();

    }

    public void build_trajectories() {
        Pose2d startPose = new Pose2d(12, -63, Math.toRadians(90));
        drive.setPoseEstimate(startPose);

        sensor_on = new AtomicBoolean(false);


        delivery3 = drive.trajectoryBuilder(startPose)
                .addTemporalMarker(0.5, () -> robot.cupa.delivery34())
                .splineToLinearHeading(new Pose2d(3.5, -51, Math.toRadians(120)), Math.toRadians(120), velocityConstraint(vv), accelerationConstraint())
                .addDisplacementMarker(() -> robot.cupa.desface())
                .build();

        delivery2 = drive.trajectoryBuilder(startPose)
                .addTemporalMarker(0.5, () -> robot.cupa.delivery2())
                .splineToLinearHeading(new Pose2d(5, -37, Math.toRadians(155)), Math.toRadians(150), velocityConstraint(20), accelerationConstraint())
                .addDisplacementMarker(() -> robot.cupa.toggleDeget())
                .build();

        delivery1 = drive.trajectoryBuilder(startPose)
                .lineToLinearHeading(new Pose2d(5, -36, Math.toRadians(143)), velocityConstraint(20), accelerationConstraint())
                .build();

//        update_supply_trajectory(0);
    }

    public void delivery_x() {
        robot.cupa.strange();

        int level = 3;

        if(level == 1)  {
            robot.glisiere.setToPosition(2);
            sleep(700);
            robot.cupa.delivery34();
            sleep(500);
            robot.glisiere.setToPosition(1);
            drive.followTrajectory(delivery1);
            robot.cupa.desface();
        } else if(level == 2) {
            robot.glisiere.setToPosition(2);
            sleep(500);
            drive.followTrajectory(delivery2);
        }

        if(level == 3)  {
            robot.glisiere.setToPosition(4);
            sleep(500);
            drive.followTrajectory(delivery3);
        }
    }

    public void cycle(int id) {
        telemetry.addData("Cycle", id);
        telemetry.update();

        update_supply_trajectory(id);

        drive.followTrajectorySequenceAsync(supply);
        while(drive.isBusy()) {
            drive.update();

            if(robot.distance.getDistance(DistanceUnit.CM) <= 5 && sensor_on.get()) {
                drive.breakFollowing();
                telemetry.addData("Break", "true");
                telemetry.update();
                break;
            }
        }
        sensor_on.set(false);
        robot.cupa.strange();
        sleep(200);
        robot.maturica.eject();
        sleep(200);
        robot.glisiere.setToPosition(4);

        Trajectory delivery_brk = drive.trajectoryBuilder(drive.getPoseEstimate())
                .addTemporalMarker(0.7, () -> robot.cupa.delivery34())
                .addTemporalMarker(1.5, () -> robot.maturica.stop())
//                .splineTo(new Vector2d(15, -65), 0, velocityConstraint(vv), accelerationConstraint())
//                .splineToLinearHeading(new Pose2d(3.5, -51, Math.toRadians(120 - 2.5 * id)), Math.toRadians(120), velocityConstraint(vv), accelerationConstraint())
                .splineToSplineHeading(new Pose2d(15, -65, Math.toRadians(176)), Math.toRadians(176), velocityConstraint(vv), accelerationConstraint())
                .splineToSplineHeading(new Pose2d(3.5, -51, Math.toRadians(120 - 2.5)), Math.toRadians(120), velocityConstraint(vv), accelerationConstraint())
                .build();

        drive.followTrajectory(delivery_brk);
        robot.cupa.desface();
        sleep(300);
    }

    public void park_robot() {
        park = drive.trajectorySequenceBuilder(drive.getPoseEstimate())
                .addTemporalMarker(0.5, () -> robot.cupa.collect())
                .addTemporalMarker(1, () -> robot.glisiere.setToPosition(0))
                .splineToLinearHeading(new Pose2d(15, -63.5, Math.toRadians(177)), Math.toRadians(0), velocityConstraint(25), accelerationConstraint())
//                .addDisplacementMarker(() -> robot.maturica.collect())
                .addDisplacementMarker(() -> sensor_on.set(true))
                .lineTo(new Vector2d(40, -67))
//                .lineTo(new Vector2d(60, -67), velocityConstraint(7), accelerationConstraint())
                .build();

        drive.followTrajectorySequence(park);
    }

    @Override
    public void runOpMode() throws InterruptedException {
        ElapsedTime timer = new ElapsedTime();
        drive = new SampleMecanumDriveCancelable(hardwareMap);
        robot = new Mugurel(hardwareMap);

        build_trajectories();


        waitForStart();

        timer.reset();

        delivery_x();
        sleep(200);

        cycle(1);
        cycle(2);
        if(timer.seconds() < 20)
            cycle(3);

        park_robot();
    }
}