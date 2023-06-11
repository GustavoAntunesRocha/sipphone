package br.com.controller;

import java.io.IOException;

import org.pjsip.pjsua2.PresenceStatus;
import org.pjsip.pjsua2.pjsip_transport_type_e;
import org.pjsip.pjsua2.pjsua_buddy_status;
import org.pjsip.pjsua2.pjrpid_activity;

import br.com.App;
import br.com.model.AccountConfigModel;
import br.com.model.AccountEntity;
import br.com.model.TransportConfigModel;
import br.com.model.AccountEntity.Status;
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
        AccountEntity accountEntity = MainController.getInstance().getAccountEntity();
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
        AccountEntity accountEntity = new AccountEntity();
        AccountSettingsWindow controller = loader.getController();
        accountEntity.setAccountConfigModel(new AccountConfigModel(
                    controller.getAccountUsernameField().getText(), controller.getAccountPasswordField().getText(), 
                    controller.getAccountDomainField().getText(), "digest", "*", 0));
                accountEntity.setTransportConfigModel(new TransportConfigModel(
                    5060, pjsip_transport_type_e.PJSIP_TRANSPORT_UDP));
                accountEntity.setId(1);
                accountEntity.setName(controller.getAccountNameField().getText());
        if(App.getAcc() != null){
            App.getAcc().delete();
            MainController.getInstance().setAccountEntity(null);
        }
        AccountEntity.writeAccountToFile(accountEntity, "account.ser");
        App.setAcc(accountEntity);
        App.connectSipServer();
        Platform.runLater(() -> {
            MainController.getInstance().updateAccountText();
        });
        this.stage.close();
    }

    public void handleDeleteAccount(){
        App.deleteLibrary();
        MainController.getInstance().setAccountEntity(null);
        App.setAcc(null);
        AccountEntity.deleteAccountFile("account.ser");
        Platform.runLater(() -> {
            MainController.getInstance().updateAccountText();
        });
        this.stage.close();
    }

    public void handlePresence(String menuItemText) {
        PresenceStatus status = new PresenceStatus();
        try {
            switch (menuItemText) {
                case "Online":
                    status.setStatus(pjsua_buddy_status.PJSUA_BUDDY_STATUS_ONLINE);
                    App.getAcc().setOnlineStatus(status);
                    App.getAcc().setStatus(Status.ONLINE);
                    break;
                case "Offline":
                    status.setStatus(pjsua_buddy_status.PJSUA_BUDDY_STATUS_OFFLINE);
                    App.getAcc().setOnlineStatus(status);
                    App.getAcc().setStatus(Status.OFFLINE);
                    break;
                case "Away":
                    status.setStatus(pjsua_buddy_status.PJSUA_BUDDY_STATUS_ONLINE);
                    status.setActivity(pjrpid_activity.PJRPID_ACTIVITY_AWAY);
                    App.getAcc().setOnlineStatus(status);
                    App.getAcc().setStatus(Status.AWAY);
                    break;
                case "Busy":
                    status.setStatus(pjsua_buddy_status.PJSUA_BUDDY_STATUS_ONLINE);
                    status.setActivity(pjrpid_activity.PJRPID_ACTIVITY_BUSY);
                    App.getAcc().setOnlineStatus(status);
                    App.getAcc().setStatus(Status.BUSY);
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}