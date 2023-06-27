package br.com;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.CompoundControl;
import javax.sound.sampled.Control;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.Line;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.Port;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;

import org.pjsip.pjsua2.AccountConfig;
import org.pjsip.pjsua2.AudDevManager;
import org.pjsip.pjsua2.AudioDevInfo;
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

            probePort();

        } catch (Exception e) {
            System.out.println(e);
            return;
        }
    }

    public static void printPlaybackDevices() {
        Mixer.Info[] mixerInfos = AudioSystem.getMixerInfo();
        System.out.println("\n\nPlayback devices:");
        for (Mixer.Info mixerInfo : mixerInfos) {
            Mixer mixer = AudioSystem.getMixer(mixerInfo);
            Line.Info[] lineInfos = mixer.getTargetLineInfo();
            if (lineInfos.length >= 1 && lineInfos[0].getLineClass().equals(TargetDataLine.class)) {
                System.out.println(mixerInfo.getName());
            }
        }
        System.out.println("\n\n");
    }

    public static void printMixer() {
        Mixer.Info[] mixerInfos = AudioSystem.getMixerInfo();
        System.out.println("\n\nPlayback devices:");
        for (Mixer.Info info : mixerInfos) {
            System.out
                    .println(String.format("\n\nName: %s, Description: %s\n\n", info.getName(), info.getDescription()));
        }

    }

    public static void showMixers() {
        ArrayList<Mixer.Info> mixInfos = new ArrayList<Mixer.Info>(
                Arrays.asList(
                        AudioSystem.getMixerInfo()));
        Line.Info sourceDLInfo = new Line.Info(
                SourceDataLine.class);
        Line.Info targetDLInfo = new Line.Info(
                TargetDataLine.class);
        Line.Info clipInfo = new Line.Info(Clip.class);
        Line.Info portInfo = new Line.Info(Port.class);
        String support;
        for (Mixer.Info mixInfo : mixInfos) {
            Mixer mixer = AudioSystem.getMixer(
                    mixInfo);
            support = ", supports ";
            if (mixer.isLineSupported(
                    sourceDLInfo))
                support += "SourceDataLine ";
            if (mixer.isLineSupported(
                    clipInfo))
                support += "Clip ";
            if (mixer.isLineSupported(
                    targetDLInfo))
                support += "TargetDataLine ";
            if (mixer.isLineSupported(
                    portInfo))
                support += "Port ";
            System.out.println("Mixer: "
                    + mixInfo.getName() +
                    support + ", " +
                    mixInfo.getDescription());
        }
    }

    public static void probePort()
            throws Exception {
        ArrayList<Mixer.Info> mixerInfos = new ArrayList<Mixer.Info>(
                Arrays.asList(
                        AudioSystem.getMixerInfo()));
        Line.Info portInfo = new Line.Info(Port.class);
        for (Mixer.Info mixerInfo : mixerInfos) {
            Mixer mixer = AudioSystem.getMixer(
                    mixerInfo);
            if (mixer.isLineSupported(
                    portInfo)) {
                // found a Port Mixer
                disp("Found mixer: " +
                        mixerInfo.getName());
                disp("\t" +
                        mixerInfo.getDescription());
                disp("Source Line Supported:");
                ArrayList<Line.Info> srcInfos = new ArrayList<Line.Info>(
                        Arrays.asList(
                                mixer.getSourceLineInfo()));
                for (Line.Info srcInfo : srcInfos) {
                    Port.Info pi = (Port.Info) srcInfo;
                    disp("\t" + pi.getName() +
                            ", " + (pi.isSource() ? "source" : "target"));
                    showControls(mixer.getLine(
                            srcInfo));
                } // of for Line.Info
                disp("Target Line Supported:");
                ArrayList<Line.Info> targetInfos = new ArrayList<Line.Info>(
                        Arrays.asList(
                                mixer.getTargetLineInfo()));
                for (Line.Info targetInfo : targetInfos) {
                    Port.Info pi = (Port.Info) targetInfo;
                    disp("\t" + pi.getName()
                            + ", " +
                            (pi.isSource() ? "source" : "taget"));
                    showControls(mixer.getLine(
                            targetInfo));
                }
            } // of if
              // (mixer.isLineSupported)
        } // of for (Mixer.Info)
    }

    private static void showControls(
            Line inLine) throws Exception {
        // must open the line to get
        // at controls
        inLine.open();
        disp("\t\tAvailable controls:");
        ArrayList<Control> ctrls = new ArrayList<Control>(
                Arrays.asList(
                        inLine.getControls()));
        for (Control ctrl : ctrls) {
            disp("\t\t\t" +
                    ctrl.toString());
            if (ctrl instanceof CompoundControl) {
                CompoundControl cc = ((CompoundControl) ctrl);
                ArrayList<Control> ictrls = new ArrayList<Control>(
                        Arrays.asList(
                                cc.getMemberControls()));
                for (Control ictrl : ictrls)
                    disp("\t\t\t\t" +
                            ictrl.toString());
            } // of if (ctrl instanceof)
        } // of for(Control ctrl)
        inLine.close();
    }

    private static void disp(String msg) {
        System.out.println(msg);
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
        launch(argv);
    }

}
