package com.hust.baseweb.applications.education.teacherclassassignment.controller;

import com.google.gson.Gson;
import com.hust.baseweb.applications.education.teacherclassassignment.entity.*;
import com.hust.baseweb.applications.education.teacherclassassignment.model.*;
import com.hust.baseweb.applications.education.teacherclassassignment.service.ClassTeacherAssignmentPlanService;
import com.hust.baseweb.applications.education.teacherclassassignment.service.TeacherClassAssignmentAlgoService;
import com.hust.baseweb.applications.education.teacherclassassignment.service.TeacherClassAssignmentService;
import com.hust.baseweb.entity.UserLogin;
import com.hust.baseweb.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.xml.ws.Response;
import java.io.InputStream;
import java.security.Principal;
import java.util.ArrayList;
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

    @PostMapping("/upload-excel-class-4-teacher-assignment")
    public ResponseEntity<?> uploadExcelClass4TeacherAssignment(
        Principal principal, @RequestParam("inputJson") String inputJson,
        @RequestParam("file") MultipartFile file
    ) {
        Gson gson = new Gson();
        UploadExcelClass4TeacherAssignmentInputModel input = gson.fromJson(inputJson, UploadExcelClass4TeacherAssignmentInputModel.class);
        UUID planId = input.getPlanId();
        log.info("uploadExcelClass4TeacherAssignment, json = " + inputJson + " planId = " + planId);
        classTeacherAssignmentPlanService.extractExcelAndStoreDB(planId, file);
        return ResponseEntity.ok().body("OK");

    }
    @GetMapping("/get-all-class-teacher-assignment-plan")
    public ResponseEntity<?> getAllClassTeacherAssignmentPlan(Principal principal){
        UserLogin u = userService.findById(principal.getName());
        List<ClassTeacherAssignmentPlan> classTeacherAssignmentPlanList = classTeacherAssignmentPlanService.findAll();
        return ResponseEntity.ok().body(classTeacherAssignmentPlanList);
    }
    @PostMapping("/upload-excel-teacher-course")
    public ResponseEntity<?> uploadExcelTeacherCourse(
        Principal principal, @RequestParam("inputJson") String inputJson,
        @RequestParam("file") MultipartFile file
    ) {
        Gson gson = new Gson();
        UploadExcelTeacherCourseInputModel input = gson.fromJson(
            inputJson,
            UploadExcelTeacherCourseInputModel.class);
        UUID planId = input.getPlanId();
        String choice = input.getChoice();
        log.info("uploadExcelTeacherCourse, choice = " + choice);
        boolean ok = classTeacherAssignmentPlanService.extractExcelAndStoreDBTeacherCourse(planId, choice, file);
        return ResponseEntity.ok().body(ok);
    }
    @GetMapping("/get-all-teachers")
    public ResponseEntity<?> getAllTeachers(Principal principal){
        List<EduTeacher> eduTeachers = classTeacherAssignmentPlanService.findAllTeachers();
        return ResponseEntity.ok().body(eduTeachers);
    }
    @GetMapping("/get-teacher-for-assignment/{planId}")
    public ResponseEntity<?> getTeacherForAssignmentPlan(Principal principal, @PathVariable UUID planId){
        List<TeacherForAssignmentPlan> teacherForAssignmentPlans =
            classTeacherAssignmentPlanService.findAllTeacherByPlanId(planId);
        return ResponseEntity.ok().body(teacherForAssignmentPlans);

    }
    @GetMapping("/get-teacher-course-4-assignment/{planId}")
    public ResponseEntity<?> getTeacherCourseForAssignment(Principal principal, @PathVariable UUID planId){
        List<TeacherCourseForAssignmentPlan> teacherCourses =
            classTeacherAssignmentPlanService.findTeacherCourseOfPlan(planId);

        return ResponseEntity.ok().body(teacherCourses);
    }
    @GetMapping("/get-pair-of-conflict-timetable-class/{planId}")
    public ResponseEntity<?> getPairOfConflictTimetableClass(Principal principal, @PathVariable UUID planId){
        List<PairOfConflictTimetableClassModel> lst = classTeacherAssignmentPlanService.getPairOfConflictTimetableClass(planId);
        log.info("getPairOfConflictTimetableClass, return list.sz = " + lst.size());
        return ResponseEntity.ok().body(lst);
    }
    @GetMapping("/get-all-teacher-course")
    public ResponseEntity<?> getAllTeacherCourse(Principal principal){
        List<TeacherCourse> teacherCourses = classTeacherAssignmentPlanService.findAllTeacherCourse();

        return ResponseEntity.ok().body(teacherCourses);
    }
    @PostMapping("/add-teacher-to-assign-plan")
    public ResponseEntity<?> addTeacherToAssignmentPlan(Principal principal,
          @RequestParam(required = false, name = "planId") UUID planId,
          @RequestParam(required = false, name = "teacherList") String teacherList
    ){
        log.info("addTeacherToAssignmentPlan, planId = " + planId + " teacherList = " + teacherList);

        boolean ok = classTeacherAssignmentPlanService.addTeacherToAssignmentPlan(planId, teacherList);

        return ResponseEntity.ok().body("OK");

    }

    @PostMapping("/add-teacher-course-to-assign-plan")
    public ResponseEntity<?> addTeacherCourseToAssignmentPlan(Principal principal,
                @RequestParam(required = false, name = "planId") UUID planId,
                @RequestParam(required = false, name = "teacherCourseList") String teacherCourseList
                ){
        log.info("addTeacherCourseToAssignmentPlan, planId = " + planId + " teacherCourseList = " + teacherCourseList);

        boolean ok = classTeacherAssignmentPlanService.addTeacherCourseToAssignmentPlan(planId, teacherCourseList);

        return ResponseEntity.ok().body("OK");

    }
    @PostMapping("/auto-assign-teacher-2-class")
    public ResponseEntity<?> autoAssignTeacher2Class(Principal principal, @RequestBody RunAutoAssignTeacher2ClassInputModel input){
        log.info("autoAssignTeacher2Class");
        classTeacherAssignmentPlanService.autoAssignTeacher2Class(input.getPlanId());
        return ResponseEntity.ok().body("OK");
    }
    @GetMapping("/get-class-teacher-assignment-solution/{planId}")
    public ResponseEntity<?> getClassTeacherAssignmentSolutions(Principal principal, @PathVariable UUID planId){
        log.info("getClassTeacherAssignmentSolutions, planId = " + planId);
        List<ClassTeacherAssignmentSolutionModel> classTeacherAssignmentSolutionModels =
            classTeacherAssignmentPlanService.getClassTeacherAssignmentSolution(planId);
        return ResponseEntity.ok().body(classTeacherAssignmentSolutionModels);
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
