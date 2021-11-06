package com.hust.baseweb.applications.education.programmingcontest.repo;

import com.hust.baseweb.applications.education.programmingcontest.entity.ContestProblemNew;
import com.hust.baseweb.applications.education.programmingcontest.entity.TestCase;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface TestCaseRepo extends JpaRepository<TestCase, UUID> {
    TestCase findTestCaseByTestCaseId(UUID uuid);
    List<TestCase> findAllByContestProblemNew(ContestProblemNew contestProblemNew);
}
