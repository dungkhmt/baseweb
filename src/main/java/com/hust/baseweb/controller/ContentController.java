package com.hust.baseweb.controller;

import java.io.IOException;

import com.hust.baseweb.entity.Content;
import com.hust.baseweb.service.ContentService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import okhttp3.Response;

@RestController
@RequestMapping("/content")
public class ContentController {
    @Autowired
    private ContentService contentService;

    @PostMapping("/")
    public ResponseEntity<String> createContent(@RequestParam("file") MultipartFile file) {
        Content content = null;
        try {
            content = contentService.createContent(file.getInputStream(), file.getOriginalFilename(),
                    file.getContentType());
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(content.getContentId().toString());
    }

    @GetMapping("/{contentId}")
    public ResponseEntity<?> get(@PathVariable String contentId) {

        try {
            Response response = contentService.getContentData(contentId);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType(response.header("Content-Type")));
            return new ResponseEntity<>(response.body().bytes(), headers, HttpStatus.OK);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }

    }
}