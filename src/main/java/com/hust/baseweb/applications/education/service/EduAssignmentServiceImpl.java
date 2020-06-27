package com.hust.baseweb.applications.education.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import localsearch.model.LocalSearchManager;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import com.hust.baseweb.applications.education.core.model.AssignmentInput;
import com.hust.baseweb.applications.education.core.model.Class;
import com.hust.baseweb.applications.education.core.model.Course;
import com.hust.baseweb.applications.education.core.model.Session;
import com.hust.baseweb.applications.education.core.model.Teacher;
import com.hust.baseweb.applications.education.core.solver.BCA_Problem;
import com.hust.baseweb.applications.education.entity.EduClass;
import com.hust.baseweb.applications.education.entity.EduClassTeacherAssignment;
import com.hust.baseweb.applications.education.entity.EduCourse;
import com.hust.baseweb.applications.education.entity.EduCourseTeacherPreference;
import com.hust.baseweb.applications.education.entity.EduTeacher;
import com.hust.baseweb.applications.education.entity.ExeAssignmentResult;
import com.hust.baseweb.applications.education.repo.EduAssignmentRepo;
import com.hust.baseweb.applications.education.repo.EduAssignmentRepo.EduClassTeacherAssignmentOutputModel;
import com.hust.baseweb.applications.education.repo.EduClassRepo;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
@Log4j2
public class EduAssignmentServiceImpl implements EduAssignmentService {

	EduAssignmentRepo assignmentRepo;
	EduClassService classService;
	EduCourseTeacherPreferenceService prefService;
	EduCourseService courseService;
	EduTeacherService teacherService;
	EduClassRepo eduClassRepo;

	@Override
	public List<EduClassTeacherAssignment> findAll() {
		// TODO Auto-generated method stub
		return assignmentRepo.findAll();
	}

	@Override
	public List<EduClassTeacherAssignment> findByTeacherId(String teacherId) {
		// TODO Auto-generated method stub
		return assignmentRepo.findByClassTeacherCompositeIdTeacherId(teacherId);
	}

	@Override
	public EduClassTeacherAssignment findByClassId(String classId) {
		// TODO Auto-generated method stub
		return assignmentRepo.findByClassTeacherCompositeIdClassId(classId);
	}

