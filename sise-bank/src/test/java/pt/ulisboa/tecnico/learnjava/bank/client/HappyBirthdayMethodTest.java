package pt.ulisboa.tecnico.learnjava.bank.client;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.learnjava.bank.domain.Bank;
import pt.ulisboa.tecnico.learnjava.bank.domain.CheckingAccount;
import pt.ulisboa.tecnico.learnjava.bank.domain.Client;
import pt.ulisboa.tecnico.learnjava.bank.domain.Person;
import pt.ulisboa.tecnico.learnjava.bank.domain.YoungAccount;
import pt.ulisboa.tecnico.learnjava.bank.exceptions.AccountException;
import pt.ulisboa.tecnico.learnjava.bank.exceptions.BankException;
import pt.ulisboa.tecnico.learnjava.bank.exceptions.ClientException;
import pt.ulisboa.tecnico.learnjava.bank.services.Services;

public class HappyBirthdayMethodTest {
	private Bank bank;
	private Client youngClient;
	private YoungAccount young;
	private Services services;

	@Before
	public void setUp() throws AccountException, ClientException, BankException {
		this.services = new Services();
		this.bank = new Bank("CGD");

		Person person = new Person("José", "Manuel", "987654321", "Street", 16);
		this.youngClient = new Client(this.bank, person, "123456780");

		this.young = (YoungAccount) this.services
				.getAccountByIban(this.bank.createAccount(Bank.AccountType.YOUNG, this.youngClient, 100, 0));
		this.bank.createAccount(Bank.AccountType.YOUNG, this.youngClient, 100, 0);
		this.bank.createAccount(Bank.AccountType.YOUNG, this.youngClient, 100, 0);
		this.bank.createAccount(Bank.AccountType.YOUNG, this.youngClient, 100, 0);
	}

	@Test
	public void successNoUpgrade() throws BankException, AccountException, ClientException {
		this.youngClient.happyBirthDay();
		Person person = this.youngClient.getPerson();

		assertEquals(17, person.getAge());
		assertTrue(this.youngClient.getAccounts().allMatch(a -> a instanceof YoungAccount));
	}

	@Test
	public void successUpGrade() throws BankException, AccountException, ClientException {
		this.youngClient.happyBirthDay();
		this.youngClient.happyBirthDay();

		Person person = this.youngClient.getPerson();

		assertEquals(18, person.getAge());
		assertTrue(this.youngClient.getAccounts().allMatch(a -> a instanceof CheckingAccount));
	}

	@After
	public void tearDown() {
		Bank.clearBanks();
	}

}
