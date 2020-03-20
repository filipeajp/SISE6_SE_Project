package pt.ulisboa.tecnico.learnjava.sibs.domain;

import pt.ulisboa.tecnico.learnjava.bank.exceptions.AccountException;
import pt.ulisboa.tecnico.learnjava.bank.services.Services;

public class Retry extends State {
	private static Retry instance = null;

	private Retry() {
	}

	public static State getInstance() {
		if (instance == null)
			instance = new Retry();
		return instance;
	}

	@Override
	public void process(TransferOperation t, Services services) throws AccountException {
		String sourceIban = t.getSourceIban();
		String targetIban = t.getTargetIban();
		if (t.attempts <= 0) {
			if (t.getLastState() instanceof Withdrawn) {
				services.deposit(t.getSourceIban(), t.getValue());
			} else if (t.getLastState() instanceof Deposited) {
				services.withdraw(targetIban, t.getValue());
				services.deposit(t.getSourceIban(), t.getValue());
			}
			t.setState(Error.getInstance());
			t.attempts = 3;
		} else {
			t.attempts--;
			t.setState(t.getLastState());
		}
	}

	@Override
	public void cancel(TransferOperation t, Services services) {

	}

}
