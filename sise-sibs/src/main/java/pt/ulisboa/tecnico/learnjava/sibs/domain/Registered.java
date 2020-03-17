package pt.ulisboa.tecnico.learnjava.sibs.domain;

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
	public void process(TransferOperation t) {
		t.setState(Withdrawn.getInstance());
	}
}
