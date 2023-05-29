package br.com.view;

import java.io.IOException;

import br.com.App;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;

public class CallWindow {
    
    @FXML
    private Label callerNumber;

    @FXML
    private Label callerName;

    @FXML
    private void handleHangUp() {
        // Handle hang up button click
    }

    @FXML
    private void handleAnswer() {
        // Handle answer button click
    }

    public void setCallerNumber(String callerNumber) {
        this.callerNumber.setText(callerNumber);
    }

    public void setCallerName(String callerName) {
        this.callerName.setText(callerName);
    }

    public Scene getScene() throws IOException {
        return new Scene(App.loadFXML("CallWindow"));
    }
}
