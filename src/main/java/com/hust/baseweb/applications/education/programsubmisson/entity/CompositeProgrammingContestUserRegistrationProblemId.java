package com.hust.baseweb.applications.education.programsubmisson.entity;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class CompositeProgrammingContestUserRegistrationProblemId implements Serializable {

    private String contestId;
    private String userLoginId;
    private String problemId;
}
