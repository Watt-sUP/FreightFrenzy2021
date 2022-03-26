package com.example.meepmeeptesting;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

public class MeepMeepTesting {
    public static void main(String[] args) {
        MeepMeep meepMeep = new MeepMeep(600);

        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(25, 25, Math.toRadians(318.24606247129276), Math.toRadians(318.24606247129276), 5.1)
                .followTrajectorySequence(drive ->
                        drive.trajectorySequenceBuilder(new Pose2d(13, -63, Math.toRadians(90)))
                                .splineTo(new Vector2d(-10, -49.5), Math.toRadians(93))
                                .splineToSplineHeading(new Pose2d(10, -66, Math.toRadians(180)), Math.toRadians(180))
                                .lineTo(new Vector2d(45, -66))
                                .lineTo(new Vector2d(20, -66))
                                .splineTo(new Vector2d(-10, -48), Math.toRadians(98))
                                .build()
                );

        meepMeep.setBackground(MeepMeep.Background.FIELD_FREIGHTFRENZY_ADI_DARK)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }
}