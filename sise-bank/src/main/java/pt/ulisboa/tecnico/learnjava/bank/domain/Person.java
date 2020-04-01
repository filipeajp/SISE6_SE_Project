package pt.ulisboa.tecnico.learnjava.bank.domain;

import pt.ulisboa.tecnico.learnjava.bank.exceptions.ClientException;

// Refactor for Keep Unit Interfaces Small
public class Person {
	private final String firstName;
	private final String lastName;
	private final String phoneNumber;
	private final String address;
	private int age;

	public Person(String firstName, String lastName, String phoneNumber, String address, int age)
			throws ClientException {
		checkParameters(phoneNumber, age);
		this.firstName = firstName;
		this.lastName = lastName;
		this.phoneNumber = phoneNumber;
		this.address = address;
		this.age = age;
	}

	public void checkParameters(String phoneNumber, int age) throws ClientException {
		if (age < 0) {
			throw new ClientException();
		}

		if (phoneNumber.length() != 9 || !phoneNumber.matches("[0-9]+")) {
			throw new ClientException();
		}
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public String getAddress() {
		return address;
	}

	public int getAge() {
		return this.age;
	}

	public void setAge(int age) {
		this.age = age;
	}

}
