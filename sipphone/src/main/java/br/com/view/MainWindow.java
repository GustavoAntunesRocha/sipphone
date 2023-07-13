package br.com.view;

import br.com.App;
import br.com.controller.AccountController;
import br.com.controller.AppSettingsController;
import br.com.controller.CallController;
import br.com.controller.ContactController;
import br.com.controller.MainController;
import br.com.model.CallHistoryEntry;
import br.com.model.Contact;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.text.Text;

public class MainWindow {

    @FXML
    private Text username;

    @FXML
    private Text domain;

    @FXML
    private Text status;

    @FXML
    private TextField numberField;

    @FXML
    private Button callButton;

    @FXML
    private TableView<CallHistoryEntry> callHistoryTable;

    @FXML
    private TableColumn<CallHistoryEntry, String> name;

    @FXML
    private TableColumn<CallHistoryEntry, String> number;

    @FXML
    private TableColumn<CallHistoryEntry, String> date;

    @FXML
    private TableColumn<CallHistoryEntry, String> duration;

    @FXML
    private TableColumn<CallHistoryEntry, String> info;

    @FXML
    private TableView<Contact> contactTable;

    @FXML
    private TableColumn<Contact, String> contactName;

    @FXML
    private TableColumn<Contact, String> contactNumber;

    @FXML
    private TableColumn<Contact, String> contactPresence;

    @FXML
    private MenuItem editMenuItem;

    private static MainWindow instance;

    public void initialize() {
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        number.setCellValueFactory(new PropertyValueFactory<>("number"));
        date.setCellValueFactory(new PropertyValueFactory<>("date"));
        duration.setCellValueFactory(new PropertyValueFactory<>("duration"));
        contactName.setCellValueFactory(new PropertyValueFactory<>("contactName"));
        contactNumber.setCellValueFactory(new PropertyValueFactory<>("contactNumber"));
        contactPresence.setCellValueFactory(new PropertyValueFactory<>("contactPresence"));
        callHistoryTable.setItems(FXCollections.observableList(App.acc.getCallHistory()));

        Platform.runLater(() -> {
            numberField.requestFocus();

            

            // Disable the "Edit" menu item by default
            editMenuItem.setDisable(true);

            // Add a context menu listener to the contactTable
            // contactTable.setContextMenu(new ContextMenu());
            contactTable.setOnContextMenuRequested(event -> {
                if (contactTable.getSelectionModel().getSelectedItem() != null) {
                    // Enable the "Edit" menu item if a row is selected
                    editMenuItem.setDisable(false);
                    Contact selectedContact = contactTable.getSelectionModel().getSelectedItem();
                    if (selectedContact != null) {
                        editMenuItem.setOnAction(e -> handleEditContact(selectedContact));
                    }
                } else {
                    // Disable the "Edit" menu item if no row is selected
                    editMenuItem.setDisable(true);
                }
            });
            contactTable.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2) { // Check if it's a double-click
                    Contact selectedContact = contactTable.getSelectionModel().getSelectedItem();
                    if (selectedContact != null) {
                        // Handle the double-click event for the selected contact
                        CallController.getInstance().handleCall(selectedContact.getContactNumber());
                    }
                }
            });
        });
    }

    public static MainWindow getInstance() {
        if (instance == null) {
            instance = new MainWindow();
        }
        return instance;
    }

    public MainWindow() {
        username = new Text();
        domain = new Text();
        status = new Text();
        numberField = new TextField();
        callButton = new Button();
        contactTable = new TableView<Contact>();
        instance = this;
    }

    public void setUsername(String username) {
        this.username.setText(username);
    }

    public void setDomain(String domain) {
        this.domain.setText(domain);
    }

    public void setStatus(String status) {
        this.status.setText(status);
    }

    public void addCallHistoryEntry(String name, String number, String date, String duration, String info) {
        CallHistoryEntry entry = new CallHistoryEntry(name, number, date, duration, info);
        callHistoryTable.getItems().add(entry);
    }

    public void updateCallHistoryTable() {
        ObservableList<CallHistoryEntry> callHistoryList = FXCollections.observableArrayList(App.acc.getCallHistory());
        callHistoryTable.setItems(callHistoryList);
    }

    public void addContact(Contact contact) {
        contactTable.getItems().add(contact);
    }

    public void updateContactTable() {
        ObservableList<Contact> contactList = FXCollections.observableArrayList(App.acc.getContacts());
        contactTable.setItems(contactList);
        contactTable.refresh();
    }

    public void setContactPresenceText(Contact contact, String presenceText) {
        for (Contact c : contactTable.getItems()) {
            if (c.getContactNumber().equals(contact.getContactNumber())) {
                contactTable.getItems().set(contactTable.getItems().indexOf(c), c);
            }
        }
    }

    @FXML
    public void handleContactWindow() {
        ContactController.getInstance().handleContactWindow();
    }

    @FXML
    private void handleCall() {
        // Handle call button click
        CallController.getInstance().handleCall(this.numberField.getText());
    }

    @FXML
    private void handleExit() {
        // Handle exit menu item click
        MainController.handleExit();
    }

    @FXML
    private void handleAbout() {
        // Handle about menu item click
    }

    @FXML
    private void handleDigitButton(ActionEvent event) {
        Button button = (Button) event.getSource();
        String buttonText = button.getText();
        this.numberField.setText(this.numberField.getText() + buttonText);
    }

    @FXML
    private void handleAccountSettings() {
        // Handle account settings menu item click
        AccountController.getInstance().handleAccountSettingsWindow();
    }

    @FXML
    private void handlePresence(ActionEvent event) {
        MenuItem menuItem = (MenuItem) event.getSource();
        String menuItemText = menuItem.getText();
        this.status.setText(menuItemText);
        AccountController.getInstance().handlePresence(menuItemText);
    }

    @FXML
    private void handleEnterKey(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            handleCall();
        }
    }

    @FXML
    private void handleAppSettings() {
        // Handle app settings menu item click
        AppSettingsController.getInstance().showAppSettingsWindow();
    }

    private void handleEditContact(Contact contact) {
        ContactController.getInstance().editContact(contact);
    }

    public Text getUsername() {
        return username;
    }

    public Text getDomain() {
        return domain;
    }

    public Text getStatus() {
        return status;
    }
}
