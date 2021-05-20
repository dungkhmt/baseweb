package com.hust.baseweb.applications.education.quiztest.repo;

import com.hust.baseweb.applications.education.quiztest.entity.EduTestQuizGroup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface EduQuizTestGroupRepo extends JpaRepository<EduTestQuizGroup, UUID> {
    List<EduTestQuizGroup> findByTestId(String testId);
    EduTestQuizGroup findEduTestQuizGroupByTestIdAndQuizGroupId(
        String testId,
        UUID quizGroupId
    );
}
