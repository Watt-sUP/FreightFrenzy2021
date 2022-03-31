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

        RoadRunnerBotEntity myBotMiddleCube = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(51, 51, Math.toRadians(318.24606247129276), Math.toRadians(318.24606247129276), 5.1)
                .followTrajectorySequence(drive ->
                        drive.trajectorySequenceBuilder(new Pose2d(12, -63, Math.toRadians(90)))
                                .splineToLinearHeading(new Pose2d(-12, -42, Math.toRadians(90)), Math.toRadians(90))
                                .lineToLinearHeading(new Pose2d(14, -64, Math.toRadians(176)))
                                .lineToConstantHeading(new Vector2d(50, -67))
                                .build()
                );

        meepMeep.setBackground(MeepMeep.Background.FIELD_FREIGHTFRENZY_ADI_DARK)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBotMiddleCube)
                .start();

//        MeepMeep meepMeep2 = new MeepMeep(600);
//
//        RoadRunnerBotEntity myBotBottomCube = new DefaultBotBuilder(meepMeep2)
//                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
//                .setConstraints(51, 51, Math.toRadians(318.24606247129276), Math.toRadians(318.24606247129276), 5.1)
//                .followTrajectorySequence(drive ->
//                        drive.trajectorySequenceBuilder(new Pose2d(12, -63, Math.toRadians(90)))
//                                .splineToLinearHeading(new Pose2d(3.5, -36, Math.toRadians(143)), Math.toRadians(35))
//                                .build()
//                );
//
//        meepMeep2.setBackground(MeepMeep.Background.FIELD_FREIGHTFRENZY_ADI_DARK)
//                .setDarkMode(true)
//                .setBackgroundAlpha(0.95f)
//                .addEntity(myBotBottomCube)
//                .start();

//        MeepMeep meepMeep3 = new MeepMeep(600);
//
//        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep3)
//                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
//                .setConstraints(51, 51, Math.toRadians(318.24606247129276), Math.toRadians(318.24606247129276), 5.1)
//                .followTrajectorySequence(drive ->
//                                drive.trajectorySequenceBuilder(new Pose2d(-35.5, -62, Math.toRadians(90)))
//                                        .lineToLinearHeading(new Pose2d(-24, -36, Math.toRadians(37)))
//                                        .build()
//                );
//
//        meepMeep3.setBackground(MeepMeep.Background.FIELD_FREIGHTFRENZY_ADI_DARK)
//                .setDarkMode(true)
//                .setBackgroundAlpha(0.95f)
//                .addEntity(myBot)
//                .start();
    }
}