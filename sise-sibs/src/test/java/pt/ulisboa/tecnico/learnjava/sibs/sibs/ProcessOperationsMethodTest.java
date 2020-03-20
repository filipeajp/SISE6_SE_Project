package pt.ulisboa.tecnico.learnjava.sibs.sibs;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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
import pt.ulisboa.tecnico.learnjava.sibs.domain.Error;
import pt.ulisboa.tecnico.learnjava.sibs.domain.Operation;
import pt.ulisboa.tecnico.learnjava.sibs.domain.Retry;
import pt.ulisboa.tecnico.learnjava.sibs.domain.Sibs;
import pt.ulisboa.tecnico.learnjava.sibs.domain.TransferOperation;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.OperationException;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.SibsException;

public class ProcessOperationsMethodTest {
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
	private Services mockServices;
	private TransferOperation mockOperation;

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
	public void successWithProcessing()
			throws OperationException, SibsException, AccountException, BankException, ClientException {
		String sourceIban = this.sourceBank.createAccount(Bank.AccountType.CHECKING, this.sourceClient, 1000, 0);
		String targetIban = this.targetBank.createAccount(Bank.AccountType.CHECKING, this.targetClient, 1000, 0);

		this.sibs.transfer(sourceIban, targetIban, VALUE);
		this.sibs.transfer(sourceIban, targetIban, VALUE);

		TransferOperation operation1 = (TransferOperation) this.sibs.getOperation(0);
		TransferOperation operation2 = (TransferOperation) this.sibs.getOperation(1);

		this.sibs.processOperations();

		assertEquals(788, this.services.getAccountByIban(sourceIban).getBalance());
		assertEquals(1200, this.services.getAccountByIban(targetIban).getBalance());
		assertEquals(200, this.sibs.getTotalValueOfOperations());
		assertEquals(200, this.sibs.getTotalValueOfOperationsForType(Operation.OPERATION_TRANSFER));
		assertEquals(0, this.sibs.getTotalValueOfOperationsForType(Operation.OPERATION_PAYMENT));
		assertEquals(2, this.sibs.getNumberOfOperations());
		assertTrue(operation1.getCurrentState() instanceof Completed);
		assertTrue(operation2.getCurrentState() instanceof Completed);
	}

	@Test
	public void successWithCancel()
			throws OperationException, SibsException, BankException, AccountException, ClientException {
		String sourceIban = this.sourceBank.createAccount(Bank.AccountType.CHECKING, this.sourceClient, 1000, 0);
		String targetIban = this.targetBank.createAccount(Bank.AccountType.CHECKING, this.targetClient, 1000, 0);

		this.sibs.transfer(sourceIban, targetIban, VALUE);
		TransferOperation operation1 = (TransferOperation) this.sibs.getOperation(0);
		assertTrue(operation1.getCurrentState() instanceof Completed);

		this.sibs.transfer(sourceIban, targetIban, VALUE);
		TransferOperation operation2 = (TransferOperation) this.sibs.getOperation(1);
		assertTrue(operation2.getCurrentState() instanceof Completed);

		this.sibs.transfer(sourceIban, targetIban, VALUE);
		TransferOperation operation3 = (TransferOperation) this.sibs.getOperation(2);
		assertTrue(operation3.getCurrentState() instanceof Completed);

		this.sibs.cancelOperation(1);
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

	@Test
	public void retryOperationTest()
			throws BankException, AccountException, SibsException, OperationException, ClientException {

		this.mockServices = mock(Services.class);

		this.sibs = new Sibs(100, this.mockServices);

		when(this.mockServices.AccountExists(IBAN_1)).thenReturn(true);
		when(this.mockServices.AccountExists(IBAN_2)).thenReturn(true);

		when(this.mockServices.getBankCodeByIban(IBAN_1)).thenReturn(BANK_1);
		when(this.mockServices.getBankCodeByIban(IBAN_2)).thenReturn(BANK_1);

		doThrow(new AccountException()).when(this.mockServices).deposit(IBAN_2, AMOUNT_1);

		try {
			this.sibs.transfer(IBAN_1, IBAN_2, AMOUNT_1);
		} catch (SibsException e1) {
			assertEquals(((TransferOperation) this.sibs.getOperation(0)).getCurrentState(), Retry.getInstance());
		}
	}

	@Test
	public void ThreeRetriesOperationTest()
			throws BankException, AccountException, SibsException, OperationException, ClientException {

		this.mockServices = mock(Services.class);

		this.sibs = new Sibs(100, this.mockServices);

		when(this.mockServices.AccountExists(IBAN_1)).thenReturn(true);
		when(this.mockServices.AccountExists(IBAN_2)).thenReturn(true);

		when(this.mockServices.getBankCodeByIban(IBAN_1)).thenReturn(BANK_1);
		when(this.mockServices.getBankCodeByIban(IBAN_2)).thenReturn(BANK_1);

		doThrow(new AccountException()).when(this.mockServices).deposit(IBAN_2, AMOUNT_1);

		try {
			this.sibs.transfer(IBAN_1, IBAN_2, AMOUNT_1);
		} catch (SibsException e1) {
			doThrow(new AccountException()).when(this.mockServices).deposit(IBAN_2, AMOUNT_1);
			try {
				this.sibs.processOperations();
			} catch (SibsException e2) {
				doThrow(new AccountException()).when(this.mockServices).deposit(IBAN_2, AMOUNT_1);
				try {
					this.sibs.processOperations();
				} catch (SibsException e3) {
					doThrow(new AccountException()).when(this.mockServices).deposit(IBAN_2, AMOUNT_1);
					try {
						this.sibs.processOperations();

					} catch (Exception e4) {
						assertEquals(((TransferOperation) this.sibs.getOperation(0)).getCurrentState(),
								Error.getInstance());
					}
				}
			}
		}
	}

	@After
	public void tearDown() {
		this.sibs = null;
		Bank.clearBanks();
	}
}
