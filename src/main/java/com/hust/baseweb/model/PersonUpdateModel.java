package com.hust.baseweb.model;

import java.util.Date;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PersonUpdateModel {
	private String firstName;
	private String lastName;
	private String middleName;
	private Date birthDate;
	private String partyCode;
	private List<String> roles;

	public PersonUpdateModel() {
		
	}
}
