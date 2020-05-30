package com.hust.baseweb.applications.tms.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;
import java.util.List;
import java.util.UUID;

/**
 * @author Hien Hoang (hienhoang2702@gmail.com)
 */
public class DriverModel {

    @Getter
    @Setter
    public static class InputCreate {

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

    @Getter
    @Setter
    public static class FindAll {

        private String statusId;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class Output {

        private String userLoginId;
        private String driverFullName;
        private UUID partyDriverId;
    }
}
