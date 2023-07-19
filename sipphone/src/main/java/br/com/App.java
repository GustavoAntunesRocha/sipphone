package br.com;

import java.io.IOException;

import org.pjsip.pjsua2.AccountConfig;
import org.pjsip.pjsua2.AudDevManager;
import org.pjsip.pjsua2.AudioDevInfo;
import org.pjsip.pjsua2.AuthCredInfo;
import org.pjsip.pjsua2.Endpoint;
import org.pjsip.pjsua2.EpConfig;
import org.pjsip.pjsua2.TransportConfig;
import org.pjsip.pjsua2.pjsip_transport_type_e;

import br.com.controller.AppSettingsController;
import br.com.controller.MainController;
import br.com.model.AccountEntity;
import br.com.view.MainWindow;
import ch.loway.oss.ari4java.ARI;
import ch.loway.oss.ari4java.AriVersion;
import ch.loway.oss.ari4java.generated.actions.requests.ChannelsListGetRequest;
import ch.loway.oss.ari4java.generated.models.AsteriskInfo;
import ch.loway.oss.ari4java.generated.models.Channel;
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

    private static String asteriskUrl;

    private static String asteriskAriUsername;

    private static String asteriskAriPassword;

    public static void connectSipServer() {
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

            MainController.getInstance().loadContactsPresenceSubscription();
            MainController.getInstance().updateContactTable();

            ARI ari = ARI.build(asteriskUrl, "asterisk", asteriskAriUsername, asteriskAriPassword, AriVersion.IM_FEELING_LUCKY);
            for (ch.loway.oss.ari4java.generated.models.Endpoint endpoint : ari.endpoints().list().execute()) {
                System.out.println("\n\nEndpoint name: " + endpoint.getResource() + " - status: " + endpoint.getState());
                ari.closeAction(endpoint);
            }

        } catch (Exception e) {
            System.out.println(e);
            return;
        }
    }

    public static void printAudioDevices() {
        try {
            AudDevManager audDevManager = Endpoint.instance().audDevManager();
            System.out.println("\n\nInput devices:");
            for (AudioDevInfo audioDevInfo : audDevManager.enumDev2()) {
                System.out.println(audioDevInfo.getName());
            }
            System.out.println("\n\n");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private static void initLibrary() {
        System.loadLibrary("pjsua2");
        System.out.println("Library loaded");

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

        AppSettingsController.getInstance().loadAppSettings();;

        acc = AccountEntity.readAccountFromFile();

        if (acc.getName() != null)
            connectSipServer();

    }

    public static void deleteLibrary() {
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
        asteriskUrl = System.getProperty("asteriskUrl");
        asteriskAriUsername = System.getProperty("asteriskAriUsername");
        asteriskAriPassword = System.getProperty("asteriskAriPassword");
        launch(argv);
    }

}
