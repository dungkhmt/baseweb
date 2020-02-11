package com.hust.baseweb.model.dto;

import java.util.Date;

import com.hust.baseweb.rest.user.DPerson;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DTOPerson {
	private String fullName;
	private String status;
	private String partyType;
	private Date createdDate;
	private String userLoginId;
	private String partyCode;
	public DTOPerson(String fullName, String status, String partyType,
			Date createdDate, String userLoginId, String partyCode) {
		super();
		this.fullName = fullName;
		this.status = status;
		this.partyType = partyType;
		this.createdDate = createdDate;
		this.userLoginId = userLoginId;
		this.partyCode = partyCode;
	}
	public DTOPerson(DPerson p){
		this.fullName = p.getPerson() != null ? p.getPerson().getFirstName() + " " + p.getPerson().getMiddleName() + " " + p.getPerson().getLastName() : null;
		this.status = p.getStatus();
		this.partyType = p.getType().getId();
		this.createdDate = p.getCreatedDate();
		this.userLoginId = p.getUserLogin().getUserLoginId();
		this.partyCode = p.getPartyCode();
	}
}
