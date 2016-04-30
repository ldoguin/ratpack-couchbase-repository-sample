package org.couchbase.devex;

import com.couchbase.client.java.repository.annotation.Field;
import com.couchbase.client.java.repository.annotation.Id;
import com.fasterxml.jackson.annotation.JsonProperty;

public class User {

	@Id
	private String username;

	private Integer age;

	@Field("fName")
	@JsonProperty("fName")
	private String firstName;

	@Field("lName")
	@JsonProperty("lName")
	private String lastName;
	
	private String type = "user";

	public User() {
	}

	public User(String username, Integer age, String firstName, String lastName) {
		this.username = username;
		this.age = age;
		this.firstName = firstName;
		this.lastName = lastName;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getType() {
		return type;
	}
	
	@Override
	public String toString() {
		return "User [username=" + username + ", age=" + age + ", firstName=" + firstName + ", lastName=" + lastName
				+ "]";
	}

}
