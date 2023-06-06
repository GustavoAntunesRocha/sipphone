package br.com.view;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

public class AccountSettingsWindow {
    
    @FXML
    private Text accountName;

    @FXML
    private Text accountDomain;

    @FXML
    private Text accountUsername;

    @FXML
    private Text accountPassword;

    @FXML
    private TextField accountNameField;

    @FXML
    private TextField accountDomainField;

    @FXML
    private TextField accountUsernameField;

    @FXML
    private PasswordField accountPasswordField;

    public AccountSettingsWindow(){
        accountName = new Text();
        accountDomain = new Text();
        accountUsername = new Text();
        accountPassword = new Text();
        accountNameField = new TextField();
        accountDomainField = new TextField();
        accountUsernameField = new TextField();
        accountPasswordField = new PasswordField();
    }

    @FXML
    private void handleSave() {
        // TODO
    }

    @FXML
    private void handleCancel() {
        // TODO
    }

    @FXML
    private void handleDelete() {
        // TODO
    }

    public void setFields(String name, String domain, String username, String password) {
        this.accountNameField.setText(name);
        this.accountDomainField.setText(domain);
        this.accountUsernameField.setText(username);
        this.accountPasswordField.setText(password);
    }


}
