package br.com.view;

import java.util.List;

import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;

public class AppSettingsWindow {

    @FXML
    private ChoiceBox<String> listeningDeviceChoiceBox;

    @FXML
    private ChoiceBox<String> ringDeviceChoiceBox;
    
    @FXML
    private ChoiceBox<String> inputDeviceChoiceBox;

    public void setListeningDevice(List<String> devices) {
        listeningDeviceChoiceBox.getItems().addAll(devices);
    }

    public void setRingDevice(List<String> devices) {
        ringDeviceChoiceBox.getItems().addAll(devices);
    }

    public void setInputDevice(List<String> devices) {
        inputDeviceChoiceBox.getItems().addAll(devices);
    }

    @FXML
    private void handleSave() {
        // TODO
    }

    @FXML
    private void handleCancel() {
        // TODO
    }

}
