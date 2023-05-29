package br.com.controller;

import java.io.IOException;

import br.com.view.CallWindow;
import javafx.stage.Stage;

public class CallController {
    
    private CallWindow callWindow;

    public void setCallWindow(CallWindow callWindow, String callerNumber, String callerName){
        this.callWindow = callWindow;
        callWindow.setCallerNumber(callerNumber);
        callWindow.setCallerName(callerName);

        Stage stage = new Stage();
        try {
            stage.setScene(callWindow.getScene());
            stage.show();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
