package com.hust.baseweb.applications.education.programsubmisson.service;

import com.hust.baseweb.applications.education.programsubmisson.controller.ProgramSubmissionController;
import com.hust.baseweb.applications.education.programsubmisson.entity.ContestProblem;
import com.hust.baseweb.applications.education.programsubmisson.entity.ContestProblemTest;
import com.hust.baseweb.applications.education.programsubmisson.model.ContestProblemInputModel;
import com.hust.baseweb.applications.education.programsubmisson.model.CreateContestProblemTestInputModel;
import com.hust.baseweb.applications.education.programsubmisson.repo.ContestProblemRepo;
import com.hust.baseweb.applications.education.programsubmisson.repo.ContestProblemTestRepo;
import com.hust.baseweb.config.FileSystemStorageProperties;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;

@Log4j2
@AllArgsConstructor(onConstructor = @__(@Autowired))
@Service
public class ContestProblemServiceImpl implements ContestProblemService {
    //public static final String problemDir = "problems";

    private ContestProblemRepo contestProblemRepo;
    private ContestProblemTestRepo contestProblemTestRepo;

    FileSystemStorageProperties uploadConfigProperties;

    @Override
    public ContestProblem findByProblemId(String problemId) {
        ContestProblem contestProblem = contestProblemRepo.findByProblemId(problemId);
        return contestProblem;
    }

    @Override
    public ContestProblem save(ContestProblemInputModel input) {
        String rootDir = uploadConfigProperties.getFilesystemRoot() +
                         uploadConfigProperties.getProgramSubmissionDataPath();
        log.info("save, problemId = " + input.getProblemId() + " problemName = " + input.getProblemName()
                 + " problem statement = " + input.getProblemStatement());
        ContestProblem contestProblem = new ContestProblem();
        contestProblem.setProblemId(input.getProblemId());
        contestProblem.setProblemName(input.getProblemName());
        contestProblem.setProblemStatement(input.getProblemStatement());
        contestProblem.setTimeLimit(input.getTimeLimit());
        contestProblem.setLevelId(input.getLevelId());
        contestProblem.setCategoryId(input.getCategoryId());

        contestProblem = contestProblemRepo.save(contestProblem);
        File dir = new File(rootDir + "/" + ProgramSubmissionController.problemDir);
        if (!dir.exists()) {
            dir.mkdir();
        }
        dir = new File(rootDir + "/" + ProgramSubmissionController.problemDir + "/" + contestProblem.getProblemId());
        if (!dir.exists()) {
            dir.mkdir();
        }

        return contestProblem;
    }

    @Override
    public ContestProblem update(ContestProblemInputModel input) {
        ContestProblem contestProblem = contestProblemRepo.findByProblemId(input.getProblemId());
        if (contestProblem == null) {
            log.info("update cannot find problem " + input.getProblemId());
            return null;
        }
        contestProblem.setProblemName(input.getProblemName());
        contestProblem.setTimeLimit(input.getTimeLimit());
        contestProblem.setLevelId(input.getLevelId());
        contestProblem.setCategoryId(input.getCategoryId());
        contestProblem.setProblemStatement(input.getProblemStatement());
        contestProblemRepo.save(contestProblem);
        return contestProblem;
    }

    @Override
    public List<ContestProblem> findAll() {
        return contestProblemRepo.findAll();
    }

    @Override
    public List<ContestProblemTest> findAllContestProblemTestByProblemId(String problemId) {
        return contestProblemTestRepo.findAllByProblemId(problemId);
    }

    @Override
    public ContestProblemTest createContestProblemTest(
        CreateContestProblemTestInputModel input,
        MultipartFile[] files
    ) {

        String rootDir = uploadConfigProperties.getFilesystemRoot() +
                         "/" +
                         uploadConfigProperties.getProgramSubmissionDataPath()
                         +
                         "/" +
                         ProgramSubmissionController.problemDir;
        File dir = new File(rootDir);
        if (!dir.exists()) {
            dir.mkdir();
        }
        String problemRootDir = rootDir + "/" + input.getProblemId();

        dir = new File(problemRootDir);
        if (!dir.exists()) {
            log.info("createContestProblemTest, dir " + problemRootDir + " not exists -> mkdir");
            dir.mkdir();
            log.info("createContestProblemTest, dir " + problemRootDir + " not exists -> mkdir OK");
        }
        //String stdTestName = input.getProblemId() + "-" + input.getTestName();
        String stdTestName = input.getTestName();
        //List<ContestProblemTest> contestProblemTests = contestProblemTestRepo.findAllByProblemTestFilename(input.getTestName());
        List<ContestProblemTest> contestProblemTests = contestProblemTestRepo.findAllByProblemTestFilenameAndProblemId(
            stdTestName,
            input.getProblemId());

        if (contestProblemTests != null && contestProblemTests.size() > 0) {
            log.info("createContestProblemTest, testName " + input.getTestName() + " exists -> RETURN");
            return null;
        }

        // copy input and output files to the corresponding directory
        //File inputF = new File(problemRootDir + "/" + input.getTestName() + ".inp");
        File inputF = new File(problemRootDir + "/" + stdTestName + ".inp");
        try {
            files[0].transferTo(inputF);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //File outputF = new File(problemRootDir + "/" + input.getTestName() + ".out");
        File outputF = new File(problemRootDir + "/" + stdTestName + ".out");
        try {
            files[1].transferTo(outputF);
        } catch (Exception e) {
            e.printStackTrace();
        }

        ContestProblemTest contestProblemTest = new ContestProblemTest();
        contestProblemTest.setProblemId(input.getProblemId());
        contestProblemTest.setProblemTestFilename(stdTestName);
        //contestProblemTest.setProblemTestFilename(input.getTestName());

        int testPoint = 0;
        try {
            testPoint = Integer.valueOf(input.getTestPoint());
        } catch (Exception e) {

            e.printStackTrace();
            return null;
        }
        contestProblemTest.setProblemTestPoint(testPoint);
        contestProblemTest = contestProblemTestRepo.save(contestProblemTest);

        return contestProblemTest;
    }
}
