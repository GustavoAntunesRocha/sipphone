package br.com.controller;

import java.io.IOException;

import org.pjsip.pjsua2.CallOpParam;

import br.com.model.AccountEntity;
import br.com.model.CallEntity;
import br.com.view.CallWindow;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class CallController {

    private CallEntity callEntity;
    
    private static CallController instance;

    private Stage stage;

    private Alert alert;

    private CallController() {}

    public static CallController getInstance(){
        if(instance == null){
            instance = new CallController();
        }
        return instance;
    }

    public void setCallWindow(CallWindow callWindow, String callerNumber, String callerName){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                stage = new Stage();
                try {
                    stage.setScene(callWindow.getScene());
                    stage.show();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        });

        
    }

    public boolean answerInput(CallEntity call){
        CallOpParam callParam = new CallOpParam();
        this.callEntity = call;
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Alert alert;
                try {
                    alert = new Alert(Alert.AlertType.CONFIRMATION, "Answer call from: " + call.getInfo().getRemoteContact(), ButtonType.YES, ButtonType.NO);
                    alert.initModality(Modality.APPLICATION_MODAL); // Set the modality to APPLICATION_MODAL
                    alert.showAndWait(); // Show the alert and wait for it
                    if(alert.getResult() == ButtonType.YES){
                        callParam.setStatusCode(200);
                        call.answer(callParam);
                    }else{
                        callParam.setStatusCode(603);
                        call.hangup(callParam);
                    }
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                
            }
        });
        if(callParam.getStatusCode() == 200){
            return true;
        }else{  
            return false;
        }
    }

    public void handleHangupButtonAction(CallEntity call) {
        try {
            if(call != null){
                CallOpParam callParam = new CallOpParam();
                callParam.setStatusCode(603);
                call.hangup(callParam);
                this.stage.close();
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } // Call the hangup method on the CallEntity object
    }

    public CallEntity getCallEntity() {
        return callEntity;
    }

    public void setCallEntity(CallEntity callEntity) {
        this.callEntity = callEntity;
    }

    public void handleCall(String number){
        this.callEntity = CallEntity.makeCall(AccountEntity.getInstance(), number);
    }

    public void showCallingAlert(){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Alert alert;
                try {
                    alert = new Alert(Alert.AlertType.INFORMATION, "Calling: " + CallController.getInstance().getCallEntity().getInfo().getRemoteUri(), ButtonType.CANCEL);
                    alert.initModality(Modality.APPLICATION_MODAL); // Set the modality to APPLICATION_MODAL
                    CallController.getInstance().setAlert(alert);
                    alert.show();
                    if(alert.getResult() == ButtonType.CANCEL){
                        handleHangupButtonAction(CallController.getInstance().getCallEntity());
                        closeAlertWindow();
                    }
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                
            }
        });
    }

    public void closeAlertWindow(){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                CallController.getInstance().getAlert().close();
            }
        });
    }

    public Alert getAlert() {
        return alert;
    }

    public void setAlert(Alert alert) {
        this.alert = alert;
    }

    public void closeCallWindow(){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                CallController.getInstance().getStage().close();
            }
        });
    }

    public Stage getStage() {
        return stage;
    }
    

}
