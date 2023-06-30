package br.com.model;

public class AppSettings {
    
    private String listeningDevice;
    private String ringDevice;
    private String inputDevice;
    
    public AppSettings() {
        this.listeningDevice = "";
        this.ringDevice = "";
        this.inputDevice = "";
    }
    
    public AppSettings(String listeningDevice, String ringDevice, String inputDevice) {
        this.listeningDevice = listeningDevice;
        this.ringDevice = ringDevice;
        this.inputDevice = inputDevice;
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
    
    @Override
    public String toString() {
        return "AppSettings [listeningDevice=" + listeningDevice + ", ringDevice=" + ringDevice + ", inputDevice="
                + inputDevice + "]";
    }
}
