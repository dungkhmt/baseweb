package com.hust.baseweb.applications.education.controller;

import com.hust.baseweb.applications.education.entity.EduClass;
import com.hust.baseweb.applications.education.entity.EduCourse;
import com.hust.baseweb.applications.education.entity.EduSemester;
import com.hust.baseweb.applications.education.entity.EduTeacher;
import com.hust.baseweb.applications.education.entity.mongodb.Course;
import com.hust.baseweb.applications.education.entity.mongodb.Teacher;
import com.hust.baseweb.applications.education.model.*;
import com.hust.baseweb.applications.education.repo.EduAssignmentRepo.EduClassTeacherAssignmentOutputModel;
import com.hust.baseweb.applications.education.repo.EduCourseRepo;
import com.hust.baseweb.applications.education.service.*;
import com.hust.baseweb.applications.education.service.mongodb.CourseService;
import com.hust.baseweb.applications.education.service.mongodb.TeacherService;
import com.poiji.bind.Poiji;
import com.poiji.exception.PoijiExcelType;
import com.poiji.option.PoijiOptions;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

@RestController
@AllArgsConstructor(onConstructor = @__(@Autowired))
@Log4j2
public class EduControllerAPI {

    EduCourseRepo eduCourseRepo;
    UploadExcelService uploadService;
    EduTeacherService eduTeacherService;
    EduCourseService eduCourseService;
    EduClassService classService;
    EduAssignmentService assignmentService;
    EduSemesterService semesterService;
    EduCourseTeacherPreferenceService preferenceService;
    EduDepartmentService departmentService;
    CourseService courseService;
    TeacherService teacherService;

    @GetMapping("edu/get-all-department")
    public ResponseEntity<?> getAllDepartment(Principal principal) {
        return ResponseEntity.ok().body(departmentService.findAll());
    }

    @GetMapping("edu/get-teacher-info/{teacherId}")
    public ResponseEntity<?> getTeacherInfo(Principal principal, @PathVariable String teacherId) {
        return ResponseEntity.ok(eduTeacherService.findByTeacherId(teacherId));
    }

    @GetMapping("edu/get-all-teachers")
    public ResponseEntity<?> getAllTeacher(Principal principal) {
        return ResponseEntity.ok(eduTeacherService.findAll());
    }

    @PostMapping("edu/create-teacher")
    public ResponseEntity<?> createTeacher(Principal principal, @RequestBody CreateTeacherInputModel input) {
        EduTeacher result = eduTeacherService.save(input.getEmail(), input.getTeacherName(), input.getEmail(),
                                                   input.getMaxCredit());
        log.info("createTeacher, teacher " + result.getEmail() + " saved.");
        return ResponseEntity.ok().body(result);
    }

    @GetMapping("edu/get-all-courses")
    public ResponseEntity<?> getAllCourses(Principal principal) {
        return ResponseEntity.ok(eduCourseService.findAll());
    }

    @GetMapping("edu/get-all-courses/{teacherId}")
    public ResponseEntity<?> getAllCoursesByTeacherId(Principal principal, @PathVariable String teacherId) {
        return ResponseEntity.ok(preferenceService.findByCompositeIdTeacherId(teacherId));
    }

    @PostMapping("edu/create-course")
    public ResponseEntity<?> createCourse(Principal principal, @RequestBody CreateCourseInputModel input) {
        EduCourse result = eduCourseService.save(input.getCourseId(), input.getCourseName(), input.getCredit());
        log.info("createCourse, courseId " + result.getCourseId() + " saved.");
        return ResponseEntity.ok().body(result);
    }

    @GetMapping("edu/get-all-classes")
    public ResponseEntity<?> getAllClass(Principal principal) {
        return ResponseEntity.ok(classService.findAll());
    }

    @PostMapping("edu/create-class")
    public ResponseEntity<?> createClass(Principal principal, @RequestBody CreateClassInputModel input) {
        EduClass result = classService.save(
            input.getClassId(),
            input.getClassName(),
            input.getClassType(),
            input.getCourseId(),
            input.getSessionId(),
            input.getDepartmentId(),
            input.getSemesterId());
        log.info("createClass, class " + result.getClassId() + " saved.");
        return ResponseEntity.ok().body(result);
    }

    @GetMapping("edu/get-all-semester")
    public ResponseEntity<?> getAllSemester(Principal principal) {
        return ResponseEntity.ok(semesterService.findAll());
    }

    @PostMapping("edu/create-semester")
    public ResponseEntity<?> createSemester(Principal principal, @RequestBody CreateSemesterInputModel input) {
        EduSemester result = semesterService.save(input.getSemesterId(), input.getSemesterName());
        log.info("createSemester, semester " + result.getSemesterId() + " saved.");
        return ResponseEntity.ok().body(result);
    }

    @GetMapping("edu/execute-class-teacher-assignment/{semesterId}")
    public ResponseEntity<?> executeAssignment(Principal principal, @PathVariable String semesterId) {
        log.info("executeAssignment");
        return ResponseEntity.ok().body(assignmentService.executeAssignment(semesterId));
    }

    @PostMapping("edu/execute-class-teacher-assignment-service")
    public ResponseEntity<?> executeAssignmentForJsonInput(Principal principal, @RequestBody BCAJsonInputModel input) {
        // get json input from request body
        // execute assignment and return result
        // not save to db
        return null;
    }

    @GetMapping("edu/get-all-assignment/{semesterId}")
    public ResponseEntity<?> getAllAssignmentBySemesterId(Principal principal, @PathVariable String semesterId) {
        return ResponseEntity.ok(assignmentService.getEduClassTeacherAssignmentBySemesterId(semesterId));
    }

