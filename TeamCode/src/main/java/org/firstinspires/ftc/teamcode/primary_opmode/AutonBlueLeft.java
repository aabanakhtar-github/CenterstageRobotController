package org.firstinspires.ftc.teamcode.primary_opmode;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.components.BluePropDetector;
import org.firstinspires.ftc.teamcode.components.DriveTrain;
import org.firstinspires.ftc.teamcode.components.IntakeDepositSystem_DC;
import org.firstinspires.ftc.teamcode.components.RedPropDetector;
import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.trajectorysequence.TrajectorySequence;

@Autonomous(name = "Auto Blue: Left")
public class AutonBlueLeft extends LinearOpMode {

    private SampleMecanumDrive m_Drive;

    private BluePropDetector m_PropDetector;

    private IntakeDepositSystem_DC m_IntakeSys;

    @Override
    public void runOpMode() {
        m_Drive = new SampleMecanumDrive(hardwareMap);
        m_IntakeSys = new IntakeDepositSystem_DC(hardwareMap);
        m_PropDetector = new BluePropDetector(hardwareMap);

        m_IntakeSys.SPINTAKE_POWER = 0.5;

        BluePropDetector.Positions prop_location = m_PropDetector.GetDetectionPosition();
        // prop detection
        while (opModeInInit() && !isStopRequested()) {
            prop_location = m_PropDetector.GetDetectionPosition();
        }

        waitForStart();

        m_Drive.setPoseEstimate(new Pose2d(12, 65, Math.toRadians(90 )));

        TrajectorySequence to_prop_location = null;
        double placement_Y = 36.0;
        double placement_degrees = 0.0f;
        switch (prop_location) {
            case LEFT:
                to_prop_location = m_Drive.trajectorySequenceBuilder(m_Drive.getPoseEstimate())
                        .setReversed(true)
                        .splineTo(new Vector2d(36, 36), Math.toRadians(0))
                        .setReversed(false)
                        .turn(Math.toRadians(180))
                        .build();
                placement_Y = 44;
                break;
            case RIGHT:
                to_prop_location = m_Drive.trajectorySequenceBuilder(m_Drive.getPoseEstimate())
                        .setReversed(true)
                        .lineTo(new Vector2d(12, 36))
                        .turn(Math.toRadians(-90))
                        .setReversed(false)
                        .build();

                placement_Y = 32;
                break;
            case MIDDLE:
                to_prop_location = m_Drive.trajectorySequenceBuilder(m_Drive.getPoseEstimate())
                        .lineTo(new Vector2d(12, 36))
                        .build();
                break;
        }


        m_Drive.followTrajectorySequence(to_prop_location);

        m_IntakeSys.SpintakeMode = IntakeDepositSystem_DC.SpintakeModes.SPIT;
        m_IntakeSys.UpdateStateMachine(true);
        sleep(1999);
        m_IntakeSys.SpintakeMode = IntakeDepositSystem_DC.SpintakeModes.STOP;
        m_IntakeSys.UpdateStateMachine(true);

        // drop purple
        TrajectorySequence reset_and_go_to_backdrop = m_Drive.trajectorySequenceBuilder(m_Drive.getPoseEstimate())
                .addTemporalMarker(() -> {
                    m_IntakeSys.CurrentLocationState = IntakeDepositSystem_DC.SET_LINE_1;
                    m_IntakeSys.UpdateStateMachine(true);
                })
                .splineTo(new Vector2d(58, placement_Y), Math.toRadians(0))
                .build();

        m_Drive.followTrajectorySequence(reset_and_go_to_backdrop);

        m_IntakeSys.ReleaseDeposit();
        sleep(2000);

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

