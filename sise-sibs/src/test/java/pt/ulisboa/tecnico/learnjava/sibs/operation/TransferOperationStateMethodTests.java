package pt.ulisboa.tecnico.learnjava.sibs.operation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import pt.ulisboa.tecnico.learnjava.sibs.domain.Cancelled;
import pt.ulisboa.tecnico.learnjava.sibs.domain.Completed;
import pt.ulisboa.tecnico.learnjava.sibs.domain.Deposited;
import pt.ulisboa.tecnico.learnjava.sibs.domain.Operation;
import pt.ulisboa.tecnico.learnjava.sibs.domain.Registered;
import pt.ulisboa.tecnico.learnjava.sibs.domain.TransferOperation;
import pt.ulisboa.tecnico.learnjava.sibs.domain.Withdrawn;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.OperationException;

public class TransferOperationStateMethodTests {

	private static final String SOURCE_IBAN = "SourceIban";
	private static final String TARGET_IBAN = "TargetIban";
	private static final int VALUE = 100;

	@Test
	public void stateTest() throws OperationException {
		TransferOperation operation = new TransferOperation(SOURCE_IBAN, TARGET_IBAN, VALUE);

		assertTrue(operation.getCurrentState() instanceof Registered);
		operation.process();
		assertTrue(operation.getCurrentState() instanceof Withdrawn);
		operation.process();
		assertTrue(operation.getCurrentState() instanceof Deposited);
		operation.process();
		assertTrue(operation.getCurrentState() instanceof Completed);
		operation.cancel();
		assertTrue(operation.getCurrentState() instanceof Completed);

		assertEquals(Operation.OPERATION_TRANSFER, operation.getType());
		assertEquals(100, operation.getValue());
		assertEquals(SOURCE_IBAN, operation.getSourceIban());
		assertEquals(TARGET_IBAN, operation.getTargetIban());
	}

	@Test
	public void stateNoFeeTest() throws OperationException {
		TransferOperation operation = new TransferOperation(SOURCE_IBAN, SOURCE_IBAN, VALUE);

		assertTrue(operation.getCurrentState() instanceof Registered);
		operation.process();
		assertTrue(operation.getCurrentState() instanceof Withdrawn);
		operation.process();
		assertTrue(operation.getCurrentState() instanceof Completed);

		assertEquals(Operation.OPERATION_TRANSFER, operation.getType());
		assertEquals(100, operation.getValue());
		assertEquals(SOURCE_IBAN, operation.getSourceIban());
		assertEquals(SOURCE_IBAN, operation.getTargetIban());
	}

	@Test
	public void cancelStateTest() throws OperationException {
		TransferOperation operation = new TransferOperation(SOURCE_IBAN, TARGET_IBAN, VALUE);

		assertTrue(operation.getCurrentState() instanceof Registered);
		operation.process();
		assertTrue(operation.getCurrentState() instanceof Withdrawn);
		operation.cancel();
		assertTrue(operation.getCurrentState() instanceof Cancelled);
		operation.process();
		assertTrue(operation.getCurrentState() instanceof Cancelled);

		assertEquals(Operation.OPERATION_TRANSFER, operation.getType());
		assertEquals(100, operation.getValue());
		assertEquals(SOURCE_IBAN, operation.getSourceIban());
		assertEquals(TARGET_IBAN, operation.getTargetIban());
	}

}
