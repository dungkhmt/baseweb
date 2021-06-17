package com.hust.baseweb.applications.education.teacherclassassignment.controller;

import com.hust.baseweb.applications.education.teacherclassassignment.entity.ClassTeacherAssignmentClassInfo;
import com.hust.baseweb.applications.education.teacherclassassignment.entity.ClassTeacherAssignmentPlan;
import com.hust.baseweb.applications.education.teacherclassassignment.model.AlgoTeacherAssignmentIM;
import com.hust.baseweb.applications.education.teacherclassassignment.model.ClassTeacherAssignmentPlanCreateModel;
import com.hust.baseweb.applications.education.teacherclassassignment.model.ClassTeacherAssignmentPlanDetailModel;
import com.hust.baseweb.applications.education.teacherclassassignment.model.TeacherClassAssignmentOM;
import com.hust.baseweb.applications.education.teacherclassassignment.service.ClassTeacherAssignmentPlanService;
import com.hust.baseweb.applications.education.teacherclassassignment.service.TeacherClassAssignmentAlgoService;
import com.hust.baseweb.applications.education.teacherclassassignment.service.TeacherClassAssignmentService;
import com.hust.baseweb.entity.UserLogin;
import com.hust.baseweb.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

@Log4j2
@Controller
@Validated
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class TeacherClassAssignmentController {

    private TeacherClassAssignmentAlgoService algoService;

    private TeacherClassAssignmentService assignmentService;

    private UserService userService;
    private ClassTeacherAssignmentPlanService classTeacherAssignmentPlanService;

    @GetMapping("/get-all-class-teacher-assignment-plan")
    public ResponseEntity<?> getAllClassTeacherAssignmentPlan(Principal principal){
        UserLogin u = userService.findById(principal.getName());
        List<ClassTeacherAssignmentPlan> classTeacherAssignmentPlanList = classTeacherAssignmentPlanService.findAll();
        return ResponseEntity.ok().body(classTeacherAssignmentPlanList);
    }

    @GetMapping("/get-class-teacher-assignment-plan/detail/{planId}")
    public ResponseEntity<?> getClassTeacherAssignmentPlanDetail(Principal principal, @PathVariable UUID planId){
        log.info("getClassTeacherAssignmentPlanDetail, planId = " + planId);
        ClassTeacherAssignmentPlanDetailModel classTeacherAssignmentPlanDetailModel = classTeacherAssignmentPlanService
            .getClassTeacherAssignmentPlanDetail(planId);
        return ResponseEntity.ok().body(classTeacherAssignmentPlanDetailModel);
    }
    @PostMapping("/create-class-teacher-assignment-plan")
    public ResponseEntity<?> createClassTeacherAssignmentPlan(Principal principal, @RequestBody
                                                              ClassTeacherAssignmentPlanCreateModel input
                                                              ){
        UserLogin u = userService.findById(principal.getName());
        log.info("createClassTeacherAssignmentPlan, planName   = " + input.getPlanName());
        ClassTeacherAssignmentPlan classTeacherAssignmentPlan = classTeacherAssignmentPlanService.create(u, input);
        return ResponseEntity.ok().body(classTeacherAssignmentPlan);

    }

    @GetMapping("/get-class-list-for-assignment-2-teacher/{planId}")
    public ResponseEntity<?> getClassListForAssignment2Teacher(Principal principal, @PathVariable UUID planId){
        log.info("getClassListForAssignment2Teacher, planId = " + planId);
        List<ClassTeacherAssignmentClassInfo> classTeacherAssignmentClassInfos =
            classTeacherAssignmentPlanService.findAllClassTeacherAssignmentClassByPlanId(planId);
        return ResponseEntity.ok().body(classTeacherAssignmentClassInfos);
    }


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
