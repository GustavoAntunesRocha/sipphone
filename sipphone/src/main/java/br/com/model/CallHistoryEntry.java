package br.com.model;

import java.io.Serializable;

public class CallHistoryEntry implements Serializable{
    
    private String name;
    private String number;
    private String date;
    private String duration;
    private String info;

    public CallHistoryEntry(String name, String number, String date, String duration, String info) {
        this.name = name;
        this.number = number;
        this.date = date;
        this.duration = duration;
        this.info = info;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
