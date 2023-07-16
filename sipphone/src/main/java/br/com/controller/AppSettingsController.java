package br.com.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.Line;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.Port;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

import org.apache.commons.lang3.StringUtils;
import org.pjsip.pjsua2.AudDevManager;
import org.pjsip.pjsua2.AudioDevInfo;
import org.pjsip.pjsua2.Endpoint;

import br.com.App;
import br.com.model.AppSettings;
import br.com.view.AppSettingsWindow;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Duration;

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

    private TargetDataLine line;

    private Timeline timeline;

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
            this.stage.setOnCloseRequest(event -> {
                if (this.timeline != null) {
                    this.timeline.stop();
                    this.line.stop();
                    this.line.close();
                }
            });
            Platform.runLater(() -> {
                try {
                    appSettingsWindow.setListeningDevice(listOutputDevices4());
                    appSettingsWindow.setRingDevice(listOutputDevices4());
                    appSettingsWindow.setInputDevice(listInputAudioDevices());
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

            String selectedDeviceName = getDeviceName(selectedDevice.getName());

            int selectedDeviceIndex = audDevManager.lookupDev(audDevManager.getDevInfo(0).getDriver(),
                    selectedDeviceName);
            // Set the playback device to the selected device
            audDevManager.setPlaybackDev(selectedDeviceIndex);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setMicDevice(Mixer.Info selectedDevice) {
        try {
            // Get the index of the selected device
            AudDevManager audDevManager = Endpoint.instance().audDevManager();

            String selectedDeviceName = getDeviceName(selectedDevice.getName());

            int selectedDeviceIndex = audDevManager.lookupDev(audDevManager.getDevInfo(0).getDriver(),
                    selectedDeviceName);
            // Set the playback device to the selected device
            audDevManager.setCaptureDev(selectedDeviceIndex);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean areStringsSimilar(String str1, String str2, double threshold) {
        double distance = StringUtils.getJaroWinklerDistance(str1, str2);
        return distance >= threshold;
    }

    private String getDeviceName(String device) {
        try {
            AudDevManager audDevManager = Endpoint.instance().audDevManager();
            for (AudioDevInfo audioDevInfo : audDevManager.enumDev2()) {
                if (areStringsSimilar(audioDevInfo.getName(), device, 0.8)) {
                    return audioDevInfo.getName();
                }
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    private Mixer.Info getSelectedDevice(String deviceName) {
        Mixer.Info[] mixerInfos = AudioSystem.getMixerInfo();
        for (Mixer.Info mixerInfo : mixerInfos) {
            if (mixerInfo.getName().startsWith(deviceName)) {
                return mixerInfo;
            }
        }
        return null;
    }

    public void handleSaveSettings(String listeningDevice, String ringDevice, String inputDevice) {
        if (this.timeline != null) {
            this.timeline.stop();
            this.line.stop();
            this.line.close();
        }
        appSettings.setListeningDevice(listeningDevice);
        appSettings.setRingDevice(ringDevice);
        appSettings.setInputDevice(inputDevice);
        setPlaybackDevice(getSelectedDevice(listeningDevice));
        setMicDevice(getSelectedDevice(inputDevice));

        this.listeningDevice = getSelectedDevice(listeningDevice);
        this.ringDevice = getSelectedDevice(ringDevice);
        this.inputDevice = getSelectedDevice(inputDevice);

        saveAppSettingsToFile(appSettings, "appSettings.ser");

        this.stage.close();

    }

    public void loadAppSettings() {
        appSettings = readAppSettingsFromFile("appSettings.ser");
        String path = AppSettingsController.class.getResource("/sounds/oldphone-mono.wav").getPath();
        try {
            appSettings.setRingSound(URLDecoder.decode(path, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if (appSettings != null) {
            this.listeningDevice = getSelectedDevice(appSettings.getListeningDevice());
            this.ringDevice = getSelectedDevice(appSettings.getRingDevice());
            this.inputDevice = getSelectedDevice(appSettings.getInputDevice());
        }
    }

    private void saveAppSettingsToFile(AppSettings appSettings, String filePath) {
        try {
            if (this.timeline != null) {
                this.timeline.stop();
                this.line.stop();
                this.line.close();
            }
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
        if (this.timeline != null) {
            this.timeline.stop();
            this.line.stop();
            this.line.close();
        }
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

    public List<Mixer.Info> listInputDevices()
            throws Exception {
        ArrayList<Mixer.Info> mixerInfos = new ArrayList<Mixer.Info>(
                Arrays.asList(
                        AudioSystem.getMixerInfo()));
        Line.Info portInfo = new Line.Info(Port.class);
        ArrayList<Mixer.Info> inputDevices2 = new ArrayList<>();
        for (Mixer.Info mixerInfo : mixerInfos) {
            Mixer mixer = AudioSystem.getMixer(
                    mixerInfo);
            if (mixer.isLineSupported(
                    portInfo)) {
                inputDevices2.add(mixerInfo);
            } // of if
              // (mixer.isLineSupported)
        } // of for (Mixer.Info)
        return inputDevices2;
    }

    public List<Mixer.Info> getRecordingDevices() {
        List<Mixer.Info> mixerInfos = new ArrayList<>();
        Mixer.Info[] infos = AudioSystem.getMixerInfo();
        for (Mixer.Info info : infos) {
            Mixer mixer = AudioSystem.getMixer(info);
            Line.Info[] lineInfos = mixer.getTargetLineInfo();
            if (lineInfos.length >= 1 && lineInfos[0].getLineClass().equals(TargetDataLine.class)) {
                // This mixer supports recording
                mixerInfos.add(info);
            }
        }
        return mixerInfos;
    }

    public List<Mixer.Info> listInputAudioDevices() {
        try {
            List<Mixer.Info> mixerInfos = new ArrayList<>();
            AudDevManager audDevManager = Endpoint.instance().audDevManager();
            for (AudioDevInfo audioDevInfo : audDevManager.enumDev2()) {
                if (audioDevInfo.getInputCount() > 0) {
                    mixerInfos.add(getSelectedDevice(audioDevInfo.getName()));
                }
            }
            return mixerInfos;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    public void progressBarVolume() {
        Platform.runLater(() -> {
            AudioFormat format = new AudioFormat(48000, 16, 1, true, false);
            try {
                Mixer mixer = AudioSystem.getMixer(this.inputDevice);

                line = (TargetDataLine) mixer
                        .getLine(new DataLine.Info(TargetDataLine.class, format));
                line.open(format);
                line.start();
                // Set up a periodic task to update the volume level
                timeline = new Timeline(new KeyFrame(Duration.millis(100), event -> {
                    byte[] buffer = new byte[1024];
                    int count = line.read(buffer, 0, buffer.length);
                    if (count > 0) {
                        double rms = calculateRMS(buffer, count);
                        appSettingsWindow.setVolumeLevel(rms / 2000);
                    }
                }));
                timeline.setCycleCount(Timeline.INDEFINITE);
                timeline.play();
            } catch (LineUnavailableException e) {
                e.printStackTrace();
            }
        });
    }

    private double calculateRMS(byte[] buffer, int count) {
        double sum = 0;
        for (int i = 0; i < count; i += 2) {
            short sample = (short) ((buffer[i + 1] << 8) | buffer[i]);
            sum += sample * sample;
        }
        double rms = Math.sqrt(sum / (count / 2));
        return rms;
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
