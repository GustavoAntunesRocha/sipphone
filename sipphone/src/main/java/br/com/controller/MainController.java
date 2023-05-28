package br.com.controller;

import br.com.App;
import br.com.MainWindow;
import br.com.model.AccountEntity;
import javafx.application.Platform;


public class MainController {

    private AccountEntity accountEntity;
    private MainWindow mainWindow;

    public void setAccountEntity(AccountEntity accountEntity) {
        this.accountEntity = accountEntity;
    }

    public void setMainWindow(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
        mainWindow.setUsername(accountEntity.getAccountConfigModel().getUsername());
        mainWindow.setDomain(accountEntity.getAccountConfigModel().getDomain());
        mainWindow.setStatus("Offline");
        /* mainWindow.setDateColumn(new TableColumn("Date"));
        mainWindow.setNumberColumn(new TableColumn("Number"));
        mainWindow.setDurationColumn(new TableColumn("Duration")); */
        /* mainWindow.getCallHistoryTable().getColumns().addAll(mainWindow.getDateColumn(),
                mainWindow.getNumberColumn(), mainWindow.getDurationColumn()); */
    }

    public void handleCall() {
        // Handle call button click
        mainWindow.setStatus("Calling...");
    }

    public void handleHangUp() {
        // Handle hang up button click
        mainWindow.setStatus("Offline");
    }

    public static void handleExit() {
        // Handle exit menu item click
        App.deleteLibrary();
        Platform.exit();
    }

    public void handleAbout() {
        // Handle about menu item click
        // Show about dialog
    }
}
