package mbwayInterface;

public class MBWayAccount {

	private String iban;
	private boolean isConfirmed;

	public MBWayAccount(String phoneNumber) {
		this.iban = phoneNumber;
		this.isConfirmed = false;
	}

	public String getIBAN() {
		return this.iban;
	}

	public boolean confirmationState() {
		return isConfirmed;
	}

	public void confirm() {
		this.isConfirmed = true;
	}

}
