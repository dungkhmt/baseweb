package com.hust.baseweb.applications.education.classmanagement.controller;

import com.hust.baseweb.applications.education.classmanagement.service.ClassServiceImpl;
import com.hust.baseweb.applications.education.entity.EduClass;
import com.hust.baseweb.applications.education.entity.EduCourse;
import com.hust.baseweb.applications.education.entity.EduDepartment;
import com.hust.baseweb.applications.education.entity.Semester;
import com.hust.baseweb.applications.education.exception.SimpleResponse;
import com.hust.baseweb.applications.education.model.*;
import com.hust.baseweb.applications.education.service.CourseService;
import com.hust.baseweb.applications.education.service.EduDepartmentService;
import com.hust.baseweb.applications.education.service.SemesterService;
import com.hust.baseweb.entity.UserLogin;
import com.hust.baseweb.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.security.Principal;
import java.util.List;
import java.util.UUID;

@Log4j2
@Controller
@Validated
@RequestMapping("/edu/class")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class ClassController {

    private ClassServiceImpl classService;
    private CourseService courseService;
    private SemesterService semesterService;
    private UserService userService;
    private EduDepartmentService eduDepartmentService;

    @PostMapping
    public ResponseEntity<?> getClassesOfCurrSemester(
        Principal principal,
        @RequestParam
        @Min(value = 0, message = "Số trang có giá trị không âm") Integer page,
        @RequestParam
        @Min(value = 0, message = "Kích thước trang có giá trị không âm") Integer size,
        @RequestBody GetClassesIM filterParams
    ) {
        if (null == page) {
            page = 0;
        }

        if (null == size) {
            size = 20;
        }

        return ResponseEntity
            .ok()
            .body(classService.getClassesOfCurrentSemester(
                principal.getName(),
                filterParams,
                PageRequest.of(page, size)));
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody RegistIM im, Principal principal) {
        SimpleResponse res = classService.register(im.getClassId(), principal.getName());
        return ResponseEntity.status(res.getStatus()).body(res.getMessage());
    }

    @GetMapping("/{id}/students")
    public ResponseEntity<List<GetStudentsOfClassOM>> getStudentsOfClass(@PathVariable UUID id) {
        return ResponseEntity.ok().body(classService.getStudentsOfClass(id));
    }

    @Secured({"ROLE_EDUCATION_TEACHING_MANAGEMENT_TEACHER"})
    @GetMapping("/{id}/registered-students")
    public ResponseEntity<List<GetStudentsOfClassOM>> getRegistStudentsOfClass(@PathVariable UUID id) {
        return ResponseEntity.ok().body(classService.getRegistStudentsOfClass(id));
    }

    @Secured({"ROLE_EDUCATION_TEACHING_MANAGEMENT_TEACHER"})
    @PutMapping("/registration-status")
    public ResponseEntity<?> updateRegistStatus(@Valid @RequestBody UpdateRegistStatusIM im) {
        return ResponseEntity
            .ok()
            .body(classService.updateRegistStatus(im.getClassId(), im.getStudentIds(), im.getStatus()));
    }

    @Secured({"ROLE_EDUCATION_TEACHING_MANAGEMENT_TEACHER"})
    @GetMapping("/list/teacher")
    public ResponseEntity<?> getClassesOfTeacher(Principal principal) {
        return ResponseEntity.ok().body(classService.getClassesOfTeacher(principal.getName()));
    }

    @GetMapping("/list/student")
    public ResponseEntity<?> getClassesOfStudent(Principal principal) {
        return ResponseEntity.ok().body(classService.getClassesOfStudent(principal.getName()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getClassDetail(@PathVariable UUID id) {
        return ResponseEntity.ok().body(classService.getClassDetail(id));
    }

    @GetMapping("/{id}/assignments")
    public ResponseEntity<?> getAssignmentsOfClass(@PathVariable UUID id) {
        return ResponseEntity.ok().body(classService.getAssignments(id));
    }

    @PostMapping("/add")
    public ResponseEntity<?> addEduClass(Principal principal, @RequestBody AddClassModel addClassModel){
        log.info("addEduClass, start....");
        UserLogin userLogin = userService.findById(principal.getName());
        EduClass aClass = classService.save(userLogin,addClassModel);
        return ResponseEntity.ok().body(aClass);
    }
    @Secured({"ROLE_EDUCATION_TEACHING_MANAGEMENT_TEACHER"})
    @GetMapping("/get-all-courses")
    public ResponseEntity<?> getAllCourses(Principal principal){
        log.info("getAllCourses start...");
        List<EduCourse> courses= courseService.findAll();
        log.info("getAllCourses, GOT " + courses.size());
        return ResponseEntity.ok().body(courses);
    }
    @Secured({"ROLE_EDUCATION_TEACHING_MANAGEMENT_TEACHER"})
    @GetMapping("/get-all-semesters")
    public ResponseEntity<?> getAllSemesters(Principal principal){
        log.info("getAllSemester start...");
        List<Semester> semesters = semesterService.findAll();
        log.info("getAllSemester GOT " + semesters.size());
        return ResponseEntity.ok().body(semesters);
    }
    @GetMapping("/get-all-departments")
    public ResponseEntity<?> getAllEduDepartments(Principal principal){

        List<EduDepartment> eduDepartments = eduDepartmentService.findAll();
        log.info("getAllEduDepartments, GOT sz = " + eduDepartments.size());
        return ResponseEntity.ok().body(eduDepartments);
    }
    
}
