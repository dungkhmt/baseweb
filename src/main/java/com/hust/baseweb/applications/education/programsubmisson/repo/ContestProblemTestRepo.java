package com.hust.baseweb.applications.education.programsubmisson.repo;

import com.hust.baseweb.applications.education.programsubmisson.entity.ContestProblemTest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ContestProblemTestRepo extends JpaRepository<ContestProblemTest, UUID> {
    ContestProblemTest save(ContestProblemTest contestProblemTest);
    List<ContestProblemTest> findAllByProblemId(String problemId);
    List<ContestProblemTest> findAllByProblemTestFilename(String problemTestFilename);
    List<ContestProblemTest> findAllByProblemTestFilenameAndProblemId(String problemTestFilename, String problemId);
}
