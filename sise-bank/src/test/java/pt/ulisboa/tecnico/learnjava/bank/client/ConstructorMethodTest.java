package pt.ulisboa.tecnico.learnjava.bank.client;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.learnjava.bank.domain.Bank;
import pt.ulisboa.tecnico.learnjava.bank.domain.Client;
import pt.ulisboa.tecnico.learnjava.bank.domain.Person;
import pt.ulisboa.tecnico.learnjava.bank.exceptions.BankException;
import pt.ulisboa.tecnico.learnjava.bank.exceptions.ClientException;

public class ConstructorMethodTest {
	private static final String ADDRESS = "Ave.";
	private static final String PHONE_NUMBER = "987654321";
	private static final String NIF = "123456789";
	private static final String LAST_NAME = "Silva";
	private static final String FIRST_NAME = "António";
	private static final int AGE = 33;

	private Bank bank;
	private Person person;

	@Before
	public void setUp() throws BankException {
		this.bank = new Bank("CGD");
	}

	@Test
	public void success() throws ClientException {
		this.person = new Person(FIRST_NAME, LAST_NAME, PHONE_NUMBER, ADDRESS, AGE);
		Client client = new Client(this.bank, person, NIF);

		assertEquals(this.bank, client.getBank());
		assertNotNull(client.getPerson());
		assertEquals(NIF, client.getNif());
		assertTrue(this.bank.isClientOfBank(client));
	}

	@Test(expected = ClientException.class)
	public void no9DigitsNif() throws ClientException {
		new Client(this.bank, this.person, "12345678A");
	}

	public void twoClientsSameNif() throws ClientException {
		new Client(this.bank, this.person, NIF);
		try {
			new Client(this.bank, this.person, NIF);
			fail();
		} catch (ClientException e) {
			assertEquals(1, this.bank.getTotalNumberOfClients());
		}
	}

	@After
	public void tearDown() {
		Bank.clearBanks();
	}

}
