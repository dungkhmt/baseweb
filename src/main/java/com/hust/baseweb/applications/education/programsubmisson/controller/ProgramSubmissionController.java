package com.hust.baseweb.applications.education.programsubmisson.controller;

import com.hust.baseweb.applications.education.programsubmisson.model.ProgramSubmissonModel;
import localsearch.domainspecific.vehiclerouting.vrp.utils.ScannerInput;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.tomcat.jni.Proc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.swing.text.html.parser.Entity;
import java.io.*;
import java.security.Principal;
import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

@Log4j2
@Controller
//@RequestMapping("/edu/programsubmission")
@AllArgsConstructor(onConstructor_ = @Autowired)
@CrossOrigin
public class ProgramSubmissionController {

    @PostMapping("/submit")
    public ResponseEntity<?> submit(Principal principal, @RequestBody ProgramSubmissonModel input){
        System.out.println(input.getProgram());
        return ResponseEntity.ok().body("OK");
    }
    private static class StreamGobbler implements Runnable{
        private InputStream inputStream;
        private Consumer<String> consumer;
        public StreamGobbler(InputStream inputStream, Consumer<String> consumer){
            this.inputStream = inputStream;
            this.consumer = consumer;
        }
        @Override
        public void run(){
            new BufferedReader(new InputStreamReader(inputStream)).lines().forEach(consumer);
        }
        static void processInformation(String info){
            //System.out.println("processInformation, info = " + info);
            System.out.println(info);
            try{
                Scanner in = new Scanner(new File("D:/projects/baseweb/data/output.txt"));
                String rs = in.nextLine();
                if(rs.trim().equals(info.trim())){
                    System.out.println("SUCCESS!!!!");
                }else{
                    System.out.println("FAILED!!!!");
                }
                in.close();
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    @PostMapping("/upload-program")
    public ResponseEntity<?> updateFile(Principal principal, @RequestParam("file")MultipartFile file){
        System.out.println("::submit a program...");
        boolean isWindows = System.getProperty("os.name")
                                  .toLowerCase().startsWith("windows");

        try {
            InputStream inputStream = file.getInputStream();
            String filename = file.getOriginalFilename();
            Scanner in = new Scanner(inputStream);
            String content = "";//in.toString();
            while(in.hasNext()){
                content = content + in.nextLine() + '\n';
            }

            PrintWriter out = new PrintWriter("D:/projects/baseweb/data/" + filename);
            out.print(content);
            out.close();
            //System.out.println("::uploadFile  " + filename + ", content = " + content);

            ProcessBuilder processBuilder = new ProcessBuilder();
            if(isWindows){
                //processBuilder.command("cmd.exe","/c","dir");
                //processBuilder.command("cmd.exe","/c","java -cp . InputReader");
                //processBuilder.command("cmd.exe","/c","\"c:\\Program Files (x86)\\CodeBlocks\\MinGW\\bin\"\\g++ cbus.cpp");
                //processBuilder.command("cmd.exe","/c","c:\\Program Files (x86)\\CodeBlocks\\MinGW\\bin\\g++ cbus.cpp");
                //processBuilder.command("cmd.exe","/c","D:\\tmp\\g++.exe cbus.cpp");
                //processBuilder.command("cmd.exe","/c","gpp.exe cbus.cpp");
                //processBuilder.command("cmd.exe","/c","gpp.exe");
                //processBuilder.command("cmd.exe","/c","gpp");
                //processBuilder.command("cmd.exe","/c","a.exe");

                // process only one input test
                //processBuilder.command("cmd.exe", "/c","copy 1.inp input.txt");
                processBuilder.command("cmd.exe", "/c", "python " + filename);
                //String dir = System.getProperty("user.home");
                String dir = "D:/projects/baseweb/data/";
                //System.out.println("System.getProperty(\"user.home\") = " + System.getProperty("user.home"));

                processBuilder.directory(new File(dir));
                Process process = processBuilder.start();
                //System.out.println("process start OK");
                //StreamGobbler streamGobbler = new StreamGobbler(process.getInputStream(),System.out::println);
                StreamGobbler streamGobbler = new StreamGobbler(process.getInputStream(),StreamGobbler::processInformation);
                Executors.newSingleThreadExecutor().submit(streamGobbler);
                int exitCode = process.waitFor();

                assert exitCode == 0;


                /* cho chay nhieu test
                for(int i = 1; i <= 3; i++) {
                    // ?? lenh copy nay khong chay
                    processBuilder.command("cmd.exe", "/c","copy D:/projects/baseweb/data/" + i + ".inp D:/projects/baseweb/data/input.txt");
                    processBuilder.command("cmd.exe", "/c","copy D:/projects/baseweb/data/" + i + ".out D:/projects/baseweb/data/output.txt");

                    processBuilder.command("cmd.exe", "/c", "python " + filename);
                    //String dir = System.getProperty("user.home");
                    String dir = "D:/projects/baseweb/data/";
                    //System.out.println("System.getProperty(\"user.home\") = " + System.getProperty("user.home"));

                    processBuilder.directory(new File(dir));
                    Process process = processBuilder.start();
                    //System.out.println("process start OK");
                    //StreamGobbler streamGobbler = new StreamGobbler(process.getInputStream(),System.out::println);
                    StreamGobbler streamGobbler = new StreamGobbler(process.getInputStream(),StreamGobbler::processInformation);
                    Executors.newSingleThreadExecutor().submit(streamGobbler);
                    int exitCode = process.waitFor();

                    assert exitCode == 0;
                }
                */

            }else{
                processBuilder.command("sh","-c","ls");
            }

        }catch(Exception e){
            e.printStackTrace();
        }
        return ResponseEntity.ok().body("OK");
    }
}
