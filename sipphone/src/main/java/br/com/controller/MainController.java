package br.com.controller;

import br.com.App;
import br.com.model.AccountEntity;
import br.com.model.CallHistoryEntry;
import br.com.model.Contact;
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
        AccountEntity accountEntity = App.acc;
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
        AccountEntity accountEntity = App.acc;
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
                MainWindow.getInstance().updateCallHistoryTable();
            }
        });
    }

    public void updateContactTable() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                MainWindow.getInstance().updateContactTable();
            }
        });
    }

    public void updateContactPresence(Contact contact, String presenceText) {
        App.acc.getContacts().stream().filter(c -> c.equals(contact)).findFirst().ifPresent(c -> {
            c.setContactPresence(presenceText);
        });
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                MainWindow.getInstance().setContactPresenceText(contact, presenceText);
            }
        });
    }

    public void loadContactsPresenceSubscription(){
        App.acc.getContacts().stream().forEach(c -> {
            try {
                Contact.presenceSubscribe(c);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        });
    }

    public void setMainWindow(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
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
