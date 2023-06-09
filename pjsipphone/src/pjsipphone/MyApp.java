package pjsipphone;

import java.io.IOException;

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

    private static Scene scene;

    static {
        System.loadLibrary("pjsua2");
        System.out.println("Library loaded");
    }

    @Override
    public void start(Stage stage) throws Exception {

        Scene scene = new Scene(loadFXML("/pjsipphone/src/fxml/MainWindow.fxml"), 640, 480);
        stage.setScene(scene);
        stage.show();

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
            /* FXMLLoader loader = new FXMLLoader(getClass().getResource("./resources/fxml/MainWindow.fxml"));
            MainWindow mainWindow = loader.getController();
            mainWindow.setUsername(acc.getAccountConfigModel().getUsername());
            mainWindow.setDomain(acc.getAccountConfigModel().getDomain());
            mainWindow.setStatus("Offline"); */
            

            // Initialize the controller
            
             /* MainController mainController = new MainController();
             mainController.setAccountEntity(acc);
             mainController.setMainWindow(mainWindow);
             */

            // Set up the scene and show the window
            

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

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MyApp.class.getResource(fxml));
        return fxmlLoader.load();
    }

    public static void main(String argv[]) {
        launch(argv);
    }
}
