package br.com;

import java.io.IOException;

import org.pjsip.pjsua2.AccountConfig;
import org.pjsip.pjsua2.AuthCredInfo;
import org.pjsip.pjsua2.Endpoint;
import org.pjsip.pjsua2.EpConfig;
import org.pjsip.pjsua2.TransportConfig;
import org.pjsip.pjsua2.pjsip_transport_type_e;

import br.com.controller.MainController;
import br.com.model.AccountEntity;
import br.com.view.MainWindow;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {

    private static Scene scene;
    private static Endpoint ep;

    public static AccountEntity acc;

    public final static String ACC_FILE_PATH = "account.bin";

    public static void connectSipServer(){
        try {
            
            // Create SIP transport. Error handling sample is shown
                TransportConfig sipTpConfig = new TransportConfig();
                sipTpConfig.setPort(5060);
                ep.transportCreate(pjsip_transport_type_e.PJSIP_TRANSPORT_UDP, sipTpConfig);
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

    private static void initLibrary(){
        System.loadLibrary("pjsua2");
            System.out.println("Library loaded");
            
            //if(ep == null){
                // Create endpoint
                ep = new Endpoint();
                try {
                    ep.libCreate();
                    // Initialize endpoint
                    EpConfig epConfig = new EpConfig();
                    ep.libInit(epConfig);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
    
                
            //}
    }

    @Override
    public void start(Stage stage) throws Exception {
        initLibrary();

        acc = new AccountEntity();

        MainController mainController = MainController.getInstance();
        MainWindow mainWindow = MainWindow.getInstance();
        stage.setMinHeight(361);
        stage.setMinWidth(331);
        scene = new Scene(loadFXML("MainWindow"));
        stage.setScene(scene);
        stage.show();

        mainController.addCallHistoryEntry(null);
        
        stage.setOnCloseRequest(event -> {
            deleteLibrary();
        });
        mainController.setMainWindow(mainWindow);
        
        acc = AccountEntity.readAccountFromFile();
        mainController.updateContactTable();
                
        if(acc.getName() != null)
            connectSipServer();
         
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
