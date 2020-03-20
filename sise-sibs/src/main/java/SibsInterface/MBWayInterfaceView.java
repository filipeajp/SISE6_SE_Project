package SibsInterface;

public class MBWayInterfaceView {
	
	public void printExitMessage() {
		System.out.println("You exit the app.");
	}
	
	public void printCode(String code) {
		System.out.println("Code: " + code + " (dont't share it with anyone)");
	}
	
	public void correctCode() {
		System.out.println("MBWAY association confirmed successfully!");
	}
	
	public void wrongCode() {
		System.out.println("Wrong confirmation code. Try association again.");
	}
	
	public void successfullTransfer() {
		System.out.println("Transfer performed successfully!");
	}
	
	public void wrongPhoneNumber() {
		System.out.println("Wrong phone number.");
	}
	
	public void notEnoughMoney() {
		System.out.println("Not enough money on the source account.");
	}	
	
	public void friendNotRegistered(String phoneNr) {
		System.out.println("Friend " + phoneNr + " is not registered.");
	}

	public void missingFriends() {
		System.out.println("Oh no! Friends are missing.");
	}

	public void tooManyFriends() {
		System.out.println("Oh no! Too many friends");		
	}
	
	public void billWrong() {
		System.out.println("Something is wrong. Did you set the bill right?");		
	}

}
