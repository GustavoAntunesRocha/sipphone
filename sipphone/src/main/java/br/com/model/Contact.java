package br.com.model;

import java.io.Serializable;

import org.pjsip.pjsua2.Buddy;
import org.pjsip.pjsua2.BuddyConfig;

import br.com.App;

public class Contact extends Buddy implements Serializable{
    
    private int id;
    private String contactName;
    private String contactNumber;
    private String email;
    private boolean presenceSubscription;

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

    public void setPresenceSubscription(boolean presenceSubscription) {
        this.presenceSubscription = presenceSubscription;
    }

    public void presenceSubscribe(Contact contact){
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
