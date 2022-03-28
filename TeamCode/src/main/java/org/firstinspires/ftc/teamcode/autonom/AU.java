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

    public void cupa_delivery() {
        robot.cupa.servo.setPosition(0.5);
    }

    @Override
    public void runOpMode() throws InterruptedException {
        drive = new SampleMecanumDriveCancelable(hardwareMap);
        robot = new Mugurel(hardwareMap);

        AtomicBoolean sensor_on = new AtomicBoolean(false);

        Pose2d startPose = new Pose2d(12, -63, Math.toRadians(90));
        drive.setPoseEstimate(startPose);

        double vv = 35;


        Trajectory delivery = drive.trajectoryBuilder(startPose)
                .addTemporalMarker(0.5, this::cupa_delivery)
                .splineToLinearHeading(new Pose2d(3.5, -51, Math.toRadians(120)), Math.toRadians(120), velocityConstraint(vv), accelerationConstraint())
                .addDisplacementMarker(() -> robot.cupa.toggleDeget())
                .build();

        TrajectorySequence supply = drive.trajectorySequenceBuilder(delivery.end())
                .addTemporalMarker(0.5, () -> robot.cupa.servo.setPosition(0.97))
                .addTemporalMarker(1, () -> robot.glisiere.setToPosition(0))
                .addTemporalMarker(1.5, () -> robot.maturica.toggleCollect())
                .splineToLinearHeading(new Pose2d(15, -65, Math.toRadians(182)), Math.toRadians(0), velocityConstraint(vv), accelerationConstraint())
                .addDisplacementMarker(() -> sensor_on.set(true))
                .lineTo(new Vector2d(48, -67), velocityConstraint(15), accelerationConstraint())
                .build();

//        Trajectory reverse = drive.trajectoryBuilder(supply.end())
//                .splineToConstantHeading(new Vector2d(20, -65), Math.toRadians(180))
//                .splineToSplineHeading(new Pose2d(0, -48, Math.toRadians(120)), Math.toRadians(120))
//                .build();


        waitForStart();

        robot.cupa.toggleDeget();
        robot.glisiere.setToPosition(4);
        sleep(500);
        drive.followTrajectory(delivery);
        sleep(200);
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
        robot.cupa.toggleDeget();
        sleep(200);
        robot.maturica.toggleEject();
        sleep(200);
        robot.glisiere.setToPosition(4);

        TrajectorySequence delivery_brk = drive.trajectorySequenceBuilder(drive.getPoseEstimate())
                .addTemporalMarker(0.7, this::cupa_delivery)
                .lineTo(new Vector2d(15, -65), velocityConstraint(vv), accelerationConstraint())
                .splineToLinearHeading(new Pose2d(3.5, -51, Math.toRadians(120)), Math.toRadians(120), velocityConstraint(vv), accelerationConstraint())
//                .addDisplacementMarker(() -> robot.cupa.toggleDeget())
                .build();

        drive.followTrajectorySequence(delivery_brk);

        // CYCLE 2
        telemetry.addData("Cycle", "2");
        telemetry.update();
        robot.cupa.toggleDeget();
        sleep(200);
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
        robot.cupa.toggleDeget();
        sleep(200);
        robot.maturica.toggleEject();
        sleep(200);
        robot.glisiere.setToPosition(4);

        delivery_brk = drive.trajectorySequenceBuilder(drive.getPoseEstimate())
                .addTemporalMarker(0.7, this::cupa_delivery)
                .lineTo(new Vector2d(15, -65), velocityConstraint(vv), accelerationConstraint())
                .splineToLinearHeading(new Pose2d(3.5, -51, Math.toRadians(120)), Math.toRadians(120), velocityConstraint(vv), accelerationConstraint())
//                .addDisplacementMarker(() -> robot.cupa.toggleDeget())
                .build();

        drive.followTrajectorySequence(delivery_brk);
        robot.cupa.toggleDeget();

//        drive.followTrajectory(supply);
//        sleep(1000);
//        drive.followTrajectory(reverse);
    }
}