package com.hust.baseweb.applications.postsys.controller;

import com.hust.baseweb.applications.postsys.entity.PostOffice;
import com.hust.baseweb.applications.postsys.model.postoffice.CreatePostOfficeInputModel;
import com.hust.baseweb.applications.postsys.service.PostOfficeService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@Log4j2
public class PostAPIController {
    @Autowired
    private PostOfficeService postOfficeService;

    @PostMapping("/create-post-office")
    public ResponseEntity<?> createPostOffice(Principal principal, @RequestBody CreatePostOfficeInputModel input) {
        PostOffice newPostOffice = postOfficeService.save(input);
        log.info("createPostOffice, new post office name = " + newPostOffice.getPostOfficeName());
        return ResponseEntity.ok().body(newPostOffice);
    }

    @GetMapping("/get-all-post-office")
    public ResponseEntity<?> getAllPostOffice(Principal principal) {
        List<PostOffice> result = postOfficeService.findAll();
        log.info("getAllPostOffice, " + result.size() + " item(s) sent.");
        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/get-post-office-by-id/{postOfficeId}")
    public ResponseEntity<?> getPostOfficeById(Principal principal, @PathVariable String postOfficeId) {
        PostOffice result = postOfficeService.findByPostOfficeId(postOfficeId);
        log.info("getPostOfficeById = " + postOfficeId);
        return ResponseEntity.ok().body(result);
    }

    @DeleteMapping("/delete-post-office/{postOfficeId}")
    public ResponseEntity<?> deletePostOfficeById(Principal principal, @PathVariable String postOfficeId) {
        postOfficeService.deleteByPostOfficeId(postOfficeId);
        log.info("deletePostOfficeById = " + postOfficeId);
        return ResponseEntity.ok().body(null);
    }
}
