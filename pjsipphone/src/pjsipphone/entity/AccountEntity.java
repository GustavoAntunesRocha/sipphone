package pjsipphone.entity;

import org.pjsip.pjsua2.Account;
import org.pjsip.pjsua2.AccountInfo;
import org.pjsip.pjsua2.Call;
import org.pjsip.pjsua2.CallInfo;
import org.pjsip.pjsua2.CallOpParam;
import org.pjsip.pjsua2.OnIncomingCallParam;
import org.pjsip.pjsua2.OnRegStateParam;

public class AccountEntity extends Account{

	public AccountEntity() {}
	
	@Override
	public void onRegState(OnRegStateParam prm) {
		try {
			AccountInfo ai = getInfo();
			System.out.println(ai.getRegIsActive() ? "\n\nRegister: code=" : "*** Unregister: code=" + prm.getCode());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public void onIncomingCall(OnIncomingCallParam prm) {
		try {
			int callId = prm.getCallId();
			CallEntity call = new CallEntity(this, callId);
			CallOpParam callParam = new CallOpParam();
			callParam.setStatusCode(180);
			call.answer(callParam);
			CallInfo callInfo = call.getInfo();
			System.out.println("\n\nIncoming call from: " + callInfo.getRemoteUri());
			callParam.setStatusCode(200);
			call.answer(callParam);
			//call.hangup(callParam);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
