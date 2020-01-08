package com.hust.baseweb.model;

import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetUserLoginOutputModel {
	private String userName;
	private UUID partyId;
	public GetUserLoginOutputModel(){
		
	}
	public GetUserLoginOutputModel(String userName, UUID partyId){
		this.userName = userName;
		this.partyId = partyId;
	}
}
