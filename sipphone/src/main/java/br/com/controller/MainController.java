package br.com.controller;

import java.util.Optional;

import br.com.App;
import br.com.model.AccountEntity;
import br.com.model.CallHistoryEntry;
import br.com.view.MainWindow;
import javafx.application.Platform;

public class MainController {

    private MainWindow mainWindow;
    private static MainController instance;

    private MainController() {
    }

    public static MainController getInstance() {
        if (instance == null) {
            instance = new MainController();
        }
        return instance;
    }

    public void updateAccountText() {
        AccountEntity accountEntity = AccountEntity.getInstance();
        if (accountEntity.getName() != null) {
            try {
                MainWindow.getInstance().setUsername(accountEntity.getAccountConfigModel().getUsername());
                MainWindow.getInstance().setDomain(accountEntity.getAccountConfigModel().getDomain());
                MainWindow.getInstance()
                        .setStatus(accountEntity.getStatus());
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else {
            MainWindow.getInstance().setUsername("");
            MainWindow.getInstance().setDomain("");
            MainWindow.getInstance().setStatus("");
        }
    }

    public void loadCallHistory() {
        AccountEntity accountEntity = AccountEntity.getInstance();
        if (accountEntity.getName() != null) {
            try {
                for (CallHistoryEntry callHistoryEntry : accountEntity.getCallHistory()) {
                    addCallHistoryEntry(callHistoryEntry);
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    

    public void addCallHistoryEntry(CallHistoryEntry callHistoryEntry) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                /* MainWindow.getInstance().addCallHistoryEntry(callHistoryEntry.getName(),
                        callHistoryEntry.getNumber(),
                        callHistoryEntry.getDate(),
                        callHistoryEntry.getDuration(),
                        callHistoryEntry.getInfo()); */
                MainWindow.getInstance().updateCallHistoryTable();
            }
        });
    }

    public void setMainWindow(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
        // mainWindow.setUsername(accountEntity.getAccountConfigModel().getUsername());
        // mainWindow.setDomain(accountEntity.getAccountConfigModel().getDomain());
        // mainWindow.setStatus("Offline");
        /*
         * mainWindow.setDateColumn(new TableColumn("Date"));
         * mainWindow.setNumberColumn(new TableColumn("Number"));
         * mainWindow.setDurationColumn(new TableColumn("Duration"));
         */
        /*
         * mainWindow.getCallHistoryTable().getColumns().addAll(mainWindow.getDateColumn
         * (),
         * mainWindow.getNumberColumn(), mainWindow.getDurationColumn());
         */
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

    public MainWindow getMainWindow() {
        return mainWindow;
    }
}
