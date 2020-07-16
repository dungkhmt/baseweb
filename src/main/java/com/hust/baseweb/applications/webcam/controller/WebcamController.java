package com.hust.baseweb.applications.webcam.controller;

import com.hust.baseweb.applications.webcam.dto.WebcamVideoDto;
import com.hust.baseweb.applications.webcam.service.WebcamService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;

/**
 * @author Hien Hoang (hienhoang2702@gmail.com)
 */
@RestController
@CrossOrigin
@AllArgsConstructor(onConstructor = @__(@Autowired))
@RequestMapping("/webcam")
@Log4j2
public class WebcamController {

    private final WebcamService webcamService;

    @PostMapping("/upload")
    public ResponseEntity<WebcamVideoDto> upload(
        @RequestPart("file") MultipartFile multipartFile,
        Principal principal
    ) {
        return ResponseEntity.ok(webcamService.upload(multipartFile, principal.getName()));
    }

    @GetMapping("/get")
    public ResponseEntity<InputStreamResource> get(String objectId) {
        return ResponseEntity.ok(webcamService.get(new ObjectId(objectId)));
    }

    @GetMapping("/all")
    public ResponseEntity<List<WebcamVideoDto>> all() {
        return ResponseEntity.ok(webcamService.all());
    }

    @GetMapping("/all-by-user-login-id")
    public ResponseEntity<List<WebcamVideoDto>> allByUserLoginId(@RequestParam String userLoginId) {
        return ResponseEntity.ok(webcamService.all(userLoginId));
    }
}
