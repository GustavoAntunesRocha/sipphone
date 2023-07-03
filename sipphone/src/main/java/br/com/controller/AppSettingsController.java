package br.com.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.Control;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.Line;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.Port;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.tools.FileObject;

import org.pjsip.pjsua2.AudDevManager;
import org.pjsip.pjsua2.Endpoint;

import br.com.App;
import br.com.model.AppSettings;
import br.com.view.AppSettingsWindow;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class AppSettingsController {

    private static AppSettingsController instance;

    private AppSettingsWindow appSettingsWindow;

    private Mixer.Info listeningDevice;
    private Mixer.Info ringDevice;
    private Mixer.Info inputDevice;

    private Stage stage;
    private Scene scene;

    private FXMLLoader loader;

    private AppSettings appSettings;

    private AppSettingsController() {
    }

    public static AppSettingsController getInstance() {
        if (instance == null) {
            instance = new AppSettingsController();
        }
        return instance;
    }

    public void showAppSettingsWindow() {
        try {
            this.stage = new Stage();
            this.stage.setTitle("Application Settings");
            this.loader = new FXMLLoader(App.class.getResource("AppSettingsWindow.fxml"));
            Parent root = loader.load();
            this.scene = new Scene(root);
            this.stage.setScene(this.scene);
            this.stage.show();
            this.appSettingsWindow = loader.getController();
            Platform.runLater(() -> {
                try {
                    appSettingsWindow.setListeningDevice(listOutputDevices4());
                    appSettingsWindow.setRingDevice(listOutputDevices4());
                    appSettingsWindow.setInputDevice(listInputDevices());
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            });
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void setPlaybackDevice(Mixer.Info selectedDevice) {
        try {
            // Get the index of the selected device
            AudDevManager audDevManager = Endpoint.instance().audDevManager();

            String selectedDeviceName = selectedDevice.getName().split(" ")[1]
                    .split("\\[")[1].split(":")[0] + ":CARD=" + selectedDevice.getName().split(" ")[0] +
                    ",DEV=0";
            String selectedDeviceVendor = selectedDevice.getVendor().split(" ")[0];

            int selectedDeviceIndex = audDevManager.lookupDev(selectedDeviceVendor, selectedDeviceName);
            // Set the playback device to the selected device
            audDevManager.setPlaybackDev(selectedDeviceIndex);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Mixer.Info getSelectedDevice(String deviceName) {
        Mixer.Info[] mixerInfos = AudioSystem.getMixerInfo();
        for (Mixer.Info mixerInfo : mixerInfos) {
            if (mixerInfo.getName().equals(deviceName)) {
                return mixerInfo;
            }
        }
        return null;
    }

    public void handleSaveSettings(String listeningDevice, String ringDevice, String inputDevice) {
        appSettings.setListeningDevice(listeningDevice);
        appSettings.setRingDevice(ringDevice);
        appSettings.setInputDevice(inputDevice);
        setPlaybackDevice(getSelectedDevice(listeningDevice));

        // TODO: Set ring device, and input device
        this.listeningDevice = getSelectedDevice(listeningDevice);
        this.ringDevice = getSelectedDevice(ringDevice);
        this.inputDevice = getSelectedDevice(inputDevice);

        saveAppSettingsToFile(appSettings, "appSettings.ser");

        this.stage.close();

    }

    public void loadAppSettings() {
        appSettings = readAppSettingsFromFile("appSettings.ser");
        appSettings.setRingSound("/home/gustavo/oldphone-mono.wav");
        if (appSettings != null) {
            this.listeningDevice = getSelectedDevice(appSettings.getListeningDevice());
            this.ringDevice = getSelectedDevice(appSettings.getRingDevice());
            this.inputDevice = getSelectedDevice(appSettings.getInputDevice());
        }
    }

    private void saveAppSettingsToFile(AppSettings appSettings, String filePath) {
        try {
            // Create a new file output stream for the specified file path
            FileOutputStream fileOutputStream = new FileOutputStream(filePath);

            // Create a new object output stream for the file output stream
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);

            // Write the AppSettings object to the object output stream
            objectOutputStream.writeObject(appSettings);

            // Close the object output stream and file output stream
            objectOutputStream.close();
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private AppSettings readAppSettingsFromFile(String filePath) {
        try {
            // Create a new file input stream for the specified file path
            FileInputStream fileInputStream = new FileInputStream(filePath);

            // Create a new object input stream for the file input stream
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);

            // Read the AppSettings object from the object input stream
            AppSettings appSettings = (AppSettings) objectInputStream.readObject();

            // Close the object input stream and file input stream
            objectInputStream.close();
            fileInputStream.close();

            // Return the AppSettings object
            return appSettings;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return new AppSettings();
        }
    }

    public void handleCancelSettings() {
        this.stage.close();
    }

    public List<Mixer.Info> listOutputDevices4() throws Exception {
        ArrayList<Mixer.Info> mixerInfos = new ArrayList<Mixer.Info>(
                Arrays.asList(AudioSystem.getMixerInfo()));
        Line.Info portInfo = new Line.Info(SourceDataLine.class);
        List<Mixer.Info> outputDevices = new ArrayList<>();
        for (Mixer.Info mixerInfo : mixerInfos) {
            Mixer mixer = AudioSystem.getMixer(mixerInfo);
            if (mixer.isLineSupported(portInfo)) {
                ArrayList<Line.Info> sourceInfos = new ArrayList<Line.Info>(
                        Arrays.asList(mixer.getSourceLineInfo()));
                for (Line.Info sourceInfo : sourceInfos) {
                    if (sourceInfo instanceof DataLine.Info) {
                        DataLine.Info dataLineInfo = (DataLine.Info) sourceInfo;
                        AudioFormat[] formats = dataLineInfo.getFormats();
                        boolean hasStereo = false;
                        for (AudioFormat format : formats) {
                            if (format.getChannels() >= 2) {
                                hasStereo = true;
                                break;
                            }
                        }
                        if (hasStereo) {
                            outputDevices.add(mixerInfo);
                        }
                    }
                }
            }
        }
        return outputDevices;
    }

    public List<String> listOutputDevices3() throws Exception {
        ArrayList<Mixer.Info> mixerInfos = new ArrayList<Mixer.Info>(
                Arrays.asList(AudioSystem.getMixerInfo()));
        Line.Info portInfo = new Line.Info(SourceDataLine.class);
        List<String> outputDevices = new ArrayList<>();
        for (Mixer.Info mixerInfo : mixerInfos) {
            Mixer mixer = AudioSystem.getMixer(mixerInfo);
            if (mixer.isLineSupported(portInfo)) {
                ArrayList<Line.Info> sourceInfos = new ArrayList<Line.Info>(
                        Arrays.asList(mixer.getSourceLineInfo()));
                for (Line.Info sourceInfo : sourceInfos) {
                    if (sourceInfo instanceof DataLine.Info) {
                        DataLine.Info dataLineInfo = (DataLine.Info) sourceInfo;
                        AudioFormat[] formats = dataLineInfo.getFormats();
                        boolean hasStereo = false;
                        for (AudioFormat format : formats) {
                            if (format.getChannels() >= 2) {
                                hasStereo = true;
                                break;
                            }
                        }
                        if (hasStereo) {
                            if (dataLineInfo.getLineClass().equals(SourceDataLine.class)) {
                                SourceDataLine sourceDataLine = (SourceDataLine) mixer.getLine(dataLineInfo);
                                Control[] controls = sourceDataLine.getControls();
                                if (controls.length > 0) {
                                    String portName = controls[0].toString();
                                    outputDevices.add(
                                            mixerInfo.getName() + ": " + portName + " - " + dataLineInfo.toString());
                                } else {
                                    outputDevices.add(mixerInfo.getName() + ": " + dataLineInfo.toString());
                                }
                            }
                        }
                    }
                }
            }
        }
        return outputDevices;
    }

    public List<String> listOutputDevices2() throws Exception {
        ArrayList<Mixer.Info> mixerInfos = new ArrayList<Mixer.Info>(
                Arrays.asList(AudioSystem.getMixerInfo()));
        Line.Info portInfo = new Line.Info(SourceDataLine.class);
        List<String> outputDevices = new ArrayList<>();
        for (Mixer.Info mixerInfo : mixerInfos) {
            Mixer mixer = AudioSystem.getMixer(mixerInfo);
            if (mixer.isLineSupported(portInfo)) {
                ArrayList<Line.Info> sourceInfos = new ArrayList<Line.Info>(
                        Arrays.asList(mixer.getSourceLineInfo()));
                for (Line.Info sourceInfo : sourceInfos) {
                    if (sourceInfo instanceof DataLine.Info) {
                        DataLine.Info dataLineInfo = (DataLine.Info) sourceInfo;
                        AudioFormat[] formats = dataLineInfo.getFormats();
                        boolean hasStereo = false;
                        for (AudioFormat format : formats) {
                            if (format.getChannels() >= 2) {
                                hasStereo = true;
                                break;
                            }
                        }
                        if (hasStereo) {
                            outputDevices.add(mixerInfo.getName() + ": " + dataLineInfo.toString());
                        }
                    }
                }
            }
        }
        return outputDevices;
    }

    public List<String> listOutputDevices()
            throws Exception {
        ArrayList<Mixer.Info> mixerInfos = new ArrayList<Mixer.Info>(
                Arrays.asList(
                        AudioSystem.getMixerInfo()));
        Line.Info portInfo = new Line.Info(Port.class);
        List<String> outputDevices = new ArrayList<>();
        for (Mixer.Info mixerInfo : mixerInfos) {
            Mixer mixer = AudioSystem.getMixer(
                    mixerInfo);
            if (mixer.isLineSupported(
                    portInfo)) {
                ArrayList<Line.Info> srcInfos = new ArrayList<Line.Info>(
                        Arrays.asList(
                                mixer.getTargetLineInfo()));
                for (Line.Info srcInfo : srcInfos) {
                    Port.Info pi = (Port.Info) srcInfo;
                    outputDevices.add(pi.getName());
                } // of for (Line.Info)
            } // of if
              // (mixer.isLineSupported)
        } // of for (Mixer.Info)
        return outputDevices;
    }

    public List<String> listInputDevices()
            throws Exception {
        ArrayList<Mixer.Info> mixerInfos = new ArrayList<Mixer.Info>(
                Arrays.asList(
                        AudioSystem.getMixerInfo()));
        Line.Info portInfo = new Line.Info(Port.class);
        List<String> inputDevices = new ArrayList<>();
        for (Mixer.Info mixerInfo : mixerInfos) {
            Mixer mixer = AudioSystem.getMixer(
                    mixerInfo);
            if (mixer.isLineSupported(
                    portInfo)) {
                ArrayList<Line.Info> targetInfos = new ArrayList<Line.Info>(
                        Arrays.asList(
                                mixer.getSourceLineInfo()));
                for (Line.Info targetInfo : targetInfos) {
                    Port.Info pi = (Port.Info) targetInfo;
                    inputDevices.add(pi.getName());
                } // of for (Line.Info)
            } // of if
              // (mixer.isLineSupported)
        } // of for (Mixer.Info)
        return inputDevices;
    }

    public void playSound(File soundFile, Mixer.Info mixerInfo)
            throws IOException, UnsupportedAudioFileException, LineUnavailableException {
        Mixer mixer = AudioSystem.getMixer(mixerInfo);
        Line.Info[] lineInfos = mixer.getSourceLineInfo();
        for (Line.Info lineInfo : lineInfos) {
            Line line = mixer.getLine(lineInfo);
            if (line instanceof Clip) {
                Clip clip = (Clip) line;
                clip.addLineListener(new LineListener() {
                    @Override
                    public void update(LineEvent event) {
                        if (event.getType() == LineEvent.Type.STOP) {
                            clip.close();
                        }
                    }
                });
                clip.open(AudioSystem.getAudioInputStream(soundFile));
                clip.start();
            }
        }
    }

    public Mixer.Info getListeningDevice() {
        return listeningDevice;
    }

    public void setListeningDevice(Mixer.Info listeningDevice) {
        this.listeningDevice = listeningDevice;
    }

    public Mixer.Info getRingDevice() {
        return ringDevice;
    }

    public void setRingDevice(Mixer.Info ringDevice) {
        this.ringDevice = ringDevice;
    }

    public Mixer.Info getInputDevice() {
        return inputDevice;
    }

    public void setInputDevice(Mixer.Info inputDevice) {
        this.inputDevice = inputDevice;
    }

    public String getRingSoundFilePath() {
        return this.appSettings.getRingSound();
    }
}
