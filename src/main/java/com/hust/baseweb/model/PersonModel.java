package com.hust.baseweb.model;

import java.util.Date;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PersonModel {
	private String userName;
	private String password;
	private List<String> roles;
	private String partyCode;
	private String firstName;
	private String lastName;
	private String middleName;
	private String gender;
	private Date birthDate;
	public PersonModel(){
		
	}
}
