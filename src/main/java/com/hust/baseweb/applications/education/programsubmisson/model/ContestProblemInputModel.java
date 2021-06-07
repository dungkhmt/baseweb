package com.hust.baseweb.applications.education.programsubmisson.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ContestProblemInputModel {

    private String problemId;
    private String problemName;
    private int timeLimit;
    private String problemStatement;
    private String levelId;
    private String categoryId;
}