	@Override
	public ExeAssignmentResult executeAssignment(String semesterId) {
		// TODO Auto-generated method stub


		List<EduClass> eduClasses = eduClassRepo.findNotAssignedBySemesterId(semesterId);
		ArrayList<Class> classList = new ArrayList<Class>();

		for (EduClass eduClass : eduClasses) {
			Class inputClass = new Class();
			inputClass.setCode(eduClass.getClassId());
			EduCourse eduCourse = courseService.findByCourseId(eduClass.getCourseId());
			inputClass
					.setCourse(new Course(eduCourse.getCourseId(), eduCourse.getCourseName(), eduClass.getClassType()));
			inputClass.setCredit(eduCourse.getCredit());

			// parse session string
			String sessionsText[] = eduClass.getSessionId().split(";");
			Session inputSession[] = new Session[sessionsText.length];
			int index = 0;
			for (String text : sessionsText) {
				text = text.replaceAll(" ", "");
				String data[] = text.split(",");
				Session session = new Session();
				session.setIndex(Integer.parseInt(data[0]));
				session.setStartTime(Integer.parseInt(data[1]));
				session.setEndTime(Integer.parseInt(data[2]));
				session.setLocation(data[data.length - 1]);

				ArrayList<Integer> weeks = new ArrayList<Integer>();
				for (int i = 3; i < data.length - 1; i++) {
					if (!data[i].contains("-")) {
						weeks.add(Integer.parseInt(data[i]));
					} else {
						String w[] = data[i].split("-");
						for (int j = Integer.parseInt(w[0]); j <= Integer.parseInt(w[1]); j++) {
							weeks.add(j);
						}
					}
				}
				session.setWeeks(weeks);
				inputSession[index++] = session;
			}
			inputClass.setTimeTable(inputSession);
			classList.add(inputClass);
		}

		ArrayList<Teacher> teachers = new ArrayList<Teacher>();
		List<EduTeacher> eduTeachers = teacherService.findAll();
		for (EduTeacher eduTeacher : eduTeachers) {
			Teacher inputTeacher = new Teacher(eduTeacher.getTeacherId(), eduTeacher.getTeacherName(),
					eduTeacher.getMaxCredit(), new ArrayList<Course>());
			List<EduCourseTeacherPreference> coursePref = prefService
					.findByCompositeIdTeacherId(eduTeacher.getTeacherId());
			for (EduCourseTeacherPreference pref : coursePref) {
				EduCourse eduCourse = courseService.findByCourseId(pref.getCompositeId().getCourseId());
				inputTeacher.getCourses().add(new Course(pref.getCompositeId().getCourseId(), eduCourse.getCourseName(),
						pref.getCompositeId().getClassType()));
			}
			teachers.add(inputTeacher);
		}

		AssignmentInput input = new AssignmentInput(classList.toArray(new Class[classList.size()]),
				teachers.toArray(new Teacher[teachers.size()]));

		BCA_Problem solver = new BCA_Problem(input);
		List<EduClassTeacherAssignment> assignments = new ArrayList<EduClassTeacherAssignment>();
		List<String> exceptionClassesCode = new ArrayList<String>();

		solver.solve(assignments, exceptionClassesCode);

		if (!exceptionClassesCode.isEmpty() && teacherService.findByTeacherId("NULL") == null) {
			teacherService.save("NULL", "Chưa được phân công", "Chưa được phân công", 0);
		}
		for (EduClassTeacherAssignment assignment : assignments) {
			assignmentRepo.save(assignment);
		}

		List<EduClass> exceptionClass = new ArrayList<EduClass>();
		ExeAssignmentResult result = new ExeAssignmentResult();
		result.setClassesNo(eduClasses.size());
		result.setException(exceptionClass);

		if (exceptionClassesCode.isEmpty()) {
			result.setSucessNo(result.getClassesNo());
			result.setSucessRate(1.0);
		} else {
			result.setSucessNo(result.getClassesNo() - exceptionClassesCode.size());
			result.setSucessRate(Math.round(result.getSucessNo() * 10000.0 / result.getClassesNo()) / 10000.0);
			for (String exception : exceptionClassesCode) {
				for (EduClass cls : eduClasses) {
					if (exception.equalsIgnoreCase(cls.getClassId())) {
						exceptionClass.add(cls);
						break;
					}
				}
			}
		}
		return result;
	}

	@Override
	public List<EduClassTeacherAssignmentOutputModel> getEduClassTeacherAssignmentBySemesterId(String semesterId) {
		// TODO Auto-generated method stub
		return assignmentRepo.getClassTeacherAssignmentBySemesterId(semesterId);
	}

	@Override
	public List<EduClassTeacherAssignmentOutputModel> getEduClassTeacherAssignmentBySemesterIdTeacherId(
			String semesterId, String teacherId) {
		// TODO Auto-generated method stub
		return assignmentRepo.getClassTeacherAssignmentBySemesterIdTeacherId(semesterId, teacherId);
	}

	public static StreamingResponseBody generateResponseStream(List<EduClassTeacherAssignmentOutputModel> output)
			throws IOException {
		String headerColumns[] = new String[] { "Mã lớp", "Mã học phần", "Tên học phần", "Loại lớp", "Số tín chỉ",
				"Ca học", "Giáo viên", "Email" };
		Workbook workbook = new XSSFWorkbook();
		Sheet sheet = workbook.createSheet();

		Row header = sheet.createRow(0);
		for (int i = 0; i < headerColumns.length; i++) {
			Cell cell = header.createCell(i);
			cell.setCellValue(headerColumns[i]);
		}

		int rowIndex = 1;
		for (EduClassTeacherAssignmentOutputModel item : output) {
			Row row = sheet.createRow(rowIndex++);
			row.createCell(0).setCellValue(item.getClassId());
			row.createCell(1).setCellValue(item.getCourseId());
			row.createCell(2).setCellValue(item.getCourseName());
			row.createCell(3).setCellValue(item.getClassType());
			row.createCell(4).setCellValue(item.getCredit());
			row.createCell(5).setCellValue(item.getSessionId());
			row.createCell(6).setCellValue(item.getTeacherName());
			row.createCell(7).setCellValue(item.getEmail());
		}

		for (int i = 0; i < headerColumns.length; i++) {
			sheet.autoSizeColumn(i);
		}

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		workbook.write(outputStream);

		StreamingResponseBody stream = response -> {
			response.write(outputStream.toByteArray());
		};

		return stream;
	}

}
