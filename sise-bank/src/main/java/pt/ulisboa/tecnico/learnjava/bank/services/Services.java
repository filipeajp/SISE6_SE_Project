package pt.ulisboa.tecnico.learnjava.bank.services;

import pt.ulisboa.tecnico.learnjava.bank.domain.Account;
import pt.ulisboa.tecnico.learnjava.bank.domain.Bank;
import pt.ulisboa.tecnico.learnjava.bank.exceptions.AccountException;

public class Services {
	public void deposit(String iban, int amount) throws AccountException {
		Account account = getAccountByIban(iban);

		account.deposit(amount);
	}

	public void withdraw(String iban, int amount) throws AccountException {
		Account account = getAccountByIban(iban);

		account.withdraw(amount);
	}

	public Account getAccountByIban(String iban) {
		String code = iban.substring(0, 3);
		String accountId = iban.substring(3);

		Bank bank = Bank.getBankByCode(code);
		Account account = bank.getAccountByAccountId(accountId);

		return account;
	}

	public boolean AccountExists(String iban) {
		if (this.getAccountByIban(iban) == null) {
			return false;
		}
		return true;
	}

	public boolean checkSameBanks(String iban1, String iban2) {
		return getBankCodeByIban(iban1).equals(getBankCodeByIban(iban2));
	}

	public String getBankCodeByIban(String iban) {
		return iban.substring(0, 3);
	}

}
