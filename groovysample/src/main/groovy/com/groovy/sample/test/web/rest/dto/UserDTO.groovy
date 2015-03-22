package com.groovy.sample.test.web.rest.dto

import groovy.transform.ToString

import javax.validation.constraints.NotNull
import javax.validation.constraints.Pattern
import javax.validation.constraints.Size

import org.hibernate.validator.constraints.Email

@ToString(includeNames=true)
public class UserDTO {

	@Pattern(regexp = "^[a-z0-9]*\$")
	@NotNull
	@Size(min = 1, max = 50)
	private String login

	@NotNull
	@Size(min = 5, max = 100)
	private String password

	@Size(max = 50)
	private String firstName

	@Size(max = 50)
	private String lastName

	@Email
	@Size(min = 5, max = 100)
	private String email

	@Size(min = 2, max = 5)
	private String langKey

	private List<String> roles

	public UserDTO() {
	}

	public UserDTO(String login, String password, String firstName, String lastName, String email, String langKey,
	List<String> roles) {
		this.login = login
		this.password = password
		this.firstName = firstName
		this.lastName = lastName
		this.email = email
		this.langKey = langKey
		this.roles = roles
	}
}