package org.firstinspires.ftc.teamcode.tests;

import com.arcrobotics.ftclib.drivebase.MecanumDrive;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.teamcode.components.DriveTrain;

@TeleOp(name = "Field Centric Drive Test")
public class FieldCentricTest extends LinearOpMode {
    private DriveTrain m_Drivetrain;

    @Override
    public void runOpMode() throws InterruptedException {
        m_Drivetrain = new DriveTrain(hardwareMap);
        waitForStart();

        while (opModeIsActive() && !isStopRequested()) {
            m_Drivetrain.Update(gamepad1.left_stick_x , -gamepad1.left_stick_y, gamepad1.right_stick_x);
        }
    }
}
