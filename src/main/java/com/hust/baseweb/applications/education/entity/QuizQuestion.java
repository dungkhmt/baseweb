package com.hust.baseweb.applications.education.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "quiz_question")
public class QuizQuestion {

    public static final String QUIZ_LEVEL_EASY = "QUIZ_LEVEL_EASY";
    public static final String QUIZ_LEVEL_INTERMEDIATE = "QUIZ_LEVEL_INTERMEDIATE";
    public static final String QUIZ_LEVEL_HARD = "QUIZ_LEVEL_HARD";

    public static final String STATUS_PRIVATE = "STATUS_PRIVATE";
    public static final String STATUS_PUBLIC = "STATUS_PUBLIC";
    public static final String STATUS_DELETE = "STATUS_DELETE";

    @Id
    @Column(name = "question_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID questionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_topic_id", referencedColumnName = "quiz_course_topic_id")
    private QuizCourseTopic quizCourseTopic;

    @Column(name = "level_id")
    private String levelId;

    @Column(name = "question_content")
    private String questionContent;

    @Column(name = "attachment")
    private String attachment;
    @Column(name = "status_id")
    private String statusId;

    @Column(name= "created_stamp")
    private Date createdStamp;
}
