package pt.ulisboa.tecnico.learnjava.sibs.domain;

import pt.ulisboa.tecnico.learnjava.bank.exceptions.AccountException;
import pt.ulisboa.tecnico.learnjava.bank.services.Services;

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
	public void process(TransferOperation t, Services services) throws AccountException {
		String sourceIban = t.getSourceIban();
		String targetIban = t.getTargetIban();

		if (t.getBankCodeByIban(sourceIban).equals(t.getBankCodeByIban(targetIban)))
			services.withdraw(t.getSourceIban(), t.getValue());
		else
			services.withdraw(t.getSourceIban(), t.getValue() + t.commission());

		t.setState(Withdrawn.getInstance());

	}

	@Override
	public void cancel(TransferOperation t, Services services) {
		t.setState(Cancelled.getInstance());
	}
}
