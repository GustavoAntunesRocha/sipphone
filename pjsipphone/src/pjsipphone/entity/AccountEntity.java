package pjsipphone.entity;

import org.pjsip.pjsua2.Account;
import org.pjsip.pjsua2.AccountInfo;
import org.pjsip.pjsua2.OnIncomingCallParam;
import org.pjsip.pjsua2.OnRegStateParam;

public class AccountEntity extends Account{

	public AccountEntity() {}
	
	@Override
	public void onRegState(OnRegStateParam prm) {
		try {
			AccountInfo ai = getInfo();
			System.out.println(ai.getRegIsActive() ? "Register: code=" : "*** Unregister: code=" + prm.getCode());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public void onIncomingCall(OnIncomingCallParam prm) {
		
	}
}
