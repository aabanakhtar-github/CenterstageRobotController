import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.components.BluePropDetector;
import org.firstinspires.ftc.teamcode.components.IntakeDepositSystem_DC;
import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.trajectorysequence.TrajectorySequence;

@TeleOp(name = "blue auton left")
public class Auto_BlueLeft extends LinearOpMode {
    BluePropDetector m_PropDetector;

    SampleMecanumDrive m_Drive;

    IntakeDepositSystem_DC m_IntakeSys;

    @Override
    public void runOpMode() throws InterruptedException {
        m_Drive = new SampleMecanumDrive(hardwareMap);
        m_IntakeSys = new IntakeDepositSystem_DC(hardwareMap);
        m_PropDetector = new BluePropDetector(hardwareMap);
        m_Drive.setPoseEstimate(new Pose2d(12, 57, Math.toRadians(270)));
        BluePropDetector.Positions PropPosition = BluePropDetector.Positions.MIDDLE;

        while(opModeInInit() && !isStopRequested()) {
            PropPosition = m_PropDetector.GetDetectionPosition();
        }

        waitForStart();

        TrajectorySequence purple_pixel_drop = m_Drive.trajectorySequenceBuilder(m_Drive.getPoseEstimate())
                .splineTo(new Vector2d(12, 6), Math.toRadians(270))
                .build();

        m_Drive.followTrajectorySequence(purple_pixel_drop);

        m_IntakeSys.SpintakeMode = IntakeDepositSystem_DC.SpintakeModes.SPIT;
        m_IntakeSys.UpdateStateMachine(true);
        sleep(1000);
        m_IntakeSys.SpintakeMode = IntakeDepositSystem_DC.SpintakeModes.STOP;
        m_IntakeSys.UpdateStateMachine(true);

        TrajectorySequence yellow_pixel_drop = m_Drive.trajectorySequenceBuilder(m_Drive.getPoseEstimate())
                .splineTo(new Vector2d(52, 36), Math.toRadians(0))
                .build();

        m_Drive.followTrajectorySequence(yellow_pixel_drop);

        m_IntakeSys.CurrentLocationState = IntakeDepositSystem_DC.SET_LINE_1;
        m_IntakeSys.UpdateStateMachine(true);
        sleep(3000);
        m_IntakeSys.CurrentLocationState = IntakeDepositSystem_DC.LOW_POSITION;
        m_IntakeSys.UpdateStateMachine(true);

        TrajectorySequence park = m_Drive.trajectorySequenceBuilder(m_Drive.getPoseEstimate())
                .back(12)
                .splineTo(new Vector2d(60, 12), Math.toRadians(0))
                .build();

        m_Drive.followTrajectorySequence(park);
    }
}