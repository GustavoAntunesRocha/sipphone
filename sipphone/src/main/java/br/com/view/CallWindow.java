package br.com.view;

import java.io.IOException;

import br.com.App;
import br.com.controller.CallController;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;

public class CallWindow {
    
    @FXML
    private Label callerNumber = new Label();

    @FXML
    private Label callerName = new Label();

    public CallWindow(){
        try {
            callerName.setText(CallController.getInstance().getCallEntity().getInfo().getRemoteContact());
            callerNumber.setText(CallController.getInstance().getCallEntity().getInfo().getRemoteUri());
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @FXML
    private void handleHangUp() {
        CallController.getInstance().handleHangupButtonAction(CallController.getInstance().getCallEntity());
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
