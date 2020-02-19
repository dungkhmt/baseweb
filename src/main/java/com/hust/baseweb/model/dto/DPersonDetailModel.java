package com.hust.baseweb.model.dto;

import java.util.Date;
import java.util.UUID;

import com.hust.baseweb.rest.user.DPerson;

import org.springframework.hateoas.RepresentationModel;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DPersonDetailModel extends RepresentationModel<DPersonDetailModel> {

	private UUID partyId;
	private String fullName;
	private String firstName;
	private String middleName;
	private String lastName;

	private String status;
	private String partyType;
	private Date createdDate;
	private Date birthDate;
	private String userLoginId;
	private String partyCode;

	public DPersonDetailModel(String fullName, String status, String partyType, Date createdDate, String userLoginId,
			String partyCode) {
		super();
		this.fullName = fullName;
		this.status = status;
		this.partyType = partyType;
		this.createdDate = createdDate;
		this.userLoginId = userLoginId;
		this.partyCode = partyCode;
	}

	public DPersonDetailModel(DPerson p) {
		this.partyId = p.getPartyId();
		this.fullName = p.getPerson() != null
				? p.getPerson().getFirstName() + " " + p.getPerson().getMiddleName() + " " + p.getPerson().getLastName()
				: null;
		this.middleName = p.getPerson().getMiddleName();
		this.firstName = p.getPerson().getFirstName();
		this.lastName = p.getPerson().getLastName();
		this.birthDate = p.getPerson().getBirthDate();
		this.status = p.getStatus();
		this.partyType = p.getType().getId();
		this.createdDate = p.getCreatedDate();
		this.userLoginId = p.getUserLogin().getUserLoginId();
		this.partyCode = p.getPartyCode();
	}
}
