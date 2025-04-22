package com.generated;

public class User {
	private String id;
	private String name;
	private String age;
	private String salary;
	private String email;

	public User() {
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

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public String getSalary() {
		return salary;
	}

	public void setSalary(String salary) {
		this.salary = salary;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public String toString() {
		return "User {" +
				"id: " + id + ", " + "name: " + name + ", " + "age: " + age + ", " + "salary: " + salary + ", " + "email: " + email + "}";
	}
}