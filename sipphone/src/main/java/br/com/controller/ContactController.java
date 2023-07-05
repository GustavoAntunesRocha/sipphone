package br.com.controller;

import br.com.App;
import br.com.model.AccountEntity;
import br.com.model.Contact;
import br.com.view.ContactWindow;
import br.com.view.MainWindow;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ContactController {

    private static ContactController instance;

    private Stage stage;
    private Scene scene;

    private FXMLLoader loader;

    public static ContactController getInstance() {
        if (instance == null) {
            instance = new ContactController();
        }
        return instance;
    }
    
    public ContactController() {
        
    }

    public void handleContactWindow(){
        try {
            this.stage = new Stage();
            this.stage.setTitle("Add Contact");
            this.loader = new FXMLLoader(App.class.getResource("ContactWindow.fxml"));
            Parent root = loader.load();
            this.scene = new Scene(root);
            this.stage.setScene(this.scene);
            this.stage.show();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void editContact(Contact contact){
        handleContactWindow();
        ContactWindow contactWindow = this.loader.getController();
        contactWindow.setFields(contact.getContactName(), contact.getContactNumber(), contact.getEmail(), contact.isPresenceSubscription());
    }

    public void addContact(String name, String phoneNumber, String email, boolean presenceSubscription) {
        Contact existingContact = null;
        for (Contact contact : App.acc.getContacts()) {
            if (contact.getContactNumber().equals(phoneNumber)) {
                existingContact = contact;
                break;
            }
        }
        if (existingContact != null) {
            // Update the existing contact with the new information
            existingContact.setContactName(name);
            existingContact.setEmail(email);
            existingContact.setPresenceSubscription(presenceSubscription);
            App.acc.getContacts().set(App.acc.getContacts().indexOf(existingContact), existingContact);
            Platform.runLater(() -> MainWindow.getInstance().updateContactTable());
        } else {
            // Add a new contact
            int id = 0;
            if (!App.acc.getContacts().isEmpty()) {
                id = App.acc.getContacts().get(App.acc.getContacts().size() - 1).getId() + 1;
            }
            Contact contact = new Contact(id, name, phoneNumber, email, presenceSubscription);
            App.acc.getContacts().add(contact);
            Platform.runLater(() -> MainWindow.getInstance().addContact(contact));
        }
        AccountEntity.writeAccountToFile(App.acc);
        this.stage.close();
    }
}
