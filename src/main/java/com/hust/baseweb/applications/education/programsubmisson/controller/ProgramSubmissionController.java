package com.hust.baseweb.applications.education.programsubmisson.controller;

import com.google.gson.Gson;
import com.hust.baseweb.applications.education.programsubmisson.entity.*;
import com.hust.baseweb.applications.education.programsubmisson.model.*;
import com.hust.baseweb.applications.education.programsubmisson.repo.ContestProblemRepo;
import com.hust.baseweb.applications.education.programsubmisson.repo.ContestProblemTestRepo;
import com.hust.baseweb.applications.education.programsubmisson.repo.ContestProgramSubmissionRepo;
import com.hust.baseweb.applications.education.programsubmisson.repo.ProgrammingContestUserRegistrationProblemRepo;
import com.hust.baseweb.applications.education.programsubmisson.service.ContestProblemService;
import com.hust.baseweb.applications.education.programsubmisson.service.ContestProgramSubmissionService;
import com.hust.baseweb.applications.education.programsubmisson.service.ProgrammingContestService;
import com.hust.baseweb.entity.UserLogin;
import com.hust.baseweb.framework.properties.UploadConfigProperties;
import com.hust.baseweb.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

@Log4j2
@Controller
//@RequestMapping("/edu/programsubmission")
@AllArgsConstructor(onConstructor_ = @Autowired)
@CrossOrigin
public class ProgramSubmissionController {

    public static final String userSubmissionDir = "usersubmissions";
    public static final String problemDir = "problems";
    public static final String contestDir = "contests";
    public static final String defaultContest = "DEFAULT_CONTEST";


    @Autowired
    UploadConfigProperties uploadConfigProperties;
    @Autowired
    private ContestProblemService contestProblemService;
    @Autowired
    private ContestProblemTestRepo contestProblemTestRepo;
    @Autowired
    private ContestProblemRepo contestProblemRepo;
    @Autowired
    private ContestProgramSubmissionRepo contestProgramSubmissionRepo;
    @Autowired
    private ContestProgramSubmissionService contestProgramSubmissionService;

    @Autowired
    private ProgrammingContestUserRegistrationProblemRepo programmingContestUserRegistrationProblemRepo;
    @Autowired
    private ProgrammingContestService programmingContestService;
    @Autowired
    private UserService userService;

    @GetMapping("get-all-programming-contest-list")
    public ResponseEntity<?> getAllProgrammingContetList(Principal principal){
        log.info("getAllProgrammingContetList START");
        UserLogin userLogin = userService.findById(principal.getName());
        List<ProgrammingContest> programmingContestList = programmingContestService.findAll();
        log.info("getAllProgrammingContetList, GOT sz = " + programmingContestList.size());
        return ResponseEntity.ok().body(programmingContestList);
    }

    @PostMapping("create-programming-contest")
    public ResponseEntity<?> createProgrammingContest(Principal principal, @RequestBody
        CreateProgrammingContestInputModel input
    ){
        UserLogin userLogin = userService.findById(principal.getName());
        ProgrammingContest programmingContest = programmingContestService.save(userLogin,input);
        return ResponseEntity.ok().body(programmingContest);
    }

    @PostMapping("/submit")
    public ResponseEntity<?> submit(Principal principal, @RequestBody ProgramSubmissonModel input) {
        //System.out.println(input.getProgram());
        return ResponseEntity.ok().body("OK");
    }

    private static class StreamGobbler implements Runnable {

        private InputStream inputStream;
        private Consumer<String> consumer;

        public StreamGobbler(InputStream inputStream, Consumer<String> consumer) {
            this.inputStream = inputStream;
            this.consumer = consumer;
        }

        @Override
        public void run() {
            new BufferedReader(new InputStreamReader(inputStream)).lines().forEach(consumer);
        }

