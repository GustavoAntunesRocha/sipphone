package br.com;

import java.io.IOException;

import org.pjsip.pjsua2.AccountConfig;
import org.pjsip.pjsua2.AuthCredInfo;
import org.pjsip.pjsua2.Endpoint;
import org.pjsip.pjsua2.EpConfig;
import org.pjsip.pjsua2.TransportConfig;
import org.pjsip.pjsua2.pjsip_transport_type_e;

import br.com.model.AccountConfigModel;
import br.com.model.AccountEntity;
import br.com.model.TransportConfigModel;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


//Subclass to extend the Account and get notifications etc.

public class App extends Application {

    private static Scene scene;
    private static AccountEntity acc;
    private static Endpoint ep;

    static {
        System.loadLibrary("pjsua2");
        System.out.println("Library loaded");
    }

    @Override
    public void start(Stage stage) throws Exception {

        scene = new Scene(loadFXML("MainWindow"), 640, 480);
        stage.setScene(scene);
        stage.show();
        stage.setOnCloseRequest(event -> {
            deleteLibrary();
        });

        try {
            // Create endpoint
            ep = new Endpoint();
            ep.libCreate();
            // Initialize endpoint
            EpConfig epConfig = new EpConfig();
            ep.libInit(epConfig);

            acc = AccountEntity.readAccountFromFile("account.ser");
            if(acc == null) {
                acc = new AccountEntity();
                acc.setAccountConfigModel(new AccountConfigModel(
                    "8351", "e5f9a03a83cb5de23b180181f15e2521", 
                    "telefonia.orlac.local", "digest", "*", 0));
                acc.setTransportConfigModel(new TransportConfigModel(
                    5060, pjsip_transport_type_e.PJSIP_TRANSPORT_UDP));
                acc.setId(1);
                acc.setName("Account 1");
                AccountEntity.writeAccountToFile(acc, "account.ser");
            }

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


        } catch (Exception e) {
            System.out.println(e);
            return;
        }
    }

    public static void deleteLibrary(){
        try {
            acc.delete();
            ep.libDestroy();
            ep.delete();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    public static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String argv[]) {
        launch(argv);
    }
}