    @GetMapping("edu/get-all-assignment/{semesterId}/{teacherId}")
    public ResponseEntity<?> getAllAssignmentBySemesterIdTeacherId(
        Principal principal, @PathVariable String semesterId,
        @PathVariable String teacherId
    ) {
        return ResponseEntity
            .ok(assignmentService.getEduClassTeacherAssignmentBySemesterIdTeacherId(semesterId, teacherId));
    }

    @PostMapping("/edu/upload/course-for-teacher")
    public ResponseEntity<?> uploadClass4Teacher(Principal principal, @RequestParam("file") MultipartFile multipartFile)
        throws IOException {
        List<Course4teacherInputModel> rows = Poiji.fromExcel(
            multipartFile.getInputStream(),
            PoijiExcelType.XLSX,
            Course4teacherInputModel.class,
            PoijiOptions.PoijiOptionsBuilder
                .settings()
                .sheetIndex(0)
                .build());
        log.info("uploadCourse4Teacher, " + rows.size() + " rows.");
        return ResponseEntity.ok(uploadService.saveCourseTeacherPreference(rows));
    }

    @PostMapping("/edu/upload/class/{semesterId}")
    public ResponseEntity<?> uploadClassTeacherPreference(
        Principal principal,
        @RequestParam("file") MultipartFile multipartFile, @PathVariable String semesterId
    ) throws IOException {
        List<ClassesInputModel> rows = Poiji.fromExcel(
            multipartFile.getInputStream(),
            PoijiExcelType.XLSX,
            ClassesInputModel.class,
            PoijiOptions.PoijiOptionsBuilder
                .settings()
                .sheetIndex(0)
                .build());
        log.info("uploadClassTeacherPreference, " + rows.size() + " rows.");

        return ResponseEntity.ok(uploadService.saveClasses(rows, semesterId));
    }

    @GetMapping("edu/download/class-teacher-assignment/{semesterId}")
    public ResponseEntity<?> downloadAssignmentBySemesterId(Principal principal, @PathVariable String semesterId)
        throws IOException {
        List<EduClassTeacherAssignmentOutputModel> result = assignmentService
            .getEduClassTeacherAssignmentBySemesterId(semesterId);
        return ResponseEntity.ok(EduAssignmentServiceImpl.generateResponseStream(result));
    }

    @GetMapping("edu/download/class-teacher-assignment/{semesterId}/{teacherId}")
    public ResponseEntity<?> downloadAssignmentBySemesterIdTeacherId(
        Principal principal,
        @PathVariable String semesterId, @PathVariable String teacherId
    ) throws IOException {
        List<EduClassTeacherAssignmentOutputModel> result = assignmentService
            .getEduClassTeacherAssignmentBySemesterIdTeacherId(semesterId, teacherId);
        return ResponseEntity.ok(EduAssignmentServiceImpl.generateResponseStream(result));

    }

    /**
     * Upload list of courses. Before inserting, a {@link Course} in pairs of courses with the same <b>courseId</b>
     * wil be removed.
     *
     * @param principal
     * @param courses
     * @return message
     * @author AnhTuan-AiT (anhtuan0126104@gmail.com)
     */
    @PostMapping("edu/course/add-list-of-courses")
    public ResponseEntity<?> uploadListOfCourses(Principal principal, @RequestBody List<Course> courses) {
        /*// RDBMS
        eduCourseRepo.saveAll(courses);*/

        Course course;
        int len = courses.size() - 1;

        for (int i = 0; i < len; i++) {
            course = courses.get(i);

            for (int j = i + 1; j < courses.size(); j++) {
                if (courses.get(j).getCourseId().equals(course.getCourseId())) {
                    courses.remove(j);
                    len--;
                }
            }
        }

        courseService.uploadListOfCourses(courses);
        return ResponseEntity.ok().body("Đã lưu");
    }

    /**
     * Add a new course.
     *
     * @param principal
     * @param course
     * @return message
     * @author AnhTuan-AiT (anhtuan0126104@gmail.com)
     */
    @PostMapping("edu/course/add-course")
    public ResponseEntity<?> addNewCourse(Principal principal, @RequestBody EduCourse course) {
        eduCourseRepo.save(course);
        return ResponseEntity.ok().body("Đã lưu");
    }

    /**
     * Return list all courses.
     *
     * @return list of {@link Course}
     * @author AnhTuan-AiT (anhtuan0126104@gmail.com)
     */
    @GetMapping("edu/course/all")
    public ResponseEntity<?> getAllCourses() {
        return ResponseEntity.ok().body(courseService.getAllCourses());
    }

    /**
     * Upload list of teachers. Before inserting, a {@link Teacher} in pairs of teachers with the same <b>email</b>
     * wil be removed.
     *
     * @param principal
     * @param teachers
     * @return message
     */
    @PostMapping("edu/teacher/add-list-of-teachers")
    public ResponseEntity<?> uploadListOfTeachers(Principal principal, @RequestBody List<Teacher> teachers) {
        Teacher teacher;
        int len = teachers.size() - 1;

        for (int i = 0; i < len; i++) {
            teacher = teachers.get(i);

            for (int j = i + 1; j < teachers.size(); j++) {
                if (teacher.getEmail().equalsIgnoreCase(teachers.get(j).getEmail())) {
                    teachers.remove(j);
                    len--;
                }
            }
        }

        teacherService.uploadListOfTeachers(teachers);
        return ResponseEntity.ok().body("Đã lưu");
    }

    /**
     * Return list all teachers.
     *
     * @return list of {@link Teacher}
     * @author AnhTuan-AiT (anhtuan0126104@gmail.com)
     */
    @GetMapping("edu/teacher/all")
    public ResponseEntity<?> getAllTeachers() {
        return ResponseEntity.ok().body(teacherService.getAllTeachers());
    }

}
