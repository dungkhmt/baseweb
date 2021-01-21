package com.hust.baseweb.applications.postsys.controller;

import com.hust.baseweb.applications.postsys.entity.PostDriverPostOfficeAssignment;
import com.hust.baseweb.applications.postsys.entity.PostFixedTrip;
import com.hust.baseweb.applications.postsys.model.postdriver.UpdatePostDriverPostOfficeAssignmentInputModel;
import com.hust.baseweb.applications.postsys.model.posttrip.ExecuteTripInputModel;
import com.hust.baseweb.applications.postsys.service.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

@Controller
@Log4j2(topic = "POST_LOG")
public class PostTripController {

    @Autowired
    private PostOfficeService postOfficeService;
    @Autowired
    private PostCustomerService postCustomerService;
    @Autowired
    private PostOrderService postOrderService;
    @Autowired
    PostPackageTypeService postPackageTypeService;
    @Autowired
    PostTripService postTripService;
    @Autowired
    PostmanService postmanService;
    @Autowired
    private PostDriverService postDriverService;


    @DeleteMapping("/delete-post-driver-post-office-assignment")
    public ResponseEntity deletePostDriverPostOfficeAssignment(
        @RequestBody
            UpdatePostDriverPostOfficeAssignmentInputModel updatePostDriverPostOfficeAssignmentInputModel
    ) {
        return ResponseEntity
            .ok()
            .body(postDriverService.deletePostDriverPostOfficeAssignment(updatePostDriverPostOfficeAssignmentInputModel));
    }

    @GetMapping("/get-office-assignment-list-by-post-driver/{postDriverId}")
    public ResponseEntity getPostTripListByPostDriver(@PathVariable String postDriverId) {
        List<PostDriverPostOfficeAssignment> postDriverPostOfficeAssignments = postTripService.findAllPostOfficeAssignmentByPostDriver(
            UUID.fromString(postDriverId));
        return ResponseEntity.ok().body(postDriverPostOfficeAssignments);
    }

    @PostMapping(("/add-post-driver-post-office-assignment"))
    public ResponseEntity addPostDriverPostOfficeAssignment(
        @RequestBody
            UpdatePostDriverPostOfficeAssignmentInputModel updatePostDriverPostOfficeAssignmentInputModel
    ) {
        return ResponseEntity
            .ok()
            .body(postDriverService.addPostDriverPostOfficeAssignment(updatePostDriverPostOfficeAssignmentInputModel));
    }

    @DeleteMapping("/delete-execute-trip")
    public ResponseEntity deletePostTripExecute(@RequestBody ExecuteTripInputModel executeTripInputModel) {
        return ResponseEntity.ok().body(postTripService.deletePostTripExecute(executeTripInputModel));
    }
}
