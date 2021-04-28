package com.hust.baseweb.applications.education.classmanagement.controller;

import com.google.gson.Gson;
import com.hust.baseweb.applications.education.classmanagement.service.ClassServiceImpl;
import com.hust.baseweb.applications.education.content.Video;
import com.hust.baseweb.applications.education.content.VideoService;
import com.hust.baseweb.applications.education.entity.*;
import com.hust.baseweb.applications.education.exception.SimpleResponse;
import com.hust.baseweb.applications.education.model.*;
import com.hust.baseweb.applications.education.model.quiz.QuizQuestionDetailModel;
import com.hust.baseweb.applications.education.service.*;
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
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.xml.ws.Response;
import java.security.Principal;
import java.util.ArrayList;
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
    private EduCourseChapterService eduCourseChapterService;
    private EduCourseChapterMaterialService eduCourseChapterMaterialService;
    private LogUserLoginCourseChapterMaterialService logUserLoginCourseChapterMaterialService;
    private VideoService videoService;

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

    @Secured({"ROLE_EDUCATION_TEACHING_MANAGEMENT_TEACHER"})
    @GetMapping("/{id}/assignments/teacher")
    public ResponseEntity<?> getAssignOfClass4Teacher(@PathVariable UUID id) {
        return ResponseEntity.ok().body(classService.getAssign4Teacher(id));
    }

    @GetMapping("/{id}/assignments/student")
    public ResponseEntity<?> getAssignOfClass4Student(@PathVariable UUID id) {
        return ResponseEntity.ok().body(classService.getAssign4Student(id));
    }

    @PostMapping("/add")
    public ResponseEntity<?> addEduClass(Principal principal, @RequestBody AddClassModel addClassModel) {
        log.info("addEduClass, start....");
        UserLogin userLogin = userService.findById(principal.getName());
        EduClass aClass = classService.save(userLogin, addClassModel);
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
    @GetMapping("/get-chapters-of-course/{courseId}")
    public ResponseEntity<?> getChaptersOfCourse(Principal principal, @PathVariable String courseId){
        log.info("getChaptersOfCourse start... courseId = " + courseId);
        //List<EduCourseChapter> eduCourseChapters = eduCourseChapterService.findAll();
        List<EduCourseChapter> eduCourseChapters = eduCourseChapterService.findAllByCourseId(courseId);
        log.info("getChaptersOfCourse, GOT " + eduCourseChapters.size());
        return ResponseEntity.ok().body(eduCourseChapters);
    }
    @GetMapping("/get-chapters-of-class/{classId}")
    public ResponseEntity<?> getChaptersOfClass(Principal principal, @PathVariable UUID classId) {
        GetClassDetailOM eduClass = classService.getClassDetail(classId);
        String courseId = eduClass.getCourseId();

        List<EduCourseChapter> eduCourseChapters = eduCourseChapterService.findAllByCourseId(courseId);
        log.info("getChaptersOfClass, classId = " + classId + ", courseId = " + courseId
        + " RETURN list.sz = " + eduCourseChapters.size());

        return ResponseEntity.ok().body(eduCourseChapters);
    }

    @Secured({"ROLE_EDUCATION_TEACHING_MANAGEMENT_TEACHER"})
    @PostMapping("/create-chapter-of-course")
    public ResponseEntity<?> createChapterOfCourse(Principal principal, @RequestBody EduCourseChapterModelCreate eduCourseChapterModelCreate){
        log.info("createChapterOfCourse, courseId = " + eduCourseChapterModelCreate.getCourseId() + " chapterName = " +
                 eduCourseChapterModelCreate.getChapterName());
        EduCourseChapter eduCourseChapter = eduCourseChapterService.save(eduCourseChapterModelCreate);
        return ResponseEntity.ok().body(eduCourseChapter);
    }
    @GetMapping("/change-chapter-status/{chapterId}")
    public ResponseEntity<?> changeChapterStatus(Principal principal, @PathVariable UUID chapterId){
        log.info("changeChapterStatus, chapterId = " + chapterId);
        String statusId = eduCourseChapterService.changeOpenCloseChapterStatus(chapterId);
        return ResponseEntity.ok().body(statusId);
    }

    @GetMapping("/get-course-chapter-material-type-list")
    public ResponseEntity<?> getCourseChapterMaterialTypeList(Principal principal){
        log.info("getCourseChapterMaterialTypeList");
        List<String> types = new ArrayList<>();
        types.add(EduCourseChapterMaterial.EDU_COURSE_MATERIAL_TYPE_SLIDE);
        types.add(EduCourseChapterMaterial.EDU_COURSE_MATERIAL_TYPE_VIDEO);
        return ResponseEntity.ok().body(types);
    }
    @Secured({"ROLE_EDUCATION_TEACHING_MANAGEMENT_TEACHER"})
    @PostMapping("/create-chapter-material-of-course")
    //public ResponseEntity<?> createChapterMaterialOfCourse(Principal principal, @RequestBody
      //  EduCourseChapterMaterialModelCreate eduCourseChapterMaterialModelCreate){
        public ResponseEntity<?> createChapterMaterialOfCourse(Principal principal, @RequestParam("inputJson") String inputJson,
                                                               @RequestParam("files") MultipartFile[] files){
        Gson gson = new Gson();
        EduCourseChapterMaterialModelCreate eduCourseChapterMaterialModelCreate = gson.fromJson(inputJson, EduCourseChapterMaterialModelCreate.class);

            log.info("createChapterMaterialOfCourse, chapterId = " + eduCourseChapterMaterialModelCreate.getChapterId()
        + " materialName = " + eduCourseChapterMaterialModelCreate.getMaterialName() + " materialType = " +
                 eduCourseChapterMaterialModelCreate.getMaterialType());
        EduCourseChapterMaterial eduCourseChapterMaterial = null;
            try {
            Video video = videoService.create(files[0]);
            log.info("createChapterMaterialOfCourse, videoId = " + video.getId());
            eduCourseChapterMaterial = eduCourseChapterMaterialService.save(eduCourseChapterMaterialModelCreate,video);

        }catch(Exception e){
            e.printStackTrace();
        }

        return ResponseEntity.ok().body(eduCourseChapterMaterial);
    }
    @Secured({"ROLE_EDUCATION_TEACHING_MANAGEMENT_TEACHER"})
    @GetMapping("/get-course-chapter-material-detail/{id}")
    public ResponseEntity<?> getCourseChapterMaterialDetail(Principal principal, @PathVariable UUID id){
        log.info("getCourseChapterMaterialDetail, id = " + id);
        UserLogin userLogin = userService.findById(principal.getName());
        logUserLoginCourseChapterMaterialService.logUserLoginMaterial(userLogin,id);
        log.info("getCourseChapterMaterialDetail, id = " + id);
        EduCourseChapterMaterial eduCourseChapterMaterial = eduCourseChapterMaterialService.findById(id);
        return ResponseEntity.ok().body(eduCourseChapterMaterial);
    }

    @Secured({"ROLE_EDUCATION_TEACHING_MANAGEMENT_TEACHER"})
    @GetMapping("/get-chapter-materials-of-course/{chapterId}")
    public ResponseEntity<?> getChapterMaterialsOfCourse(Principal principal, @PathVariable UUID chapterId){
        //List<EduCourseChapterMaterial> eduCourseChapterMaterials = eduCourseChapterMaterialService.findAll();
        List<EduCourseChapterMaterial> eduCourseChapterMaterials = eduCourseChapterMaterialService.findAllByChapterId(chapterId);
        return ResponseEntity.ok().body(eduCourseChapterMaterials);
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
