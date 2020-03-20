package pt.ulisboa.tecnico.learnjava.sibs.domain;

import pt.ulisboa.tecnico.learnjava.bank.exceptions.AccountException;
import pt.ulisboa.tecnico.learnjava.bank.services.Services;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.OperationException;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.SibsException;

public class Sibs {
	final Operation[] operations;
	Services services;

	public Sibs(int maxNumberOfOperations, Services services) {
		this.operations = new Operation[maxNumberOfOperations];
		this.services = services;
	}

	public void transfer(String sourceIban, String targetIban, int amount)
			throws SibsException, AccountException, OperationException {

		if (!this.services.AccountExists(sourceIban) || !this.services.AccountExists(targetIban)) {
			throw new SibsException();
		}

		this.addOperation(Operation.OPERATION_TRANSFER, sourceIban, targetIban, amount);
	}

	public void processOperations() throws SibsException, AccountException {
		TransferOperation operation = null;
		for (int i = 0; i < this.operations.length; i++) {
			if (this.operations[i] != null && this.operations[i] instanceof TransferOperation) {
				try {
					operation = (TransferOperation) this.getOperation(i);
					if (!(operation.getCurrentState() instanceof Cancelled
							|| operation.getCurrentState() instanceof Completed)) {
						while (!(operation.getCurrentState() instanceof Completed)) {
							operation.process();
						}
					}
				} catch (SibsException | AccountException e) {
					operation.setLastState(operation.getCurrentState());
					operation.setState(Retry.getInstance());
					operation.process();
				}
			}
		}
	}

	public void cancelOperation(int position) throws SibsException, AccountException {
		TransferOperation operation = (TransferOperation) this.getOperation(position);
		if (operation == null)
			throw new SibsException();

		operation.cancel();
	}

	public int addOperation(String type, String sourceIban, String targetIban, int value)
			throws OperationException, SibsException {
		int position = -1;
		for (int i = 0; i < this.operations.length; i++) {
			if (this.operations[i] == null) {
				position = i;
				break;
			}
		}

		if (position == -1) {
			throw new SibsException();
		}

		Operation operation;
		if (type.equals(Operation.OPERATION_TRANSFER)) {
			operation = new TransferOperation(sourceIban, targetIban, value, this.services);
		} else {
			operation = new PaymentOperation(targetIban, value);
		}

		this.operations[position] = operation;
		return position;
	}

	public void removeOperation(int position) throws SibsException {
		if (position < 0 || position > this.operations.length) {
			throw new SibsException();
		}
		this.operations[position] = null;
	}

	public Operation getOperation(int position) throws SibsException {
		if (position < 0 || position > this.operations.length) {
			throw new SibsException();
		}
		return this.operations[position];
	}

	public int getNumberOfOperations() {
		int result = 0;
		for (int i = 0; i < this.operations.length; i++) {
			if (this.operations[i] != null) {
				result++;
			}
		}
		return result;
	}

	public int getTotalValueOfOperations() {
		int result = 0;
		for (int i = 0; i < this.operations.length; i++) {
			if (this.operations[i] != null) {
				result = result + this.operations[i].getValue();
			}
		}
		return result;
	}

	public int getTotalValueOfOperationsForType(String type) {
		int result = 0;
		for (int i = 0; i < this.operations.length; i++) {
			if (this.operations[i] != null && this.operations[i].getType().equals(type)) {
				result = result + this.operations[i].getValue();
			}
		}
		return result;
	}
}
