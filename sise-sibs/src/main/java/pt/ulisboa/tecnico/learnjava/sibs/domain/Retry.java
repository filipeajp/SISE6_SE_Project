package pt.ulisboa.tecnico.learnjava.sibs.domain;

import pt.ulisboa.tecnico.learnjava.bank.services.Services;

public class Retry extends State {
	private static Retry instance = null;
	private int attempt = 0;

	private Retry() {
	}

	public static State getInstance() {
		if (instance == null)
			instance = new Retry();
		return instance;
	}

	@Override
	public void process(TransferOperation t, Services services) {

	}

	@Override
	public void cancel(TransferOperation t, Services services) {

	}

}
