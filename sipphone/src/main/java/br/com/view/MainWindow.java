package br.com.view;

import br.com.controller.MainController;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class MainWindow {

    @FXML
    private Label usernameLabel;

    @FXML
    private Label domainLabel;

    @FXML
    private Label statusLabel;

    /* @FXML
    private TableView callHistoryTable;

    @FXML
    private TableColumn dateColumn;

    @FXML
    private TableColumn numberColumn;

    @FXML
    private TableColumn durationColumn; */

    public void setUsername(String username) {
        usernameLabel.setText(username);
    }

    public void setDomain(String domain) {
        domainLabel.setText(domain);
    }

    public void setStatus(String status) {
        statusLabel.setText(status);
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
    private void handleHangUp() {
        // Handle hang up button click
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
}
