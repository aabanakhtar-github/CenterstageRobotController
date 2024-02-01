package org.firstinspires.ftc.teamcode.tests;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;

@TeleOp(name = "Spintake Test")
public class SpintakeTest extends LinearOpMode {
    private DcMotorEx m_Spintake;

    @Override
    public void runOpMode() throws InterruptedException {
        m_Spintake = hardwareMap.get(DcMotorEx.class, "spintake");

        waitForStart();

        m_Spintake.setPower(1.0);
        while (!isStopRequested() && opModeIsActive()) {

        }
    }
}
