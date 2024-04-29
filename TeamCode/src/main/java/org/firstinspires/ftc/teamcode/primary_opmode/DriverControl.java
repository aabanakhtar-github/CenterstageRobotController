package org.firstinspires.ftc.teamcode.primary_opmode;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.components.DriveTrain;
import org.firstinspires.ftc.teamcode.components.IntakeDepositSystem_DC;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Config
@TeleOp(name = "Driver Control")
public class DriverControl extends LinearOpMode {
    public static double COOLDOWN_TIME = 2;

    private DriveTrain m_Drive;

    private ElapsedTime m_Cooldown;

    private IntakeDepositSystem_DC m_IntakeSys;

    private Servo m_Launcher;

    @Override
    public void runOpMode() throws InterruptedException {
        int counter = 0;

        m_Cooldown = new ElapsedTime();
        m_Drive = new DriveTrain(hardwareMap);
        m_IntakeSys = new IntakeDepositSystem_DC(hardwareMap);
        m_Launcher = hardwareMap.get(Servo.class, "shoot");

        List<Integer> extension_states = Arrays.asList(
                IntakeDepositSystem_DC.SET_LINE_1,
                IntakeDepositSystem_DC.SET_LINE_2,
                IntakeDepositSystem_DC.SET_LINE_3
        );

        waitForStart();
        m_Cooldown.reset();

        while (opModeIsActive() && !isStopRequested()) {

            if (gamepad1.dpad_up && m_Cooldown.time(TimeUnit.MILLISECONDS) > (int) (COOLDOWN_TIME * 1000.0)) {//m_Cooldown.time() > 0.5) {;
                m_IntakeSys.CurrentLocationState = extension_states.get(counter % 3);
                counter += 1;
                m_Cooldown.reset();
            } else if (gamepad1.dpad_down) {
                m_IntakeSys.CurrentLocationState = IntakeDepositSystem_DC.LOW_POSITION;
            }

            if (gamepad1.right_trigger > 0.5) m_IntakeSys.SpintakeMode = IntakeDepositSystem_DC.SpintakeModes.INTAKE;
            else if (gamepad1.left_trigger > 0.5) m_IntakeSys.SpintakeMode = IntakeDepositSystem_DC.SpintakeModes.SPIT;
            else m_IntakeSys.SpintakeMode = IntakeDepositSystem_DC.SpintakeModes.STOP;

            // slow down anti - tip
            double factor = 1.0;
            if (!(gamepad1.left_stick_y * -1 > 0.5)) {
                if (m_IntakeSys.CurrentLocationState > IntakeDepositSystem_DC.LOW_POSITION)
                    factor = 0.5;
            }

            if (gamepad1.b)
                m_IntakeSys.DriverPressedButtonB = true;

            m_Drive.Update(gamepad1.left_stick_x * factor * -1, gamepad1.left_stick_y * factor, gamepad1.right_stick_x * factor );
            m_IntakeSys.UpdateStateMachine(false);

            if (gamepad1.start)
                m_Launcher.setPosition(1.0);
        }

    }
}
