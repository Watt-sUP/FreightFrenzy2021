package com.example.meepmeeptesting;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.constraints.TrajectoryAccelerationConstraint;
import com.acmerobotics.roadrunner.trajectory.constraints.TrajectoryVelocityConstraint;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

public class MeepMeepTesting {
    public static void main(String[] args) {
        MeepMeep meepMeep = new MeepMeep(600);

        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(51, 51, Math.toRadians(318.24606247129276), Math.toRadians(318.24606247129276), 5.1)
                .followTrajectorySequence(drive ->
                        drive.trajectorySequenceBuilder(new Pose2d(3.5, -51, Math.toRadians(120)))
//                        drive.trajectorySequenceBuilder(new Pose2d(46, -67, Math.toRadians(176)))
//                                .splineToLinearHeading(new Pose2d(3.5, -51, Math.toRadians(120)), Math.toRadians(120))
                                .splineToLinearHeading(new Pose2d(15, -64.5, Math.toRadians(176)), Math.toRadians(0))
//                                .lineToConstantHeading(new Vector2d(40, -67))
                                .lineToConstantHeading(new Vector2d(60, -67))
//                                .splineToSplineHeading(new Pose2d(60, -67, Math.toRadians(176)), Math.toRadians(0))
//                                .splineToSplineHeading(new Pose2d(15, -65, Math.toRadians(176)), Math.toRadians(176))
//                                .splineToSplineHeading(new Pose2d(3.5, -51, Math.toRadians(120 - 2.5)), Math.toRadians(120))
                                .build()
                );

        meepMeep.setBackground(MeepMeep.Background.FIELD_FREIGHTFRENZY_ADI_DARK)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }
}