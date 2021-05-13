package com.hust.baseweb.applications.education.quiztest.entity;

import com.hust.baseweb.applications.education.quiztest.entity.compositeid.CompositeQuizGroupQuestionAssignmentId;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "quiz_group_question_assignment")
@IdClass(CompositeQuizGroupQuestionAssignmentId.class)
public class QuizGroupQuestionAssignment {
    @Id
    @Column(name="question_id uuid")
    private UUID questionId;

    @Id
    @Column(name="quiz_group_id uuid")
    private UUID quizGroupId;

}
