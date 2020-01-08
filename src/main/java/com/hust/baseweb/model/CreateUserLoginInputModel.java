package com.hust.baseweb.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateUserLoginInputModel {
	private String userName;
	private String password;
	public CreateUserLoginInputModel(){
		
	}
}
