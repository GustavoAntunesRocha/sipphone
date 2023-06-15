package br.com.view;

import br.com.controller.AccountController;
import br.com.controller.CallController;
import br.com.controller.MainController;
import br.com.model.AccountEntity;
import br.com.model.CallHistoryEntry;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
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

    private static MainWindow instance;

    public void initialize() {
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        number.setCellValueFactory(new PropertyValueFactory<>("number"));
        date.setCellValueFactory(new PropertyValueFactory<>("date"));
        duration.setCellValueFactory(new PropertyValueFactory<>("duration"));
        callHistoryTable.setItems(FXCollections.observableList(AccountEntity.getInstance().getCallHistory()));
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
        //callHistoryTable = new TableView<CallHistoryEntry>();
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
        ObservableList<CallHistoryEntry> callHistoryList = FXCollections.observableArrayList(AccountEntity.getInstance().getCallHistory());
        callHistoryTable.setItems(callHistoryList);
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
