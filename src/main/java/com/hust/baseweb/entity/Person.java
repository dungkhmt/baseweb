package com.hust.baseweb.entity;

import lombok.Getter;
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


    public Person(UUID partyId, String firstName, String middleName, String lastName, String gender, Date birthDate) {
        this.partyId = partyId;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.gender = gender;
        this.birthDate = birthDate;
    }

    public Person() {
    }
}