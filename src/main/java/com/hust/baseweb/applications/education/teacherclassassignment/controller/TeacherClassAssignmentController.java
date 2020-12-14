package com.hust.baseweb.applications.education.teacherclassassignment.controller;

import com.hust.baseweb.applications.education.teacherclassassignment.model.AlgoTeacherAssignmentIM;
import com.hust.baseweb.applications.education.teacherclassassignment.model.TeacherClassAssignmentOM;
import com.hust.baseweb.applications.education.teacherclassassignment.service.TeacherClassAssignmentAlgoService;
import com.hust.baseweb.applications.education.teacherclassassignment.service.TeacherClassAssignmentService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.security.Principal;

@Log4j2
@Controller
@Validated
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class TeacherClassAssignmentController {

    private TeacherClassAssignmentAlgoService algoService;

    private TeacherClassAssignmentService assignmentService;

    @PostMapping("/teacherclassassignment/algo")
    public ResponseEntity<?> computeTeacherClassAssignment(
        Principal principal, @RequestBody
        AlgoTeacherAssignmentIM input
    ) {
        System.out.println("computeTeacherClassAssignment start");
        TeacherClassAssignmentOM teacherClassAssignmentOM = algoService.computeTeacherClassAssignment(
            input);

        return ResponseEntity.ok().body(teacherClassAssignmentOM);
    }

    @PostMapping("/teacher-class-assignment/mip")
    public ResponseEntity<?> assign(@RequestBody AlgoTeacherAssignmentIM input) {
        return ResponseEntity.ok().body(assignmentService.assign(input));
    }
}
