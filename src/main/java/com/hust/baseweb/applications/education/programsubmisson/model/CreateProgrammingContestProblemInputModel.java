package com.hust.baseweb.applications.education.programsubmisson.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateProgrammingContestProblemInputModel {
    private String contestId;
    private String problemId;
}
