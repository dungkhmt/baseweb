package com.hust.baseweb.applications.education.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name="log_user_login_quiz_question")

public class LogUserLoginQuizQuestion {
    @Id
    @Column(name="log_user_login_quiz_question_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID logUserLoginQuizQuestionId;

    @Column(name="user_login_id")
    private String userLoginId;

    @Column(name="question_id")
    private UUID questionId;

    @Column(name="created_stamp")
    private Date createStamp;

}
