package com.hust.baseweb.applications.education.programsubmisson.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SearchProgrammingContestUserRegistrationInputModel {
    private String userLoginId;
    private String contestId;
    private String statusId;
}
