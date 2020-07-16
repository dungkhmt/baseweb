package com.hust.baseweb.applications.education.controller;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.hust.baseweb.applications.education.entity.EduSemester;
import com.hust.baseweb.applications.education.model.BCAJsonInputModel;
import com.hust.baseweb.applications.education.model.ClassesInputModel;
import com.hust.baseweb.applications.education.model.Course4teacherInputModel;
import com.hust.baseweb.applications.education.model.CreateSemesterInputModel;
import com.hust.baseweb.applications.education.repo.EduAssignmentRepo.EduClassTeacherAssignmentOutputModel;
import com.hust.baseweb.applications.education.service.EduAssignmentService;
import com.hust.baseweb.applications.education.service.EduAssignmentServiceImpl;
import com.hust.baseweb.applications.education.service.EduClassService;
import com.hust.baseweb.applications.education.service.EduCourseService;
import com.hust.baseweb.applications.education.service.EduCourseTeacherPreferenceService;
import com.hust.baseweb.applications.education.service.EduSemesterService;
import com.hust.baseweb.applications.education.service.EduTeacherService;
import com.hust.baseweb.applications.education.service.UploadExcelService;
import com.poiji.bind.Poiji;
import com.poiji.exception.PoijiExcelType;
import com.poiji.option.PoijiOptions;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RestController
@AllArgsConstructor(onConstructor = @__(@Autowired))
@Log4j2
public class EduControllerAPI {

	UploadExcelService uploadService;
	EduTeacherService teacherService;
	EduCourseService courseService;
	EduClassService classService;
	EduAssignmentService assignmentService;
	EduSemesterService semesterService;
	EduCourseTeacherPreferenceService preferenceService;

	@GetMapping("edu/get-teacher-info/{teacherId}")
	public ResponseEntity<?> getTeacherInfo(Principal principal, @PathVariable String teacherId) {
		return ResponseEntity.ok(teacherService.findByTeacherId(teacherId));
	}

	@GetMapping("edu/get-all-teachers")
	public ResponseEntity<?> getAllTeacher(Principal principal) {
		return ResponseEntity.ok(teacherService.findAll());
	}

	@GetMapping("edu/get-all-courses")
	public ResponseEntity<?> getAllCourses(Principal principal) {
		return ResponseEntity.ok(courseService.findAll());
	}

	@GetMapping("edu/get-all-courses/{teacherId}")
	public ResponseEntity<?> getAllCoursesByTeacherId(Principal principal, @PathVariable String teacherId) {
		return ResponseEntity.ok(preferenceService.findByCompositeIdTeacherId(teacherId));
	}

	@GetMapping("edu/get-all-classes")
	public ResponseEntity<?> getAllClass(Principal principal) {
		return ResponseEntity.ok(classService.findAll());
	}

	@GetMapping("edu/get-all-semester")
	public ResponseEntity<?> getAllSemester(Principal principal) {
		return ResponseEntity.ok(semesterService.findAll());
	}

	@PostMapping("edu/create-semester")
	public ResponseEntity<?> createSemester(Principal principal, @RequestBody CreateSemesterInputModel input) {
		EduSemester result = semesterService.save(input.getSemesterId(), input.getSemesterName());
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
	public ResponseEntity<?> getAllAssignmentBySemesterIdTeacherId(Principal principal, @PathVariable String semesterId,
			@PathVariable String teacherId) {
		return ResponseEntity
				.ok(assignmentService.getEduClassTeacherAssignmentBySemesterIdTeacherId(semesterId, teacherId));
	}

	@PostMapping("/edu/upload/course-for-teacher")
	public ResponseEntity<?> uploadClass4Teacher(Principal principal, @RequestParam("file") MultipartFile multipartFile)
			throws IOException {
		List<Course4teacherInputModel> rows = Poiji.fromExcel(multipartFile.getInputStream(), PoijiExcelType.XLSX,
				Course4teacherInputModel.class, PoijiOptions.PoijiOptionsBuilder.settings().sheetIndex(0).build());
		log.info("uploadCourse4Teacher, " + rows.size() + " rows.");
		return ResponseEntity.ok(uploadService.saveCourseTeacherPreference(rows));
	}

	@PostMapping("/edu/upload/class/{semesterId}")
	public ResponseEntity<?> uploadClassTeacherPreference(Principal principal,
			@RequestParam("file") MultipartFile multipartFile, @PathVariable String semesterId) throws IOException {
		List<ClassesInputModel> rows = Poiji.fromExcel(multipartFile.getInputStream(), PoijiExcelType.XLSX,
				ClassesInputModel.class, PoijiOptions.PoijiOptionsBuilder.settings().sheetIndex(0).build());
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
	public ResponseEntity<?> downloadAssignmentBySemesterIdTeacherId(Principal principal,
			@PathVariable String semesterId, @PathVariable String teacherId) throws IOException {
		List<EduClassTeacherAssignmentOutputModel> result = assignmentService
				.getEduClassTeacherAssignmentBySemesterIdTeacherId(semesterId, teacherId);
		return ResponseEntity.ok(EduAssignmentServiceImpl.generateResponseStream(result));

	}
}
