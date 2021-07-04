package com.hust.baseweb.applications.education.programsubmisson.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "contest_program_submission")
@Getter
@Setter

public class ContestProgramSubmission {

    @Id
    @Column(name = "contest_program_submission_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID contestProgramSubmissionId;

    @Column(name = "contest_id")
    private String contestId;

    @Column(name = "problem_id")
    private String problemId;

    @Column(name = "submitted_by_user_login_id")
    private String submittedByUserLoginId;

    @Column(name = "points")
    private int points;

    @Column(name = "full_link_file")
    private String fullLinkFile;


    @Column(name = "created_stamp")
    private Date createdStamp;
}
