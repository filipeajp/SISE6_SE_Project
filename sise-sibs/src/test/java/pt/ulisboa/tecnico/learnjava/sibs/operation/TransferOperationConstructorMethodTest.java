package pt.ulisboa.tecnico.learnjava.sibs.operation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.learnjava.bank.services.Services;
import pt.ulisboa.tecnico.learnjava.sibs.domain.Operation;
import pt.ulisboa.tecnico.learnjava.sibs.domain.Registered;
import pt.ulisboa.tecnico.learnjava.sibs.domain.TransferOperation;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.OperationException;

public class TransferOperationConstructorMethodTest {
	private static final String SOURCE_IBAN = "SourceIban";
	private static final String TARGET_IBAN = "TargetIban";
	private static final int VALUE = 100;

	private Services services;

	@Before
	public void setup() {
		this.services = new Services();
	}

	@Test
	public void success() throws OperationException {
		TransferOperation operation = new TransferOperation(SOURCE_IBAN, TARGET_IBAN, VALUE, this.services);

		assertEquals(Operation.OPERATION_TRANSFER, operation.getType());
		assertEquals(100, operation.getValue());
		assertEquals(SOURCE_IBAN, operation.getSourceIban());
		assertEquals(TARGET_IBAN, operation.getTargetIban());
		assertTrue(operation.getCurrentState() instanceof Registered);
	}

	@Test(expected = OperationException.class)
	public void nonPositiveValue() throws OperationException {
		new TransferOperation(SOURCE_IBAN, TARGET_IBAN, 0, this.services);
	}

	@Test(expected = OperationException.class)
	public void nullSourceIban() throws OperationException {
		new TransferOperation(null, TARGET_IBAN, 100, this.services);
	}

	@Test(expected = OperationException.class)
	public void emptySourceIban() throws OperationException {
		new TransferOperation("", TARGET_IBAN, 100, this.services);
	}

	@Test(expected = OperationException.class)
	public void nullTargetIban() throws OperationException {
		new TransferOperation(SOURCE_IBAN, null, 100, this.services);
	}

	@Test(expected = OperationException.class)
	public void emptyTargetIban() throws OperationException {
		new TransferOperation(SOURCE_IBAN, "", 100, this.services);
	}

	@After
	public void tearDown() {
		this.services = null;
	}

}
