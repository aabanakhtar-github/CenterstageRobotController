package org.firstinspires.ftc.teamcode.primary_opmode;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.components.BluePropDetector;
import org.firstinspires.ftc.teamcode.components.IntakeDepositSystem_DC;
import org.firstinspires.ftc.teamcode.components.RedPropDetector;
import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.trajectorysequence.TrajectorySequence;

@Autonomous(name = "Auto: blue right")
public class AutonBlueRight extends LinearOpMode {

    SampleMecanumDrive m_Drive;

    IntakeDepositSystem_DC m_IntakeSys;

    BluePropDetector m_RedPropDetector;

    @Override
    public void runOpMode() throws InterruptedException {
        m_Drive = new SampleMecanumDrive(hardwareMap);
        m_IntakeSys = new IntakeDepositSystem_DC(hardwareMap);
        m_RedPropDetector = new BluePropDetector(hardwareMap);

        BluePropDetector.Positions prop_location = BluePropDetector.Positions.MIDDLE;
        m_IntakeSys.SPINTAKE_POWER = 0.5;
        m_Drive.setPoseEstimate(new Pose2d(-36, 60, Math.toRadians(90)));

        while (opModeInInit() && !isStopRequested()) {
            prop_location = m_RedPropDetector.GetDetectionPosition();
            telemetry.addData("hahah" ,prop_location.toString());
            telemetry.update();
        }

        double placement_Y = 35.0;
        double placement_degrees = 0.0f;
        TrajectorySequence to_prop_location = null;

        switch (prop_location) {
            case LEFT:
                to_prop_location = m_Drive.trajectorySequenceBuilder(m_Drive.getPoseEstimate())
                        .lineTo(new Vector2d(-36, 36))
                        .turn(Math.toRadians(90))
                        .build();
                placement_Y = 40;
                break;
            case RIGHT:
                to_prop_location = m_Drive.trajectorySequenceBuilder(m_Drive.getPoseEstimate())
                        .lineTo(new Vector2d(-36, 36))
                        .turn(Math.toRadians(-90))
                        .build();

                placement_Y = 30;
                break;
            case MIDDLE:
                to_prop_location = m_Drive.trajectorySequenceBuilder(m_Drive.getPoseEstimate())
                        .lineTo(new Vector2d(-36, 30))
                        .build();
                break;
        }

        waitForStart();

        m_Drive.followTrajectorySequence(to_prop_location);

        m_IntakeSys.SpintakeMode = IntakeDepositSystem_DC.SpintakeModes.SPIT;
        m_IntakeSys.UpdateStateMachine(true);
        sleep(2000);
        m_IntakeSys.SpintakeMode = IntakeDepositSystem_DC.SpintakeModes.STOP;
        m_IntakeSys.UpdateStateMachine(true);

        TrajectorySequence to_start_and_around = m_Drive.trajectorySequenceBuilder(m_Drive.getPoseEstimate())
                .splineTo(m_Drive.getPoseEstimate().vec().plus(new Vector2d(0, -1)), Math.toRadians(270))
                .lineTo(new Vector2d(-36, 48 ))
                .splineTo(new Vector2d(-48, 48), Math.toRadians(270))
                .splineTo(new Vector2d(-55, 0), Math.toRadians(270))
                .build();

        m_Drive.followTrajectorySequence(to_start_and_around);

        TrajectorySequence to_backdrop = m_Drive.trajectorySequenceBuilder(m_Drive.getPoseEstimate())
                .splineTo(new Vector2d(-36, 6 ), Math.toRadians(0))
                .lineTo(new Vector2d(0, 0))
                .addTemporalMarker(() -> {
                    m_IntakeSys.CurrentLocationState = IntakeDepositSystem_DC.SET_LINE_1;
                    m_IntakeSys.UpdateStateMachine(true);
                })
                .splineTo(new Vector2d(60, placement_Y), Math.toRadians(0))
                .build();

        m_Drive.followTrajectorySequence(to_backdrop);

        m_IntakeSys.ReleaseDeposit();
        sleep(3000);

        TrajectorySequence park = m_Drive.trajectorySequenceBuilder(m_Drive.getPoseEstimate())
                .setReversed(true)
                .splineTo(new Vector2d(24, 3 ), Math.toRadians(180))
                .addTemporalMarker(() -> {
                    m_IntakeSys.CurrentLocationState = IntakeDepositSystem_DC.LOW_POSITION;
                    m_IntakeSys.UpdateStateMachine(true);
                })
                .lineTo(new Vector2d(53 , 3))
                .build();

        m_Drive.followTrajectorySequence(park);

    }

}
