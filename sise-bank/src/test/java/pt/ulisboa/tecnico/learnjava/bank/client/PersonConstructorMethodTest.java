package pt.ulisboa.tecnico.learnjava.bank.client;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.learnjava.bank.domain.Bank;
import pt.ulisboa.tecnico.learnjava.bank.domain.Person;
import pt.ulisboa.tecnico.learnjava.bank.exceptions.BankException;
import pt.ulisboa.tecnico.learnjava.bank.exceptions.ClientException;

public class PersonConstructorMethodTest {
	private static final String ADDRESS = "Ave.";
	private static final String PHONE_NUMBER = "987654321";
	private static final String NIF = "123456789";
	private static final String LAST_NAME = "Silva";
	private static final String FIRST_NAME = "António";
	private static final int AGE = 33;

	private Bank bank;

	@Before
	public void setUp() throws BankException {
		this.bank = new Bank("CGD");
	}

	@Test
	public void success() throws ClientException {
		Person person = new Person(FIRST_NAME, LAST_NAME, PHONE_NUMBER, ADDRESS, AGE);

		assertEquals(FIRST_NAME, person.getFirstName());
		assertEquals(LAST_NAME, person.getLastName());
		assertEquals(PHONE_NUMBER, person.getPhoneNumber());
		assertEquals(ADDRESS, person.getAddress());
		assertEquals(AGE, person.getAge());

	}

	@Test(expected = ClientException.class)
	public void negativeAge() throws ClientException {
		new Person(FIRST_NAME, LAST_NAME, PHONE_NUMBER, ADDRESS, -1);
	}

	@Test(expected = ClientException.class)
	public void no9DigitsPhoneNumber() throws ClientException {
		new Person(FIRST_NAME, LAST_NAME, "A87654321", ADDRESS, AGE);
	}

	@After
	public void tearDown() {
		Bank.clearBanks();
	}
}
