package br.com.view;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.Line;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.UnsupportedAudioFileException;

import br.com.App;
import br.com.controller.AppSettingsController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.util.StringConverter;

public class AppSettingsWindow {

    @FXML
    private ChoiceBox<Mixer.Info> listeningDeviceChoiceBox;

    @FXML
    private ChoiceBox<Mixer.Info> ringDeviceChoiceBox;

    @FXML
    private ChoiceBox<String> inputDeviceChoiceBox;

    /*
     * public void setListeningDeviceOld(List<Mixer.Info> outputDevices) {
     * try {
     * ObservableList<String> observableOutputDevices =
     * FXCollections.observableArrayList();
     * for (Mixer.Info mixerInfo : outputDevices) {
     * observableOutputDevices.add(mixerInfo.getName());
     * }
     * listeningDeviceChoiceBox.setItems(observableOutputDevices);
     * } catch (Exception e) {
     * e.printStackTrace();
     * }
     * }
     */

    public void setListeningDevice(List<Mixer.Info> outputDevices) {
        try {
            ObservableList<Mixer.Info> observableOutputDevices = FXCollections.observableArrayList(outputDevices);
            listeningDeviceChoiceBox.setItems(observableOutputDevices);
            listeningDeviceChoiceBox.setConverter(new StringConverter<Mixer.Info>() {
                @Override
                public String toString(Mixer.Info mixerInfo) {
                    if (mixerInfo == null) {
                        return null;
                    }
                    String mixerName = mixerInfo.getName();
                    String mixerDescription = mixerInfo.getDescription();
                    
                    return String.format("%s - %s", mixerName, mixerDescription);
                }

                @Override
                public Mixer.Info fromString(String string) {
                    throw new UnsupportedOperationException("Not supported yet.");
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setRingDevice(List<Mixer.Info> devices) {
        try {
            ObservableList<Mixer.Info> observableOutputDevices = FXCollections.observableArrayList(devices);
            ringDeviceChoiceBox.setItems(observableOutputDevices);
            ringDeviceChoiceBox.setConverter(new StringConverter<Mixer.Info>() {
                @Override
                public String toString(Mixer.Info mixerInfo) {
                    if (mixerInfo == null) {
                        return null;
                    }
                    String mixerName = mixerInfo.getName();
                    String mixerDescription = mixerInfo.getDescription();
                    
                    return String.format("%s - %s", mixerName, mixerDescription);
                }

                @Override
                public Mixer.Info fromString(String string) {
                    throw new UnsupportedOperationException("Not supported yet.");
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setInputDevice(List<String> devices) {
        inputDeviceChoiceBox.getItems().addAll(devices);
    }

    /* @FXML
    private void handlePlaySoundButtonAction(ActionEvent event) {
        File soundFile = new File("/home/gustavo/oldphone-mono.wav");
        Mixer.Info mixerInfo = null;
        String selectedDevice = listeningDeviceChoiceBox.getValue();
        for (Mixer.Info info : AudioSystem.getMixerInfo()) {
            if (info.getName().equals(selectedDevice)) {
                mixerInfo = info;
                break;
            }
        }
        try {
            AppSettingsController.getInstance().playSound(soundFile, mixerInfo);
        } catch (IOException | UnsupportedAudioFileException | LineUnavailableException e) {
            e.printStackTrace();
        }
    } */

    @FXML
    private void testSoundListeningDevice(ActionEvent event) {
        File soundFile = new File("/home/gustavo/oldphone-mono.wav");
        Mixer.Info mixerInfo = listeningDeviceChoiceBox.getValue();        
        try {
            AppSettingsController.getInstance().playSound(soundFile, mixerInfo);
        } catch (IOException | UnsupportedAudioFileException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void testSoundRingDevice(ActionEvent event) {
        File soundFile = new File("/home/gustavo/oldphone-mono.wav");
        Mixer.Info mixerInfo = ringDeviceChoiceBox.getValue();        
        try {
            AppSettingsController.getInstance().playSound(soundFile, mixerInfo);
        } catch (IOException | UnsupportedAudioFileException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleSave() {
        // TODO
    }

    @FXML
    private void handleCancel() {
        // TODO
    }

}
