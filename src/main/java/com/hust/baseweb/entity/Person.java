package com.hust.baseweb.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.sql.Date;
import java.util.UUID;

/**
 * Person
 */

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Person {
    @Id
    @Column(name = "party_id")
    private UUID partyId;

    //@Column(name="first_name")
    private String firstName;

    //@Column(name="middle_name")
    private String middleName;

    //@Column(name="last_name")
    private String lastName;

    //@Column(name="gender")
    private String gender;

    //@Column(name="birth_date")
    private Date birthDate;

    public BasicInfoModel getBasicInfoModel() {
        return new BasicInfoModel(partyId, firstName + " " + middleName + " " + lastName, gender);
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class BasicInfoModel {
        private UUID partyId;
        private String fullName;
        private String gender;
    }
}

