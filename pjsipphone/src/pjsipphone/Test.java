package pjsipphone;

import org.pjsip.pjsua2.Account;
import org.pjsip.pjsua2.AccountConfig;
import org.pjsip.pjsua2.AuthCredInfo;
import org.pjsip.pjsua2.Endpoint;
import org.pjsip.pjsua2.EpConfig;
import org.pjsip.pjsua2.OnRegStateParam;
import org.pjsip.pjsua2.TransportConfig;
import org.pjsip.pjsua2.pjsip_transport_type_e;

//Subclass to extend the Account and get notifications etc.
class MyAccount extends Account {
@Override
public void onRegState(OnRegStateParam prm) {
   System.out.println("*** On registration state: " + prm.getCode() + prm.getReason());
}
}

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
       acfg.setIdUri("sip:test@sip.pjsip.org");
       acfg.getRegConfig().setRegistrarUri("sip:sip.pjsip.org");
       AuthCredInfo cred = new AuthCredInfo("digest", "*", "test", 0, "secret");
       acfg.getSipConfig().getAuthCreds().add( cred );
       // Create the account
       MyAccount acc = new MyAccount();
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
