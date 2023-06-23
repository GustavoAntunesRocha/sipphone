package br.com.model;

import java.io.Serializable;

import org.pjsip.pjsua2.Buddy;
import org.pjsip.pjsua2.BuddyConfig;
import org.pjsip.pjsua2.BuddyInfo;

import br.com.App;
import br.com.controller.MainController;

public class Contact extends Buddy implements Serializable{
    
    private int id;
    private String contactName;
    private String contactNumber;
    private String email;
    private boolean presenceSubscription;
    private String contactPresence;

    public Contact(int id, String name, String phoneNumber, String email, boolean presenceSubscription) {
        this.id = id;
        this.contactName = name;
        this.contactNumber = phoneNumber;
        this.email = email;
        this.presenceSubscription = presenceSubscription;
        if(presenceSubscription){
            presenceSubscribe(phoneNumber);
        }
    }

    @Override
    public void onBuddyState() {
        try {
            BuddyInfo bi = getInfo();
            MainController.getInstance().updateContactPresence(this, bi.getPresStatus().getStatusText());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isPresenceSubscription() {
        return presenceSubscription;
    }

    public String getContactPresence() {
        return contactPresence;
    }

    public void setContactPresence(String contactPresence) {
        this.contactPresence = contactPresence;
    }

    public void setPresenceSubscription(boolean presenceSubscription) {
        this.presenceSubscription = presenceSubscription;
    }

    public static void presenceSubscribe(Contact contact){
        try {
            BuddyConfig cfg = new BuddyConfig();
            cfg.setUri("sip:" + contact.getContactNumber() + "@" + App.acc.getAccountConfigModel().getDomain());
            contact.create(App.acc, cfg);
            contact.subscribePresence(true);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
    }

    private void presenceSubscribe(String phoneNumber){
        try {
            BuddyConfig cfg = new BuddyConfig();
            cfg.setUri("sip:" + phoneNumber + "@" + App.acc.getAccountConfigModel().getDomain());
            create(App.acc, cfg);
            subscribePresence(true);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
