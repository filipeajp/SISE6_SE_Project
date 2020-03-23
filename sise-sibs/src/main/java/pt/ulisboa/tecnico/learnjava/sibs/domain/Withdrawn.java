package pt.ulisboa.tecnico.learnjava.sibs.domain;

import pt.ulisboa.tecnico.learnjava.bank.exceptions.AccountException;
import pt.ulisboa.tecnico.learnjava.bank.services.Services;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.SibsException;

public class Withdrawn extends State {
	private static Withdrawn instance = null;

	private Withdrawn() {
	}

	public static State getInstance() {
		if (instance == null)
			instance = new Withdrawn();
		return instance;
	}

	@Override
	public void process(TransferOperation t, Services services) throws AccountException, SibsException {
		try {
			services.deposit(t.getTargetIban(), t.getValue());
			if (services.checkSameBanks(t.getSourceIban(), t.getTargetIban()))
				t.setState(Completed.getInstance());
			else
				t.setState(Deposited.getInstance());
		} catch (AccountException e) {
			throw new SibsException();
		}
	}

	@Override
	public void cancel(TransferOperation t, Services services) throws AccountException {
		services.deposit(t.getSourceIban(), t.getValue());

		t.setState(Cancelled.getInstance());
	}

}
