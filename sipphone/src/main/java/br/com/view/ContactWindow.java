package br.com.view;

import br.com.controller.ContactController;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;

public class ContactWindow {
    
    @FXML
    private TextField contactNameField;

    @FXML
    private TextField contactNumberField;

    @FXML
    private TextField contactEmailField;

    @FXML
    private CheckBox presenceSubCheckBox;

    public ContactWindow() {
        this.contactNameField = new TextField();
        this.contactNumberField = new TextField();
        this.contactEmailField = new TextField();
        this.presenceSubCheckBox = new CheckBox();
    }

    public void setFields(String name, String phoneNumber, String email, boolean presenceSubscription){
        contactNameField.setText(name);
        contactNumberField.setText(phoneNumber);
        contactEmailField.setText(email);
        presenceSubCheckBox.setSelected(presenceSubscription);
    }

    public void handleSave(){
        String name = contactNameField.getText();
        String phoneNumber = contactNumberField.getText();
        String email = contactEmailField.getText();
        boolean presenceSubscription = presenceSubCheckBox.isSelected();
        ContactController.getInstance().addContact(name, phoneNumber, email, presenceSubscription);
    }

    public void handleCancel(){
        // TODO
    }

    public void handleDelete(){
        // TODO
    }
}
