package com.hust.baseweb.applications.education.programmingcontest.repo;

import com.hust.baseweb.applications.education.programmingcontest.entity.ContestProblemNew;
import com.hust.baseweb.applications.education.programmingcontest.entity.ProblemSubmission;
import com.hust.baseweb.entity.UserLogin;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface ProblemSubmissionRepo extends JpaRepository<ProblemSubmission, UUID> {
    @Query("select p.problemSubmissionId, p.timeSubmitted, p.status, p.score, p.runtime, p.memoryUsage, p.sourceCodeLanguages from ProblemSubmission p where p.userLogin = :user and p.contestProblemNew = :problem")
    List<Object[]> getListProblemSubmissionByUserAndProblemId(@Param("user") UserLogin user, @Param("problem")
        ContestProblemNew problem);

    ProblemSubmission findByProblemSubmissionId(UUID id);
}
