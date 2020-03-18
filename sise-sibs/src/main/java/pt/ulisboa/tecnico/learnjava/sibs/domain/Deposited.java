package pt.ulisboa.tecnico.learnjava.sibs.domain;

import pt.ulisboa.tecnico.learnjava.bank.exceptions.AccountException;
import pt.ulisboa.tecnico.learnjava.bank.services.Services;

public class Deposited extends State {
	private static Deposited instance = null;
	Services services = new Services();

	private Deposited() {
	}

	public static State getInstance() {
		if (instance == null)
			instance = new Deposited();
		return instance;
	}

	@Override
	public void process(TransferOperation t) {
		t.setState(Completed.getInstance());
	}

	@Override
	public void cancel(TransferOperation t) throws AccountException {
		String sourceIban = t.getSourceIban();
		String targetIban = t.getTargetIban();

		services.withdraw(targetIban, t.getValue());

		if (t.getBankCodeByIban(sourceIban).equals(t.getBankCodeByIban(targetIban)))
			services.deposit(t.getSourceIban(), t.getValue());
		else
			services.deposit(t.getSourceIban(), t.getValue() + t.commission());

		t.setState(Cancelled.getInstance());
	}
}
