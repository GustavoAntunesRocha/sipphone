package br.com.view;

import java.io.IOException;

import br.com.App;
import br.com.controller.CallController;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.text.Text;

public class CallWindow {
    
    @FXML
    private Text callerNumber;

    @FXML
    private Text callerName;

    public void initialize() {
        try {
            
            this.callerNumber.setText("In call with: " + CallController.getInstance().getCallEntity().getInfo().getRemoteUri());
            this.callerName.setText(CallController.getInstance().getCallEntity().getInfo().getRemoteContact());
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public CallWindow(){
        try {
            this.callerNumber = new Text();
            this.callerName = new Text();
            
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
        this.callerNumber.setText("In call with: " + callerNumber);
    }

    public void setCallerName(String callerName) {
        this.callerName.setText(callerName);
    }

    public Scene getScene() throws IOException {
        return new Scene(App.loadFXML("CallWindow"));
    }
}
