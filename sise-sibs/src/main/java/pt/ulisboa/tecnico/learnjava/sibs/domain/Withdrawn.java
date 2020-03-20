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
		String sourceIban = t.getSourceIban();
		String targetIban = t.getTargetIban();

		try {
			services.deposit(targetIban, t.getValue());
			if (t.getBankCodeByIban(sourceIban).equals(t.getBankCodeByIban(targetIban)))
				t.setState(Completed.getInstance());
			else
				t.setState(Deposited.getInstance());
		} catch (AccountException e) {
			throw new SibsException();
		}
	}

	@Override
	public void cancel(TransferOperation t, Services services) throws AccountException {
		String sourceIban = t.getSourceIban();
		String targetIban = t.getTargetIban();

		if (t.getBankCodeByIban(sourceIban).equals(t.getBankCodeByIban(targetIban)))
			services.deposit(t.getSourceIban(), t.getValue());
		else
			services.deposit(t.getSourceIban(), t.getValue() + t.commission());

		t.setState(Cancelled.getInstance());
	}

}
