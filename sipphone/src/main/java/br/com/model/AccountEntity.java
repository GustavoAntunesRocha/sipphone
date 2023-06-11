package br.com.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import org.pjsip.pjsua2.Account;
import org.pjsip.pjsua2.AccountInfo;
import org.pjsip.pjsua2.CallOpParam;
import org.pjsip.pjsua2.OnIncomingCallParam;
import org.pjsip.pjsua2.OnRegStateParam;

import br.com.controller.CallController;
import br.com.controller.MainController;
import br.com.view.MainWindow;
import javafx.application.Platform;

public class AccountEntity extends Account implements Serializable{

	private int id;

	private String name;

	private AccountConfigModel accountConfig;

	private TransportConfigModel transportConfig;

	private static AccountEntity instance;

	public enum Status {
		ONLINE,
		OFFLINE,
		BUSY,
		DISCONECTED,
		AWAY
	}

	private Status status;

	private AccountEntity() {}

	public static synchronized AccountEntity getInstance() {
        if (instance == null) {
            instance = new AccountEntity();
			instance.setAccountConfigModel(new AccountConfigModel());
			instance.setTransportConfigModel(new TransportConfigModel());
        }
        return instance;
    }
	
	@Override
	public void onRegState(OnRegStateParam prm) {
		try {
			AccountInfo ai = getInfo();
			System.out.println(ai.getRegIsActive() ? "\n\nRegister: code=" : "*** Unregister: code=" + prm.getCode());
			this.status = ai.getRegIsActive() ? Status.ONLINE : Status.OFFLINE;
			Platform.runLater(() -> {
				if (ai.getRegIsActive()) {
					MainController.getInstance().updateAccountText();
				} else {
					System.out.println("\n\n\n\nUnregister");
				}
			});
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
			CallController callController = CallController.getInstance();
			callController.setCallEntity(call);
			callController.answerInput(call);
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

	public static AccountEntity readAccountFromFile(String filePath) {
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

	public void removeAccount() {
		instance = null;
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

	public AccountConfigModel getAccountConfigModel() {
		return accountConfig;
	}

	public void setAccountConfigModel(AccountConfigModel accountConfig) {
		this.accountConfig = accountConfig;
	}

	public TransportConfigModel getTransportConfigModel() {
		return transportConfig;
	}

	public void setTransportConfigModel(TransportConfigModel transportConfig) {
		this.transportConfig = transportConfig;
	}

    public static void deleteAccountFile(String fileLocation) {
		File file = new File(fileLocation);
		if (file.exists()) {
			file.delete();
		}
    }

	public String getStatus(){
		return status.toString();
	}

	public void setStatus(Status status){
		this.status = status;
	}

	
}
