package pjsipphone;

import org.pjsip.pjsua2.AccountConfig;
import org.pjsip.pjsua2.AuthCredInfo;
import org.pjsip.pjsua2.Endpoint;
import org.pjsip.pjsua2.EpConfig;
import org.pjsip.pjsua2.TransportConfig;
import org.pjsip.pjsua2.pjsip_transport_type_e;

import pjsipphone.entity.AccountEntity;

//Subclass to extend the Account and get notifications etc.


public class Test {
static {
	System.setProperty("java.library.path", "/pjsipphone/libs");
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
       // Create SIP transport. Error handling sample is shown
       TransportConfig sipTpConfig = new TransportConfig();
       sipTpConfig.setPort(5060);
       ep.transportCreate(pjsip_transport_type_e.PJSIP_TRANSPORT_UDP, sipTpConfig);
       // Start the library
       ep.libStart();

       AccountConfig acfg = new AccountConfig();
       acfg.setIdUri("sip:211@telefonia.orlac.local");
       acfg.getRegConfig().setRegistrarUri("sip:telefonia.orlac.local");
       AuthCredInfo cred = new AuthCredInfo("digest", "*", "211", 0, "senha");
       acfg.getSipConfig().getAuthCreds().add( cred );
       // Create the account
       AccountEntity acc = new AccountEntity();
       acc.create(acfg);
       // Here we don't have anything else to do..
       Thread.sleep(10000);
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
