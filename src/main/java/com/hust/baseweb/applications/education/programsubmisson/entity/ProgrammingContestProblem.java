package com.hust.baseweb.applications.education.programsubmisson.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "programming_contest_problem")
@AllArgsConstructor
@NoArgsConstructor
@IdClass(CompositeProgrammingContestProblemId.class)
public class ProgrammingContestProblem {

    @Id
    @Column(name = "contest_id")
    private String contestId;

    @Id
    @Column(name = "problem_id")
    private String problemId;

}
