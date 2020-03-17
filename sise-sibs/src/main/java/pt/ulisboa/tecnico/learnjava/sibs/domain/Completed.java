package pt.ulisboa.tecnico.learnjava.sibs.domain;

public class Completed extends State {
	private static Completed instance = null;

	private Completed() {
	}

	public static State getInstance() {
		if (instance == null)
			instance = new Completed();
		return instance;
	}
}
