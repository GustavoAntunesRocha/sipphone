package br.com.model;

import java.io.Serializable;

import org.pjsip.pjsua2.AccountConfig;

public class AccountConfigModel extends AccountConfig implements Serializable{

    private String username;
    private String password;
    private String domain;
    private String scheme;
    private String realm;
    private int data_type;

    public AccountConfigModel() {
    }


    public AccountConfigModel(String username, String password, String domain, String scheme, String realm, int data_type) {
        this.username = username;
        this.password = password;
        this.domain = domain;
        this.scheme = scheme;
        this.realm = realm;
        this.data_type = data_type;
    }


    public String getUsername() {
        return username;
    }


    public void setUsername(String username) {
        this.username = username;
    }


    public String getPassword() {
        return password;
    }


    public void setPassword(String password) {
        this.password = password;
    }


    public String getDomain() {
        return domain;
    }


    public void setDomain(String domain) {
        this.domain = domain;
    }


    public String getScheme() {
        return scheme;
    }


    public void setScheme(String scheme) {
        this.scheme = scheme;
    }


    public String getRealm() {
        return realm;
    }


    public void setRealm(String realm) {
        this.realm = realm;
    }


    public int getData_type() {
        return data_type;
    }


    public void setData_type(int data_type) {
        this.data_type = data_type;
    }

    
    
}
