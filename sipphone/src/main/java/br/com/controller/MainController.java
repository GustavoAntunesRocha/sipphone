package br.com.controller;

import br.com.App;
import br.com.model.AccountEntity;
import br.com.view.MainWindow;
import javafx.application.Platform;


public class MainController {

    private AccountEntity accountEntity;
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

    public void setAccountEntity(AccountEntity accountEntity) {
        this.accountEntity = accountEntity;
    }

    public void updateAccountText(){
        MainWindow.getInstance().setUsername(accountEntity.getAccountConfigModel().getUsername());
        MainWindow.getInstance().setDomain(accountEntity.getAccountConfigModel().getDomain());
        try {
            MainWindow.getInstance().setStatus(MainController.getInstance().getAccountEntity().getInfo().getRegStatusText());
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void setMainWindow(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
        //mainWindow.setUsername(accountEntity.getAccountConfigModel().getUsername());
        //mainWindow.setDomain(accountEntity.getAccountConfigModel().getDomain());
        //mainWindow.setStatus("Offline");
        /* mainWindow.setDateColumn(new TableColumn("Date"));
        mainWindow.setNumberColumn(new TableColumn("Number"));
        mainWindow.setDurationColumn(new TableColumn("Duration")); */
        /* mainWindow.getCallHistoryTable().getColumns().addAll(mainWindow.getDateColumn(),
                mainWindow.getNumberColumn(), mainWindow.getDurationColumn()); */
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

    public AccountEntity getAccountEntity() {
        return accountEntity;
    }

    public MainWindow getMainWindow() {
        return mainWindow;
    }
}
