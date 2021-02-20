package com.hust.baseweb.applications.education.programsubmisson.entity;

import lombok.*;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class CompositeProgrammingContestProblemId implements Serializable {
    private String contestId;
    private String problemId;
}
