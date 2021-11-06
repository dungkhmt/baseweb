package com.hust.baseweb.applications.education.programmingcontest.entity;

import com.hust.baseweb.entity.UserLogin;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "problem_submission_new")
@Builder
public class ProblemSubmission {
    @Id
    @Column(name = "problem_submission_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID problemSubmissionId;

    @JoinColumn(name = "problem_id", referencedColumnName = "problem_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private ContestProblemNew contestProblemNew;

    @JoinColumn(name = "submitted_by_user_login_id", referencedColumnName = "user_login_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private UserLogin userLogin;

    @Column(name = "source_code")
    private String sourceCode;

    @Column(name = "source_code_language")
    private String sourceCodeLanguages;

    @Column(name = "status")
    private String status;

    @Column(name = "score")
    private int score;

    @Column(name = "runtime")
    private String runtime;

    @Column(name = "memory_usage")
    private float memoryUsage;

    @Column(name = "created_stamp")
    private String timeSubmitted;

    @Column(name = "test_case_pass")
    private String testCasePass;

    @PrePersist
    protected void onCreate() throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        timeSubmitted = formatter.format(date);

    }
}
