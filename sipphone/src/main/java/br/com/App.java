package br.com;

import java.io.IOException;

import org.pjsip.pjsua2.AccountConfig;
import org.pjsip.pjsua2.AuthCredInfo;
import org.pjsip.pjsua2.Endpoint;
import org.pjsip.pjsua2.EpConfig;
import org.pjsip.pjsua2.TransportConfig;
import org.pjsip.pjsua2.pjsip_transport_type_e;

import br.com.controller.MainController;
import br.com.model.AccountConfigModel;
import br.com.model.AccountEntity;
import br.com.model.TransportConfigModel;
import br.com.view.MainWindow;
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

    public static void connectSipServer(){
        try {
            System.loadLibrary("pjsua2");
            System.out.println("Library loaded");
            // Create endpoint
            ep = new Endpoint();
            ep.libCreate();
            // Initialize endpoint
            EpConfig epConfig = new EpConfig();
            ep.libInit(epConfig);

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


        } catch (Exception e) {
            System.out.println(e);
            return;
        }
    }

    @Override
    public void start(Stage stage) throws Exception {

        MainController mainController = MainController.getInstance();
        MainWindow mainWindow = MainWindow.getInstance();
        
        scene = new Scene(loadFXML("MainWindow"), 640, 480);
        stage.setScene(scene);
        stage.show();
        stage.setOnCloseRequest(event -> {
            deleteLibrary();
        });
        mainController.setMainWindow(mainWindow);

        acc = AccountEntity.readAccountFromFile("account.ser");
        if(acc != null)
            connectSipServer();
         
    }

    public static void deleteLibrary(){
        try {
            if(acc != null){
                acc.delete();
                ep.libDestroy();
                ep.delete();
            }
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

    public static AccountEntity getAcc() {
        return acc;
    }

    public static void setAcc(AccountEntity acc) {
        App.acc = acc;
    }
    
}
