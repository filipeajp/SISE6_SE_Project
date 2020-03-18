package pt.ulisboa.tecnico.learnjava.sibs.operation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.learnjava.bank.domain.Bank;
import pt.ulisboa.tecnico.learnjava.bank.domain.Client;
import pt.ulisboa.tecnico.learnjava.bank.exceptions.AccountException;
import pt.ulisboa.tecnico.learnjava.bank.exceptions.BankException;
import pt.ulisboa.tecnico.learnjava.bank.exceptions.ClientException;
import pt.ulisboa.tecnico.learnjava.bank.services.Services;
import pt.ulisboa.tecnico.learnjava.sibs.domain.Cancelled;
import pt.ulisboa.tecnico.learnjava.sibs.domain.Completed;
import pt.ulisboa.tecnico.learnjava.sibs.domain.Deposited;
import pt.ulisboa.tecnico.learnjava.sibs.domain.Operation;
import pt.ulisboa.tecnico.learnjava.sibs.domain.Registered;
import pt.ulisboa.tecnico.learnjava.sibs.domain.Sibs;
import pt.ulisboa.tecnico.learnjava.sibs.domain.TransferOperation;
import pt.ulisboa.tecnico.learnjava.sibs.domain.Withdrawn;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.OperationException;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.SibsException;

public class TransferOperationStateMethodTests {

	private static final String FIRST_NAME = "Filipe";
	private static final String LAST_NAME = "Pinheiro";
	private static final String NIF = "123456789";
	private static final String PHONE_NUMBER = "913123123";
	private static final String ADDRESS = "Rua da Ribeira";
	private static final int VALUE = 100;

	private Sibs sibs;
	private Bank sourceBank;
	private Bank targetBank;
	private Client sourceClient;
	private Client targetClient;
	private Services services;

//	private static final String SOURCE_IBAN = "SourceIban";
//	private static final String TARGET_IBAN = "TargetIban";

	@Before
	public void setUp() throws ClientException, BankException, AccountException {
		this.services = new Services();
		this.sibs = new Sibs(100, services);
		this.sourceBank = new Bank("CGD");
		this.targetBank = new Bank("BPI");
		this.sourceClient = new Client(this.sourceBank, FIRST_NAME, LAST_NAME, NIF, PHONE_NUMBER, ADDRESS, 33);
		this.targetClient = new Client(this.targetBank, FIRST_NAME, LAST_NAME, NIF, PHONE_NUMBER, ADDRESS, 22);
	}

	@Test
	public void stateTest() throws OperationException, AccountException, SibsException, BankException, ClientException {
		String sourceIban = this.sourceBank.createAccount(Bank.AccountType.CHECKING, this.sourceClient, 1000, 0);
		String targetIban = this.targetBank.createAccount(Bank.AccountType.CHECKING, this.targetClient, 1000, 0);

		TransferOperation operation = new TransferOperation(sourceIban, targetIban, VALUE);

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
		assertEquals(sourceIban, operation.getSourceIban());
		assertEquals(targetIban, operation.getTargetIban());
	}

	@Test
	public void stateNoFeeTest()
			throws OperationException, AccountException, SibsException, BankException, ClientException {
		String sourceIban = this.sourceBank.createAccount(Bank.AccountType.CHECKING, this.sourceClient, 1000, 0);

		TransferOperation operation = new TransferOperation(sourceIban, sourceIban, VALUE);

		assertTrue(operation.getCurrentState() instanceof Registered);
		operation.process();
		assertTrue(operation.getCurrentState() instanceof Withdrawn);
		operation.process();
		assertTrue(operation.getCurrentState() instanceof Completed);

		assertEquals(Operation.OPERATION_TRANSFER, operation.getType());
		assertEquals(100, operation.getValue());
		assertEquals(sourceIban, operation.getSourceIban());
		assertEquals(sourceIban, operation.getTargetIban());
	}

	@Test
	public void cancelStateTest()
			throws OperationException, AccountException, SibsException, BankException, ClientException {
		String sourceIban = this.sourceBank.createAccount(Bank.AccountType.CHECKING, this.sourceClient, 1000, 0);
		String targetIban = this.targetBank.createAccount(Bank.AccountType.CHECKING, this.targetClient, 1000, 0);

		TransferOperation operation = new TransferOperation(sourceIban, targetIban, VALUE);

		assertTrue(operation.getCurrentState() instanceof Registered);
		operation.process();
		assertTrue(operation.getCurrentState() instanceof Withdrawn);
		operation.cancel();
		assertTrue(operation.getCurrentState() instanceof Cancelled);
		operation.process();
		assertTrue(operation.getCurrentState() instanceof Cancelled);

		assertEquals(Operation.OPERATION_TRANSFER, operation.getType());
		assertEquals(100, operation.getValue());
		assertEquals(sourceIban, operation.getSourceIban());
		assertEquals(targetIban, operation.getTargetIban());
	}

	@After
	public void tearDown() {
		this.sibs = null;
		Bank.clearBanks();
	}

}
