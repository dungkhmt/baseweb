package com.hust.baseweb.applications.education.classmanagement.controller;

import com.hust.baseweb.applications.education.classmanagement.service.IStorage;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Log4j2
@Controller
@Validated
@RequestMapping("/edu/content")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class FileController {

    IStorage storage;

    @PostMapping
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file) {
        storage.uploadToOneDrive(file);
        return ResponseEntity.ok().body("Uploaded");
    }
}
