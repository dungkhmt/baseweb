package com.hust.baseweb.applications.education.programsubmisson.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateProgrammingContestInputModel {
    private String contestId;
    private String contestName;
    private String contestTypeId;
}
