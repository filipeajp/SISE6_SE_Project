package mbwayInterface;

import pt.ulisboa.tecnico.learnjava.bank.exceptions.AccountException;
import pt.ulisboa.tecnico.learnjava.bank.services.Services;
import pt.ulisboa.tecnico.learnjava.sibs.domain.Sibs;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.MBAccountException;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.OperationException;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.SibsException;

public class MBWayInterfaceController {

	private MBWayInterfaceModel model;
	private MBWayInterfaceView view;

	private Services services;
	private Sibs sibs;

	private boolean isRunning;

	private int nrOperations = 100;

	private String[] friends;
	private int totalBill;

	public MBWayInterfaceController(MBWayInterfaceModel model, MBWayInterfaceView view, Services services, Sibs sibs) {
		this.model = model;
		this.view = view;

		this.isRunning = true;
		this.services = services;
		this.sibs = sibs;

	}

	public Boolean isRunning() {
		return this.isRunning;
	}

	public void stopRunning() {
		this.isRunning = false;
	}

	public String getUserInput() {
		return model.getUserInput();
	}

	public void setUserInput(String userInput) {
		model.setUserInput(userInput);
	}

	public void updateView() throws SibsException, AccountException, OperationException, MBAccountException {

		String[] userArgs = this.processInput(this.getUserInput());

		String command = userArgs[0];

		switch (command) {
		case "exit":
			view.printExitMessage();
			this.stopRunning();
			break;

		case "associate-mbway":
			associatembway(userArgs);
			break;

		case "confirm-mbway":
			confirmmbway(userArgs);
			break;

		case "mbway-transfer":
			mbwayTransfer(userArgs);
			break;

		case "mbway-split-bill":

			int nrFriends = Integer.parseInt(userArgs[1]);
			int totalAmount = Integer.parseInt(userArgs[2]);
			int totalAccum = 0;

			friends = new String[nrFriends];
			int counter = 0;

			while (userArgs[0].equals("friend")) {

				if (counter++ > nrFriends) {
					view.tooManyFriends();
				} else {
					String phone = userArgs[1];
					int amount = Integer.parseInt(userArgs[2]);
					if (model.getMBAccount(phone).confirmationState()) {
						totalAccum += amount;
						friends[counter] = phone;
						counter++;
					} else {
						view.friendNotRegistered(phone);
					}
				}
			}

			if (totalAmount != totalAccum) {
				view.billWrong();
			} else if (counter < nrFriends) {
				view.missingFriends();
			} else {

				int amountToPay = totalAmount / (nrFriends + 1);
				MBWayAccount friendAccount;
				MBWayAccount userAccount = model.getMBAccount(model.getPhoneNumber());
				int countSuccess = 0;

				for (String friendPhone : friends) {
					friendAccount = model.getMBAccount(friendPhone);

					if (this.services.getAccountByIban(friendAccount.getIBAN()).getBalance() < amountToPay) {
						view.friendNotEnoughMoney();
					} else {
						this.sibs.transfer(friendAccount.getIBAN(), userAccount.getIBAN(), amountToPay);
						countSuccess++;
					}
				}
				// ver isto
				if (nrFriends == countSuccess) {
					view.successfullBillTransfer();
				}
			}

			break;
		}

	}

	private void confirmmbway(String[] userArgs) {
		String code = userArgs[1];
		boolean correctCode = model.confirmCode(code);

		if (correctCode) {
			view.correctCode();
		} else {
			view.wrongCode();
		}
	}

	private void associatembway(String[] userArgs) {
		String iban = userArgs[1];
		String phone = userArgs[2];

		view.printCode(model.generateCode(iban, phone));
	}

	private void mbwayTransfer(String[] userArgs) throws SibsException, AccountException, OperationException {
		String sourcePhone = userArgs[1];
		String targetPhone = userArgs[2];
		int amount = Integer.parseInt(userArgs[3]);

		MBWayAccount sourceAccount = null;
		MBWayAccount targetAccount = null;
		try {
			sourceAccount = model.getMBAccount(sourcePhone);
			targetAccount = model.getMBAccount(targetPhone);
		} catch (MBAccountException e) {
			view.wrongPhoneNumber();
		}

		if (!targetAccount.confirmationState()) {
			view.wrongPhoneNumber();
		} else if (this.services.getAccountByIban(sourceAccount.getIBAN()).getBalance() < amount) {
			view.notEnoughMoney();
		} else {
			this.sibs.transfer(sourceAccount.getIBAN(), targetAccount.getIBAN(), amount);
			view.successfullTransfer();
		}
	}

	private String[] processInput(String input) {
		return input.split(" ");
	}

}
