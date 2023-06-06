package br.com.controller;

import java.io.IOException;

import br.com.App;
import br.com.model.AccountEntity;
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
            FXMLLoader loader = new FXMLLoader(App.class.getResource("AccountSettingsWindow.fxml"));
            Parent root = loader.load();
            AccountSettingsWindow controller = loader.getController();
            controller.setFields(accountEntity.getName(), 
                accountEntity.getAccountConfigModel().getDomain(), 
                accountEntity.getAccountConfigModel().getUsername(), 
                accountEntity.getAccountConfigModel().getPassword());
            this.scene = new Scene(root);
            this.stage.setScene(this.scene);
            this.stage.show();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
