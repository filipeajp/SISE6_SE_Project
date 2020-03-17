package pt.ulisboa.tecnico.learnjava.sibs.domain;

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
	public void process(TransferOperation t) {
		t.setState(Completed.getInstance());
	}
}
