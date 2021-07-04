package com.hust.baseweb.applications.education.programsubmisson.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Table(name = "programming_contest_user_registration_problem")
@Getter
@Setter
@Entity
@IdClass(CompositeProgrammingContestUserRegistrationProblemId.class)
public class ProgrammingContestUserRegistrationProblem {

    @Id
    @Column(name = "contest_id")
    private String contestId;

    @Id
    @Column(name = "user_login_id")
    private String userLoginId;

    @Id
    @Column(name = "problem_id")
    private String problemId;

    @Column(name = "points")
    private int points;

    @Column(name = "last_points")
    private int lastPoints;

}
