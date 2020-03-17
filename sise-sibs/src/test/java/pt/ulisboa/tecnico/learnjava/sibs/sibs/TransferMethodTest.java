package pt.ulisboa.tecnico.learnjava.sibs.sibs;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;

import pt.ulisboa.tecnico.learnjava.bank.exceptions.AccountException;
import pt.ulisboa.tecnico.learnjava.bank.exceptions.BankException;
import pt.ulisboa.tecnico.learnjava.bank.exceptions.ClientException;
import pt.ulisboa.tecnico.learnjava.bank.services.Services;
import pt.ulisboa.tecnico.learnjava.sibs.domain.Operation;
import pt.ulisboa.tecnico.learnjava.sibs.domain.Sibs;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.OperationException;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.SibsException;

public class TransferMethodTest {

	private static final int AMOUNT_1 = 100;
	private static final int AMOUNT_2 = 106;

	private static final String BANK_1 = "NBA";
	private static final String BANK_2 = "CGD";

	private static final String IBAN_1 = "CK1";
	private static final String IBAN_2 = "CK2";

	private static final int NOPS = 1;

	private Services mockServices;
	private Sibs sibs;

	@Test(expected = SibsException.class)
	public void noSuccessSourceAccount()
			throws BankException, AccountException, SibsException, OperationException, ClientException {

		this.mockServices = mock(Services.class);
		this.sibs = new Sibs(NOPS, mockServices);

		when(this.mockServices.AccountExists(IBAN_1)).thenReturn(false);
		when(this.mockServices.AccountExists(IBAN_2)).thenReturn(true);

		this.sibs.transfer(IBAN_1, IBAN_2, AMOUNT_1);
	}

	@Test(expected = SibsException.class)
	public void noSuccessTargetAccount()
			throws BankException, AccountException, SibsException, OperationException, ClientException {

		this.mockServices = mock(Services.class);
		this.sibs = new Sibs(NOPS, mockServices);

		when(this.mockServices.AccountExists(IBAN_1)).thenReturn(true);
		when(this.mockServices.AccountExists(IBAN_2)).thenReturn(false);

		this.sibs.transfer(IBAN_1, IBAN_2, AMOUNT_1);
	}

	@Test
	public void SuccessNoComission()
			throws BankException, AccountException, SibsException, OperationException, ClientException {

		this.mockServices = mock(Services.class);
		this.sibs = new Sibs(NOPS, mockServices);

		when(this.mockServices.AccountExists(IBAN_1)).thenReturn(true);
		when(this.mockServices.AccountExists(IBAN_2)).thenReturn(true);

		// same bank
		when(this.mockServices.getBankCodeByIban(IBAN_1)).thenReturn(BANK_1);
		when(this.mockServices.getBankCodeByIban(IBAN_2)).thenReturn(BANK_1);

		this.sibs.transfer(IBAN_1, IBAN_2, AMOUNT_1);

//		verify(this.mockServices).withdraw(IBAN_1, AMOUNT_1);
//		verify(this.mockServices).deposit(IBAN_2, AMOUNT_1);

		assertEquals(1, this.sibs.getNumberOfOperations());
		assertEquals(AMOUNT_1, this.sibs.getTotalValueOfOperations());
		assertEquals(AMOUNT_1, this.sibs.getTotalValueOfOperationsForType(Operation.OPERATION_TRANSFER));
		assertEquals(0, this.sibs.getTotalValueOfOperationsForType(Operation.OPERATION_PAYMENT));

	}

	@Test
	public void SuccessWithComission()
			throws BankException, AccountException, SibsException, OperationException, ClientException {

		this.mockServices = mock(Services.class);
		this.sibs = new Sibs(NOPS, mockServices);

		when(this.mockServices.AccountExists(IBAN_1)).thenReturn(true);
		when(this.mockServices.AccountExists(IBAN_2)).thenReturn(true);

		// different banks
		when(this.mockServices.getBankCodeByIban(IBAN_1)).thenReturn(BANK_1);
		when(this.mockServices.getBankCodeByIban(IBAN_2)).thenReturn(BANK_2);

		this.sibs.transfer(IBAN_1, IBAN_2, AMOUNT_1);

//		verify(this.mockServices).withdraw(IBAN_1, AMOUNT_2);
//		verify(this.mockServices).deposit(IBAN_2, AMOUNT_1);

		assertEquals(1, this.sibs.getNumberOfOperations());
		assertEquals(AMOUNT_1, this.sibs.getTotalValueOfOperations());
		assertEquals(AMOUNT_1, this.sibs.getTotalValueOfOperationsForType(Operation.OPERATION_TRANSFER));
		assertEquals(0, this.sibs.getTotalValueOfOperationsForType(Operation.OPERATION_PAYMENT));

	}

	@Test
	public void operationFailTest()
			throws BankException, AccountException, SibsException, OperationException, ClientException {

		this.mockServices = mock(Services.class);
		this.sibs = new Sibs(NOPS, mockServices);

		when(this.mockServices.AccountExists(IBAN_1)).thenReturn(true);
		when(this.mockServices.AccountExists(IBAN_2)).thenReturn(true);

		when(this.mockServices.getBankCodeByIban(IBAN_1)).thenReturn(BANK_1);
		when(this.mockServices.getBankCodeByIban(IBAN_2)).thenReturn(BANK_1);

		doThrow(new AccountException()).when(this.mockServices).deposit(IBAN_2, AMOUNT_1);

		try {
			this.sibs.transfer(IBAN_1, IBAN_2, AMOUNT_1);
//			fail();
		} catch (SibsException e) {
			verify(this.mockServices, times(1)).withdraw(IBAN_1, AMOUNT_1);
			verify(this.mockServices, times(1)).deposit(IBAN_2, AMOUNT_1);

			assertEquals(0, this.sibs.getNumberOfOperations());
			assertEquals(0, this.sibs.getTotalValueOfOperations());
			assertEquals(0, this.sibs.getTotalValueOfOperationsForType(Operation.OPERATION_TRANSFER));
			assertEquals(0, this.sibs.getTotalValueOfOperationsForType(Operation.OPERATION_PAYMENT));
		}

	}

}
