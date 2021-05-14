package com.hust.baseweb.applications.education.quiztest.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "edu_quiz_test")
public class EduQuizTest {
    @Id
    @Column(name="test_id")
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    private String testId;

    @Column(name="test_name")
    private String testName;

    @Column(name="schedule_datetime")
    private Date scheduleDatetime;

    @Column(name="duration")
    private Integer duration;

    @Column(name="course_id")
    private String courseId;

    @Column(name="status_id")
    private String statusId;

    @Column(name="created_by_user_login_id")
    private String createdByUserLoginId;

    @Column(name="last_updated_stamp")
    private Date lastUpdatedStamp;

    @Column(name="created_stamp")
    private Date createdStamp;

    @Column(name="class_id")
    private UUID classId;

}
