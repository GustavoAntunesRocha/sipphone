package pjsipphone.entity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.pjsip.pjsua2.Account;
import org.pjsip.pjsua2.AccountConfig;
import org.pjsip.pjsua2.AccountInfo;
import org.pjsip.pjsua2.CallInfo;
import org.pjsip.pjsua2.CallOpParam;
import org.pjsip.pjsua2.OnIncomingCallParam;
import org.pjsip.pjsua2.OnRegStateParam;
import org.pjsip.pjsua2.TransportConfig;

public class AccountEntity extends Account{

	private int id;

	private String name;

	private AccountConfig accountConfig;

	private TransportConfig transportConfig;

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

	public static void writeAccountToFile(AccountEntity accountEntity, String filePath) {
		try {
			File file = new File(filePath);
			FileOutputStream fileOutputStream = new FileOutputStream(file);
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
			objectOutputStream.writeObject(accountEntity);
			objectOutputStream.close();
			fileOutputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static AccountEntity readAccountConfigFromFile(String filePath) {
		try {
			File file = new File(filePath);
			FileInputStream fileInputStream = new FileInputStream(file);
			ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
			AccountEntity accountEntity = (AccountEntity) objectInputStream.readObject();
			objectInputStream.close();
			fileInputStream.close();
			return accountEntity;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public AccountEntity(String name, AccountConfig accountConfig, TransportConfig transportConfig) {
		this.name = name;
		this.accountConfig = accountConfig;
		this.transportConfig = transportConfig;
	}

	public AccountEntity(int id, String name, AccountConfig accountConfig, TransportConfig transportConfig) {
		this.id = id;
		this.name = name;
		this.accountConfig = accountConfig;
		this.transportConfig = transportConfig;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public AccountConfig getAccountConfig() {
		return accountConfig;
	}

	public void setAccountConfig(AccountConfig accountConfig) {
		this.accountConfig = accountConfig;
	}

	public TransportConfig getTransportConfig() {
		return transportConfig;
	}

	public void setTransportConfig(TransportConfig transportConfig) {
		this.transportConfig = transportConfig;
	}

	
}
