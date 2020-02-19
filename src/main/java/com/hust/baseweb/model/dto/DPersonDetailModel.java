package com.hust.baseweb.model.dto;

import com.hust.baseweb.rest.user.DPerson;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

import java.util.Date;
import java.util.UUID;

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

    public DPersonDetailModel(DPerson dPerson) {
        this.partyId = dPerson.getPartyId();
        if (dPerson.getPerson() != null) {
            this.fullName = dPerson.getPerson().getFirstName() + " " + dPerson.getPerson().getMiddleName() + " " + dPerson.getPerson().getLastName();
            this.middleName = dPerson.getPerson().getMiddleName();
            this.firstName = dPerson.getPerson().getFirstName();
            this.lastName = dPerson.getPerson().getLastName();
            this.birthDate = dPerson.getPerson().getBirthDate();
        }
        this.status = dPerson.getStatus();
        this.partyType = dPerson.getType().getId();
        this.createdDate = dPerson.getCreatedDate();
        this.userLoginId = dPerson.getUserLogin().getUserLoginId();
        this.partyCode = dPerson.getPartyCode();
    }
}
