package org.firstinspires.ftc.teamcode.tests;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.trajectorysequence.TrajectorySequence;

import java.util.Arrays;
import java.util.List;

@TeleOp(name = "Path Test Custom")
public class AutonomousPathTest extends LinearOpMode {
    SampleMecanumDrive m_DriveTrain;

    @Override
    public void runOpMode() throws InterruptedException {

        waitForStart();
        m_DriveTrain = new SampleMecanumDrive(hardwareMap);
        m_DriveTrain.setPoseEstimate(new Pose2d(0, 0, 0));

        TrajectorySequence traj1 = m_DriveTrain.trajectorySequenceBuilder(new Pose2d(0.07, -0.22, Math.toRadians(0.00)))
                .splineTo(new Vector2d(6.08, 13.26), Math.toRadians(100.26))
                .splineTo(new Vector2d(-5.49, 30.84), Math.toRadians(180.00))
                .splineTo(new Vector2d(-29.96, 25.12), Math.toRadians(255.60))
                .splineTo(new Vector2d(-29.08, -13.84), Math.toRadians(242.17))
                .splineTo(new Vector2d(0.37, -0.66), Math.toRadians(188.39))
                .build();


        m_DriveTrain.followTrajectorySequence(traj1);
        while (m_DriveTrain.isBusy()) {}

    }
}
