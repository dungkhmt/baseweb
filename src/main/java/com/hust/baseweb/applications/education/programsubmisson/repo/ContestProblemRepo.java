package com.hust.baseweb.applications.education.programsubmisson.repo;

import com.hust.baseweb.applications.education.programsubmisson.entity.ContestProblem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContestProblemRepo extends JpaRepository<ContestProblem, String> {
    ContestProblem findByProblemId(String problemId);
    ContestProblem save(ContestProblem contestProblem);
}
