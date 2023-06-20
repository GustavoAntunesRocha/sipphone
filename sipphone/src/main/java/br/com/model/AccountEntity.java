package br.com.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.pjsip.pjsua2.Account;
import org.pjsip.pjsua2.AccountInfo;
import org.pjsip.pjsua2.CallOpParam;
import org.pjsip.pjsua2.OnIncomingCallParam;
import org.pjsip.pjsua2.OnRegStateParam;

import br.com.App;
import br.com.controller.CallController;
import br.com.controller.MainController;
import javafx.application.Platform;

public class AccountEntity extends Account implements Serializable {

	private int id;

	private String name;

	private AccountConfigModel accountConfig;

	private TransportConfigModel transportConfig;

	private List<CallHistoryEntry> callHistory;

	private List<Contact> contacts;

	public enum Status {
		ONLINE,
		OFFLINE,
		BUSY,
		DISCONECTED,
		AWAY
	}

	private Status status;

	public AccountEntity() {
		super();
		this.id = 0;
		this.accountConfig = new AccountConfigModel();
		this.transportConfig = new TransportConfigModel();
		this.callHistory = new ArrayList<>();
		this.contacts = new ArrayList<>();
	}

	@Override
	public void onRegState(OnRegStateParam prm) {
		try {
			AccountInfo ai = getInfo();
			System.out.println(ai.getRegIsActive() ? "\n\n\n\nRegister: code="
					: "*** Unregister: code=" + prm.getCode() + "\n\n\n");
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

	public static void writeAccountToFile(AccountEntity accountEntity) {
		try {
			File file = new File(App.ACC_FILE_PATH);
			FileOutputStream fileOutputStream = new FileOutputStream(file);
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
			objectOutputStream.writeObject(accountEntity);
			objectOutputStream.writeObject(accountEntity.getAccountConfigModel());
			objectOutputStream.writeObject(accountEntity.getTransportConfigModel());
			objectOutputStream.writeObject(accountEntity.getCallHistory());
			objectOutputStream.writeObject(accountEntity.getContacts());
			objectOutputStream.close();
			fileOutputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static AccountEntity readAccountFromFile() {
		AccountEntity accountEntity = App.acc;
		try {
			File file = new File(App.ACC_FILE_PATH);
			FileInputStream fileInputStream = new FileInputStream(file);
			ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
			accountEntity = (AccountEntity) objectInputStream.readObject();
			accountEntity.setAccountConfigModel((AccountConfigModel) objectInputStream.readObject());
			accountEntity.setTransportConfigModel((TransportConfigModel) objectInputStream.readObject());
			List<CallHistoryEntry> callHistory = (List<CallHistoryEntry>) objectInputStream.readObject();
			List<Contact> contacts = (List<Contact>) objectInputStream.readObject();
			accountEntity.setCallHistory(callHistory);
			accountEntity.setContacts(contacts);
			objectInputStream.close();
			fileInputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return accountEntity;
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

	public static void deleteAccountFile() {
		File file = new File(App.ACC_FILE_PATH);
		if (file.exists()) {
			file.delete();
		}
	}

	public String getStatus() {
		return status.toString();
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public List<CallHistoryEntry> getCallHistory() {
		return callHistory;
	}

	public void setCallHistory(List<CallHistoryEntry> callHistory) {
		this.callHistory = callHistory;
	}

	public void addCallHistoryEntry(CallHistoryEntry callHistoryEntry) {
		this.callHistory.add(callHistoryEntry);
	}

	public List<Contact> getContacts() {
		return contacts;
	}

	public void setContacts(List<Contact> contacts) {
		this.contacts = contacts;
	}

}
