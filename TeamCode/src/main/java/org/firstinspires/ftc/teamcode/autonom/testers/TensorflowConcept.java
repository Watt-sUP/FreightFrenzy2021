package org.firstinspires.ftc.teamcode.autonom.testers;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import java.util.List;
import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.State;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer.CameraDirection;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.teamcode.hardware.Config;
import org.firstinspires.ftc.teamcode.hardware.Cupa;
import org.firstinspires.ftc.teamcode.hardware.Glisiere;

@TeleOp(name = "Concept: TensorFlow Object Detection", group = "Concept")
@Disabled
public class TensorflowConcept extends LinearOpMode {

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

    Glisiere glisiera = new Glisiere(hardwareMap, telemetry);

    @Override
    public void runOpMode() {
        initVuforia();
        initTfod();

        if (tfod != null) {
            tfod.activate();
            tfod.setZoom(1.5, 16.0/9.0);
        }

        while (!opModeIsActive()) {
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
            }
        waitForStart();
        while (opModeIsActive()) {
            if(markerLocation == Locations.Top) glisiera.setToPosition(3);
            else if(markerLocation == Locations.Middle) glisiera.setToPosition(2);
            else if(markerLocation == Locations.Bottom) glisiera.setToPosition(1);
        }
    }



    private void initVuforia() {
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();

        parameters.vuforiaLicenseKey = VUFORIA_KEY;
        parameters.cameraDirection = CameraDirection.BACK;

        vuforia = ClassFactory.getInstance().createVuforia(parameters);
    }

    private void initTfod() {
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
