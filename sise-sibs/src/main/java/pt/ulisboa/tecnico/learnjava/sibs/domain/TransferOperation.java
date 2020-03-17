package pt.ulisboa.tecnico.learnjava.sibs.domain;

import pt.ulisboa.tecnico.learnjava.sibs.exceptions.OperationException;

public class TransferOperation extends Operation {
	private final String sourceIban;
	private final String targetIban;

	private State currentState;
	private int attempts = 3;

	public TransferOperation(String sourceIban, String targetIban, int value) throws OperationException {
		super(Operation.OPERATION_TRANSFER, value);

		if (invalidString(sourceIban) || invalidString(targetIban)) {
			throw new OperationException();
		}

		this.sourceIban = sourceIban;
		this.targetIban = targetIban;
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

	public void process() {
		this.currentState.process(this);
	}

	public void cancel() {
		if (!(this.currentState instanceof Completed))
			this.currentState = Cancelled.getInstance();
	}

	public void retry() {
		if (attempts == 0) {
			this.currentState = Error.getInstance();
		} else {
			this.attempts--;
			this.currentState = Retry.getInstance();
		}
	}

	public String getBankCodeByIban(String iban) {
		return iban.substring(0, 3);
	}

}
