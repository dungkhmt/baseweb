package com.hust.baseweb.applications.education.programsubmisson.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name="programming_contest_user_registration")
@IdClass(CompositeProgrammingContestUserRegistrationId.class)
public class ProgrammingContestUserRegistration {
    @Id
    @Column(name="contest_id")
    private String contestId;

    @Id
    @Column(name="user_login_id")
    private String userLoginId;

}
