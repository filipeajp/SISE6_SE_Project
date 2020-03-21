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

	public static void main(String[] args) throws SibsException, AccountException, OperationException, BankException,
			ClientException, MBAccountException {
		Services services = new Services();
		Sibs sibs = new Sibs(100, services);
		Bank cgd = new Bank("CGD");

		Client clientOne = new Client(cgd, "Filipe", "Pinheiro", "242130704", "914801584", "First Street", 24);
		Client clientTwo = new Client(cgd, "Joseph", "Manuel", "123456789", "987654322", "Second Street", 34);
		Client clientThree = new Client(cgd, "Joseph", "Manuel", "123456782", "987654323", "Second Street", 34);
		Client clientFour = new Client(cgd, "Joseph", "Manuel", "123456783", "987654324", "Second Street", 34);
		Client clientFive = new Client(cgd, "Joseph", "Manuel", "123456784", "987654325", "Second Street", 34);
		Client clientSix = new Client(cgd, "Joseph", "Manuel", "123456785", "987654326", "Second Street", 34);
		Client clientSeven = new Client(cgd, "Joseph", "Manuel", "123456786", "987654327", "Second Street", 34);
		Client clientEight = new Client(cgd, "Joseph", "Manuel", "123456787", "987654328", "Second Street", 34);
		Client clientNine = new Client(cgd, "Joseph", "Manuel", "123456788", "987654329", "Second Street", 34);
		Client clientTen = new Client(cgd, "Joseph", "Manuel", "123556789", "987654333", "Second Street", 34);

		String iban1 = cgd.createAccount(AccountType.CHECKING, clientOne, 1000, 0);
		String iban2 = cgd.createAccount(AccountType.CHECKING, clientTwo, 1000, 0);
		String iban3 = cgd.createAccount(AccountType.CHECKING, clientThree, 1000, 0);
		String iban4 = cgd.createAccount(AccountType.CHECKING, clientFour, 1000, 0);
		String iban5 = cgd.createAccount(AccountType.CHECKING, clientFive, 1000, 0);
		String iban6 = cgd.createAccount(AccountType.CHECKING, clientSix, 1000, 0);
		String iban7 = cgd.createAccount(AccountType.CHECKING, clientSeven, 1000, 0);
		String iban8 = cgd.createAccount(AccountType.CHECKING, clientEight, 1000, 0);
		String iban9 = cgd.createAccount(AccountType.CHECKING, clientNine, 1000, 0);
		String iban10 = cgd.createAccount(AccountType.CHECKING, clientTen, 1000, 0);

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
