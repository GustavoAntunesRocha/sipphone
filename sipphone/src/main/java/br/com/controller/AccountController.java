package br.com.controller;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.pjsip.pjsua2.PresenceStatus;
import org.pjsip.pjsua2.pjsip_transport_type_e;
import org.pjsip.pjsua2.pjsua_buddy_status;
import org.pjsip.pjsua2.pjrpid_activity;

import br.com.App;
import br.com.model.AccountConfigModel;
import br.com.model.AccountEntity;
import br.com.model.CallEntity;
import br.com.model.TransportConfigModel;
import br.com.model.AccountEntity.Status;
import br.com.model.CallHistoryEntry;
import br.com.view.AccountSettingsWindow;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class AccountController {
    
    private static AccountController instance;

    private Stage stage;
    private Scene scene;

    private FXMLLoader loader;

    public static AccountController getInstance() {
        if (instance == null) {
            instance = new AccountController();
        }
        return instance;
    }

    private AccountController() {
        instance = this;
    }

    public void handleAccountSettingsWindow() {
        AccountEntity accountEntity = App.acc;
        try {
            this.stage = new Stage();
            this.stage.setTitle("Account Settings");
            this.loader = new FXMLLoader(App.class.getResource("AccountSettingsWindow.fxml"));
            Parent root = loader.load();
            if(accountEntity != null){
                AccountSettingsWindow controller = loader.getController();
                controller.setFields(accountEntity.getName(), 
                    accountEntity.getAccountConfigModel().getDomain(), 
                    accountEntity.getAccountConfigModel().getUsername(),
                    accountEntity.getAccountConfigModel().getPassword());
            }
            this.scene = new Scene(root);
            this.stage.setScene(this.scene);
            this.stage.show();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void handleSaveAccount(){
        AccountEntity accountEntity = App.acc;
        AccountSettingsWindow controller = loader.getController();
        accountEntity.setAccountConfigModel(new AccountConfigModel(
                    controller.getAccountUsernameField().getText(), controller.getAccountPasswordField().getText(), 
                    controller.getAccountDomainField().getText(), "digest", "*", 0));
                accountEntity.setTransportConfigModel(new TransportConfigModel(
                    5060, pjsip_transport_type_e.PJSIP_TRANSPORT_UDP));
                accountEntity.setId(0);
                accountEntity.setName(controller.getAccountNameField().getText());
        AccountEntity.writeAccountToFile(accountEntity);
        App.connectSipServer();
        Platform.runLater(() -> {
            MainController.getInstance().updateAccountText();
        });
        this.stage.close();
    }

    public void handleDeleteAccount(){
        App.deleteLibrary();
        AccountEntity.deleteAccountFile();
        Platform.runLater(() -> {
            MainController.getInstance().updateAccountText();
        });
        this.stage.close();
    }

    public void handlePresence(String menuItemText) {
        PresenceStatus status = new PresenceStatus();
        AccountEntity accountEntity = App.acc;
        try {
            switch (menuItemText) {
                case "Online":
                    status.setStatus(pjsua_buddy_status.PJSUA_BUDDY_STATUS_ONLINE);
                    accountEntity.setOnlineStatus(status);
                    accountEntity.setStatus(Status.ONLINE);
                    break;
                case "Offline":
                    status.setStatus(pjsua_buddy_status.PJSUA_BUDDY_STATUS_OFFLINE);
                    accountEntity.setOnlineStatus(status);
                    accountEntity.setStatus(Status.OFFLINE);
                    break;
                case "Away":
                    status.setStatus(pjsua_buddy_status.PJSUA_BUDDY_STATUS_ONLINE);
                    status.setActivity(pjrpid_activity.PJRPID_ACTIVITY_AWAY);
                    accountEntity.setOnlineStatus(status);
                    accountEntity.setStatus(Status.AWAY);
                    break;
                case "Busy":
                    status.setStatus(pjsua_buddy_status.PJSUA_BUDDY_STATUS_ONLINE);
                    status.setActivity(pjrpid_activity.PJRPID_ACTIVITY_BUSY);
                    accountEntity.setOnlineStatus(status);
                    accountEntity.setStatus(Status.BUSY);
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public CallHistoryEntry addCallHistoryEntry(CallEntity call){
        try {
            String name = "";
            String number = "";
            if(call.getInfo().getRemoteUri().contains("\"")){
                String[] split = call.getInfo().getRemoteUri().split("\"");
                name = split[1];
                number = split[2].split(":")[1].split("@")[0];
            } else{
                name = call.getInfo().getRemoteUri().split(":")[1].split("@")[0];
                number = name;
            }
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss dd-MM-yyyy");
            String formattedDateTime = now.format(formatter);
            int minutes = call.getInfo().getConnectDuration().getSec() / 60;
            int seconds = call.getInfo().getConnectDuration().getSec() % 60;
            String callDuration = String.format("%d:%02d", minutes, seconds);
            CallHistoryEntry callHistoryEntry = new CallHistoryEntry(name,
                        number, formattedDateTime,
                        callDuration,
                        call.getInfo().getLastReason());
            App.acc.addCallHistoryEntry(callHistoryEntry);
            AccountEntity.writeAccountToFile(App.acc);
            return callHistoryEntry;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }
}
