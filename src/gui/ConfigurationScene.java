package gui;

import configuration.Configuration;
import configuration.IConfigurationSceneListener;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.VBox;

class ConfigurationScene extends Scene {

    ConfigurationScene(IConfigurationSceneListener listener) {
        super(new VBox(), 200, 70);
        VBox parent = (VBox) getRoot();

        Label numberOfBirdsTextLabel = new Label("Number of Birds:");
        parent.getChildren().add(numberOfBirdsTextLabel);

        Label numberOfBirdsLabel = new Label();
        parent.getChildren().add(numberOfBirdsLabel);

        Slider numberOfBirdsSlider = new Slider();
        numberOfBirdsSlider.setMin(1);
        numberOfBirdsSlider.setMax(250);
        numberOfBirdsSlider.valueProperty().addListener((obs, oldval, newVal) ->
                numberOfBirdsSlider.setValue((int) Math.round(newVal.doubleValue())));
        parent.getChildren().add(numberOfBirdsSlider);

        numberOfBirdsLabel.textProperty().bind(numberOfBirdsSlider.valueProperty().asString());

        Button startButton = new Button("Starten");

        startButton.setOnMouseClicked(event -> {
            if (event.getButton().equals(MouseButton.PRIMARY)) {
                listener.receiveConfigurationToChangeScene(new Configuration((int) numberOfBirdsSlider.getValue()));
            }
        });

        parent.getChildren().add(startButton);
    }
}
