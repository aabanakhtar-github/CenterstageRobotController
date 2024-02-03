package org.firstinspires.ftc.teamcode.components;

import com.arcrobotics.ftclib.drivebase.MecanumDrive;
import com.arcrobotics.ftclib.hardware.motors.Motor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.hardware.ImuOrientationOnRobot;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

import java.util.Arrays;

public class DriveTrain {

    MecanumDrive m_MecanumDrivebase;

    IMU m_IMU;

    public DriveTrain(HardwareMap hwmap) {
        Motor tl = new Motor(hwmap, "leftFront");
        Motor tr = new Motor(hwmap, "rightFront");
        Motor bl = new Motor(hwmap, "leftBack");
        Motor br = new Motor(hwmap, "rightBack");

        Arrays.asList(tl, br, bl, br).forEach(x -> {
            x.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE);
        });

        m_MecanumDrivebase = new MecanumDrive(tl, tr, bl, br);

        m_IMU = hwmap.get(IMU.class, "imu");
        IMU.Parameters params;
        m_IMU.resetYaw();
    }

    public void Update(double x, double y, double rx) {

        m_MecanumDrivebase.driveRobotCentric(x * 0.5, y, rx);
    }
}
