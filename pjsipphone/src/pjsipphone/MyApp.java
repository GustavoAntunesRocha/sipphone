package pjsipphone;

import org.pjsip.pjsua2.AccountConfig;
import org.pjsip.pjsua2.AuthCredInfo;
import org.pjsip.pjsua2.Endpoint;
import org.pjsip.pjsua2.EpConfig;
import org.pjsip.pjsua2.TransportConfig;

import pjsipphone.model.AccountEntity;

//Subclass to extend the Account and get notifications etc.


public class MyApp {
static {
   System.loadLibrary("pjsua2");
   System.out.println("Library loaded");
}

public static void main(String argv[]) {
   try {
       // Create endpoint
       Endpoint ep = new Endpoint();
       ep.libCreate();
       // Initialize endpoint
       EpConfig epConfig = new EpConfig();
       ep.libInit( epConfig );

       AccountEntity acc = AccountEntity.readAccountFromFile("account.ser");

       // Create SIP transport. Error handling sample is shown
       TransportConfig sipTpConfig = new TransportConfig();
       sipTpConfig.setPort(acc.getTransportConfigModel().getPort());
       ep.transportCreate(acc.getTransportConfigModel().getType(), sipTpConfig);
       // Start the library
       ep.libStart(); 
       
       AccountConfig acfg = new AccountConfig();
       acfg.setIdUri("sip:" + acc.getAccountConfigModel().getUsername() + "@" + acc.getAccountConfigModel().getDomain());
       acfg.getRegConfig().setRegistrarUri("sip:" + acc.getAccountConfigModel().getDomain());
       AuthCredInfo cred = new AuthCredInfo(acc.getAccountConfigModel().getScheme(),
            acc.getAccountConfigModel().getRealm(), acc.getAccountConfigModel().getUsername(),
        0, acc.getAccountConfigModel().getPassword());
       acfg.getSipConfig().getAuthCreds().add( cred );
       // Create the account
       
       acc.create(acfg);
       
       
       //Waiting for an call or the press of any key to continue
       System.out.println("Press any key to exit...");
       System.in.read();
       
       /* Explicitly delete the account.
        * This is to avoid GC to delete the endpoint first before deleting
        * the account.
        */
       acc.delete();

       // Explicitly destroy and delete endpoint
       ep.libDestroy();
       ep.delete();

   } catch (Exception e) {
       System.out.println(e);
       return;
   }
}
}
