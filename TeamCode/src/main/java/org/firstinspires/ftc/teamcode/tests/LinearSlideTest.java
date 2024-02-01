package org.firstinspires.ftc.teamcode.tests;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.components.IntakeDepositSystem_DC;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Config
@TeleOp(name = "Linear Slide Test")
public class LinearSlideTest extends LinearOpMode {

    private ElapsedTime m_Cooldown = new ElapsedTime();

    private IntakeDepositSystem_DC m_IntakeSys;

    // humans are slow
    public static double COOLDOWN_TIME = 1;

    @Override
    public void runOpMode() throws InterruptedException {
        m_IntakeSys = new IntakeDepositSystem_DC(hardwareMap);
        int counter = 0;
        List<Integer> extension_states = Arrays.asList(
                IntakeDepositSystem_DC.SET_LINE_1,
                IntakeDepositSystem_DC.SET_LINE_2,
                IntakeDepositSystem_DC.SET_LINE_3
        );

        waitForStart();
        m_Cooldown.reset();

        while (opModeIsActive() && !isStopRequested()) {

            if (gamepad1.dpad_up && m_Cooldown.time(TimeUnit.MILLISECONDS) > (int)(COOLDOWN_TIME * 1000.0)){//m_Cooldown.time() > 0.5) {
                if (counter > 2) counter = 0;
                m_IntakeSys.CurrentLocationState = extension_states.get(counter);
                counter += 1;
                m_Cooldown.reset();
            }
            else if (gamepad1.dpad_down) {
                m_IntakeSys.CurrentLocationState = IntakeDepositSystem_DC.LOW_POSITION;
            }

            if (gamepad1.y) m_IntakeSys.SpintakeMode = IntakeDepositSystem_DC.SpintakeModes.INTAKE;
            else m_IntakeSys.SpintakeMode = IntakeDepositSystem_DC.SpintakeModes.STOP;

            m_IntakeSys.UpdateStateMachine(false);
        }
    }
}
