package com.hust.baseweb.applications.education.programsubmisson.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "contest_problem")
public class ContestProblem {
    @Id
    @Column(name="problem_id")
    private String problemId;

    @Column(name="problem_name")
    private String problemName;

    @Column(name="problem_statement")
    private String problemStatement;
}
