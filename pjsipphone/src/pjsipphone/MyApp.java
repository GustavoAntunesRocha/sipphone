package pjsipphone;

import org.pjsip.pjsua2.AccountConfig;
import org.pjsip.pjsua2.AuthCredInfo;
import org.pjsip.pjsua2.Endpoint;
import org.pjsip.pjsua2.EpConfig;
import org.pjsip.pjsua2.TransportConfig;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import pjsipphone.controller.MainController;
import pjsipphone.model.AccountEntity;
import pjsipphone.view.MainWindow;

//Subclass to extend the Account and get notifications etc.

public class MyApp extends Application {
    static {
        System.loadLibrary("pjsua2");
        System.out.println("Library loaded");
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        try {
            // Create endpoint
            Endpoint ep = new Endpoint();
            ep.libCreate();
            // Initialize endpoint
            EpConfig epConfig = new EpConfig();
            ep.libInit(epConfig);

            AccountEntity acc = AccountEntity.readAccountFromFile("account.ser");

            // Create SIP transport. Error handling sample is shown
            TransportConfig sipTpConfig = new TransportConfig();
            sipTpConfig.setPort(acc.getTransportConfigModel().getPort());
            ep.transportCreate(acc.getTransportConfigModel().getType(), sipTpConfig);
            // Start the library
            ep.libStart();

            AccountConfig acfg = new AccountConfig();
            acfg.setIdUri(
                    "sip:" + acc.getAccountConfigModel().getUsername() + "@" + acc.getAccountConfigModel().getDomain());
            acfg.getRegConfig().setRegistrarUri("sip:" + acc.getAccountConfigModel().getDomain());
            AuthCredInfo cred = new AuthCredInfo(acc.getAccountConfigModel().getScheme(),
                    acc.getAccountConfigModel().getRealm(), acc.getAccountConfigModel().getUsername(),
                    0, acc.getAccountConfigModel().getPassword());
            acfg.getSipConfig().getAuthCreds().add(cred);
            // Create the account

            acc.create(acfg);

            // Initialize the view
            FXMLLoader loader = new FXMLLoader(getClass().getResource("./resources/fxml/MainWindow.fxml"));
            Parent root = loader.load();
            MainWindow mainWindow = loader.getController();
            mainWindow.setUsername(acc.getAccountConfigModel().getUsername());
            mainWindow.setDomain(acc.getAccountConfigModel().getDomain());
            mainWindow.setStatus("Offline");
            

            // Initialize the controller
            
             MainController mainController = new MainController();
             mainController.setAccountEntity(acc);
             mainController.setMainWindow(mainWindow);
            

            // Set up the scene and show the window
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.show();

            // Waiting for an call or the press of any key to continue
            System.out.println("Press any key to exit...");
            System.in.read();

            /*
             * Explicitly delete the account.
             * This is to avoid GC to delete the endpoint first before deleting
             * the account.
             */
            acc.delete();

            // Explicitly destroy and delete endpoint
            ep.libDestroy();
            ep.delete();

        } catch (Exception e) {
            System.out.println(e);
            return;
        }
    }

    public static void main(String argv[]) {
        launch(argv);
    }
}
