package SibsInterface;

import java.util.HashMap;
import java.util.Random;

public class MBWayInterfaceModel {
	private Random r = new Random();
	
	private HashMap<String, MBWayAccount> users = new HashMap<>();
	
	private String userInput;	
	private String code;
	private String phoneNumber;

	public String getUserInput() {
		return this.userInput;
	}
	
	public String getPhoneNumber() {
		return this.phoneNumber;
	}

	public void setUserInput(String userInput) {
		this.userInput = userInput;
	}
	
	public String generateCode(String iban, String phoneNumber) {
		
		this.phoneNumber = phoneNumber;
		
		if(this.users.containsKey(this.phoneNumber)) {
			this.users.replace(this.phoneNumber, new MBWayAccount(iban));
		} else {
			this.users.put(this.phoneNumber, new MBWayAccount(iban));
		}
		
		this.code = String.valueOf(r.nextInt((999999 - 100000) + 1) + 100000);
			
		return this.code;
	}
	
	public boolean confirmCode(String code) {
		if(this.code.equals(code)) {
			
			this.users.get(this.phoneNumber).confirm();
			return true;
			
		} else {
			return false;
		}
	}
	
	public MBWayAccount getMBAccount(String phoneNumber) {
		return this.users.get(phoneNumber);
	}
	
}
