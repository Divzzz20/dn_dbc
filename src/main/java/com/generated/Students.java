package com.generated;

public class Students {
	private String id;
	private String name;
	private String password;

	public Students() {
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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		return "Students {" +
				"id: " + id + ", " + "name: " + name + ", " + "password: " + password + "}";
	}
}