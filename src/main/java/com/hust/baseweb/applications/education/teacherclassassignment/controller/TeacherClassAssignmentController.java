package com.hust.baseweb.applications.education.teacherclassassignment.controller;

import com.hust.baseweb.applications.education.teacherclassassignment.model.AlgoTeacherAssignmentIM;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
public class TeacherClassAssignmentController {
    @PostMapping("/teacherclassassignment/algo")
    public ResponseEntity<?> computeTeacherClassAssignment(Principal principal, @RequestBody
        AlgoTeacherAssignmentIM input
                                                           ){
        //TODO by TuanLA
        return null;
    }
}
