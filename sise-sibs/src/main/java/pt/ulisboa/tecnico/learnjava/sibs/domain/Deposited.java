package pt.ulisboa.tecnico.learnjava.sibs.domain;

import pt.ulisboa.tecnico.learnjava.bank.exceptions.AccountException;
import pt.ulisboa.tecnico.learnjava.bank.services.Services;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.SibsException;

public class Deposited extends State {
	private static Deposited instance = null;

	private Deposited() {
	}

	public static State getInstance() {
		if (instance == null)
			instance = new Deposited();
		return instance;
	}

	@Override
	public void process(TransferOperation t, Services services) throws AccountException, SibsException {
		try {
			services.withdraw(t.getSourceIban(), t.commission());
			t.setState(Completed.getInstance());
		} catch (AccountException e) {
			throw new SibsException();
		}

	}

	@Override
	public void cancel(TransferOperation t, Services services) throws AccountException {
		String sourceIban = t.getSourceIban();
		String targetIban = t.getTargetIban();

		services.withdraw(targetIban, t.getValue());
		services.deposit(t.getSourceIban(), t.getValue());

		t.setState(Cancelled.getInstance());
	}
}
