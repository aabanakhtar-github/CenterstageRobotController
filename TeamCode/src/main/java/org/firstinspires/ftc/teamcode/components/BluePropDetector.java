package org.firstinspires.ftc.teamcode.components;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.hardwareMap;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Size;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.hardware.camera.CameraName;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.internal.camera.calibration.CameraCalibration;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.VisionProcessor;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

public class BluePropDetector {

    public enum Positions {
        LEFT, RIGHT, MIDDLE
    }

    Pipeline m_Pipeline;

    VisionPortal m_VisionPortal;

    public BluePropDetector(HardwareMap hwmap) {
        m_Pipeline = new Pipeline();
        m_VisionPortal = new VisionPortal.Builder()
                .setCamera(hwmap.get(CameraName.class, "Webcam 1"))
                .setCameraResolution(new Size(800, 600))
                .addProcessor(m_Pipeline)
                .enableLiveView(true)
                .build();
    }

    public Positions GetDetectionPosition() {
        return m_Pipeline.GetDetectionPosition();
    }


    public static class Pipeline implements VisionProcessor {


        private int LeftPercentage = 0, RightPercentage = 0, MiddlePercentage = 0;

        private int m_ScreenWidth, m_ScreenHeight;

        private Positions m_DetectionPosition = Positions.MIDDLE;

        private Rect m_LeftROI, m_RightROI, m_CenterROI;

        @Override
        public void init(int width, int height, CameraCalibration calibration) {
            m_ScreenHeight = height;
            m_ScreenWidth = width;
            m_LeftROI = new Rect(0, height * 1 / 2, width / 3, height / 2);
            m_CenterROI = new Rect(width / 3, height * 1 / 2, width / 3, height / 2);
            m_RightROI = new Rect(width * 2 / 3, height * 1 / 2, width / 3, height / 2);
        }

        @Override
        public Object processFrame(Mat input, long captureTimeNanos) {
            Imgproc.cvtColor(input, input, Imgproc.COLOR_RGB2HSV);
            // scalar HSV color ranges for blue things
            Scalar lower_hsv = new Scalar(90, 50, 70);
            Scalar higher_hsv = new Scalar(150, 255, 255);

            Core.inRange(input, lower_hsv, higher_hsv, input);
            Mat left = input.submat(m_LeftROI);
            Mat right = input.submat(m_RightROI);
            Mat center = input.submat(m_CenterROI);
            // Get the average percentage covered in blue
            // get the total amount of white pixels / the max hsv value (make it BOOL) / divided by area * 100 to get the pct
            LeftPercentage = (int) (Core.sumElems(left).val[0] / 360.0 / m_LeftROI.area() * 100.0);
            RightPercentage = (int) (Core.sumElems(right).val[0] / 360.0 / m_RightROI.area() * 100.0);
            MiddlePercentage = (int) (Core.sumElems(center).val[0] / 360.0 / m_CenterROI.area() * 100.0);

            int best = Math.max(Math.max(LeftPercentage, RightPercentage), MiddlePercentage);
            if (best == LeftPercentage) m_DetectionPosition = Positions.LEFT;
            else if (best == MiddlePercentage) m_DetectionPosition = Positions.MIDDLE;
            else if (best == RightPercentage) m_DetectionPosition = Positions.RIGHT;

            Imgproc.rectangle(input, m_LeftROI, new Scalar(180, 255, 255));
            Imgproc.rectangle(input, m_RightROI, new Scalar(180, 255, 255));
            Imgproc.rectangle(input, m_CenterROI, new Scalar(180, 255, 255));

            left.release();
            right.release();
            center.release();

            return input;
        }

        @Override
        public void onDrawFrame(Canvas canvas, int onscreenWidth, int onscreenHeight, float scaleBmpPxToCanvasPx, float scaleCanvasDensity, Object userContext) {
        }

        public Positions GetDetectionPosition() {
            return m_DetectionPosition;
        }
    }
}
