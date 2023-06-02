package br.com.view;

import br.com.App;
import br.com.controller.MainController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
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

    private static MainWindow instance;

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
        instance = this;
    }

    /* @FXML
    private TableView callHistoryTable;

    @FXML
    private TableColumn dateColumn;

    @FXML
    private TableColumn numberColumn;

    @FXML
    private TableColumn durationColumn; */

    public void setUsername(String username) {
        this.username.setText(username);
    }

    public void setDomain(String domain) {
        this.domain.setText(domain);
    }

    public void setStatus(String status) {
        this.status.setText(status);
    }

    /* public void setCallHistoryTable(TableView callHistoryTable) {
        this.callHistoryTable = callHistoryTable;
    }

    public void setDateColumn(TableColumn dateColumn) {
        this.dateColumn = dateColumn;
    }

    public void setNumberColumn(TableColumn numberColumn) {
        this.numberColumn = numberColumn;
    }

    public void setDurationColumn(TableColumn durationColumn) {
        this.durationColumn = durationColumn;
    } */

    @FXML
    private void handleCall() {
        // Handle call button click
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
