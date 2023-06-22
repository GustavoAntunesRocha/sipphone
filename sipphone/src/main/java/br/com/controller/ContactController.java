package br.com.controller;

import br.com.App;
import br.com.model.AccountEntity;
import br.com.model.Contact;
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

    public void addContact(String name, String phoneNumber, String email, boolean presenceSubscription) {
        int id = 0;
        if(!App.acc.getContacts().isEmpty()){
            id = App.acc.getContacts().get(App.acc.getContacts().size() - 1).getId() + 1;
        }
        Contact contact = new Contact(id, name, phoneNumber, email, presenceSubscription);
        App.acc.getContacts().add(contact);
        AccountEntity.writeAccountToFile(App.acc);
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                MainWindow.getInstance().addContact(contact);
            }
        });
        this.stage.close();
    }
}
