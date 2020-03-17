package pt.ulisboa.tecnico.learnjava.sibs.domain;

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

}
