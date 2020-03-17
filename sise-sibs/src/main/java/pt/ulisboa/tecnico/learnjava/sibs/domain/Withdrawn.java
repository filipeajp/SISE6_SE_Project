package pt.ulisboa.tecnico.learnjava.sibs.domain;

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
	public void process(TransferOperation t) {
		String sourceIban = t.getSourceIban();
		String targetIban = t.getTargetIban();

		if (t.getBankCodeByIban(sourceIban).equals(t.getBankCodeByIban(targetIban))) {
			t.setState(Completed.getInstance());
		} else {
			t.setState(Deposited.getInstance());
		}

	}
}
