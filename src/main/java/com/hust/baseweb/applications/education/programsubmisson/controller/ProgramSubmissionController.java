package com.hust.baseweb.applications.education.programsubmisson.controller;

import com.hust.baseweb.applications.education.programsubmisson.model.ProgramSubmissonModel;
import com.hust.baseweb.framework.properties.UploadConfigProperties;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.security.Principal;
import java.util.Scanner;
import java.util.function.Consumer;

@Log4j2
@Controller
//@RequestMapping("/edu/programsubmission")
@AllArgsConstructor(onConstructor_ = @Autowired)
@CrossOrigin
public class ProgramSubmissionController {
    @Autowired
    UploadConfigProperties uploadConfigProperties;

    @PostMapping("/submit")
    public ResponseEntity<?> submit(Principal principal, @RequestBody ProgramSubmissonModel input) {
        System.out.println(input.getProgram());
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

    @PostMapping("/upload-program")
    public ResponseEntity<?> updateFile(Principal principal, @RequestParam("inputJson") String inputJson,
                                        @RequestParam("file") MultipartFile file) {
    //public ResponseEntity<?> updateFile(Principal principal, @RequestParam("files") MultipartFile[] files) {
        //UploadConfigProperties uploadProp = new UploadConfigProperties();
        System.out.println("::updateFile a program, inputJson = " + inputJson + " config dir  = " + uploadConfigProperties.getProgramSubmissionDataPath());
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
            System.out.println("::updateFile, content = " + content);

            // Modify this path, please note that on window's command prompt (cmd), \ is accepted but not /
            //String dir = "D:\\projects\\baseweb\\sscm\\baseweb\\src\\main\\java\\com\\hust\\baseweb\\applications\\education\\programsubmisson\\data\\";
            String dir = uploadConfigProperties.getRootPath() + uploadConfigProperties.getProgramSubmissionDataPath();

            PrintWriter out = new PrintWriter(dir + filename);

            out.print(content);
            out.close();
            System.out.println("::uploadFile  " + filename + " save to " + dir + filename + " OK, content = " + content);

            ProcessBuilder processBuilder = new ProcessBuilder();
            String copyCMD = "copy";
            String bashCMD = "cmd.exe";
            String optionCMD = "/c";
            String optionCOPY = " /Y ";
            if (isWindows) {

            }else{
                copyCMD = "cp";
                bashCMD = "sh";
                optionCMD = "-c";
                optionCOPY = " ";
            }
            int nbTests = 3;
            for (int i = 1; i <= nbTests; i++) {
                String firstCommand = copyCMD + optionCOPY + i + ".inp " + "input.txt";
                String secondCommand = copyCMD + optionCOPY + i + ".out " + "output.txt";
                String thirdCommand = "python " + filename;
                processBuilder.command(bashCMD, optionCMD, firstCommand + " && " + secondCommand + " && " + thirdCommand);
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

                int exitCode = process.waitFor();
                System.out.println("process start OK and return exit code = " + exitCode);

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

        } catch (Exception e) {
            e.printStackTrace();
        }
        returnMsg += " Points = " + grade;
        return ResponseEntity.ok().body(returnMsg);
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

                    int exitCode = process.waitFor();
                    System.out.println("process start OK and return exit code = " + exitCode);

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
