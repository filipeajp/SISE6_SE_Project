package pt.ulisboa.tecnico.learnjava.sibs.domain;

import pt.ulisboa.tecnico.learnjava.bank.exceptions.AccountException;
import pt.ulisboa.tecnico.learnjava.bank.services.Services;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.OperationException;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.SibsException;

public class TransferOperation extends Operation {
	private final String sourceIban;
	private final String targetIban;
	Services services;

	private State currentState;
	private State lastState;
	public int attempts = 3;

	public TransferOperation(String sourceIban, String targetIban, int value, Services services)
			throws OperationException {
		super(Operation.OPERATION_TRANSFER, value);

		if (invalidString(sourceIban) || invalidString(targetIban)) {
			throw new OperationException();
		}

		this.sourceIban = sourceIban;
		this.targetIban = targetIban;
		this.services = services;
		this.currentState = Registered.getInstance();
	}

	private boolean invalidString(String name) {
		return name == null || name.length() == 0;
	}

	@Override
	public int commission() {
		return (int) Math.round(super.commission() + getValue() * 0.05);
	}

	public String getSourceIban() {
		return this.sourceIban;
	}

	public String getTargetIban() {
		return this.targetIban;
	}

	public State getCurrentState() {
		return this.currentState;
	}

	public void setState(State s) {
		this.currentState = s;
	}

	public State getLastState() {
		return this.lastState;
	}

	public void setLastState(State s) {
		this.lastState = s;
	}

	public void process() throws AccountException, SibsException {
		this.currentState.process(this, services);
	}

	public void cancel() throws SibsException, AccountException {
		this.currentState.cancel(this, services);
	}

	public String getBankCodeByIban(String iban) {
		return iban.substring(0, 3);
	}

}
