package pt.ulisboa.tecnico.learnjava.sibs.domain;

import pt.ulisboa.tecnico.learnjava.bank.exceptions.AccountException;
import pt.ulisboa.tecnico.learnjava.bank.services.Services;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.SibsException;

public class State {

	public void process(TransferOperation t, Services services) throws SibsException, AccountException {
	}

	public void cancel(TransferOperation t, Services services) throws SibsException, AccountException {
	}

}
