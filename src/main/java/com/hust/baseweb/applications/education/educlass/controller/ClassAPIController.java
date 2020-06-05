package com.hust.baseweb.applications.education.educlass.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@Log4j2
public class ClassAPIController {


    @PostMapping("/class/exercises/upload")
    public ResponseEntity uploadExercise(@RequestParam("file") MultipartFile multipartFile){
        try{
            if(multipartFile.isEmpty()){
                return new ResponseEntity("files missing", HttpStatus.OK);
            }
            byte[] bytes = multipartFile.getBytes();
            Path path = Paths.get("D:/tmp/" + multipartFile.getOriginalFilename());
            Files.write(path,bytes);
            return new ResponseEntity("upload successfully", HttpStatus.OK);
        }catch(Exception e){
            e.printStackTrace();
            return new ResponseEntity("upload failed", HttpStatus.OK);
        }
    }
}
