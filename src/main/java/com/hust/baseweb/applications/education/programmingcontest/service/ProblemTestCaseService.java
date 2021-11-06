package com.hust.baseweb.applications.education.programmingcontest.service;

import com.hust.baseweb.applications.education.programmingcontest.entity.Contest;
import com.hust.baseweb.applications.education.programmingcontest.entity.ContestProblemNew;
import com.hust.baseweb.applications.education.programmingcontest.entity.TestCase;
import com.hust.baseweb.applications.education.programmingcontest.exception.MiniLeetCodeException;
import com.hust.baseweb.applications.education.programmingcontest.model.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface ProblemTestCaseService {

    void createContestProblem(ModelCreateContestProblem modelCreateContestProblem) throws Exception;

    ContestProblemNew updateContestProblem(ModelCreateContestProblem modelCreateContestProblem, String problemId) throws Exception;

    void updateProblemSourceCode(ModelAddProblemLanguageSourceCode modelAddProblemLanguageSourceCode, String problemId);

    TestCase createTestCase(ModelCreateTestCase modelCreateTestCase, String problemId) throws Exception;

    TestCase updateTestCase(ModelCreateTestCase modelCreateTestCase, UUID testCaseId) throws Exception;

    Page<ContestProblemNew> getContestProblemPaging(Pageable pageable) throws Exception;

    ContestProblemNew findContestProblemByProblemId(String problemId) throws Exception;

    void saveTestCase(TestCase testCase) throws Exception;

    String executableIDECode(ModelRunCodeFromIDE modelRunCodeFromIDE, String userName, String computerLanguage) throws Exception;

    ContestProblemNew getContestProblem(String problemId) throws Exception;

    ModelProblemDetailRunCodeResponse problemDetailRunCode(String problemId, ModelProblemDetailRunCode modelProblemDetailRunCode, String userName) throws Exception;

    String getTestCaseResult(String problemId, String userName, ModelGetTestCaseResult modelGetTestCaseResult) throws Exception;

    String checkCompile(ModelCheckCompile modelCheckCompile, String userName) throws Exception;

    TestCase saveTestCase(String problemId, ModelSaveTestcase modelSaveTestcase);

    ModelProblemSubmissionResponse problemDetailSubmission(ModelProblemDetailSubmission modelProblemDetailSubmission, String problemId, String userName) throws Exception;

    ListProblemSubmissionResponse getListProblemSubmissionResponse(String problemId, String userId) throws Exception;

    Contest createContest(ModelCreateContest modelCreateContest, String userName) throws Exception;

    Contest updateContest(ModelUpdateContest modelUpdateContest, String userName, String contestId) throws Exception;

    ModelProblemSubmissionDetailResponse findProblemSubmissionById(UUID id, String userName) throws MiniLeetCodeException;
}
