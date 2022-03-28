package org.firstinspires.ftc.teamcode.autonom;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.acmerobotics.roadrunner.trajectory.constraints.TrajectoryAccelerationConstraint;
import com.acmerobotics.roadrunner.trajectory.constraints.TrajectoryVelocityConstraint;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.drive.DriveConstants;
import org.firstinspires.ftc.teamcode.drive.SampleMecanumDriveCancelable;
import org.firstinspires.ftc.teamcode.hardware.Mugurel;
import org.firstinspires.ftc.teamcode.trajectorysequence.TrajectorySequence;

import java.util.concurrent.atomic.AtomicBoolean;

//@Disabled
@Autonomous(name = "Autonom test 2", group = "autonom")
public class AU2 extends LinearOpMode {

    SampleMecanumDriveCancelable drive;
    Mugurel robot;

    public TrajectoryVelocityConstraint velocityConstraint(double v) {
        return SampleMecanumDriveCancelable.getVelocityConstraint(v, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH);
    }

    public TrajectoryAccelerationConstraint accelerationConstraint() {
        return SampleMecanumDriveCancelable.getAccelerationConstraint(DriveConstants.MAX_ACCEL);
    }

    public void cupa_delivery() {
        robot.cupa.servo.setPosition(0.55);
    }

    @Override
    public void runOpMode() throws InterruptedException {
        drive = new SampleMecanumDriveCancelable(hardwareMap);
        robot = new Mugurel(hardwareMap);

        AtomicBoolean sensor_on = new AtomicBoolean(false);

        Pose2d startPose = new Pose2d(12, -63, Math.toRadians(90));
        drive.setPoseEstimate(startPose);

        double vv = 35;

        //Cub mijloc
//        Trajectory delivery = drive.trajectoryBuilder(startPose)
//                .addTemporalMarker(0.5, this::cupa_delivery)
//                .splineToLinearHeading(new Pose2d(5, -37, Math.toRadians(155)), Math.toRadians(150), velocityConstraint(20), accelerationConstraint())
//                .addDisplacementMarker(() -> robot.cupa.toggleDeget())
//                .build();

        Trajectory delivery = drive.trajectoryBuilder(startPose)
                .lineToLinearHeading(new Pose2d(5, -36, Math.toRadians(143)), velocityConstraint(20), accelerationConstraint())
                .build();

        TrajectorySequence supply = drive.trajectorySequenceBuilder(delivery.end())
                .addTemporalMarker(0.5, () -> robot.cupa.servo.setPosition(0.97))
                .addTemporalMarker(1, () -> robot.glisiere.setToPosition(0))
                .addTemporalMarker(1.5, () -> robot.maturica.toggleCollect())
                .splineToLinearHeading(new Pose2d(15, -65, Math.toRadians(182)), Math.toRadians(0), velocityConstraint(vv), accelerationConstraint())
                .addDisplacementMarker(() -> sensor_on.set(true))
                .lineTo(new Vector2d(48, -67), velocityConstraint(15), accelerationConstraint())
                .build();



        waitForStart();

        robot.cupa.toggleDeget();
        robot.glisiere.setToPosition(2);
        sleep(1000);
        robot.cupa.servo.setPosition(0.5);
        sleep(1000);
        robot.glisiere.setToPosition(1);
        sleep(1000);
        drive.followTrajectory(delivery);
        sleep(500);
        robot.cupa.toggleDegetMore();
        sleep(1000);
    }
}