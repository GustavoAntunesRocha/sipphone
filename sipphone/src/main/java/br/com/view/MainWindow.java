package br.com.view;

import br.com.controller.MainController;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.text.Text;

public class MainWindow {

    @FXML
    private Text username;

    @FXML
    private Text domain;

    @FXML
    private Text status;

    private static MainWindow instance;

    public static MainWindow getInstance() {
        if (instance == null) {
            instance = new MainWindow();
        }
        return instance;
    }

    public void initialize() {
        if(MainController.getInstance().getAccountEntity() != null) {
            this.username.setText(MainController.getInstance().getAccountEntity().getAccountConfigModel().getUsername());
            this.domain.setText(MainController.getInstance().getAccountEntity().getAccountConfigModel().getDomain());
            this.status.setText("Online");
        } else{
            System.out.println("\n\n\n\nConta nula!");

        }
    }

    public MainWindow() {
        username = new Text();
        domain = new Text();
        status = new Text();
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
}
