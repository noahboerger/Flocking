package gui;

import configuration.Configuration;
import configuration.IConfigurationSceneListener;
import javafx.stage.Stage;

public class GUIControl implements IConfigurationSceneListener {

    private Stage primaryStage;

    public GUIControl(Stage primaryStage) {
        this.primaryStage = primaryStage;
        primaryStage.setResizable(false);
    }

    public void start() {
        ConfigurationScene configurationScene = new ConfigurationScene(this);
        primaryStage.setScene(configurationScene);
        primaryStage.show();
    }

    @Override
    public void receiveConfigurationToChangeScene(Configuration configuration) {
        primaryStage.close();

        Stage stage = new Stage();
        FlockingScene flockingScene = new FlockingScene(configuration);
        stage.setScene(flockingScene);
        stage.show();
    }
}

