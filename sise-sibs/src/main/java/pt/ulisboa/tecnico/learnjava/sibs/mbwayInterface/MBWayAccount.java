package pt.ulisboa.tecnico.learnjava.sibs.mbwayInterface;

public class MBWayAccount {

	private String iban;
	private boolean isConfirmed;

	public MBWayAccount(String iban) {
		this.iban = iban;
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
