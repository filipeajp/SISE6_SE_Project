package pt.ulisboa.tecnico.learnjava.sibs.domain;

public class Cancelled extends State {
	private static Cancelled instance = null;

	private Cancelled() {
	}

	public static State getInstance() {
		if (instance == null)
			instance = new Cancelled();
		return instance;
	}

	@Override
	public void process(TransferOperation t) {

	}

	@Override
	public void cancel(TransferOperation t) {

	}
}
