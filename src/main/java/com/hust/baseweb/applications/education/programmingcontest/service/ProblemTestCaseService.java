package com.hust.baseweb.applications.education.programmingcontest.service;

import com.hust.baseweb.applications.education.programmingcontest.entity.ContestProblemNew;
import com.hust.baseweb.applications.education.programmingcontest.entity.TestCase;

import com.hust.baseweb.applications.education.programmingcontest.model.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface ProblemTestCaseService {
    void createContestProblem(ModelCreateContestProblem modelCreateContestProblem) throws Exception;

    void updateContestProblem(ModelCreateContestProblem modelCreateContestProblem, String problemId) throws Exception;

    void updateProblemSourceCode(ModelAddProblemLanguageSourceCode modelAddProblemLanguageSourceCode, String problemId);

    TestCase createTestCase(ModelCreateTestCase modelCreateTestCase, String problemId) throws Exception;

    TestCase updateTestCase(ModelCreateTestCase modelCreateTestCase, UUID testCaseId) throws Exception;

    Page<ContestProblemNew> getContestProblemPaging(Pageable pageable);

    ContestProblemNew findContestProblemByProblemId(String problemId) throws Exception;

    void saveTestCase(TestCase testCase) throws Exception;

    String executableIDECode(ModelRunCodeFromIDE modelRunCodeFromIDE, String userName, String computerLanguage) throws Exception;

    ContestProblemNew getContestProblem(String problemId) throws Exception;

    ModelProblemDetailRunCodeResponse problemDetailRunCode(String problemId, ModelProblemDetailRunCode modelProblemDetailRunCode, String userName) throws Exception;

    String getTestCaseResult(String problemId, String userName, ModelGetTestCaseResult modelGetTestCaseResult) throws Exception;

    String checkCompile(ModelCheckCompile modelCheckCompile, String userName) throws Exception;

    TestCase saveTestCase(String problemId, ModelSaveTestcase modelSaveTestcase);

//    void problemDetailSubmission()
}
