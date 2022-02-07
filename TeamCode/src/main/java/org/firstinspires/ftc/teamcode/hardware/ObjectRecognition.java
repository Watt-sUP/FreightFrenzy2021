package org.firstinspires.ftc.teamcode.hardware;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer.CameraDirection;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;

import java.util.List;

public class ObjectRecognition {

    private static final String TFOD_MODEL_ASSET = "FreightFrenzy_DM.tflite";
    private static final String[] LABELS = {
            "Duck",
            "Team Marker"
    };

    private static final String VUFORIA_KEY = Config.VuforiaKey;
    private VuforiaLocalizer vuforia;
    private TFObjectDetector tfod;
    private enum Locations {Top, Middle, Bottom};
    private Locations markerLocation = null;
    private Telemetry telemetry;
    private HardwareMap hardwareMap;

    public ObjectRecognition(HardwareMap hardwareMap, Telemetry telemetry) {
        this.telemetry = telemetry;
    }

    public int runTF() {
        if (tfod != null) {
            tfod.activate();
            tfod.setZoom(1.5, 16.0/9.0);
        }

            if (tfod != null) {
                List<Recognition> updatedRecognitions = tfod.getUpdatedRecognitions();
                if (updatedRecognitions != null) {
                    telemetry.addData("# Object Detected", updatedRecognitions.size());

                    int i = 0;
                    markerLocation = Locations.Bottom;
                    for (Recognition recognition : updatedRecognitions) {
                        if(recognition.getLabel() == "Duck") {
                            telemetry.addData("Team Marker:", "Detected");
                            if(((recognition.getRight() - recognition.getLeft()) / 2 + recognition.getLeft()) < (recognition.getImageWidth() / 2)) markerLocation = Locations.Middle;
                            else if(((recognition.getRight() - recognition.getLeft()) / 2 + recognition.getLeft()) >= (recognition.getImageWidth() / 2)) markerLocation = Locations.Top;
                            break;
                        }
                        if(recognition.getLabel() != "Duck") telemetry.addData("Team Marker:", "Not detected");
                        i++;
                    }
                    telemetry.addData("Marker Location:", markerLocation.toString());
                    telemetry.update();
                }
            }
            if(markerLocation == Locations.Bottom) return 1;
            else if(markerLocation == Locations.Middle) return 2;
            else return 3;
        }




    public void initVuforia() {
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();

        parameters.vuforiaLicenseKey = Config.VuforiaKey;
        parameters.cameraDirection = CameraDirection.BACK;

        vuforia = ClassFactory.getInstance().createVuforia(parameters);
    }

    public void initTfod() {
        int tfodMonitorViewId = hardwareMap.appContext.getResources().getIdentifier(
                "tfodMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(tfodMonitorViewId);
        tfodParameters.minResultConfidence = 0.5f;
        tfodParameters.isModelTensorFlow2 = true;
        tfodParameters.inputSize = 320;
        tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia);
        tfod.loadModelFromAsset(TFOD_MODEL_ASSET, LABELS);
    }
}
