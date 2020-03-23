package mbwayInterface;

import java.util.Scanner;

import pt.ulisboa.tecnico.learnjava.bank.domain.Bank;
import pt.ulisboa.tecnico.learnjava.bank.domain.Bank.AccountType;
import pt.ulisboa.tecnico.learnjava.bank.domain.Client;
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

	private static void createClients(Bank bank) throws ClientException {
		client = new Client[10];
		client[0] = new Client(bank, "Filipe", "Pinheiro", "242130704", "914801584", "First Street", 24);
		client[1] = new Client(bank, "Joseph", "Manuel", "123456789", "987654322", "Second Street", 34);
		client[2] = new Client(bank, "Joseph", "Manuel", "123456782", "987654323", "Second Street", 34);
		client[3] = new Client(bank, "Joseph", "Manuel", "123456783", "987654324", "Second Street", 34);
		client[4] = new Client(bank, "Joseph", "Manuel", "123456784", "987654325", "Second Street", 34);
		client[5] = new Client(bank, "Joseph", "Manuel", "123456785", "987654326", "Second Street", 34);
		client[6] = new Client(bank, "Joseph", "Manuel", "123456786", "987654327", "Second Street", 34);
		client[7] = new Client(bank, "Joseph", "Manuel", "123456787", "987654328", "Second Street", 34);
		client[8] = new Client(bank, "Joseph", "Manuel", "123456788", "987654329", "Second Street", 34);
		client[9] = new Client(bank, "Joseph", "Manuel", "123556789", "987654333", "Second Street", 34);
	}

	private static void createAccounts(Bank bank) throws BankException, AccountException, ClientException {
		bank.createAccount(AccountType.CHECKING, client[0], 1000, 0);
		bank.createAccount(AccountType.CHECKING, client[1], 1000, 0);
		bank.createAccount(AccountType.CHECKING, client[2], 1000, 0);
		bank.createAccount(AccountType.CHECKING, client[3], 1000, 0);
		bank.createAccount(AccountType.CHECKING, client[4], 1000, 0);
		bank.createAccount(AccountType.CHECKING, client[5], 1000, 0);
		bank.createAccount(AccountType.CHECKING, client[6], 1000, 0);
		bank.createAccount(AccountType.CHECKING, client[7], 1000, 0);
		bank.createAccount(AccountType.CHECKING, client[8], 1000, 0);
		bank.createAccount(AccountType.CHECKING, client[9], 1000, 0);
	}

	public static void main(String[] args) throws SibsException, AccountException, OperationException, BankException,
			ClientException, MBAccountException {
		Services services = new Services();
		Sibs sibs = new Sibs(100, services);
		Bank cgd = new Bank("CGD");

		createClients(cgd);
		createAccounts(cgd);

		Scanner s = new Scanner(System.in);

		MBWayInterfaceModel mbmodel = new MBWayInterfaceModel();
		MBWayInterfaceView mbview = new MBWayInterfaceView();

		MBWayInterfaceController mbcontroller = new MBWayInterfaceController(mbmodel, mbview, services, sibs);

		String input;

		while (mbcontroller.isRunning()) {

			input = s.nextLine();

			mbcontroller.setUserInput(input);

			mbcontroller.updateView();

		}

		s.close();

	}

}
