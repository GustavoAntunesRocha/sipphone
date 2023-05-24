package pjsipphone.model;

import java.io.Serializable;

import org.pjsip.pjsua2.TransportConfig;

public class TransportConfigModel extends TransportConfig implements Serializable{ 
    
    private int port;
    private int type;
    
    public TransportConfigModel(int port, int type) {
        this.port = port;
        this.type = type;
    }
    
    public long getPort() {
        return port;
    }
    
    public int getType() {
        return type;
    }
    
    public void setPort(int port) {
        this.port = port;
    }
    
    public void setType(int type) {
        this.type = type;
    }
    
}
