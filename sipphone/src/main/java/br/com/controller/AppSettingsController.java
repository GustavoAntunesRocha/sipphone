package br.com.controller;

import br.com.App;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class AppSettingsController {
    
    private static AppSettingsController instance;

    private Stage stage;
    private Scene scene;

    private FXMLLoader loader;

    private AppSettingsController() {
    }

    public static AppSettingsController getInstance() {
        if (instance == null) {
            instance = new AppSettingsController();
        }
        return instance;
    }

    public void showAppSettingsWindow() {
        try {
            this.stage = new Stage();
            this.stage.setTitle("Application Settings");
            this.loader = new FXMLLoader(App.class.getResource("AppSettingsWindow.fxml"));
            Parent root = loader.load();
            this.scene = new Scene(root);
            this.stage.setScene(this.scene);
            this.stage.show();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
