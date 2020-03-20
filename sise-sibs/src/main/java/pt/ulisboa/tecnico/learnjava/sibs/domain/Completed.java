package pt.ulisboa.tecnico.learnjava.sibs.domain;

import pt.ulisboa.tecnico.learnjava.bank.services.Services;

public class Completed extends State {
	private static Completed instance = null;

	private Completed() {
	}

	public static State getInstance() {
		if (instance == null)
			instance = new Completed();
		return instance;
	}

	@Override
	public void process(TransferOperation t, Services services) {
	}

	@Override
	public void cancel(TransferOperation t, Services services) {
	}
}