        static void processInformation(String info) {
            //System.out.println("processInformation, info = " + info);
            System.out.println(info);
            try {
                Scanner in = new Scanner(new File("D:/projects/baseweb/data/output.txt"));
                String rs = in.nextLine();
                if (rs.trim().equals(info.trim())) {
                    System.out.println("SUCCESS!!!!");
                } else {
                    System.out.println("FAILED!!!!");
                }
                in.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    /*
    @GetMapping("get-detail-contest-program-submission/{contestProgramSubmissionId}")
    public ResponseEntity<?> getDetailContestProgramSubmission(Principal principal,
                                                               @PathVariable String contestProgramSubmissionId){

        UUID uuidContestProgramSubmissionId = UUID.fromString(contestProgramSubmissionId);
        ContestProgramSubmission contestProgramSubmission = contestProgramSubmissionRepo.findByContestProgramSubmissionId(uuidContestProgramSubmissionId);
        String programCode = "";
        try{
            //String submissionContestUserProblemDir = establishSubmissionContestUserProblemDir(contestProgramSubmission.getContestId(),
            //                                                                                  contestProgramSubmission.getSubmittedByUserLoginId(),
            //                                                                                  contestProgramSubmission.getProblemId());


            String filename = contestProgramSubmission.getFullLinkFile();
            Scanner in = new Scanner(new File(filename));
            while(in.hasNext()){
                programCode = programCode + in.nextLine() + '\n';
            }
            in.close();
            log.info("getDetailContestProgramSubmission, programCode = " + programCode);

            //InputStream is = new FileInputStream(filename);
            //IOUtils.copyLarge(is, response.getOutputStream());
            //response.flushBuffer();

        }catch(Exception e){
            e.printStackTrace();
        }
        SubmittedProgramDetailModel submittedProgramDetailModel = new SubmittedProgramDetailModel();
        submittedProgramDetailModel.setProgramCode(programCode);

        return ResponseEntity.ok().body(submittedProgramDetailModel);


    }
    */

    @GetMapping("get-detail-contest-program-submission/{contestProgramSubmissionId}")
    @ResponseBody
    public void getDetailContestProgramSubmission(Principal principal,
                                                               @PathVariable String contestProgramSubmissionId,
                                                               HttpServletResponse response){
        response.setHeader("Content-Transfer-Encoding", "binary");
        response.setHeader(
            HttpHeaders.CONTENT_DISPOSITION,
            "attachment; filename=\"" + "program.py" + "\"");
        response.setContentType("application/octet-stream");

        UUID uuidContestProgramSubmissionId = UUID.fromString(contestProgramSubmissionId);
        ContestProgramSubmission contestProgramSubmission = contestProgramSubmissionRepo.findByContestProgramSubmissionId(uuidContestProgramSubmissionId);
        String programCode = "";
        try{
            //String submissionContestUserProblemDir = establishSubmissionContestUserProblemDir(contestProgramSubmission.getContestId(),
            //                                                                                  contestProgramSubmission.getSubmittedByUserLoginId(),
            //                                                                                  contestProgramSubmission.getProblemId());


            String filename = contestProgramSubmission.getFullLinkFile();
            Scanner in = new Scanner(new File(filename));
            while(in.hasNext()){
                programCode = programCode + in.nextLine() + '\n';
            }
            in.close();
            log.info("getDetailContestProgramSubmission, programCode = " + programCode);

            InputStream is = new FileInputStream(filename);
            //IOUtils.copyLarge(is, response.getOutputStream());
            IOUtils.copy(is, response.getOutputStream());
            response.flushBuffer();
            log.info("getDetailContestProgramSubmission, response.flushBuffer with filename = " + filename);
        }catch(Exception e){
            e.printStackTrace();
        }
        SubmittedProgramDetailModel submittedProgramDetailModel = new SubmittedProgramDetailModel();
        submittedProgramDetailModel.setProgramCode(programCode);

        //return ResponseEntity.ok().body(submittedProgramDetailModel);


    }

    @GetMapping("get-all-contest-program-submissions")
    public ResponseEntity<?> getAllContestProgramSubmissions(Principal principal){
        return ResponseEntity.ok().body(contestProgramSubmissionService.findAll());
    }
    @GetMapping("get-contest-user-problem-list")
    public ResponseEntity<?> getContestUserProblems(Principal principal){
        log.info("getContestUserProblems");
        List<ProgrammingContestUserRegistrationProblem> programmingContestUserRegistrationProblems = programmingContestUserRegistrationProblemRepo.findAll();
        return ResponseEntity.ok().body(programmingContestUserRegistrationProblems);
    }

    private String establishSubmissionContestUserProblemDir(String contestId, String userLoginId, String problemId){
        String rootDir = uploadConfigProperties.getRootPath() + "/" + uploadConfigProperties.getProgramSubmissionDataPath();
        String contestRootDir = rootDir + "/" + contestDir;
        File dir = new File(contestRootDir);
        if (!dir.exists()) {
            dir.mkdir();
        }
        contestRootDir = contestRootDir + "/" + contestId;
        dir = new File(contestRootDir);
        if (!dir.exists()) {
            dir.mkdir();
        }

        //String submissionDir = rootDir + "/" + userSubmissionDir;
        String submissionDir = contestRootDir + "/" + userSubmissionDir;
        dir = new File(submissionDir);
        if (!dir.exists()) {
            dir.mkdir();
        }

        String rootUserSubmissionDir = submissionDir + "/" + userLoginId;//principal.getName();
        dir = new File(rootUserSubmissionDir);
        if (!dir.exists()) {
            dir.mkdir();
        }

        String submissionProblemDir = rootUserSubmissionDir + "/" + problemId;

        dir = new File(submissionProblemDir);
        if (!dir.exists()) {
            dir.mkdir();
        }
        return submissionProblemDir;
    }
    private String establishSubmissionFullFilename(String submissionContestUserProblemDir,
                                                   String idTable, String filename){
        String submissionFilename = submissionContestUserProblemDir + "/" +
                                    idTable + "-" + filename;
        return submissionFilename;
    }
    @PostMapping("/upload-program")
    public ResponseEntity<?> uploadProgram(
        Principal principal, @RequestParam("inputJson") String inputJson,
        @RequestParam("file") MultipartFile file
    ) {
        String userLoginId = principal.getName();
        String contestId = defaultContest;// this should be replaced by considered contest

        //public ResponseEntity<?> updateFile(Principal principal, @RequestParam("files") MultipartFile[] files) {
        //UploadConfigProperties uploadProp = new UploadConfigProperties();
        System.out.println("::uploadProgram a program, inputJson = " +
                           inputJson +
                           " config dir  = " +
                           uploadConfigProperties.getProgramSubmissionDataPath());
        String returnMsg = "";
        int grade = 0;
        boolean isWindows = System.getProperty("os.name").toLowerCase().startsWith("windows");
        List<ProgramSubmissionItemOutput> programSubmissionItemOutputList = new ArrayList();
        Gson gson = new Gson();
        ProgramSubmissonModel programSubmissonModel = gson.fromJson(inputJson, ProgramSubmissonModel.class);
        String problemId = programSubmissonModel.getProblemId();
        List<ContestProblemTest> contestProblemTests = contestProblemTestRepo.findAllByProblemId(problemId);
        ContestProblem contestProblem = contestProblemService.findByProblemId(problemId);
        int nbTests = contestProblemTests.size();
        String rootDir = uploadConfigProperties.getRootPath() +
                         "/" +
                         uploadConfigProperties.getProgramSubmissionDataPath();
        /*
        String contestRootDir = rootDir + "/" + contestDir;
        File dir = new File(contestRootDir);
        if (!dir.exists()) {
            dir.mkdir();
        }
        contestRootDir = contestRootDir + "/" + contestId;
        if (!dir.exists()) {
            dir.mkdir();
        }

        //String submissionDir = rootDir + "/" + userSubmissionDir;
        String submissionDir = contestRootDir + "/" + userSubmissionDir;
        dir = new File(submissionDir);
        if (!dir.exists()) {
            dir.mkdir();
        }

        String rootUserSubmissionDir = submissionDir + "/" + userLoginId;//principal.getName();
        dir = new File(rootUserSubmissionDir);
        if (!dir.exists()) {
            dir.mkdir();
        }

        String submissionProblemDir = rootUserSubmissionDir + "/" + problemId;

        dir = new File(submissionProblemDir);
        if (!dir.exists()) {
            dir.mkdir();
        }
        */

        ProgrammingContestUserRegistrationProblem programmingContestUserRegistrationProblem = programmingContestUserRegistrationProblemRepo
            .findByContestIdAndUserLoginIdAndProblemId(contestId, userLoginId,problemId);
        if(programmingContestUserRegistrationProblem == null){
            programmingContestUserRegistrationProblem = new ProgrammingContestUserRegistrationProblem();
            programmingContestUserRegistrationProblem.setContestId(contestId);
            programmingContestUserRegistrationProblem.setUserLoginId(userLoginId);
            programmingContestUserRegistrationProblem.setProblemId(problemId);
            programmingContestUserRegistrationProblem.setPoints(0);
            programmingContestUserRegistrationProblem = programmingContestUserRegistrationProblemRepo.save(programmingContestUserRegistrationProblem);
        }


        String submissionContestUserProblemDir = establishSubmissionContestUserProblemDir(contestId,userLoginId,problemId);
        int totalmaxPoint = 0;
        try {
            ContestProgramSubmission contestProgramSubmission = new ContestProgramSubmission();
            contestProgramSubmission.setContestId(contestId);
            contestProgramSubmission.setProblemId(problemId);
            contestProgramSubmission.setSubmittedByUserLoginId(userLoginId);
            contestProgramSubmission.setCreatedStamp(new Date());

            contestProgramSubmission = contestProgramSubmissionRepo.save(contestProgramSubmission);

            InputStream inputStream = file.getInputStream();
            String filename = file.getOriginalFilename();
            Scanner in = new Scanner(inputStream);
            String content = "";//in.toString();

            while (in.hasNext()) {
                content = content + in.nextLine() + '\n';
            }
            System.out.println("::uploadProgram, content = " + content);

            // Modify this path, please note that on window's command prompt (cmd), \ is accepted but not /
            //String dir = "D:\\projects\\baseweb\\sscm\\baseweb\\src\\main\\java\\com\\hust\\baseweb\\applications\\education\\programsubmisson\\data\\";
            //dir = uploadConfigProperties.getRootPath() + uploadConfigProperties.getProgramSubmissionDataPath();

            //PrintWriter out = new PrintWriter(dir + filename);

            /*
            String submissionFilename = submissionProblemDir + "/" +
                                        contestProgramSubmission.getContestProgramSubmissionId().toString() + "-" + filename;
            */
            String submissionFilename = establishSubmissionFullFilename(submissionContestUserProblemDir,
                                                                        contestProgramSubmission.getContestProgramSubmissionId().toString(),filename);
            PrintWriter out = new PrintWriter(submissionFilename);
            out.print(content);
            out.close();

            contestProgramSubmission.setFullLinkFile(submissionFilename);
            contestProgramSubmission = contestProgramSubmissionRepo.save(contestProgramSubmission);

            System.out.println("::uploadProgram  " +
                               filename +
                               " save to " +
                               submissionFilename +
                               " OK, content = " +
                               content);

            ProcessBuilder processBuilder = new ProcessBuilder();
            String copyCMD = "copy";
            String bashCMD = "cmd.exe";
            String optionCMD = "/c";
            String optionCOPY = " /Y ";
            String deleteCMD = "del";
            //String pythonPath = "python.exe";
            String pythonPath = "python";
            if (isWindows) {

            } else {
                copyCMD = "cp";
                bashCMD = "sh";
                optionCMD = "-c";
                optionCOPY = " ";
                deleteCMD = "rm";
                pythonPath = "python";
            }

            //for (int i = 1; i <= nbTests; i++) {
            for (ContestProblemTest contestProblemTest : contestProblemTests) {
                File fi = new File(submissionContestUserProblemDir + "/input.txt");
                if (fi.exists()) {
                    if (fi.delete()) {
                        log.info("uploadProgram, deleted " + submissionContestUserProblemDir + "/input.txt");
                    } else {
                        log.info("uploadProgram, NOT deleted " + submissionContestUserProblemDir + "/input.txt");
                    }
                }
                File fo = new File(submissionContestUserProblemDir + "/output.txt");
                if (fo.exists()) {
                    if (fo.delete()) {
                        log.info("uploadProgram, deleted " + submissionContestUserProblemDir + "/output.txt");
                    } else {
                        log.info("uploadProgram, NOT deleted " + submissionContestUserProblemDir + "/output.txt");
                    }
                }
                File fr = new File(submissionContestUserProblemDir + "/result.txt");
                if (fr.exists()) {
                    if (fr.delete()) {
                        log.info("uploadProgram, deleted " + submissionContestUserProblemDir + "/result.txt");
                    } else {
                        log.info("uploadProgram, NOT deleted " + submissionContestUserProblemDir + "/result.txt");
                    }
                }


                String inpF = rootDir +
                              "/" +
                              problemDir +
                              "/" +
                              programSubmissonModel.getProblemId() +
                              "/" +
                              contestProblemTest.getProblemTestFilename() +
                              ".inp";
                String outF = rootDir +
                              "/" +
                              problemDir +
                              "/" +
                              programSubmissonModel.getProblemId() +
                              "/" +
                              contestProblemTest.getProblemTestFilename() +
                              ".out";

                String initCommand = deleteCMD + " input.txt output.txt result.txt";

                //String firstCommand = copyCMD + optionCOPY + i + ".inp " + "input.txt";
                //String firstCommand = copyCMD + optionCOPY + " " + inpF + " " + submissionContestUserProblemDir + "/input.txt";
                //log.info("uploadProgram, firstCommand = " + firstCommand);
                //String secondCommand = copyCMD + optionCOPY + i + ".out " + "output.txt";
                //String secondCommand = copyCMD + optionCOPY + " " + outF + " " + submissionContestUserProblemDir + "/output.txt";
                //log.info("uploadProgram, secondCommand = " + secondCommand);

                Files.copy((new File(inpF)).toPath(), (new File(submissionContestUserProblemDir + "/input.txt").toPath()));
                Files.copy((new File(outF)).toPath(), (new File(submissionContestUserProblemDir + "/output.txt").toPath()),
                           StandardCopyOption.REPLACE_EXISTING);

                //String thirdCommand = "py " + filename;
                //processBuilder.command(bashCMD, optionCMD, firstCommand + " && " + secondCommand + " && " + thirdCommand);

                //processBuilder.command(bashCMD, optionCMD, firstCommand + " && " + secondCommand);
                //if(isWindows)
                //    processBuilder.command(bashCMD, optionCMD,thirdCommand);
                //else
                //    processBuilder.command(thirdCommand);

                /*processBuilder.command("cmd.exe", "/c", "python " + filename);*/

                //processBuilder.command(bashCMD, optionCMD, initCommand + " && " + firstCommand +
                //                                           " && " + secondCommand + " && " + thirdCommand);

                /*processBuilder.command(bashCMD, optionCMD, thirdCommand); // Not use this line when run python file.*/
                //String pythonPath = "C:/Users/Asus/AppData/Local/Programs/Python/Python39/python.exe";

                //processBuilder = new ProcessBuilder(pythonPath, dir + "/" + filename);
                processBuilder = new ProcessBuilder(pythonPath, submissionFilename);
                //processBuilder.command("python",filename);
                //processBuilder.command("./python_exec", filename);

                //processBuilder.command(bashCMD, optionCMD, firstCommand + " && " + secondCommand);
                //if(isWindows)
                //    processBuilder.command(bashCMD, optionCMD,thirdCommand);
                //else
                //    processBuilder.command(thirdCommand);

                /*processBuilder.command("cmd.exe", "/c", "python " + filename);*/

                //String dir = System.getProperty("user.home");
                //System.out.println("System.getProperty(\"user.home\") = " + System.getProperty("user.home"));

                processBuilder.redirectErrorStream(true);
                processBuilder.directory(new File(submissionContestUserProblemDir));

                //File outputFile = new File(dir + "/out.txt");
                File outputFile = new File(submissionContestUserProblemDir + "/out.txt");
                processBuilder.redirectOutput(outputFile); // Redirect stdout to file.

                Process process = processBuilder.start();
                //boolean success = process.waitFor(2, TimeUnit.SECONDS);
                log.info("uploadProgram, time limit = " + contestProblem.getTimeLimit());
                boolean success = process.waitFor(contestProblem.getTimeLimit(), TimeUnit.MILLISECONDS);

                if (success == false) {
                    System.err.println("TIME LIMIT EXCEEDED");

                    // Kills process.
                    process.destroy();
                    if (process.isAlive()) {
                        process.destroyForcibly();
                    }
                } else {
                    int exitCode = process.exitValue();

                    if (exitCode == 0) {
                        System.out.println("\nSUCCESS");
                    } else {
                        System.err.println("PROCESS TERMINATES WITH EXIT CODE != 0");
                    }
                }

                // compare standard solution and submitted solution
                ProgramSubmissionItemOutput programSubmissionItemOutput = new ProgramSubmissionItemOutput();
                programSubmissionItemOutput.setTest("Test #" + contestProblemTest.getProblemTestFilename());
                Scanner stdOut = null;
                try {
                    //stdOut = new Scanner(new File(dir + "/output.txt"));
                    stdOut = new Scanner(new File(submissionContestUserProblemDir + "/output.txt"));
                    //Scanner rs = new Scanner(new File(dir + "/result.txt"));
                    Scanner rs = new Scanner(new File(submissionContestUserProblemDir + "/result.txt"));
                    int stdAns = stdOut.nextInt();
                    int submittedAns = rs.nextInt();
                    totalmaxPoint += contestProblemTest.getProblemTestPoint();
                    if (stdAns == submittedAns) {
                        grade += contestProblemTest.getProblemTestPoint();

                        programSubmissionItemOutput.setOutput(contestProblemTest.getProblemTestPoint() + "");
                    } else {
                        returnMsg += "Test #" +
                                     contestProblemTest.getProblemTestFilename() +
                                     ": expected " +
                                     stdAns +
                                     " while found " +
                                     submittedAns +
                                     "\n";
                        programSubmissionItemOutput.setOutput("Test #" +
                                                              contestProblemTest.getProblemTestFilename() +
                                                              ": expected " +
                                                              stdAns +
                                                              " while found " +
                                                              submittedAns);
                    }
                    System.out.println("stdAns = " +
                                       stdAns +
                                       " submittedAns = " +
                                       submittedAns +
                                       " grade = " +
                                       grade);
                    stdOut.close();
                    rs.close();
                } catch (Exception e) {
                    e.printStackTrace();
                    programSubmissionItemOutput.setOutput("Test #" +
                                                          contestProblemTest.getProblemTestFilename() +
                                                          " not found output");
                    if (stdOut != null) {
                        stdOut.close();
                    }
                }
                programSubmissionItemOutputList.add(programSubmissionItemOutput);
            }
            contestProgramSubmission.setPoints(grade);
            contestProgramSubmission = contestProgramSubmissionRepo.save(contestProgramSubmission);

            if(grade > programmingContestUserRegistrationProblem.getPoints()){
                programmingContestUserRegistrationProblem.setPoints(grade);
            }
            programmingContestUserRegistrationProblem.setLastPoints(grade);
            programmingContestUserRegistrationProblem = programmingContestUserRegistrationProblemRepo.save(programmingContestUserRegistrationProblem);

        } catch (Exception e) {
            e.printStackTrace();
        }

        returnMsg += " Points = " + grade + "/" + totalmaxPoint;
        ProgramSubmissionOutput programSubmissionOutput = new ProgramSubmissionOutput(
            programSubmissionItemOutputList,
            returnMsg);
        return ResponseEntity.ok().body(programSubmissionOutput);
    }


    public ResponseEntity<?> updateFileTMP(
        Principal principal, @RequestParam("inputJson") String inputJson,
        @RequestParam("file") MultipartFile file
    ) {
        //public ResponseEntity<?> updateFile(Principal principal, @RequestParam("files") MultipartFile[] files) {
        //UploadConfigProperties uploadProp = new UploadConfigProperties();
        System.out.println("::submit a program, inputJson = " +
                           inputJson +
                           " config dir  = " +
                           uploadConfigProperties.getProgramSubmissionDataPath());
        String returnMsg = "";
        int grade = 0;
        boolean isWindows = System.getProperty("os.name").toLowerCase().startsWith("windows");
        try {
            InputStream inputStream = file.getInputStream();
            String filename = file.getOriginalFilename();
            Scanner in = new Scanner(inputStream);
            String content = "";//in.toString();

            while (in.hasNext()) {
                content = content + in.nextLine() + '\n';
            }

            // Modify this path, please note that on window's command prompt (cmd), \ is accepted but not /
            //String dir = "D:\\projects\\baseweb\\sscm\\baseweb\\src\\main\\java\\com\\hust\\baseweb\\applications\\education\\programsubmisson\\data\\";
            String dir = uploadConfigProperties.getProgramSubmissionDataPath();

            PrintWriter out = new PrintWriter(dir + filename);

            out.print(content);
            out.close();
            //System.out.println("::uploadFile  " + filename + ", content = " + content);

            ProcessBuilder processBuilder = new ProcessBuilder();

            if (isWindows) {
                //processBuilder.command("cmd.exe","/c","dir");
                //processBuilder.command("cmd.exe","/c","java -cp . InputReader");
                //processBuilder.command("cmd.exe","/c","\"c:\\Program Files (x86)\\CodeBlocks\\MinGW\\bin\"\\g++ cbus.cpp");
                //processBuilder.command("cmd.exe","/c","c:\\Program Files (x86)\\CodeBlocks\\MinGW\\bin\\g++ cbus.cpp");
                //processBuilder.command("cmd.exe","/c","D:\\tmp\\g++.exe cbus.cpp");
                //processBuilder.command("cmd.exe","/c","gpp.exe cbus.cpp");
                //processBuilder.command("cmd.exe","/c","gpp.exe");
                //processBuilder.command("cmd.exe","/c","gpp");
                //processBuilder.command("cmd.exe","/c","a.exe");

                /*// Process only one input test.
                processBuilder.command("cmd.exe", "/c", "copy /Y " + "1.inp " + "input.txt");
                processBuilder.command("cmd.exe", "/c", "python " + filename);
                //String dir = System.getProperty("user.home");
                //System.out.println("System.getProperty(\"user.home\") = " + System.getProperty("user.home"));
                processBuilder.redirectErrorStream(true);
                processBuilder.directory(new File(dir));
                Process process = processBuilder.start();

                // The InputStream we get from the Process reads from the standard output of the process
                // (and also the standard error, by virtue of the line copyFiles.redirectErrorStream(true)).
                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String line;

                do {
                    line = reader.readLine();
                    if (line != null) {
                        System.out.println(line);
                    }
                } while (line != null);

                reader.close();

                int exitCode = process.waitFor();
                System.out.println("process start OK and return exit code = " + exitCode);*/

                // StreamGobbler should only be used with Java 6 and earlier.

                //StreamGobbler streamGobbler = new StreamGobbler(process.getInputStream(),System.out::println);
                //StreamGobbler streamGobbler = new StreamGobbler(
                //    process.getInputStream(),
                //    StreamGobbler::processInformation);

                //Executors.newSingleThreadExecutor().submit(streamGobbler);
                //int exitCode = process.waitFor();

                //assert exitCode == 0;

                //cho chay nhieu test

                for (int i = 1; i < 4; i++) {
                    String firstCommand = "copy /Y " + i + ".inp " + "input.txt";
                    String secondCommand = "copy /Y " + i + ".out " + "output.txt";
                    String thirdCommand = "python " + filename;
                    processBuilder.command(
                        "cmd.exe",
                        "/c",
                        firstCommand + " && " + secondCommand + " && " + thirdCommand);
                    /*processBuilder.command("cmd.exe", "/c", "python " + filename);*/

                    //String dir = System.getProperty("user.home");
                    //System.out.println("System.getProperty(\"user.home\") = " + System.getProperty("user.home"));

                    processBuilder.redirectErrorStream(true);
                    processBuilder.directory(new File(dir));

                    Process process = processBuilder.start();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                    String line;

                    do {
                        line = reader.readLine();
                        if (line != null) {
                            System.out.println(line);
                        }
                    } while (line != null);

                    reader.close();

                    //int exitCode = process.waitFor();
                    long timeOut = 1000;
                    process.wait(timeOut);

                    //System.out.println("process start OK and return exit code = " + exitCode);

                    // compare standard solution and submitted solution
                    try {
                        Scanner stdOut = new Scanner(new File(dir + "output.txt"));
                        Scanner rs = new Scanner(new File(dir + "result.txt"));
                        int stdAns = stdOut.nextInt();
                        int submittedAns = rs.nextInt();

                        if (stdAns == submittedAns) {
                            grade += 10;
                        } else {
                            returnMsg += "Test #" +
                                         i +
                                         ": expected " +
                                         stdAns +
                                         " while found " +
                                         submittedAns +
                                         "\n";
                        }
                        System.out.println("stdAns = " +
                                           stdAns +
                                           " submittedAns = " +
                                           submittedAns +
                                           " grade = " +
                                           grade);
                        stdOut.close();
                        rs.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else {

                processBuilder.command("sh", "-c", "ls");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        returnMsg += " Points = " + grade;
        return ResponseEntity.ok().body(returnMsg);
    }

}
