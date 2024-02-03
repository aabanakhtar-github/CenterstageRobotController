
package com.example.meepmeeptesting;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

public class Playground {
    public static void main(String[] args) {
        MeepMeep meepMeep = new MeepMeep(800);

        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(60, 60, Math.toRadians(180), Math.toRadians(180), 15)
                .followTrajectorySequence(drive ->
                        drive.trajectorySequenceBuilder(new Pose2d(-36, -48, Math.toRadians(270)))
                                .splineTo(new Vector2d(-36, -50), Math.toRadians(270))
                                .splineTo(new Vector2d(-50, -36), Math.toRadians(90))
                                .build()
                );

        meepMeep.setBackground(MeepMeep.Background.FIELD_CENTERSTAGE_JUICE_DARK)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }
}
/*
Blue auto, left
12, 57, Math.toRadians(270)))
                                .lineTo(new Vector2d(12, 36))
                                .turn(Math.toRadians(-90))
                                .setReversed(true)
                                .splineTo(new Vector2d(48, 30), Math.toRadians(0))
                                .setReversed(false)
                                .splineTo(new Vector2d(24, 12), Math.toRadians(180))
                                .lineTo(new Vector2d(60, 12))
                                .build()
 */

/*
Blue auto, right
new Pose2d(-36, 57, Math.toRadians(270)))
                                .lineTo(new Vector2d(-36, 36))
                                .turn(Math.toRadians(-90))
                                .lineTo(new Vector2d(-60, 36))
                                .setReversed(true)
                                .splineTo(new Vector2d(-34, 6), Math.toRadians(0)).
                                setReversed(false)
                                .turn(Math.toRadians(180))
                                .splineTo(new Vector2d(20, 12), Math.toRadians(0))
                                .splineTo(new Vector2d(36, 12), Math.toRadians(0))
                                .lineTo(new Vector2d(50, 36))
                                .back(24)
                                .splineTo(new Vector2d(60, 12), Math.toRadians(0))
                                .build()
 */

/* red auto, left
                                .lineTo(new Vector2d(-36, -36))
                                .turn(Math.toRadians(-90))
                                .lineTo(new Vector2d(-60, -36))
                                .setReversed(true)
                                .splineTo(new Vector2d(-34, -6), Math.toRadians(0)).
                                setReversed(false)
                                .turn(Math.toRadians(180))
                                .splineTo(new Vector2d(20, -12), Math.toRadians(0))
                                .splineTo(new Vector2d(36, -12), Math.toRadians(0))
                                .splineTo(new Vector2d(50, -36), Math.toRadians(0))
                                .back(24)
                                .splineTo(new Vector2d(60, -12), Math.toRadians(0))
                                .build()
 */