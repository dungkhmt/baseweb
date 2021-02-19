package com.hust.baseweb.applications.education.programsubmisson.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name="programming_contest")
@Entity
public class ProgrammingContest {
    public static final String CONTEST_TYPE_PARTICIPANT_IDENTICAL = "PARTICIPANT_IDENTICAL";
    public static final String CONTEST_TYPE_PARTICIPANT_SPECIFIC = "PARTICIPANT_SPECIFIC";

    @Id
    @Column(name="contest_id")
    private String contestId;

    @Column(name="contest_name")
    private String contestName;

    @Column(name="created_by_user_login_id")
    private String createdByUserLoginId;

    @Column(name="contest_type_id")
    private String contestTypeId;

}
