package pt.ulisboa.tecnico.learnjava.sibs.domain;

public class Error extends State {
	private static Error instance = null;

	private Error() {
	}

	public static State getInstance() {
		if (instance == null)
			instance = new Error();
		return instance;
	}

}
