package pt.ulisboa.tecnico.learnjava.sibs.domain;

import pt.ulisboa.tecnico.learnjava.bank.exceptions.AccountException;
import pt.ulisboa.tecnico.learnjava.bank.services.Services;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.SibsException;

public class Registered extends State {
	private static Registered instance = null;

	private Registered() {
	}

	public static State getInstance() {
		if (instance == null)
			instance = new Registered();
		return instance;
	}

	@Override
	public void process(TransferOperation t, Services services) throws AccountException, SibsException {
		try {
			services.withdraw(t.getSourceIban(), t.getValue());
			t.setState(Withdrawn.getInstance());
		} catch (AccountException e) {
			throw new SibsException();
		}
	}

	@Override
	public void cancel(TransferOperation t, Services services) {
		t.setState(Cancelled.getInstance());
	}
}
