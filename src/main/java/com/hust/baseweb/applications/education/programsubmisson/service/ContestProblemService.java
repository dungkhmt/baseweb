package com.hust.baseweb.applications.education.programsubmisson.service;

import com.hust.baseweb.applications.education.programsubmisson.entity.ContestProblem;
import com.hust.baseweb.applications.education.programsubmisson.entity.ContestProblemTest;
import com.hust.baseweb.applications.education.programsubmisson.model.ContestProblemInputModel;
import com.hust.baseweb.applications.education.programsubmisson.model.CreateContestProblemTestInputModel;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public interface ContestProblemService {
    ContestProblem findByProblemId(String problemId);
    ContestProblem save(ContestProblemInputModel input);
    ContestProblem update(ContestProblemInputModel input);
    List<ContestProblem> findAll();
    List<ContestProblemTest> findAllContestProblemTestByProblemId(String problemId);
    ContestProblemTest createContestProblemTest(CreateContestProblemTestInputModel input, MultipartFile[] files);
}
