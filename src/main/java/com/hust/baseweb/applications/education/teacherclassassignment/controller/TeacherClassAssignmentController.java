package com.hust.baseweb.applications.education.teacherclassassignment.controller;

import com.hust.baseweb.applications.education.teacherclassassignment.model.AlgoTeacherAssignmentIM;
import com.hust.baseweb.applications.education.teacherclassassignment.model.TeacherClassAssignmentOM;
import com.hust.baseweb.applications.education.teacherclassassignment.service.TeacherClassAssignmentAlgoService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@Log4j2
@Controller
@Validated
@AllArgsConstructor(onConstructor = @__(@Autowired))
@RestController
public class TeacherClassAssignmentController {
    @Autowired
    private TeacherClassAssignmentAlgoService teacherClassAssignmentAlgoService;

    @PostMapping("/teacherclassassignment/algo")
    public ResponseEntity<?> computeTeacherClassAssignment(Principal principal, @RequestBody
        AlgoTeacherAssignmentIM input
                                                           ){
        //TODO by TuanLA
        System.out.println("computeTeacherClassAssignment start");
        TeacherClassAssignmentOM teacherClassAssignmentOM = teacherClassAssignmentAlgoService.computeTeacherClassAssignment(input);
        return ResponseEntity.ok().body(teacherClassAssignmentOM);

    }
}
