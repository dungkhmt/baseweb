package com.hust.baseweb.applications.education.programmingcontest.service;

import com.hust.baseweb.applications.education.programmingcontest.docker.DockerClientBase;
import com.hust.baseweb.applications.education.programmingcontest.entity.*;
import com.hust.baseweb.applications.education.programmingcontest.exception.MiniLeetCodeException;
import com.hust.baseweb.applications.education.programmingcontest.model.*;
import com.hust.baseweb.applications.education.programmingcontest.repo.*;
import com.hust.baseweb.applications.education.programmingcontest.utils.ComputerLanguage;
import com.hust.baseweb.applications.education.programmingcontest.utils.TempDir;
import com.hust.baseweb.entity.UserLogin;
import com.hust.baseweb.repo.UserLoginRepo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class ProblemTestCaseServiceImpl implements ProblemTestCaseService {
    private ContestProblemNewRepo contestProblemNewRepo;
    private TestCaseRepo testCaseRepo;
    private ProblemSourceCodeRepo problemSourceCodeRepo;
    private DockerClientBase dockerClientBase;
    private TempDir tempDir;
    private ContestProblemPagingAndSortingRepo contestProblemPagingAndSortingRepo;
    private ProblemSubmissionRepo problemSubmissionRepo;
    private UserLoginRepo userLoginRepo;
    private ContestRepo contestRepo;

    @Override
    public void createContestProblem(ModelCreateContestProblem modelCreateContestProblem) throws Exception {
        if(contestProblemNewRepo.findByProblemId(modelCreateContestProblem.getProblemId()) != null){
            throw new MiniLeetCodeException("problem id already exist");
        }
        try {
            ContestProblemNew contestProblemNew = ContestProblemNew.builder()
                                                                   .problemId(modelCreateContestProblem.getProblemId())
                                                                   .problemName(modelCreateContestProblem.getProblemName())
                                                                   .problemDescription(modelCreateContestProblem.getProblemDescription())
                                                                   .categoryId(modelCreateContestProblem.getCategoryId())
                                                                   .memoryLimit(modelCreateContestProblem.getMemoryLimit())
                                                                   .timeLimit(modelCreateContestProblem.getTimeLimit())
                                                                   .levelId(modelCreateContestProblem.getLevelId())
                                                                   .correctSolutionLanguage(modelCreateContestProblem.getCorrectSolutionLanguage())
                                                                   .correctSolutionSourceCode(modelCreateContestProblem.getCorrectSolutionSourceCode())
                                                                   .solution(modelCreateContestProblem.getSolution())
//                .testCases(null)
                                                                   .build();
            contestProblemNewRepo.save(contestProblemNew);
        } catch (Exception e){
            throw new Exception(e.getMessage());
        }


    }

    @Override
    public ContestProblemNew updateContestProblem(ModelCreateContestProblem modelCreateContestProblem, String problemId) throws Exception {

        if(!contestProblemNewRepo.existsById(problemId)){
            throw new MiniLeetCodeException("problem id not found");
        }
        ContestProblemNew contestProblemNew = contestProblemNewRepo.findByProblemId(problemId);
        contestProblemNew.setProblemName(modelCreateContestProblem.getProblemName());
        contestProblemNew.setProblemDescription(modelCreateContestProblem.getProblemDescription());
        contestProblemNew.setLevelId(modelCreateContestProblem.getLevelId());
        contestProblemNew.setCategoryId(modelCreateContestProblem.getCategoryId());
        contestProblemNew.setSolution(modelCreateContestProblem.getSolution());
        contestProblemNew.setTimeLimit(modelCreateContestProblem.getTimeLimit());
        contestProblemNew.setCorrectSolutionLanguage(modelCreateContestProblem.getCorrectSolutionLanguage());
        contestProblemNew.setCorrectSolutionSourceCode(modelCreateContestProblem.getCorrectSolutionSourceCode());
        try {
            return contestProblemNewRepo.save(contestProblemNew);
        }catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }



    @Override
    public void updateProblemSourceCode(ModelAddProblemLanguageSourceCode modelAddProblemLanguageSourceCode, String problemId) {
//        ProblemSourceCode problemSourceCode = new ProblemSourceCode();
//        problemSourceCode.setProblemSourceCodeId(modelAddProblemLanguageSourceCode.getProblemSourceCodeId());
//        problemSourceCode.setMainSource(modelAddProblemLanguageSourceCode.getMainSource());
//        problemSourceCode.setBaseSource(modelAddProblemLanguageSourceCode.getBaseSource());
//        problemSourceCode.setLanguage(modelAddProblemLanguageSourceCode.getLanguage());
//        problemSourceCode.setProblemFunctionDefaultSource(modelAddProblemLanguageSourceCode.getProblemFunctionDefaultSource());
//        problemSourceCode.setProblemFunctionSolution(modelAddProblemLanguageSourceCode.getProblemFunctionSolution());
//        ContestProblem contestProblem = contestProblemRepo.findByProblemId(problemId);
//        if(contestProblem.getProblemSourceCode() == null){
//            ArrayList<ProblemSourceCode> problemSourceCodes = new ArrayList<ProblemSourceCode>();
//            problemSourceCodes.add(problemSourceCode);
//            contestProblem.setProblemSourceCode(problemSourceCodes);
//        }else{
//            contestProblem.getProblemSourceCode().add(problemSourceCode);
//        }
//        problemSourceCodeRepo.save(problemSourceCode);
//        contestProblemRepo.save(contestProblem);
    }

    @Override
    public TestCase createTestCase(ModelCreateTestCase modelCreateTestCase, String problemId) throws Exception {
        try {
            ContestProblemNew contestProblemNew = contestProblemNewRepo.findByProblemId(problemId);
            String solution = contestProblemNew.getCorrectSolutionSourceCode();
            String tempName = tempDir.createRandomScriptFileName(problemId+"-solution");
            String response = runCode(solution, contestProblemNew.getCorrectSolutionLanguage(), tempName, modelCreateTestCase.getTestCase(), contestProblemNew.getTimeLimit(), "Language Not Found");
            TestCase testCase = TestCase.builder()
                    .contestProblemNew(contestProblemNew)
                    .testCase(modelCreateTestCase.getTestCase())
                    .testCasePoint(modelCreateTestCase.getTestCasePoint())
                    .correctAnswer(response)
                    .build();
            testCaseRepo.save(testCase);
            return testCase;
        }catch (Exception e){
            throw new Exception(e.getMessage());
        }

    }

    @Override
    public TestCase updateTestCase(ModelCreateTestCase modelCreateTestCase, UUID testCaseId) throws Exception {
        try {
            TestCase testCase = testCaseRepo.findTestCaseByTestCaseId(testCaseId);
            if(testCase == null){
                throw new Exception("testcase not found");
            }
            ContestProblemNew contestProblemNew = testCase.getContestProblemNew();
            String solution = contestProblemNew.getCorrectSolutionSourceCode();
            String tempName = tempDir.createRandomScriptFileName(contestProblemNew.getProblemId() + "-solution");
            String response = runCode(solution, contestProblemNew.getCorrectSolutionLanguage(), tempName, modelCreateTestCase.getTestCase(), contestProblemNew.getTimeLimit(), "Language Not Found");
            testCase.setTestCase(modelCreateTestCase.getTestCase());
            testCase.setCorrectAnswer(response);
            return testCase;
        }catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public Page<ContestProblemNew> getContestProblemPaging(Pageable pageable) throws Exception {
        log.info("getContestProblemPaging ");
        try {
            Page<ContestProblemNew> contestProblems = contestProblemPagingAndSortingRepo.findAll(pageable);
            return contestProblemPagingAndSortingRepo.findAll(pageable);
        }catch (Exception e){
            throw new Exception(e.getMessage());
        }

    }

    @Override
    public ContestProblemNew findContestProblemByProblemId(String problemId) throws Exception {
        try {
            ContestProblemNew contestProblemNew = contestProblemNewRepo.findByProblemId(problemId);
            return contestProblemNew;
        }catch (Exception e){
            throw new Exception(e.toString());
        }
    }

    @Override
    public void saveTestCase(TestCase testCase) throws Exception {
        try {
            testCaseRepo.save(testCase);
        }catch (Exception e){
            throw new Exception(e.toString());
        }
    }

    @Override
    public String executableIDECode(ModelRunCodeFromIDE modelRunCodeFromIDE, String userName, String computerLanguage) throws Exception {
        String tempName = tempDir.createRandomScriptFileName(userName + "-" + computerLanguage);
        String response = runCode(modelRunCodeFromIDE.getSource(), computerLanguage, tempName, modelRunCodeFromIDE.getInput(), 10, "Language Not Found");
        tempDir.pushToConcurrentLinkedQueue(tempName);
        return response;
    }

    @Override
    public ContestProblemNew getContestProblem(String problemId) throws Exception {
        ContestProblemNew contestProblemNew;
        try {
            contestProblemNew = contestProblemNewRepo.findByProblemId(problemId);
            if(contestProblemNew == null){
                throw new MiniLeetCodeException("Problem not found");
            }
            return contestProblemNew;
        }catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }



    @Override
    public ModelProblemDetailRunCodeResponse problemDetailRunCode(String problemId, ModelProblemDetailRunCode modelProblemDetailRunCode, String userName) throws Exception {
        ContestProblemNew contestProblemNew = contestProblemNewRepo.findByProblemId(problemId);
        String tempName = tempDir.createRandomScriptFileName(contestProblemNew.getProblemName() + "-" + contestProblemNew.getCorrectSolutionLanguage());
        String output = runCode(modelProblemDetailRunCode.getSourceCode(),
                                modelProblemDetailRunCode.getComputerLanguage(),
                tempName+"-"+userName+"-code",
                                modelProblemDetailRunCode.getInput(),
                                contestProblemNew.getTimeLimit(),
                                "User Source Code Langua Not Found");

        output = output.substring(0, output.length()-1);

        int lastLineIndexOutput = output.lastIndexOf("\n");
        if(output.equals("Time Limit Exceeded")){
            return ModelProblemDetailRunCodeResponse.builder()
                    .status("Time Limit Exceeded")
                    .build();
        }
        String status = output.substring(lastLineIndexOutput);
        log.info("status {}", status);
        if(status.contains("Compile Error")){
            return ModelProblemDetailRunCodeResponse.builder()
                    .output(output.substring(0, lastLineIndexOutput))
                    .status("Compile Error")
                    .build();
        }
        log.info("status {}", status);
        output = output.substring(0, lastLineIndexOutput);
        String expected = runCode(
            contestProblemNew.getCorrectSolutionSourceCode(),
            contestProblemNew.getCorrectSolutionLanguage(),
                tempName+"-solution",
            modelProblemDetailRunCode.getInput(),
            contestProblemNew.getTimeLimit(),
            "Correct Solution Language Not Found");
        expected = expected.substring(0, expected.length()-1);
        int lastLinetIndexExpected = expected.lastIndexOf("\n");
        expected = expected.substring(0, lastLinetIndexExpected);
        expected = expected.replaceAll("\n", "");
        output = output.replaceAll("\n", "");
        if(output.equals(expected)){
            status = "Accept";
        }else{
            status = "Wrong Answer";
        }
        log.info("output {}", output);
        log.info("expected {}", expected);
        return ModelProblemDetailRunCodeResponse.builder()
                .expected(expected)
                .output(output)
                .status(status)
                .build();
    }

    @Override
    public String getTestCaseResult(String problemId, String userName, ModelGetTestCaseResult modelGetTestCaseResult) throws Exception {
        ContestProblemNew contestProblemNew = contestProblemNewRepo.findByProblemId(problemId);
        String tempName = tempDir.createRandomScriptFileName(userName + "-" +
                                                             contestProblemNew.getProblemName() + "-" + contestProblemNew.getCorrectSolutionLanguage());
        String output = runCode(contestProblemNew.getCorrectSolutionSourceCode(), contestProblemNew.getCorrectSolutionLanguage(), tempName, modelGetTestCaseResult.getTestcase(), contestProblemNew.getTimeLimit(), "Correct Solution Language Not Found");
        output = output.substring(0, output.length()-1);
        int lastLinetIndexExpected = output.lastIndexOf("\n");
        output = output.substring(0, lastLinetIndexExpected);
        output = output.replaceAll("\n", "");
        log.info("output {}", output);
        return output;
    }

    @Override
    public String checkCompile(ModelCheckCompile modelCheckCompile, String userName) throws Exception {
        String tempName = tempDir.createRandomScriptFileName(userName);
        String resp;
        switch (modelCheckCompile.getComputerLanguage()){
            case "CPP":
                tempDir.createScriptCompileFile(modelCheckCompile.getSource(), ComputerLanguage.Languages.CPP, tempName);
                resp = dockerClientBase.runExecutable(ComputerLanguage.Languages.CPP, tempName);
                break;
            case "JAVA":
                tempDir.createScriptCompileFile(modelCheckCompile.getSource(), ComputerLanguage.Languages.JAVA, tempName);
                resp = dockerClientBase.runExecutable(ComputerLanguage.Languages.JAVA, tempName);
                break;
            case "PYTHON3":
                tempDir.createScriptCompileFile(modelCheckCompile.getSource(), ComputerLanguage.Languages.PYTHON3, tempName);
                resp = dockerClientBase.runExecutable(ComputerLanguage.Languages.PYTHON3, tempName);
                break;
            case "GOLANG":
                tempDir.createScriptCompileFile(modelCheckCompile.getSource(), ComputerLanguage.Languages.GOLANG, tempName);
                resp = dockerClientBase.runExecutable(ComputerLanguage.Languages.GOLANG, tempName);
                break;
            default:
                throw new Exception("Language not found");
        }
        if(resp.contains("Successful")){
            return "Successful";
        }else{
            return "Compile Error";
        }
    }

    @Override
    public TestCase saveTestCase(String problemId, ModelSaveTestcase modelSaveTestcase) {

        ContestProblemNew contestProblemNew = contestProblemNewRepo.findByProblemId(problemId);
        TestCase testCase = TestCase.builder()
                .correctAnswer(modelSaveTestcase.getResult())
                .testCase(modelSaveTestcase.getInput())
                .contestProblemNew(contestProblemNew)
                .build();
        return testCaseRepo.save(testCase);
    }

    @Override
    public ModelProblemSubmissionResponse problemDetailSubmission(ModelProblemDetailSubmission modelProblemDetailSubmission, String problemId, String userName) throws Exception {
        log.info("source {} ", modelProblemDetailSubmission.getSource());
        UserLogin userLogin = userLoginRepo.findByUserLoginId(userName);
        if(userLogin.equals(null)){
            throw new Exception(("user not found"));
        }
        ContestProblemNew contestProblemNew = contestProblemNewRepo.findByProblemId(problemId);
        if(contestProblemNew.equals(null)){
            throw new Exception("Contest problem does not exist");
        }
        List<TestCase> testCaseList = testCaseRepo.findAllByContestProblemNew(contestProblemNew);
        if (testCaseList == null){
            throw new Exception("Problem Does not have testcase");
        }
        String tempName = tempDir.createRandomScriptFileName(userName+"-"+problemId);
        String response = submission(modelProblemDetailSubmission.getSource(), modelProblemDetailSubmission.getLanguage(), tempName, testCaseList, "Language Not Found", contestProblemNew.getTimeLimit());
        log.info("response {}", response);
        response = response.substring(0, response.length()-1);
        int lastIndex = response.lastIndexOf("\n");
        String status = response.substring(lastIndex, response.length());
        log.info("status {}", status);
        if(status.contains("Compile Error")){
            log.info("return problem submission compile error");
            ProblemSubmission problemSubmission = ProblemSubmission.builder()
                    .score(0)
                    .userLogin(userLogin)
                    .contestProblemNew(contestProblemNew)
                    .sourceCode(modelProblemDetailSubmission.getSource())
                    .status(status)
                    .testCasePass(0+"/"+testCaseList.size())
                    .sourceCodeLanguages(modelProblemDetailSubmission.getLanguage())
                    .build();
            problemSubmissionRepo.save(problemSubmission);
            return ModelProblemSubmissionResponse.builder()
                    .status(status)
                    .build();
        }
        String []ans = response.split("testcasedone\n");
        status = null;
        int cnt = 0;
        int score = 0;
        for(int i = 0; i < testCaseList.size(); i++){
            if(!testCaseList.get(i).getCorrectAnswer().equals(ans[i])){
                if(status == null && ans[i].contains("Time Limit Exceeded")){
                    status = "Time Limit Exceeded";
                }else{
                    status = "Wrong Answer";
                }
            }else{
                score = testCaseList.get(i).getTestCasePoint();
                cnt++;
            }
        }
        if(status == null){
            status = "Accept";
        }
        log.info("pass {}/{}", cnt, testCaseList.size());
        ProblemSubmission problemSubmission = ProblemSubmission.builder()
                .score(score)
                .userLogin(userLogin)
                .contestProblemNew(contestProblemNew)
                .sourceCode(modelProblemDetailSubmission.getSource())
                .status(status)
                .testCasePass(cnt+"/"+testCaseList.size())
                .sourceCodeLanguages(modelProblemDetailSubmission.getLanguage())
                .build();
        ProblemSubmission problemSubmission1 = problemSubmissionRepo.save(problemSubmission);
        ModelProblemSubmissionResponse res = ModelProblemSubmissionResponse.builder()
                .status(status)
                .result(cnt+"/"+testCaseList.size())
                .problemSubmissionId(problemSubmission1.getProblemSubmissionId())
                .language(modelProblemDetailSubmission.getLanguage())
                .score(score)
                .memoryUsage(problemSubmission1.getMemoryUsage())
                .runtime(problemSubmission1.getRuntime())
                .timeSubmitted(problemSubmission1.getTimeSubmitted())
                .build();
        return res;
    }

    @Override
    public ListProblemSubmissionResponse getListProblemSubmissionResponse(String problemId, String userId) throws Exception {
        UserLogin userLogin = userLoginRepo.findByUserLoginId(userId);
        ContestProblemNew contestProblemNew = contestProblemNewRepo.findByProblemId(problemId);
        if(userLogin == null || contestProblemNew == null){
            throw new Exception("not found");
        }
        List<Object[]> list = problemSubmissionRepo.getListProblemSubmissionByUserAndProblemId(userLogin,
                                                                                               contestProblemNew);
        List<ProblemSubmissionResponse> problemSubmissionResponseList = new ArrayList<>();
        try {
            list.stream().forEach(objects -> {
                log.info("objects {}", objects);
                ProblemSubmissionResponse problemSubmissionResponse = ProblemSubmissionResponse.builder()
                        .problemSubmissionId((UUID) objects[0])
                        .timeSubmitted((String) objects[1])
                        .status((String) objects[2])
                        .score((int) objects[3])
                        .runtime((String) objects[4])
                        .memoryUsage((float) objects[5])
                        .language((String) objects[6])
                        .build();
                problemSubmissionResponseList.add(problemSubmissionResponse);
            });
        } catch (Exception e){
            log.info("error");
            throw e;
        }

        ListProblemSubmissionResponse listProblemSubmissionResponse = ListProblemSubmissionResponse.builder()
                .contents(problemSubmissionResponseList)
                .isSubmitted(list.size() == 0 ? false:true)
                .build();
        return listProblemSubmissionResponse;
    }

    @Override
    public Contest createContest(ModelCreateContest modelCreateContest, String userName) throws Exception {
        try {
            Contest contestExist = contestRepo.findContestByContestId(modelCreateContest.getContestId());
            if(contestExist != null){
                throw new MiniLeetCodeException("Contest is already exist");
            }
            UserLogin userLogin = userLoginRepo.findByUserLoginId(userName);
            List<ContestProblemNew> contestProblemNews = getContestProblemsFromListContestId(modelCreateContest.getProblemIds());
            Contest contest = Contest.builder()
                    .contestId(modelCreateContest.getContestId())
                    .contestName(modelCreateContest.getContestName())
                    .contestSolvingTime(modelCreateContest.getContestTime())
                    .contestProblemNews(contestProblemNews)
                    .userLogin(userLogin)
                    .build();
            return contestRepo.save(contest);
        }catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public Contest updateContest(ModelUpdateContest modelUpdateContest, String userName, String contestId) throws Exception {
        try {
            Contest contestExist = contestRepo.findContestByContestId(contestId);
            if(contestExist == null){
                throw new MiniLeetCodeException("Contest does not exist");
            }
            UserLogin userLogin = userLoginRepo.findByUserLoginId(userName);
            //check user have privileged
            if(!userLogin.getUserLoginId().equals(contestExist.getUserLogin().getUserLoginId())){
                throw new MiniLeetCodeException("You don't have privileged");
            }
            List<ContestProblemNew> contestProblemNews = getContestProblemsFromListContestId(modelUpdateContest.getProblemIds());
            Contest contest = Contest.builder()
                    .contestName(modelUpdateContest.getContestName())
                    .contestSolvingTime(modelUpdateContest.getContestSolvingTime())
                    .contestProblemNews(contestProblemNews)
                    .userLogin(userLogin)
                    .build();
            return contestRepo.save(contest);
        }catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public ModelProblemSubmissionDetailResponse findProblemSubmissionById(UUID id, String userName) throws MiniLeetCodeException {
        ProblemSubmission problemSubmission = problemSubmissionRepo.findByProblemSubmissionId(id);
        if (!problemSubmission.getUserLogin().getUserLoginId().equals(userName)){
            throw new MiniLeetCodeException("unauthor");
        }
        ModelProblemSubmissionDetailResponse modelProblemDetailSubmissionResponse = ModelProblemSubmissionDetailResponse.builder()
                .problemSubmissionId(problemSubmission.getProblemSubmissionId())
                .problemId(problemSubmission.getContestProblemNew().getProblemId())
                .problemName(problemSubmission.getContestProblemNew().getProblemName())
                .submittedAt(problemSubmission.getTimeSubmitted())
                .submissionSource(problemSubmission.getSourceCode())
                .submissionLanguage(problemSubmission.getSourceCodeLanguages())
                .score(problemSubmission.getScore())
                .testCasePass(problemSubmission.getTestCasePass())
                .runTime(problemSubmission.getRuntime())
                .memoryUsage(problemSubmission.getMemoryUsage())
                .status(problemSubmission.getStatus())
                .build();
        return modelProblemDetailSubmissionResponse;
    }

    private List<ContestProblemNew> getContestProblemsFromListContestId(List<String> problemIds) throws MiniLeetCodeException {
        List<ContestProblemNew> contestProblemNews = new ArrayList<>();
        for(String problemId : problemIds){
            ContestProblemNew contestProblemNew = contestProblemNewRepo.findByProblemId(problemId);
            if(contestProblemNew.equals(null)){
                throw new MiniLeetCodeException("Problem " + problemId +" does not exist");
            }
            contestProblemNews.add(contestProblemNew);
        }
        return contestProblemNews;
    }


    private String submission(String source, String computerLanguage, String tempName, List<TestCase> testCaseList, String exception, int timeLimit) throws Exception {
        String ans;
        tempName = tempName.replaceAll(" ","");
        switch (computerLanguage){
            case "CPP":
                tempDir.createScriptSubmissionFile(ComputerLanguage.Languages.CPP, tempName, testCaseList, source, timeLimit);
                ans = dockerClientBase.runExecutable(ComputerLanguage.Languages.CPP,  tempName);
                break;
            case "JAVA":
                tempDir.createScriptSubmissionFile(ComputerLanguage.Languages.JAVA, tempName, testCaseList, source, timeLimit);
                ans = dockerClientBase.runExecutable(ComputerLanguage.Languages.JAVA, tempName);
                break;
            case "PYTHON3":
                tempDir.createScriptSubmissionFile(ComputerLanguage.Languages.PYTHON3, tempName, testCaseList, source, timeLimit);
                ans = dockerClientBase.runExecutable(ComputerLanguage.Languages.PYTHON3, tempName);
                break;
            case "GOLANG":
                tempDir.createScriptSubmissionFile(ComputerLanguage.Languages.GOLANG, tempName, testCaseList, source, timeLimit);
                ans = dockerClientBase.runExecutable(ComputerLanguage.Languages.GOLANG, tempName);
                break;
            default:
                throw new Exception(exception);
        }
//        tempDir.pushToConcurrentLinkedQueue(tempName);
        return ans;
    }

    private String runCode(String sourceCode, String computerLanguage, String tempName, String input, int timeLimit, String exception) throws Exception {
        String ans;
        tempName = tempName.replaceAll(" ","");
        switch (computerLanguage){
            case "CPP":
                tempDir.createScriptFile(sourceCode, input, timeLimit, ComputerLanguage.Languages.CPP, tempName);
                ans = dockerClientBase.runExecutable(ComputerLanguage.Languages.CPP,  tempName);
                break;
            case "JAVA":
                tempDir.createScriptFile(sourceCode, input, timeLimit, ComputerLanguage.Languages.JAVA, tempName);
                ans = dockerClientBase.runExecutable(ComputerLanguage.Languages.JAVA, tempName);
                break;
            case "PYTHON3":
                tempDir.createScriptFile(sourceCode, input, timeLimit, ComputerLanguage.Languages.PYTHON3, tempName);
                ans = dockerClientBase.runExecutable(ComputerLanguage.Languages.PYTHON3, tempName);
                break;
            case "GOLANG":
                tempDir.createScriptFile(sourceCode, input, timeLimit, ComputerLanguage.Languages.GOLANG, tempName);
                ans = dockerClientBase.runExecutable(ComputerLanguage.Languages.GOLANG, tempName);
                break;
            default:
                throw new Exception(exception);
        }
        tempDir.pushToConcurrentLinkedQueue(tempName);
        return ans;
    }
}
