package br.com.model;

import java.io.Serializable;

public class AppSettings implements Serializable{
    
    private String listeningDevice;
    private String ringDevice;
    private String inputDevice;
    private String ringSound;
    
    public AppSettings() {
        this.listeningDevice = "";
        this.ringDevice = "";
        this.inputDevice = "";
        this.ringSound = "";
    }
    
    public AppSettings(String listeningDevice, String ringDevice, String inputDevice, String ringSound) {
        this.listeningDevice = listeningDevice;
        this.ringDevice = ringDevice;
        this.inputDevice = inputDevice;
        this.ringSound = ringSound;
    }
    
    public String getListeningDevice() {
        return listeningDevice;
    }
    
    public void setListeningDevice(String listeningDevice) {
        this.listeningDevice = listeningDevice;
    }
    
    public String getRingDevice() {
        return ringDevice;
    }
    
    public void setRingDevice(String ringDevice) {
        this.ringDevice = ringDevice;
    }
    
    public String getInputDevice() {
        return inputDevice;
    }
    
    public void setInputDevice(String inputDevice) {
        this.inputDevice = inputDevice;
    }

    public String getRingSound() {
        return ringSound;
    }

    public void setRingSound(String ringSound) {
        this.ringSound = ringSound;
    }
    
    @Override
    public String toString() {
        return "AppSettings [listeningDevice=" + listeningDevice + ", ringDevice=" + ringDevice + ", inputDevice="
                + inputDevice + "]";
    }
}
