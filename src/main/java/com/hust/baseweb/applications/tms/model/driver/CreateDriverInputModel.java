package com.hust.baseweb.applications.tms.model.driver;

import lombok.Getter;
import lombok.Setter;

import java.sql.Date;
import java.util.List;

@Getter
@Setter
public class CreateDriverInputModel {
    private String userName;
    private String password;
    private List<String> roles;
    private String partyCode;
    private String firstName;
    private String lastName;
    private String middleName;
    private String gender;
    private Date birthDate;

}
