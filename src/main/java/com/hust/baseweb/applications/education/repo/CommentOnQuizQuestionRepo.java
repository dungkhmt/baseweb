package com.hust.baseweb.applications.education.repo;

import com.hust.baseweb.applications.education.entity.CommentOnQuizQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface CommentOnQuizQuestionRepo extends JpaRepository<CommentOnQuizQuestion, UUID> {
    List<CommentOnQuizQuestion> findAllByQuestionId(UUID questionId);
}
