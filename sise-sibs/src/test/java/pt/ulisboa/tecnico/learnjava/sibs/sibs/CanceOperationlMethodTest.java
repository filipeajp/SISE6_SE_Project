package pt.ulisboa.tecnico.learnjava.sibs.sibs;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.learnjava.bank.domain.Bank;
import pt.ulisboa.tecnico.learnjava.bank.domain.Client;
import pt.ulisboa.tecnico.learnjava.bank.domain.Person;
import pt.ulisboa.tecnico.learnjava.bank.exceptions.AccountException;
import pt.ulisboa.tecnico.learnjava.bank.exceptions.BankException;
import pt.ulisboa.tecnico.learnjava.bank.exceptions.ClientException;
import pt.ulisboa.tecnico.learnjava.bank.services.Services;
import pt.ulisboa.tecnico.learnjava.sibs.domain.Cancelled;
import pt.ulisboa.tecnico.learnjava.sibs.domain.Completed;
import pt.ulisboa.tecnico.learnjava.sibs.domain.Operation;
import pt.ulisboa.tecnico.learnjava.sibs.domain.Registered;
import pt.ulisboa.tecnico.learnjava.sibs.domain.Sibs;
import pt.ulisboa.tecnico.learnjava.sibs.domain.TransferOperation;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.OperationException;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.SibsException;

public class CanceOperationlMethodTest {
	private static final String FIRST_NAME = "Filipe";
	private static final String LAST_NAME = "Pinheiro";
	private static final String NIF = "123456789";
	private static final String PHONE_NUMBER = "913123123";
	private static final String ADDRESS = "Rua da Ribeira";
	private static final int VALUE = 100;

	private static final int AMOUNT_1 = 100;
	private static final int AMOUNT_2 = 106;

	private static final String BANK_1 = "NBA";
	private static final String BANK_2 = "CGD";

	private static final String IBAN_1 = "CK1";
	private static final String IBAN_2 = "CK2";

	private Sibs sibs;
	private Bank sourceBank;
	private Bank targetBank;
	private Client sourceClient;
	private Client targetClient;
	private Services services;
	private Person sourcePerson;
	private Person targetPerson;

	@Before
	public void setUp() throws ClientException, BankException, AccountException {
		this.services = new Services();
		this.sibs = new Sibs(100, services);
		this.sourceBank = new Bank("CGD");
		this.targetBank = new Bank("BPI");
		this.sourcePerson = new Person(FIRST_NAME, LAST_NAME, PHONE_NUMBER, ADDRESS, 33);
		this.targetPerson = new Person(FIRST_NAME, LAST_NAME, PHONE_NUMBER, ADDRESS, 33);
		this.sourceClient = new Client(this.sourceBank, this.sourcePerson, NIF);
		this.targetClient = new Client(this.targetBank, this.targetPerson, NIF);
	}

	@Test
	public void failCancel()
			throws OperationException, SibsException, BankException, AccountException, ClientException {
		String sourceIban = this.sourceBank.createAccount(Bank.AccountType.CHECKING, this.sourceClient, 1000, 0);
		String targetIban = this.targetBank.createAccount(Bank.AccountType.CHECKING, this.targetClient, 1000, 0);

		this.sibs.transfer(sourceIban, targetIban, VALUE);
		TransferOperation operation1 = (TransferOperation) this.sibs.getOperation(0);
		assertTrue(operation1.getCurrentState() instanceof Registered);

		try {
			this.sibs.cancelOperation(1);
			fail();
		} catch (SibsException e) {
			assertEquals(1000, this.services.getAccountByIban(sourceIban).getBalance());
			assertEquals(1000, this.services.getAccountByIban(targetIban).getBalance());
			assertEquals(100, this.sibs.getTotalValueOfOperations());
			assertEquals(100, this.sibs.getTotalValueOfOperationsForType(Operation.OPERATION_TRANSFER));
			assertEquals(0, this.sibs.getTotalValueOfOperationsForType(Operation.OPERATION_PAYMENT));
			assertEquals(1, this.sibs.getNumberOfOperations());
			assertTrue(operation1.getCurrentState() instanceof Registered);
		}

	}

	@Test
	public void successWithCancel()
			throws OperationException, SibsException, BankException, AccountException, ClientException {
		String sourceIban = this.sourceBank.createAccount(Bank.AccountType.CHECKING, this.sourceClient, 1000, 0);
		String targetIban = this.targetBank.createAccount(Bank.AccountType.CHECKING, this.targetClient, 1000, 0);

		this.sibs.transfer(sourceIban, targetIban, VALUE);
		TransferOperation operation1 = (TransferOperation) this.sibs.getOperation(0);
		assertTrue(operation1.getCurrentState() instanceof Registered);

		this.sibs.transfer(sourceIban, targetIban, VALUE);
		TransferOperation operation2 = (TransferOperation) this.sibs.getOperation(1);
		assertTrue(operation2.getCurrentState() instanceof Registered);
		this.sibs.cancelOperation(1);

		this.sibs.transfer(sourceIban, targetIban, VALUE);
		TransferOperation operation3 = (TransferOperation) this.sibs.getOperation(2);
		assertTrue(operation3.getCurrentState() instanceof Registered);

		this.sibs.processOperations();

		assertEquals(788, this.services.getAccountByIban(sourceIban).getBalance());
		assertEquals(1200, this.services.getAccountByIban(targetIban).getBalance());
		assertEquals(300, this.sibs.getTotalValueOfOperations());
		assertEquals(300, this.sibs.getTotalValueOfOperationsForType(Operation.OPERATION_TRANSFER));
		assertEquals(0, this.sibs.getTotalValueOfOperationsForType(Operation.OPERATION_PAYMENT));
		assertEquals(3, this.sibs.getNumberOfOperations());
		assertTrue(operation1.getCurrentState() instanceof Completed);
		assertTrue(operation2.getCurrentState() instanceof Cancelled);
		assertTrue(operation3.getCurrentState() instanceof Completed);

	}

	@After
	public void tearDown() {
		this.sibs = null;
		Bank.clearBanks();
	}

}
