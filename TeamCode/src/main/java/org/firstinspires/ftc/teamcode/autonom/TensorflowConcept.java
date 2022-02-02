package org.firstinspires.ftc.teamcode.autonom;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;
import org.firstinspires.ftc.teamcode.hardware.Config;

import java.util.List;

@Autonomous(name = "Concept Tensorflow", group = "Testing")
public class TensorflowConcept extends LinearOpMode {

    String tfodModelName = "FreightFrenzy_BCDM.tflite";
    String[] labels = {"Ball", "Cube", "Duck", "Team Marker"};

    private final String VuforiaKey = Config.VuforiaKey;
    private VuforiaLocalizer vuforia;
    private TFObjectDetector tfod;

    private void initVuforia() {
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();

        parameters.vuforiaLicenseKey = VuforiaKey;
        parameters.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;

        vuforia = ClassFactory.getInstance().createVuforia(parameters);
    }

    private void initTfod() {
        int tfodId = hardwareMap.appContext.getResources().getIdentifier("tfodMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(tfodId);

        tfodParameters.minResultConfidence = 0.8f;
        tfodParameters.isModelTensorFlow2 = true;
        tfodParameters.inputSize = 320;

        tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters ,vuforia);
        tfod.loadModelFromAsset(tfodModelName, labels);
    }

    @Override
    public void runOpMode() throws InterruptedException {

        initVuforia();
        initTfod();

        if(tfod != null) {
            tfod.activate();
            tfod.setZoom(1, 16.0/9.0);
        }

        waitForStart();

        while (opModeIsActive()) {
            if(tfod != null) {
                List<Recognition> updatedRecognitions = tfod.getUpdatedRecognitions();
                if(updatedRecognitions != null) {
                    telemetry.addData("Object(s) detected", updatedRecognitions.size());
                    int i = 0;
                    for (Recognition recognition : updatedRecognitions) {
                        telemetry.addData(String.format("Label %d", i+1), recognition.getLabel());
                        i++;
                    }
                    telemetry.update();
                }
            }
        }
    }
}
