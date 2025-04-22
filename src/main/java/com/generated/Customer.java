package com.generated;

public class Customer {
	private String id;
	private String name;
	private String number;
	private String address;

	public Customer() {
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@Override
	public String toString() {
		return "Customer {" +
				"id: " + id + ", " + "name: " + name + ", " + "number: " + number + ", " + "address: " + address + "}";
	}
}