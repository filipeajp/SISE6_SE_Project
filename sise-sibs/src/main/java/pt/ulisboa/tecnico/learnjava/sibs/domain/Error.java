package pt.ulisboa.tecnico.learnjava.sibs.domain;

import pt.ulisboa.tecnico.learnjava.bank.services.Services;

public class Error extends State {
	private static Error instance = null;

	private Error() {
	}

	public static State getInstance() {
		if (instance == null)
			instance = new Error();
		return instance;
	}

	@Override
	public void process(TransferOperation t, Services services) {
	}

	@Override
	public void cancel(TransferOperation t, Services services) {
	}

}
