package com.hust.baseweb.applications.education.programmingcontest.service;

import com.hust.baseweb.applications.education.programmingcontest.docker.DockerClientBase;
import com.hust.baseweb.applications.education.programmingcontest.entity.ContestProblemNew;
import com.hust.baseweb.applications.education.programmingcontest.entity.TestCase;
import com.hust.baseweb.applications.education.programmingcontest.model.*;
import com.hust.baseweb.applications.education.programmingcontest.repo.ContestProblemPagingAndSortingRepo;
import com.hust.baseweb.applications.education.programmingcontest.repo.ContestProblemRepoNew;
import com.hust.baseweb.applications.education.programmingcontest.repo.ProblemSourceCodeRepo;
import com.hust.baseweb.applications.education.programmingcontest.repo.TestCaseRepo;
import com.hust.baseweb.applications.education.programmingcontest.utils.ComputerLanguage;
import com.hust.baseweb.applications.education.programmingcontest.utils.TempDir;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class ProblemTestCaseServiceImpl implements ProblemTestCaseService {
    private ContestProblemRepoNew contestProblemRepoNew;
    private TestCaseRepo testCaseRepo;
    private ProblemSourceCodeRepo problemSourceCodeRepo;
    private DockerClientBase dockerClientBase;
    private TempDir tempDir;
    private ContestProblemPagingAndSortingRepo contestProblemPagingAndSortingRepo;

    @Override
    public void createContestProblem(ModelCreateContestProblem modelCreateContestProblem) throws Exception {
        if(contestProblemRepoNew.findByProblemId(modelCreateContestProblem.getProblemId()) != null){
            throw new Exception("problem id already exist");
        }
        ContestProblemNew contestProblem = ContestProblemNew.builder()
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
        contestProblemRepoNew.save(contestProblem);
    }

    @Override
    public void updateContestProblem(ModelCreateContestProblem modelCreateContestProblem, String problemId) throws Exception {
        ContestProblemNew contestProblem = contestProblemRepoNew.findByProblemId(problemId);
        if(contestProblem == null){
            throw new Exception("problem id not found");
        }
        contestProblem = ContestProblemNew.builder()
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
                                          .build();
        contestProblemRepoNew.save(contestProblem);
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
        ContestProblemNew contestProblem = contestProblemRepoNew.findByProblemId(problemId);
        String solution = contestProblem.getCorrectSolutionSourceCode();
        String tempName = tempDir.createRandomScriptFileName(problemId+"-solution");
        String response = runCode(solution, contestProblem.getCorrectSolutionLanguage(), tempName, modelCreateTestCase.getTestCase(), contestProblem.getTimeLimit(), "Language Not Found");
        TestCase testCase = TestCase.builder()
                .contestProblem(contestProblem)
                .testCase(modelCreateTestCase.getTestCase())
                .testCasePoint(modelCreateTestCase.getTestCasePoint())
                .correctAnswer(response)
                .build();
        testCaseRepo.save(testCase);
        return testCase;
    }

    @Override
    public TestCase updateTestCase(ModelCreateTestCase modelCreateTestCase, UUID testCaseId) throws Exception {
        TestCase testCase = testCaseRepo.findTestCaseByTestCaseId(testCaseId);
        if(testCase == null){
            throw new Exception("testcase not found");
        }
        ContestProblemNew contestProblem = testCase.getContestProblem();
        String solution = contestProblem.getCorrectSolutionSourceCode();
        String tempName = tempDir.createRandomScriptFileName(contestProblem.getProblemId()+"-solution");
        String response = runCode(solution, contestProblem.getCorrectSolutionLanguage(), tempName, modelCreateTestCase.getTestCase(), contestProblem.getTimeLimit(), "Language Not Found");
        testCase.setTestCase(modelCreateTestCase.getTestCase());
        testCase.setCorrectAnswer(response);
        return testCase;
    }

    @Override
    public Page<ContestProblemNew> getContestProblemPaging(Pageable pageable) {
        log.info("getContestProblemPaging ");
        Page<ContestProblemNew> contestProblems = contestProblemPagingAndSortingRepo.findAll(pageable);
//        log.info("testcase size", contestProblems.getContent().get(0).getTestCases().size());
        return contestProblemPagingAndSortingRepo.findAll(pageable);
    }

    @Override
    public ContestProblemNew findContestProblemByProblemId(String problemId) throws Exception {
        try {
            ContestProblemNew contestProblem = contestProblemRepoNew.findByProblemId(problemId);
            return contestProblem;
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
        ContestProblemNew contestProblem = contestProblemRepoNew.findByProblemId(problemId);
        if(contestProblem == null){
            throw new Exception("Problem not found");
        }
        return contestProblem;
    }



    @Override
    public ModelProblemDetailRunCodeResponse problemDetailRunCode(String problemId, ModelProblemDetailRunCode modelProblemDetailRunCode, String userName) throws Exception {
        ContestProblemNew contestProblem = contestProblemRepoNew.findByProblemId(problemId);
        String tempName = tempDir.createRandomScriptFileName(contestProblem.getProblemName() + "-" + contestProblem.getCorrectSolutionLanguage());
        String output = runCode(modelProblemDetailRunCode.getSourceCode(),
                modelProblemDetailRunCode.getComputerLanguage(),
                tempName+"-"+userName+"-code",
                modelProblemDetailRunCode.getInput(),
                contestProblem.getTimeLimit(),
                "User Source Code Langua Not Found");

        output = output.substring(0, output.length()-1);

        int lastLineIndexOutput = output.lastIndexOf("\n");
        if(output.equals("Time Limit Exceeded")){
            return ModelProblemDetailRunCodeResponse.builder()
                    .status("Time Limit Exceeded")
                    .build();
        }
        String status = output.substring(lastLineIndexOutput);
        log.info("stat {}", status);
        if(status.contains("Compile Error")){
            return ModelProblemDetailRunCodeResponse.builder()
                    .output(output.substring(0, lastLineIndexOutput))
                    .status("Compile Error")
                    .build();
        }
        log.info("status {}", status);
        output = output.substring(0, lastLineIndexOutput);
        String expected = runCode(contestProblem.getCorrectSolutionSourceCode(),
                contestProblem.getCorrectSolutionLanguage(),
                tempName+"-solution",
                modelProblemDetailRunCode.getInput(),
                contestProblem.getTimeLimit(),
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
        ContestProblemNew contestProblem = contestProblemRepoNew.findByProblemId(problemId);
        String tempName = tempDir.createRandomScriptFileName(userName + "-" +contestProblem.getProblemName() + "-" + contestProblem.getCorrectSolutionLanguage());
        String output = runCode(contestProblem.getCorrectSolutionSourceCode(), contestProblem.getCorrectSolutionLanguage(), tempName, modelGetTestCaseResult.getTestcase(), contestProblem.getTimeLimit(), "Correct Solution Language Not Found");
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

        ContestProblemNew contestProblem = contestProblemRepoNew.findByProblemId(problemId);
        TestCase testCase = TestCase.builder()
                .correctAnswer(modelSaveTestcase.getResult())
                .testCase(modelSaveTestcase.getInput())
                .contestProblem(contestProblem)
                .build();
        return testCaseRepo.save(testCase);
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
//        tempDir.pushToConcurrentLinkedQueue(tempName);
        return ans;
    }
}
