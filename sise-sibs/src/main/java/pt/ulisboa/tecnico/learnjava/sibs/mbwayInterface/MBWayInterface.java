package pt.ulisboa.tecnico.learnjava.sibs.mbwayInterface;

import java.util.Scanner;

import pt.ulisboa.tecnico.learnjava.bank.domain.Bank;
import pt.ulisboa.tecnico.learnjava.bank.domain.Bank.AccountType;
import pt.ulisboa.tecnico.learnjava.bank.domain.Client;
import pt.ulisboa.tecnico.learnjava.bank.domain.Person;
import pt.ulisboa.tecnico.learnjava.bank.exceptions.AccountException;
import pt.ulisboa.tecnico.learnjava.bank.exceptions.BankException;
import pt.ulisboa.tecnico.learnjava.bank.exceptions.ClientException;
import pt.ulisboa.tecnico.learnjava.bank.services.Services;
import pt.ulisboa.tecnico.learnjava.sibs.domain.Sibs;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.MBAccountException;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.OperationException;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.SibsException;

public class MBWayInterface {
	private static Client[] client;
	private static Person[] person;
	private static final int NR_CLIENTS = 5;

	private static void createPersons() throws ClientException {
		person = new Person[NR_CLIENTS];
		person[0] = new Person("Filipe", "Pinheiro", "914801584", "First Street", 24);
		person[1] = new Person("Joseph", "Manuel", "987654322", "Second Street", 34);
		person[2] = new Person("Joseph", "Manuel", "987654323", "Second Street", 34);
		person[3] = new Person("Joseph", "Manuel", "987654324", "Second Street", 34);
		person[4] = new Person("Joseph", "Manuel", "987654325", "Second Street", 34);
//		person[5] = new Person("Joseph", "Manuel", "987654326", "Second Street", 34);
//		person[6] = new Person("Joseph", "Manuel", "987654327", "Second Street", 34);
//		person[7] = new Person("Joseph", "Manuel", "987654328", "Second Street", 34);
//		person[8] = new Person("Joseph", "Manuel", "987654329", "Second Street", 34);
//		person[9] = new Person("Joseph", "Manuel", "987654333", "Second Street", 34);
	}

	private static void createClients(Bank bank) throws ClientException {
		client = new Client[NR_CLIENTS];
		client[0] = new Client(bank, person[0], "242130704");
		client[1] = new Client(bank, person[1], "123456789");
		client[2] = new Client(bank, person[2], "123456782");
		client[3] = new Client(bank, person[3], "123456783");
		client[4] = new Client(bank, person[4], "123456784");
//		client[5] = new Client(bank, person[5], "123456785");
//		client[6] = new Client(bank, person[6], "123456786");
//		client[7] = new Client(bank, person[7], "123456787");
//		client[8] = new Client(bank, person[8], "123456788");
//		client[9] = new Client(bank, person[9], "123556789");
	}

	private static void createAccounts(Bank bank) throws BankException, AccountException, ClientException {
		bank.createAccount(AccountType.CHECKING, client[0], 1000, 0);
		bank.createAccount(AccountType.CHECKING, client[1], 1000, 0);
		bank.createAccount(AccountType.CHECKING, client[2], 1000, 0);
		bank.createAccount(AccountType.CHECKING, client[3], 1000, 0);
		bank.createAccount(AccountType.CHECKING, client[4], 1000, 0);
//		bank.createAccount(AccountType.CHECKING, client[5], 1000, 0);
//		bank.createAccount(AccountType.CHECKING, client[6], 1000, 0);
//		bank.createAccount(AccountType.CHECKING, client[7], 1000, 0);
//		bank.createAccount(AccountType.CHECKING, client[8], 1000, 0);
//		bank.createAccount(AccountType.CHECKING, client[9], 1000, 0);
	}

	public static void main(String[] args) throws SibsException, AccountException, OperationException, BankException,
			ClientException, MBAccountException {
		Services services = new Services();
		Sibs sibs = new Sibs(100, services);
		Bank cgd = new Bank("CGD");

		createPersons();
		createClients(cgd);
		createAccounts(cgd);

		Scanner s = new Scanner(System.in);

		MBWayInterfaceModel mbmodel = new MBWayInterfaceModel();
		MBWayInterfaceView mbview = new MBWayInterfaceView();

		MBWayInterfaceController mbcontroller = new MBWayInterfaceController(s, mbmodel, mbview, services, sibs);

		String input;

		while (mbcontroller.isRunning()) {

			input = s.nextLine();

			mbcontroller.setUserInput(input);

			mbcontroller.updateView();

		}

		s.close();

	}

}
