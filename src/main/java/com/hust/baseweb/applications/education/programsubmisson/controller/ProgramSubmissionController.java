package com.hust.baseweb.applications.education.programsubmisson.controller;

import com.google.gson.Gson;
import com.hust.baseweb.applications.education.programsubmisson.entity.ContestProblem;
import com.hust.baseweb.applications.education.programsubmisson.entity.ContestProblemTest;
import com.hust.baseweb.applications.education.programsubmisson.model.*;
import com.hust.baseweb.applications.education.programsubmisson.repo.ContestProblemRepo;
import com.hust.baseweb.applications.education.programsubmisson.repo.ContestProblemTestRepo;
import com.hust.baseweb.applications.education.programsubmisson.service.ContestProblemService;
import com.hust.baseweb.framework.properties.UploadConfigProperties;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;

import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
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

    @Autowired
    UploadConfigProperties uploadConfigProperties;
    @Autowired
    private ContestProblemService contestProblemService;
    @Autowired
    private ContestProblemTestRepo contestProblemTestRepo;
    @Autowired
    private ContestProblemRepo contestProblemRepo;

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

    @GetMapping("get-contest-problem-test-list/{problemId}")
    public ResponseEntity<?> getContestProblemTestList(Principal principal, @PathVariable String problemId){
        List<ContestProblemTest> contestProblemTests = contestProblemService.findAllContestProblemTestByProblemId(problemId);
        log.info("getContestProblemTestList, GOT " + contestProblemTests.size() + " items");
        return ResponseEntity.ok().body(contestProblemTests);
    }
    @PostMapping("/create-contest-problem-test")
    public ResponseEntity<?> createContestProblemTest(Principal principal, @RequestParam("inputJson") String inputJson,
                                                      @RequestParam("files") MultipartFile[] files){
        Gson gson = new Gson();
        CreateContestProblemTestInputModel createContestProblemTestInputModel = gson.fromJson(inputJson, CreateContestProblemTestInputModel.class);
        //String rootDir = uploadConfigProperties.getRootPath() + uploadConfigProperties.getProgramSubmissionDataPath();
        //String problemDir = rootDir +
        log.info("createContestProblemTest, inputJson = " + inputJson);

        ContestProblemTest contestProblemTest = contestProblemService.createContestProblemTest(createContestProblemTestInputModel, files);

        return ResponseEntity.ok().body(contestProblemTest);
    }

    @PostMapping("/create-contest-problem")
    public ResponseEntity<?> createContestProblem(Principal principal, @RequestBody ContestProblemInputModel input){
        ContestProblem contestProblem = contestProblemService.save(input);
        return ResponseEntity.ok().body(contestProblem);
    }
    @GetMapping("get-contest-problem/{problemId}")
    public ResponseEntity<?> getContestProblem(Principal principal, @PathVariable String problemId){
        log.info("getContestProblem, problemId = " + problemId);
        ContestProblem contestProblem = contestProblemService.findByProblemId(problemId);
        return ResponseEntity.ok().body(contestProblem);
    }
    @GetMapping("contest-problem-list")
    public ResponseEntity<?> getAllContestProblems(Principal principal){
        log.info("getAllContestProblems....user = " + principal.getName());
        List<ContestProblem> contestProblems = contestProblemService.findAll();
        return ResponseEntity.ok().body(contestProblems);
    }

    @PostMapping("/upload-program")
    public ResponseEntity<?> uploadProgram(Principal principal, @RequestParam("inputJson") String inputJson,
                                        @RequestParam("file") MultipartFile file) {
    //public ResponseEntity<?> updateFile(Principal principal, @RequestParam("files") MultipartFile[] files) {
        //UploadConfigProperties uploadProp = new UploadConfigProperties();
        System.out.println("::uploadProgram a program, inputJson = " + inputJson + " config dir  = " + uploadConfigProperties.getProgramSubmissionDataPath());
        String returnMsg = "";
        int grade = 0;
        boolean isWindows = System.getProperty("os.name").toLowerCase().startsWith("windows");
        List<ProgramSubmissionItemOutput> programSubmissionItemOutputList = new ArrayList();
        Gson gson = new Gson();
        ProgramSubmissonModel programSubmissonModel = gson.fromJson(inputJson,ProgramSubmissonModel.class);
        List<ContestProblemTest> contestProblemTests = contestProblemTestRepo.findAllByProblemId(programSubmissonModel.getProblemId());

        int nbTests = contestProblemTests.size();
        String rootDir = uploadConfigProperties.getRootPath() + "/" + uploadConfigProperties.getProgramSubmissionDataPath();
        String submissionDir = rootDir + "/" + userSubmissionDir;
        File dir = new File(submissionDir);
        if(!dir.exists()){
            dir.mkdir();
        }

        String rootUserSubmissionDir = submissionDir + "/" + principal.getName();
        dir = new File(rootUserSubmissionDir);
        if(!dir.exists()){
            dir.mkdir();
        }

        try {
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
            String submissionFilename = rootUserSubmissionDir + "/" + filename;
            PrintWriter out = new PrintWriter(submissionFilename);
            out.print(content);
            out.close();

            System.out.println("::uploadProgram  " + filename + " save to " + submissionFilename + " OK, content = " + content);

            ProcessBuilder processBuilder = new ProcessBuilder();
            String copyCMD = "copy";
            String bashCMD = "cmd.exe";
            String optionCMD = "/c";
            String optionCOPY = " /Y ";
            String deleteCMD = "del";
            if (isWindows) {

            }else{
                copyCMD = "cp";
                bashCMD = "sh";
                optionCMD = "-c";
                optionCOPY = " ";
                deleteCMD = "rm";
            }

            //for (int i = 1; i <= nbTests; i++) {
            for(ContestProblemTest contestProblemTest: contestProblemTests){
                File fi = new File(rootUserSubmissionDir + "/input.txt");
                if(fi.exists()){
                    if(fi.delete())
                        log.info("uploadProgram, deleted " + rootUserSubmissionDir + "/input.txt");
                    else
                        log.info("uploadProgram, NOT deleted " + rootUserSubmissionDir + "/input.txt");
                }
                File fo = new File(rootUserSubmissionDir + "/output.txt");
                if(fo.exists()){
                    if(fo.delete())
                        log.info("uploadProgram, deleted " + rootUserSubmissionDir + "/output.txt");
                    else
                        log.info("uploadProgram, NOT deleted " + rootUserSubmissionDir + "/output.txt");
                }
                File fr = new File(rootUserSubmissionDir + "/result.txt");
                if(fr.exists()){
                    if(fr.delete())
                        log.info("uploadProgram, deleted " + rootUserSubmissionDir + "/result.txt");
                    else
                        log.info("uploadProgram, NOT deleted " + rootUserSubmissionDir + "/result.txt");
                }



                String inpF = rootDir + "/" + problemDir + "/" + programSubmissonModel.getProblemId() + "/" + contestProblemTest.getProblemTestFilename() + ".inp";
                String outF = rootDir + "/" + problemDir + "/" + programSubmissonModel.getProblemId() + "/" + contestProblemTest.getProblemTestFilename() + ".out";

                String initCommand = deleteCMD + " input.txt output.txt result.txt";

                //String firstCommand = copyCMD + optionCOPY + i + ".inp " + "input.txt";
                String firstCommand = copyCMD + optionCOPY + " " + inpF + " " + rootUserSubmissionDir + "/input.txt";
                log.info("uploadProgram, firstCommand = " + firstCommand);
                //String secondCommand = copyCMD + optionCOPY + i + ".out " + "output.txt";
                String secondCommand = copyCMD + optionCOPY + " " + outF + " " + rootUserSubmissionDir + "/output.txt";
                log.info("uploadProgram, secondCommand = " + secondCommand);

                Files.copy((new File(inpF)).toPath(), (new File(rootUserSubmissionDir + "/input.txt").toPath()));
                Files.copy((new File(outF)).toPath(), (new File(rootUserSubmissionDir + "/output.txt").toPath()),
                           StandardCopyOption.REPLACE_EXISTING);

                String thirdCommand = "python " + filename;
                //processBuilder.command(bashCMD, optionCMD, firstCommand + " && " + secondCommand + " && " + thirdCommand);

                //processBuilder.command(bashCMD, optionCMD, firstCommand + " && " + secondCommand);
                //if(isWindows)
                //    processBuilder.command(bashCMD, optionCMD,thirdCommand);
                //else
                //    processBuilder.command(thirdCommand);
                
                /*processBuilder.command("cmd.exe", "/c", "python " + filename);*/

                //processBuilder.command(bashCMD, optionCMD, initCommand + " && " + firstCommand +
                //                                           " && " + secondCommand + " && " + thirdCommand);

                processBuilder.command(bashCMD, optionCMD, thirdCommand);

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
                processBuilder.directory(new File(rootUserSubmissionDir));

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

                process.waitFor(1, TimeUnit.SECONDS);
                //process.waitFor(5, TimeUnit.SECONDS);  // let the process run for 5 seconds
                process.destroy();                     // tell the process to stop
                process.waitFor(2, TimeUnit.SECONDS); // give it a chance to stop
                process.destroyForcibly();             // tell the OS to kill the process
                process.waitFor();

                System.out.println("Process Ended after timelimit");
                                               //System.out.println("process start OK and return exit code = " + exitCode);
                                               //process.wait(1000);

                                               // compare standard solution and submitted solution
                ProgramSubmissionItemOutput programSubmissionItemOutput = new ProgramSubmissionItemOutput();
                programSubmissionItemOutput.setTest("Test #" + contestProblemTest.getProblemTestFilename());
                Scanner stdOut = null;
                try{
                    stdOut = new Scanner(new File(dir + "/output.txt"));
                    Scanner rs = new Scanner(new File(dir + "/result.txt"));
                    int stdAns = stdOut.nextInt();
                    int submittedAns = rs.nextInt();

                    if(stdAns == submittedAns){
                        grade += contestProblemTest.getProblemTestPoint();
                        programSubmissionItemOutput.setOutput(contestProblemTest.getProblemTestPoint() + "");
                    }else{
                        returnMsg += "Test #" + contestProblemTest.getProblemTestFilename() + ": expected "  + stdAns + " while found " + submittedAns + "\n";
                        programSubmissionItemOutput.setOutput("Test #" + contestProblemTest.getProblemTestFilename() + ": expected "  + stdAns + " while found " + submittedAns);
                    }
                    System.out.println("stdAns = " + stdAns + " submittedAns = " + submittedAns + " grade = " + grade);
                    stdOut.close();
                    rs.close();
                }catch(Exception e){
                    e.printStackTrace();
                    programSubmissionItemOutput.setOutput("Test #" + contestProblemTest.getProblemTestFilename() + " not found output");
                    if(stdOut != null){
                        stdOut.close();
                    }
                }
                programSubmissionItemOutputList.add(programSubmissionItemOutput);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        returnMsg += " Points = " + grade + "/" + (nbTests*10);
        ProgramSubmissionOutput programSubmissionOutput = new ProgramSubmissionOutput(programSubmissionItemOutputList,returnMsg);
        return ResponseEntity.ok().body(programSubmissionOutput);
    }


    public ResponseEntity<?> updateFileTMP(Principal principal, @RequestParam("inputJson") String inputJson,
                                        @RequestParam("file") MultipartFile file) {
        //public ResponseEntity<?> updateFile(Principal principal, @RequestParam("files") MultipartFile[] files) {
        //UploadConfigProperties uploadProp = new UploadConfigProperties();
        System.out.println("::submit a program, inputJson = " + inputJson + " config dir  = " + uploadConfigProperties.getProgramSubmissionDataPath());
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
                    processBuilder.command("cmd.exe", "/c", firstCommand + " && " + secondCommand + " && " + thirdCommand);
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
                    try{
                        Scanner stdOut = new Scanner(new File(dir + "output.txt"));
                        Scanner rs = new Scanner(new File(dir + "result.txt"));
                        int stdAns = stdOut.nextInt();
                        int submittedAns = rs.nextInt();

                        if(stdAns == submittedAns){
                            grade += 10;
                        }else{
                            returnMsg += "Test #" + i + ": expected "  + stdAns + " while found " + submittedAns + "\n";
                        }
                        System.out.println("stdAns = " + stdAns + " submittedAns = " + submittedAns + " grade = " + grade);
                        stdOut.close();
                        rs.close();
                    }catch(Exception e){
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
