package com.hust.baseweb.applications.education.programsubmisson.entity;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class CompositeProgrammingContestUserRegistrationId implements Serializable {
    private String contestId;
    private String userLoginId;
}
